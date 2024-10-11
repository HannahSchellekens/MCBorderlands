package maliwan.mcbl.weapons

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.entity.*
import maliwan.mcbl.gui.DamageParticles
import maliwan.mcbl.util.*
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.*
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.block.BlockFace
import org.bukkit.entity.*
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
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

typealias ScheduledBulletEffect = (handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) -> Unit

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
     * All scheduled bullet effects.
     * A list of Tuples: `(milli time for effect execution, affected bullet, action)`.
     */
    val scheduledBulletEffects = PriorityQueue<BulletEffect>()

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

            var customExecution: GunExecution? = null
            gunProperties.assembly?.forEachBehaviour<CustomGunExecutionBehaviour<*>> {
                customExecution = it.createGunExecution(gunProperties)
                return@forEachBehaviour
            }
            newExecution = customExecution ?: GunExecution(gunProperties)

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
                val fireRate = execution.fireRate

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
                val scheduleProbability = Probability(max(0.0, excess / 6.0))
                val fireRateBursts = baseBurstCount + if (Random.nextDouble() < scheduleProbability) 1 else 0

                fun executeGunBurst() {
                    // Bursts
                    repeat(execution.burstCount) { burstIndex ->
                        // Determine beforehand if the gun will be shot.
                        val inventory = plugin.inventoryManager[this]
                        val ammoLeft = inventory[execution.weaponClass]
                        val enoughAmmo = execution.clip > 0 && ammoLeft > 0

                        // First one must not be scheduled:
                        if (burstIndex == 0) {
                            if (execution.extraShotProbability.roll()) {
                                shootGun(this, execution, consumeAmmo = false)
                            }

                            shootGun(this, execution, consumeAmmo = execution.freeShotProbability.roll().not())
                        }
                        // Subsequent ones must be scheduled by burst delay:
                        else {
                            plugin.scheduleTask(execution.burstDelay.long * burstIndex) {
                                shootGun(this, execution, consumeAmmo = execution.freeShotProbability.roll().not())
                            }
                        }

                        // Make sure the recoil angle is not applied when there are no shots
                        // left.
                        if (enoughAmmo) {
                            plugin.scheduleTask(execution.burstDelay.long * burstIndex) {
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
    fun shootGun(player: Player, gunExecution: GunExecution, consumeAmmo: Boolean = true, triggerAfterGunShot: Boolean = true) {
        val inventory = plugin.inventoryManager[player]
        val ammoLeft = inventory[gunExecution.weaponClass]
        if (gunExecution.clip <= 0 || ammoLeft <= 0) {
            return
        }

        gunExecution.assembly?.forEachBehaviour<PreGunShotBehaviour> {
            it.beforeGunShot(gunExecution, player)
        }

        // Shoot for each pellet.
        val bullets = ArrayList<Entity>()
        repeat(gunExecution.pelletCount) { _ ->
            shootGunBullet(player, gunExecution)?.let { bullets.add(it) }
        }
        player.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS, 5.0f, 1.0f)

        if (consumeAmmo) {
            removeAmmo(player, gunExecution)
        }

        if (triggerAfterGunShot) {
            gunExecution.assembly?.forEachBehaviour<PostGunShotBehaviour> {
                it.afterGunShot(this, gunExecution, bullets, player)
            }
            gunExecution.assembly?.forEachBehaviour<TalkWhenFiring> {
                player.sendGunMessage(gunExecution, it.messageFiring())
            }
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
        gunExecution.assembly?.forEachBehaviour<TalkWhenReloading> {
            player.sendGunMessage(gunExecution, it.messageReloading())
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

    fun shootGunBullet(player: LivingEntity, gunExecution: GunExecution, directionDelta: Vector? = null): Entity? {
        val bulletType = gunExecution.assembly?.behaviours?.first<BulletTypeProvider, EntityType> {
            it.bulletType
        } ?: EntityType.ARROW

        val playerDirection = player.eyeLocation.direction.clone()
        val initialDirection = gunExecution.bulletPattern.nextRotation(playerDirection)
        val direction = initialDirection.add(directionDelta ?: Vector())

        val bullet = player.shootBullet(player.eyeLocation, direction, gunExecution, bulletType) ?: return null
        val bulletMeta = gunExecution.bulletMeta(player)
        bullets[bullet] = bulletMeta

        val millisDelay = (1000.0 / gunExecution.fireRate).toLong()
        val nextShotAllowedOn = System.currentTimeMillis() + millisDelay
        shotTimestamps[player] = nextShotAllowedOn

        gunExecution.assembly?.behaviours?.forEachType<BulletEffectBehaviour> {
            it.scheduleEffects(this, bullet, bulletMeta)
        }

        return bullet
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

        if (bulletMeta.directDamage.not()) {
            // Just splash damage: is handled by ProjectileHitEvent.
            bulletMeta.assembly?.forEachBehaviour<PostBulletLandBehaviour> {
                it.afterBulletLands(bullet, bulletMeta, null, null)
            }

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

        // Cryo damage bonus.
        val cryoMultiplier = if (Elemental.EXPLOSIVE in bulletMeta.elements) {
            elementalStatusEffects.cryoDamageMultiplier(targetEntity)
        }
        else 1.0

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
        if (isCritical && critMultiplier > 1.01) {
            plugin.damageParticles.showCritDisplay(particleLocation.clone())
        }

        // Apply damage
        event.damage = bulletMeta.damage.damage * elementalModifier * cryoMultiplier *
                elementalStatusEffects.slagMultiplier(targetEntity) * critMultiplier

        // Apply armor.
        val damageClasses = DamageClass.damageClassesOf(bulletMeta)
        val armorPoints = targetEntity.armorPoints
        val toughness = targetEntity.armorToughness
        val enchants = targetEntity.enchantmentProtectionFactor(damageClasses)
        val armourModifier = armorDamageMultiplier(event.damage, armorPoints, toughness, enchants)

        // (partially) revert modifier when the bullets penetrate armour.
        val armourModifierAfterPenetration = armourModifier + (1.0 - armourModifier) * bulletMeta.armourPenetration

        // And apply shock damage multiplier when there is armour present.
        val shockModifier = if (armorPoints > 0.00001 && Elemental.SHOCK in bulletMeta.elements) {
            2.0
        }
        else 1.0
        event.damage *= armourModifierAfterPenetration * shockModifier

        // Apply elemental effect.
        rollElementalDot(plugin, targetEntity, bulletMeta)

        // Show damage amount as particle.
        plugin.damageParticles.scheduleDisplay(DamageParticles.DamageParticleEntry(
            targetEntity,
            hitLocation?.clone() ?: particleLocation.clone(),
            Elemental.PHYSICAL,
            event.damage
        ))

        // Disable knockback for guns.
        targetEntity.setKnockbackResistance(1.0)

        plugin.scheduleTask(1L) {
            targetEntity.noDamageTicks = oldNoDamageTicks
            targetEntity.maximumNoDamageTicks = oldNoDamageTicksMax
            targetEntity.setKnockbackResistance()

            // Update health bar after the damage has been dealt.
            targetEntity.showHealthBar(plugin)
        }

        // Transfusion effect.
        val healAmount = event.finalDamage * bulletMeta.transfusion
        bulletMeta.shooter.heal(healAmount)

        bulletMeta.assembly?.forEachBehaviour<PostBulletLandBehaviour> {
            it.afterBulletLands(bullet, bulletMeta, hitLocation, targetEntity)
        }

        bullets.remove(bullet)
    }

    @EventHandler
    fun meleeDamageBonus(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player
        val gun = damager?.gunProperties()
        // Prevent elemental damage to increase damage output.
        if (event.entity.lastDamageCause?.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return

        // Cryo damage bonus.
        val target = event.entity as? LivingEntity
        val cryoMultiplier = target?.let {
            elementalStatusEffects.cryoDamageMultiplier(it)
        } ?: 1.0

        val newDamage = max(event.damage, (event.damage + (gun?.meleeDamage?.damage ?: 0.0)) * cryoMultiplier)
        event.damage = newDamage

        // Don't show particles for melee, because otherwise they are registered twice - showing twice as much
        // damage than is actually inflicted.
    }

    @EventHandler
    fun removeBulletsAndSplash(event: ProjectileHitEvent) {
        val bullet = event.entity
        val world = bullet.world
        val location = bullet.location
        val direction = bullet.velocity

        val bulletMeta = bullets[bullet]
        if (bulletMeta != null) {
            // Bounce if bounces are left:
            if (event.hitEntity == null && event.hitBlock != null && event.hitBlockFace != null && bulletMeta.bouncesLeft > 0) {
                val newBullet = bounceBullet(bullet, event.hitBlockFace!!)
                event.isCancelled = true
                bulletMeta.bouncesLeft--
                replaceScheduledEffects(bullet, newBullet)

                bullet.remove()
                bullets.remove(bullet)
                bullets[newBullet] = bulletMeta


                bulletMeta.assembly?.forEachBehaviour<PostBulletBounceBehaviour> {
                    it.afterBulletBounce(this, newBullet, bulletMeta)
                }

                return
            }

            bullet.remove()

            val boundingBox = event.hitEntity?.boundingBox ?: event.hitBlock?.boundingBox ?: return
            val hitLocation = bullet.determineHitLocation(boundingBox) ?: location
            splashDamage(plugin, hitLocation, bulletMeta)

            bulletMeta.assembly?.forEachBehaviour<PostBulletLandBehaviour> {
                it.afterBulletLands(bullet, bulletMeta, hitLocation, null)
            }

            // Only remove the bullet when not hitting a living entity because
            // the damage event requires this bullet data to apply correct damage calculations.
            if (bulletMeta.assembly is LauncherAssembly || event.hitEntity !is LivingEntity) {
                bullets.remove(bullet)
            }

            // Check if bullet must pierce, if so, create new bullet.
            if (event.hitEntity != null && bulletMeta.isPiercing) {
                // Continue slightly ahead to prevent getting stuck in the target.
                val target = event.hitEntity!!
                val size = max(target.boundingBox.widthX, target.boundingBox.widthZ) * 1.3
                val ahead = location.add(direction.clone().normalize().multiply(max(size, target.height)))
                val newBullet = world.spawnBullet(ahead, direction, direction.length(), type = bullet.type)
                registerBullet(newBullet, bulletMeta.copy())
            }
        }
    }

    /**
     * Bounces the given bullet off the given surface, returns the newly created projectile.
     * Does not delete the original.
     */
    fun bounceBullet(bullet: Entity, surface: BlockFace): Entity {
        val newVelocity = bullet.velocity.clone()
        when (surface) {
            BlockFace.EAST, BlockFace.WEST -> newVelocity.setX(newVelocity.x * -1.0)
            BlockFace.UP, BlockFace.DOWN -> newVelocity.setY(newVelocity.y * -1.0)
            BlockFace.NORTH, BlockFace.SOUTH -> newVelocity.setZ(newVelocity.z * -1.0)
            else -> Unit
        }

        bullet.velocity = newVelocity

        val newBullet = bullet.world.spawnEntity(bullet.location, bullet.type)
        newBullet.velocity = newVelocity
        return newBullet
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
        cleanExpiredBullets()
        applyBulletGravity()
        homingEffect()
        flushExpiredEffects()
        elementalStatusEffects.tick()
        particles.tick()
    }

    /**
     * Registers this bullet, so it will apply the MCBorderlands weapon engine to it.
     */
    fun registerBullet(bullet: Entity, bulletMeta: BulletMeta) {
        bullets[bullet] = bulletMeta
    }

    /**
     * Unregisters this bullet; removes it from the weapon engine.
     */
    fun unregisterBullet(bullet: Entity) {
        bullets.remove(bullet)
    }

    /**
     * Schedules an effect to be executed by the bullet if it still exists.
     *
     * @param bullet
     *          The bullet entity that must execute the effect.
     * @param inMillis
     *          In how many milliseconds the effect must be executed.
     * @param action
     *          The action to execute when the effect should be executed.
     */
    fun scheduleEffect(bullet: Entity, inMillis: Long, action: ScheduledBulletEffect) {
        if (bullet !in bullets) {
            return
        }
        val scheduleTime = System.currentTimeMillis() + inMillis
        scheduledBulletEffects.add(BulletEffect(scheduleTime, bullet, action))
    }

    /**
     * Replaces all effects of `oldBullet` to be executed for `newBullet`.
     */
    fun replaceScheduledEffects(oldBullet: Entity, newBullet: Entity) {
        val toAdd = ArrayList<BulletEffect>()
        scheduledBulletEffects.forEach {
            if (it.bullet == oldBullet) {
                toAdd += BulletEffect(it.scheduledMillis, newBullet, it.effect)
            }
        }
        scheduledBulletEffects.removeIf { it.bullet == oldBullet }
        toAdd.forEach {
            scheduledBulletEffects.add(it)
        }
    }

    private fun flushExpiredEffects() {
        // Priority queue: sorted by lowest number, so first execution time.
        val now = System.currentTimeMillis()
        while (scheduledBulletEffects.isNotEmpty()) {
            val first = scheduledBulletEffects.first()
            val (executeTime, bullet, effect) = first

            // Only execute on existing bullets.
            val bulletMeta = bullets[bullet]
            if (bulletMeta == null) {
                scheduledBulletEffects.remove(first)
                continue
            }

            // There are no effects left to schedule.
            if (now < executeTime) {
                break
            }

            effect(this, bullet, bulletMeta)
            scheduledBulletEffects.remove(first)
        }
    }

    private fun applyBulletGravity() = bullets.forEach { (bullet, meta) ->
        val newVelocity = bullet.velocity.clone()
        newVelocity.y -= meta.gravity
        // Prevent slow arrows from completely halting.
        if (newVelocity.length() < 4.0) {
            newVelocity.multiply(1.01)
        }
        bullet.velocity = newVelocity
    }

    private fun homingEffect() = bullets.forEach { (bullet, meta) ->
        if (meta.homingStrength > 0.00001) {
            tickHomingBullet(bullet, meta)
        }
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

    /**
     * @author Hannah Schellekens
     */
    data class BulletEffect(
        val scheduledMillis: Long,
        val bullet: Entity,
        val effect: ScheduledBulletEffect
    ) : Comparable<BulletEffect> {

        override fun compareTo(other: BulletEffect): Int {
            return scheduledMillis.compareTo(other.scheduledMillis)
        }
    }
}