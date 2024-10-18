package maliwan.mcbl.weapons.gun.behaviour.smg

import maliwan.mcbl.loot.ammo.AmmoPack
import maliwan.mcbl.util.Ticks
import maliwan.mcbl.util.scheduleTask
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.SmgParts
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import kotlin.math.max
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class EndothermicBlaster(

    /**
     * How long it takes for alt fire to shoot.
     */
    val windupMillis: Long = 800L

) : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, PostBulletLandBehaviour,
    OverrideManufacturerOnWeaponCard, DefaultPrefixProvider, PostGunShotBehaviour, BulletSoundProvider {

    override val baseName = "Endothermic Blaster"
    override val redText = "Sorry! Sorry, I'm sorry. Sorry."
    override val defaultPrefix = ""
    override val shootSound = Sound.BLOCK_POWDER_SNOW_STEP

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.gravity = 0.0
        properties.bulletSpeed = 20.0
        properties.isPiercing = true
        properties.reloadSpeed = Ticks(30)
        properties.splashDamage = properties.baseDamage * 0.5

        properties.extraInfoText += "Crouch while shooting for highly accurate\nicicles with +150% critical damage"
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always cryo.
        return assembly.replaceCapacitor(Capacitor.CRYO)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        val altFire = player.isSneaking

        // Alternative, sniper mode.
        if (altFire) {
            val data = execution.executionData<ExecutionData>() ?: ExecutionData()

            // Counteract SMG behaviour.
            bullets.forEach {
                handler.unregisterBullet(it)
                it.remove()

                execution.clip += 1
                handler.plugin.inventoryManager[player].add(ammoCompensation)
            }

            // Shoot sniper icicle.
            if (data.isWindingUp.not()) {
                data.isWindingUp = true
                handler.plugin.scheduleTask(windupMillis / 50) {
                    // Modify bullet behaviour to match a sniper icicle.
                    execution.bulletSpeed = 70.0
                    val oldFireRate = execution.fireRate
                    execution.fireRate = 1.0 / 0.8
                    handler.shootGunBullet(player, execution, bulletMetaTransform = {
                        it.damage *= 3.5
                        it.gravity = 0.0
                        it.bonusCritMultiplier = 1.5
                        it.isPiercing = false
                    }, accuracyModifier = 0.0)
                    execution.bulletSpeed = 20.0
                    execution.fireRate = oldFireRate
                    player.playSound(player.location, Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.6f, 1.0f)

                    // Remove 4 ammo per shot instead of 1.
                    val newClip = max(0, execution.clip - 5)
                    val removed = execution.clip - newClip
                    execution.clip = newClip
                    handler.plugin.inventoryManager[player].removeAmmo(WeaponClass.SMG, removed)
                    if (execution.clip <= 0) {
                        handler.reloadGun(player, execution)
                    }

                    data.isWindingUp = false
                    execution.setExecutionData(data)
                }
                execution.setExecutionData(data)
            }
            return
        }

        // Primary fire, flame/cryothrower.
        bullets.forEach {
            it.isVisibleByDefault = false

            // Flamethrower-style: short range only!
            val meta = handler.bullets[it]
            meta?.deathTimestamp = System.currentTimeMillis() + 750L
            meta?.bonusCritMultiplier = 0.5
            meta?.canCrit = false
            meta?.particleCount = { Random.nextInt(1, 5) }
        }
    }

    override fun afterBulletLands(
        handler: WeaponEventHandler,
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    ) {
        val shooter = meta.shooter as? Player ?: return
        if (isCriticalHit) {
            shooter.playSound(shooter, Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 1.0f, 1.0f)
        }
    }

    override fun newManufacturerName(oldName: String): String = "Meiliwan"

    data class ExecutionData(var isWindingUp: Boolean = false)

    companion object {

        val statModifiers = statModifierList {
            subtract(0.025, StatModifier.Property.ACCURACY)
            multiply(1.3, StatModifier.Property.FIRE_RATE)
            multiply(1.6, StatModifier.Property.BASE_DAMAGE)
            multiply(1.2, StatModifier.Property.MAGAZINE_SIZE)
            multiply(1.2, StatModifier.Property.ELEMENTAL_CHANCE)
        }

        private val ammoCompensation = AmmoPack(WeaponClass.SMG, 1)
    }
}