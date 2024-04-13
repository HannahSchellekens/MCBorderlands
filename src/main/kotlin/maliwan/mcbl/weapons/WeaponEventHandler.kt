package maliwan.mcbl.weapons

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.entity.*
import maliwan.mcbl.util.*
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.*
import org.bukkit.*
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
class WeaponEventHandler(val plugin: MCBorderlandsPlugin) : Listener, Runnable {

    /**
     * Handles all particles created by weapons.
     */
    val particles: WeaponParticles = WeaponParticles(this)

    /**
     * Stores for each entity the Unix timestamp where the next shot can be fired.
     */
    private val shotTimestamps = HashMap<Entity, Long>()

    /**
     * All bullets that have been shot
     */
    val bullets = HashMap<Entity, BulletMeta>()

    /**
     * The current gun executions by the players.
     */
    private val executions = HashMap<Entity, MutableMap<GunProperties, GunExecution>>()

    /**
     * Tracks for all entities what their elemental status effects are.
     */
    val elementalStatusEffects = ElementalStatusEffects(plugin)

    /**
     * Map (owner, gun) to the system time that the reload started.
     */
    private val reload = HashMap<Pair<Entity, GunProperties>, Long>()

    /**
     * Checks if the player already has an existing GunExecution with the given properties.
     */
    fun obtainGunExecution(player: Player, gunProperties: GunProperties): GunExecution {
        val gunExecutions = executions.getOrDefault(player, HashMap())
        var newExecution = gunExecutions.getOrDefault(gunProperties, null)

        // Use var/value of null to know if a new execution is created.
        // This way the init event can be applied.
        if (newExecution == null) {
            newExecution = GunExecution(gunProperties)

            gunProperties.assembly?.forEachBehaviour<GunExecutionInitializationBehaviour> {
                it.onInitializedGunExecution(newExecution, player)
            }
        }

        gunExecutions[gunProperties] = newExecution
        executions[player] = gunExecutions
        return newExecution
    }

    /**
     * Get the gun execution of the gun that the player is currently holding.
     * `null` when there is no execution.
     */
    fun obtainGunExecutionFromInventory(player: Player): GunExecution? {
        return obtainGunExecution(player, player.gunProperties() ?: return null)
    }

    @EventHandler
    fun fireGun(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) {
            // Drop reload is handled by another function [dropReload].
            return
        }

        val bowItem = event.item ?: return
        val gunProperties = bowItem.gunProperties() ?: return
        val execution = obtainGunExecution(event.player, gunProperties)

