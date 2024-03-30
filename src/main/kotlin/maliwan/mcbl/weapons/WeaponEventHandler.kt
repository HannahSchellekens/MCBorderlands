package maliwan.mcbl.weapons

import maliwan.mcbl.*
import maliwan.mcbl.entity.headLocation
import maliwan.mcbl.entity.headshotRange
import maliwan.mcbl.util.*
import maliwan.mcbl.weapons.gun.GunExecution
import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.gunProperties
import maliwan.mcbl.weapons.gun.shootBullet
import org.bukkit.*
import org.bukkit.attribute.Attribute
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
import kotlin.math.*
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
    val elementalStatusEffects = ElementalStatusEffects()

    /**
     * Checks if the player already has an existing GunExecution with the given properties.
     */
    fun obtainGunExecution(player: Player, gunProperties: GunProperties): GunExecution {
        val gunExecutions = executions.getOrDefault(player, HashMap())
        val newExecution = gunExecutions.getOrDefault(gunProperties, GunExecution(gunProperties))
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
                        // First one must not be scheduled:
                        if (burstIndex == 0) {
                            shootGun(this, execution)
                        }
                        // Subsequent ones must be scheduled by burst delay:
                        else {
                            plugin.server.scheduler.scheduleSyncDelayedTask(
                                plugin,
                                Runnable {
                                    shootGun(this, execution)
                                },
                                execution.burstDelay.long * burstIndex
                            )
                        }

                        // After each burst, update accuracy.
                        // TODO: Gun property proxy.

                        applyRecoil(execution)
                    }
                    execution.consecutiveShots++
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

    /**
     * Executes one gunshot from the given gun execution.
     */
    fun shootGun(player: Player, gunExecution: GunExecution) {
        if (gunExecution.clip <= 0) {
            return
        }

        // Shoot for each pellet.
        repeat(gunExecution.pelletCount) { _ ->
            player.shootGunBullet(gunExecution)
        }
        player.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS, 5.0f, 1.0f)
        removeAmmo(player, gunExecution)

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

        // Set clip to 0 to prevent shooting during reload.
        gunExecution.clip = 0

        val reloadTicks = gunExecution.reloadSpeed.ticks
        player.noActionTicks = reloadTicks
        player.setCooldown(Material.BOW, reloadTicks)
        player.sendTitle("", "${ChatColor.RED}Reloading...", 5, reloadTicks - 10, 5)
        player.playSound(player.location, Sound.BLOCK_GRAVEL_HIT, SoundCategory.PLAYERS, 1.0f, 1.0f)

        repeat(reloadTicks / 10) {
            plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
                player.playSound(player.location, Sound.BLOCK_GRAVEL_HIT, SoundCategory.PLAYERS, 1.0f, 1.0f)
            }, it * 10L)
        }

        plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
            gunExecution.clip = min(gunExecution.magazineSize, plugin.inventoryManager[player][gunExecution.weaponClass])
            player.playSound(player.location, Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 0.7f, 1.0f)
        }, reloadTicks.toLong())
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
        val bulletMeta = bullets[bullet] ?: return

        // Disable iframes for bullets, save old values to restore them later.
        val oldNoDamageTicks = targetEntity.noDamageTicks
        val oldNoDamageTicksMax = targetEntity.maximumNoDamageTicks

        targetEntity.noDamageTicks = 0
        targetEntity.maximumNoDamageTicks = 0

        // Set damage to gun damage.
        val armorPoints = targetEntity.getAttribute(Attribute.GENERIC_ARMOR)?.value?.roundToInt() ?: 0
        val effectivenessType = EffectivenessType.typeOf(event.entityType)
        val elementalModifier = bulletMeta.elements.fold(1.0) { acc, elemental ->
            val elementDamageModifier = effectivenessType.damageMultiplier(elemental, armorPoints)
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

        // Apply damage & elemental effect
        event.damage = bulletMeta.damage.damage * elementalModifier *
                elementalStatusEffects.slagMultiplier(targetEntity) * critMultiplier

        rollElementalDot(targetEntity, bulletMeta)

        plugin.server.scheduler.scheduleSyncDelayedTask(plugin, Runnable {
            targetEntity.noDamageTicks = oldNoDamageTicks
            targetEntity.maximumNoDamageTicks = oldNoDamageTicksMax
        }, 2L)

        bullets.remove(bullet)
    }

    /**
     * Calculates whether splash damage must occur, and applies it.
     */
    fun splashDamage(location: Location, bulletMeta: BulletMeta) = bulletMeta.elements.forEach { element ->
        if (element == Elemental.PHYSICAL) return@forEach

        // Explosive does boom, contrary to the DoT effects of the other elements.
        if (element == Elemental.EXPLOSIVE) {
            val chance = bulletMeta.elementalChance[element] ?: return@forEach
            if (chance.throwDice()) {
                val radius = bulletMeta.splashRadius
                location.world?.createExplosion(location, 0f)
                location.nearbyEntities(radius).forEach entities@{ target ->
                    if (target !is LivingEntity) return@entities
                    val slag = elementalStatusEffects.slagMultiplier(target)
                    target.damage(bulletMeta.splashDamage.damage * slag, bulletMeta.shooter)
                }
            }
        }
        // Other elements do not do boom, but they do hurt. Proc roll is incorporated in [rollElementalDot].
        else {
            val radius = bulletMeta.splashRadius

            // Show small splash animation at location.
            repeat((8 * radius).toInt()) {
                val splashLocation = location.clone().add(
                    0.0.modifyRandom(radius),
                    0.0.modifyRandom(radius),
                    0.0.modifyRandom(radius)
                )
                splashLocation.showElementalParticle(element.color, 1, size = 1.3f)
            }

            // Use slightly larger radius because locations are counted from the ground position of the entity
            // this compensates for larger entities.
            location.nearbyEntities(radius + 0.25).forEach entities@{ target ->
                if (target !is LivingEntity) return@entities
                /* Slag cannot enhance its own damage */
                val slag = if (element == Elemental.SLAG) 1.0 else elementalStatusEffects.slagMultiplier(target)
                target.damage(bulletMeta.splashDamage.damage * slag, bulletMeta.shooter)
                rollElementalDot(target, bulletMeta)
            }
        }
    }

    /**
     * Rolls if an elemental (DoT) effect can proc.
     * The elemental effect will be applied to `target` and gets its info from `bulletMeta`.
     */
    fun rollElementalDot(target: LivingEntity, bulletMeta: BulletMeta) {
        bulletMeta.elements.asSequence()
            .filter { it == Elemental.SLAG || (bulletMeta.elementalDamage[it]?.damage ?: 0.0) > 0.01 }
            .forEach {
                // Check if the effect will be procced.
                val chance = bulletMeta.elementalChance[it] ?: Chance.ZERO
                if (chance.throwDice().not()) return@forEach

                // Apply effect.
                val duration = bulletMeta.elementalDuration[it] ?: return@forEach
                val damage = bulletMeta.elementalDamage[it]!!
                val policy = bulletMeta.elementalPolicy

                val statusEffect = ElementalStatusEffect(it, duration, damage, inflictedBy = bulletMeta.shooter)
                elementalStatusEffects.applyEffect(target, statusEffect, policy)
            }
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
            splashDamage(hitLocation, bulletMeta)

            // Only remove the bullet when not hitting a living entity because
            // the damage event requires this bullet data to apply correct damage calculations.
            if (event.hitEntity !is LivingEntity) {
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
        plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
            elementalStatusEffects.cleanup(event.entity)
        }, 1L)
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