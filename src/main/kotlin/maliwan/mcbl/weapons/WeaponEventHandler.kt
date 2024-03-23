package maliwan.mcbl.weapons

import maliwan.mcbl.Chance
import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.compareTo
import maliwan.mcbl.entity.showHealthBar
import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.gunProperties
import maliwan.mcbl.weapons.gun.shootBullet
import org.bukkit.*
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class WeaponEventHandler(val plugin: MCBorderlandsPlugin) : Listener, Runnable {

    /**
     * Stores for each entity the Unix timestamp where the next shot can be fired.
     */
    private val shotTimestamps = HashMap<Entity, Long>()

    /**
     * All bullets that have been shot
     */
    private val bullets = HashMap<Entity, BulletMeta>()

    @EventHandler
    fun fireGun(event: PlayerInteractEvent) {
        val bowItem = event.item ?: return
        val gunProperties = bowItem.gunProperties() ?: return

        event.player.apply {
            if (isShotCooldownOver()) {
                // Slightly increase it. experiments show fire rate is generally lower than expected.
                val fireRate = gunProperties.fireRate / 0.8

                // Fire Rate: for regular clicking there is a max fire rate of 6 shots per second.
                // Use scheduled bursts to fake a higher fire rate.
                // Burst Count: 001 002 003 004 005 006 ...
                //   Fire rate:  6   12  18  24  30  36 ...
                // All for 1 tick scheduling delay.
                // Strategy:
                // 1) Pick the base burst count from the table above (6.0 shots/second per burst count)
                // 2) Calculate the chance of excess fire rate out of 6.0 as random chance for a burst shot.
                // 3) Profit.
                val baseBurstCount = floor(fireRate / 6.0).toInt()
                val excess = fireRate - baseBurstCount * 6.0
                val scheduleChance = Chance(max(0.0, excess / 6.0))
                val fireRateBursts = baseBurstCount + if (Random.nextDouble() < scheduleChance) 1 else 0

                fun executeGunBurst() {
                    // Bursts
                    repeat(gunProperties.burstCount) { burstIndex ->
                        // First one must not be scheduled:
                        if (burstIndex == 0) {
                            shootGun(this, gunProperties)
                        }
                        // Subsequent ones must be scheduled by burst delay:
                        else {
                            plugin.server.scheduler.scheduleSyncDelayedTask(
                                plugin,
                                Runnable {
                                    shootGun(this, gunProperties)
                                },
                                gunProperties.burstDelay.long * burstIndex
                            )
                        }

                        // After each burst, update accuracy.
                        // TODO: Gun property proxy.

                        applyRecoil(gunProperties)
                    }

                    noActionTicks = (20 * (gunProperties.burstCount / gunProperties.fireRate)).toInt()
                    setCooldown(Material.BOW, noActionTicks)
                }

                // Fake fire rate bursts
                repeat(max(1, fireRateBursts)) { fireRateBurstIndex ->
                    if (fireRateBurstIndex == 0) {
                        executeGunBurst()
                    }
                    else plugin.server.scheduler.scheduleSyncDelayedTask(plugin, Runnable {
                        executeGunBurst()
                    }, fireRateBurstIndex.toLong() * 2L)
                }
            }
        }

        event.isCancelled = true
    }

    fun shootGun(player: Player, gunProperties: GunProperties) {
        // Shoot for each pellet.
        repeat(gunProperties.pelletCount) { _ ->
            player.shootGunBullet(gunProperties)
        }
        player.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS, 5.0f, 1.0f)
    }

    /**
     * Checks whether the gun of the entity is off cooldown.
     */
    fun Entity.isShotCooldownOver(): Boolean {
        val now = System.currentTimeMillis()
        val threshold = shotTimestamps[this] ?: return true
        return if (now >= threshold) {
            shotTimestamps.remove(this)
            true
        } else false
    }

    fun Player.shootGunBullet(gunProperties: GunProperties) {
        val direction = eyeLocation.direction
        val bullet = shootBullet(eyeLocation, direction, gunProperties) ?: return
        val bulletMeta = gunProperties.bulletMeta()
        bullets[bullet] = bulletMeta

        val millisDelay = (1000.0 / gunProperties.fireRate).toLong()
        val nextShotAllowedOn = System.currentTimeMillis() + millisDelay
        shotTimestamps[this] = nextShotAllowedOn
    }

    fun Player.applyRecoil(gunProperties: GunProperties) {
        val recoilAngle = gunProperties.recoilAngle ?: return
        val location = location
        val newLocation = location.clone()
        newLocation.pitch = location.pitch + recoilAngle.toFloat()
        val oldVelocity = velocity
        teleport(newLocation)
        velocity = oldVelocity /* preserve movement */
    }

    @EventHandler
    fun disableEntityInteraction(event: PlayerInteractEntityEvent) {
        if (event.player.gunProperties() != null) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun bulletHit(event: EntityDamageByEntityEvent) {
        val targetEntity = event.entity as? LivingEntity ?: return
        val bullet = event.damager
        val bulletMeta = bullets[bullet] ?: return

        // Disable iframes for bullets, save old values to restore them later.
        val oldNoDamageTicks = targetEntity.noDamageTicks
        val oldNoDamageTicksMax = targetEntity.maximumNoDamageTicks
        targetEntity.noDamageTicks = 0
        targetEntity.maximumNoDamageTicks = 0

        // Set damage to gun damage.
        event.damage = bulletMeta.damage.damage

        val velocity = targetEntity.velocity
        plugin.server.scheduler.scheduleSyncDelayedTask(plugin, Runnable {
            // Reinstantiate iframes and preserve initial velocity.
            targetEntity.velocity = velocity
            targetEntity.noDamageTicks = oldNoDamageTicks
            targetEntity.maximumNoDamageTicks = oldNoDamageTicksMax
        }, 1L)
    }

    @EventHandler
    fun meleeDamageBonus(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player
        val gun = damager?.gunProperties() ?: return
        event.damage = max(event.damage, event.damage + gun.meleeDamage.damage)
    }

    @EventHandler
    fun removeBullets(event: ProjectileHitEvent) {
        val bullet = event.entity
        if (bullet in bullets) {
            bullet.remove()

            // Only remove the bullet when not hitting a living entity because
            // the damage event requires this bullet data to apply correct damage calculations.
            if (event.hitEntity !is LivingEntity) {
                bullets.remove(bullet)
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun healthBarUpdate(event: EntityDamageEvent) {
        (event.entity as? LivingEntity)?.showHealthBar()
    }

    @EventHandler
    fun debugDead(event: EntityDeathEvent) {
        event.entity.customName = "${ChatColor.RED}DEAD"
    }

    /**
     * Apply bullet physics.
     */
    override fun run() {
        // Apply bullet specific gravity.
        bullets.forEach { (bullet, meta) ->
            val newVelocity = bullet.velocity.clone()
            newVelocity.y -= meta.gravity
            bullet.velocity = newVelocity
        }

        // Cleanup dead bullets.
        val toRemove = ArrayList<Entity>(bullets.size / 30)
        bullets.forEach { (bullet, meta) ->
            if (meta.isDead()) {
                toRemove.add(bullet)
            }
        }
        toRemove.forEach {
            bullets.remove(it)
            it.remove()
        }
    }
}