        event.player.apply {
            if (isShotCooldownOver()) {
                // Slightly increase it. experiments show fire rate is generally lower than expected.
                val fireRate = execution.fireRate / 0.8

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
                    repeat(execution.burstCount) { burstIndex ->
                        // Determine beforehand if the gun will be shot.
                        val inventory = plugin.inventoryManager[this]
                        val ammoLeft = inventory[execution.weaponClass]
                        val enoughAmmo = execution.clip > 0 && ammoLeft > 0

                        // First one must not be scheduled:
                        if (burstIndex == 0) {
                            if (execution.extraShotChance.roll()) {
                                shootGun(this, execution, consumeAmmo = false)
                            }

                            shootGun(this, execution, consumeAmmo = execution.freeShotChance.roll().not())
                        }
                        // Subsequent ones must be scheduled by burst delay:
                        else {
                            plugin.scheduleTask(execution.burstDelay.long * burstIndex) {
                                shootGun(this, execution, consumeAmmo = execution.freeShotChance.roll().not())
                            }
                        }

                        // Make sure the recoil angle is not applied when there are no shots
                        // left.
                        if (enoughAmmo) {
                            // Delay recoil to last burst shot to make burst recoil weapons not insufferable.
                            plugin.scheduleTask(execution.burstCount * execution.burstDelay.long) {
                                applyRecoil(execution)
                            }
                        }
                    }
                    execution.consecutiveShots++
                }

                // Fake fire rate bursts
                repeat(max(1, fireRateBursts)) { fireRateBurstIndex ->
                    if (fireRateBurstIndex == 0) {
                        executeGunBurst()
                    }
                    else plugin.scheduleTask(fireRateBurstIndex.toLong() * 2L) {
                        executeGunBurst()
                    }
                }
            }
        }

        event.isCancelled = true
    }

    /**
     * Executes one gunshot from the given gun execution.
     */
    fun shootGun(player: Player, gunExecution: GunExecution, consumeAmmo: Boolean = true) {
        val inventory = plugin.inventoryManager[player]
        val ammoLeft = inventory[gunExecution.weaponClass]
        if (gunExecution.clip <= 0 || ammoLeft <= 0) {
            return
        }

        gunExecution.assembly?.forEachBehaviour<PreGunShotBehaviour> {
            it.beforeGunShot(gunExecution, player)
        }

        // Shoot for each pellet.
        repeat(gunExecution.pelletCount) { _ ->
            player.shootGunBullet(gunExecution)
        }
        player.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS, 5.0f, 1.0f)

        if (consumeAmmo) {
            removeAmmo(player, gunExecution)
        }

        gunExecution.assembly?.forEachBehaviour<PostGunShotBehaviour> {
            it.afterGunShot(gunExecution, player)
        }

        // Auto reload if clip is empty.
        if (gunExecution.clip <= 0) {
            reloadGun(player, gunExecution)
        }
        else {
            player.noActionTicks = (20 * (gunExecution.burstCount / gunExecution.fireRate)).toInt()
            player.setCooldown(Material.BOW, player.noActionTicks)
        }
    }

    /**
     * Removes ammo for the given gun execution of the given player.
     * Removes for ammo equivalence of 1 shot.
     */
    fun removeAmmo(player: Player, gunExecution: GunExecution) {
        // When more ammo per shot is required than is available in clip: do not consume extra ammo.
        val ammoThatCanBeRemoved = min(gunExecution.clip, gunExecution.ammoPerShot)
        gunExecution.clip -= ammoThatCanBeRemoved
        val inventory = plugin.inventoryManager[player]
        inventory.removeAmmo(gunExecution.weaponClass, ammoThatCanBeRemoved)
    }

    /**
     * Reloads the gun: resets clip size after a delay and shows reloading UX.
     */
    fun reloadGun(player: Player, gunExecution: GunExecution) {
        // Prevent reloading when inventory is empty.
        if (plugin.inventoryManager[player][gunExecution.weaponClass] <= 0) {
            player.sendTitle("", "${ChatColor.RED}Out of ammo!", 5, 45, 5)
            return
        }

        // Prevent reloading when the clip is full.
        if (gunExecution.clip >= gunExecution.magazineSize) {
            return
        }

        gunExecution.assembly?.forEachBehaviour<ReloadBehaviour> {
            it.beforeReload(player, gunExecution)
        }

        // Set clip to 0 to prevent shooting during reload.
        gunExecution.clip = 0
        reload[player to gunExecution.properties] = System.currentTimeMillis()

        val reloadTicks = gunExecution.reloadSpeed.ticks
        player.noActionTicks = reloadTicks
        player.setCooldown(Material.BOW, reloadTicks)
        player.sendTitle("", "${ChatColor.RED}Reloading...", 5, reloadTicks - 10, 5)
        player.playSound(player.location, Sound.BLOCK_GRAVEL_HIT, SoundCategory.PLAYERS, 1.0f, 1.0f)

        repeat(reloadTicks / 10) {
            plugin.scheduleTask(it * 10L) {
                player.playSound(player.location, Sound.BLOCK_GRAVEL_HIT, SoundCategory.PLAYERS, 1.0f, 1.0f)
            }
        }

        plugin.scheduleTask(reloadTicks.toLong()) {
            gunExecution.clip = min(gunExecution.magazineSize, plugin.inventoryManager[player][gunExecution.weaponClass])
            player.playSound(player.location, Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 0.7f, 1.0f)
            reload.remove(player to gunExecution.properties)
        }

        gunExecution.assembly?.forEachBehaviour<ReloadBehaviour> {
            it.afterReload(player, gunExecution)
        }
    }

    /**
     * What the current progress is of the player reloading in [0.0, 1.0], or `null` when the player is not reloading.
     */
    fun reloadProgress(player: Player): Double? {
        val gunExecution = obtainGunExecutionFromInventory(player) ?: return null
        val reloadStartTime = reload[player to gunExecution.properties] ?: return null
        val timeElapsed = System.currentTimeMillis() - reloadStartTime
        val reloadTime = gunExecution.reloadSpeed.millis
        return min(1.0, timeElapsed.toDouble() / reloadTime)
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
        val bulletMeta = gunProperties.bulletMeta(this)
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
        val bulletMeta = bullets[bullet] ?: run {
            bullets.remove(bullet)
            return
        }

        // Disable iframes for bullets, save old values to restore them later.
        val oldNoDamageTicks = targetEntity.noDamageTicks
        val oldNoDamageTicksMax = targetEntity.maximumNoDamageTicks

        targetEntity.noDamageTicks = 0
        targetEntity.maximumNoDamageTicks = 0

        // Set damage to gun damage.
        val effectivenessType = EffectivenessType.typeOf(event.entityType)
        val elementalModifier = bulletMeta.elements.fold(1.0) { acc, elemental ->
            val elementDamageModifier = effectivenessType.damageMultiplier(elemental, targetEntity.armorPoints.toInt())
            acc * elementDamageModifier
        }

        // Calculate critical hit bonus.
        val hitLocation = (bullet as? Projectile)?.determineHitLocation(targetEntity)
        val head = targetEntity.headLocation
        val distance = hitLocation?.distance(head) ?: Double.MAX_VALUE
        val isCritical = distance < targetEntity.headshotRange * 1.15 /* make it slightly easier to hit headshots */

        val critMultiplier = if (isCritical) {
            2.0 + (bulletMeta.bonusCritMultiplier ?: 0.0)
        }
        else 1.0

        val particleLocation = targetEntity.location.add(0.0, targetEntity.height, 0.0)
        if (isCritical) {
            plugin.damageParticles.showCritDisplay(particleLocation.clone())
        }

        // Apply damage & elemental effect
        event.damage = bulletMeta.damage.damage * elementalModifier *
                elementalStatusEffects.slagMultiplier(targetEntity) * critMultiplier
        plugin.damageParticles.showDamageDisplay(hitLocation?.clone() ?: particleLocation.clone(), event.damage)

        rollElementalDot(plugin, targetEntity, bulletMeta)

        // Disable knockback for guns.
        targetEntity.setKnockbackResistance(1.0)

        plugin.scheduleTask(1L) {
            targetEntity.noDamageTicks = oldNoDamageTicks
            targetEntity.maximumNoDamageTicks = oldNoDamageTicksMax
            targetEntity.setKnockbackResistance()

            // Update health bar after the damage has been dealt.
            targetEntity.showHealthBar(plugin)
        }

        bulletMeta.assembly?.forEachBehaviour<PostBulletLandBehaviour> {
            it.afterBulletLands(bullet, bulletMeta)
        }

        bullets.remove(bullet)
    }

    @EventHandler
    fun meleeDamageBonus(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player
        val gun = damager?.gunProperties() ?: return
        // Prevent elemental damage to increase damage output.
        if (event.entity.lastDamageCause?.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return

        event.damage = max(event.damage, event.damage + gun.meleeDamage.damage)
    }

    @EventHandler
    fun removeBulletsAndSplash(event: ProjectileHitEvent) {
        val bullet = event.entity
        val bulletMeta = bullets[bullet]
        if (bulletMeta != null) {
            bullet.remove()

            val boundingBox = event.hitEntity?.boundingBox ?: event.hitBlock?.boundingBox ?: return
            val hitLocation = bullet.determineHitLocation(boundingBox) ?: bullet.location
            splashDamage(plugin, hitLocation, bulletMeta)

            bulletMeta.assembly?.forEachBehaviour<PostBulletLandBehaviour> {
                it.afterBulletLands(bullet, bulletMeta)
            }

            // Only remove the bullet when not hitting a living entity because
            // the damage event requires this bullet data to apply correct damage calculations.
            if (bulletMeta.assembly is LauncherAssembly || event.hitEntity !is LivingEntity) {
                bullets.remove(bullet)
            }
        }
    }

    @EventHandler
    fun dropReload(event: PlayerDropItemEvent) {
        val properties = event.itemDrop.gunProperties() ?: return
        val execution = obtainGunExecution(event.player, properties)

        // Allow drop when the gun is fully loaded.
        if (execution.clip >= properties.magazineSize) {
            return
        }

        // Otherwise reload the gun.
        event.isCancelled = true
        reloadGun(event.player, execution)
    }

    @EventHandler
    fun clearEffectsOnDeath(event: EntityDeathEvent) {
        // Prevent concurrent modification with scheduled task.
        plugin.scheduleTask(1L) {
            elementalStatusEffects.cleanup(event.entity)
        }
    }

    @EventHandler
    fun leaveGame(event: PlayerQuitEvent) {
        cleanup(event.player)
    }

    /**
     * Cleans up all memory for all players.
     */
    fun cleanup() {
        executions.clear()
        elementalStatusEffects.cleanup()
    }

    /**
     * Cleans up all memory for a specific player.
     */
    fun cleanup(player: Player) {
        executions.remove(player)
        elementalStatusEffects.cleanup(player)
    }

    /**
     * Apply bullet physics.
     */
    override fun run() {
        applyBulletGravity()
        cleanExpiredBullets()
        elementalStatusEffects.tick()
        particles.tick()
    }

    private fun applyBulletGravity() = bullets.forEach { (bullet, meta) ->
        val newVelocity = bullet.velocity.clone()
        newVelocity.y -= meta.gravity
        bullet.velocity = newVelocity
    }

    private fun cleanExpiredBullets() {
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