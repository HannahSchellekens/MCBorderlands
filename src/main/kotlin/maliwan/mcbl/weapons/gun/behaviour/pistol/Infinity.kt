package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.util.modifyRandom
import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.BulletPatternProvider
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts
import maliwan.mcbl.weapons.gun.pattern.SequentialBulletPattern
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Infinity : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour, BulletPatternProvider {

    override val baseName = "Infinity"
    override val redText = "It's closer than you think!\n(no it isn't)"

    override val bulletPatternProcessor = SequentialBulletPattern.of(
        (1..30).map { pitchFunction(it / 30.0) to yawFunction(it / 30.0) }
    )

    fun pitchFunction(step /* in [0,1] */: Double, amplitude: Double = 0.035): Double {
        return amplitude * sin(step * 4 * PI)
    }

    fun yawFunction(step /* in [0,1] */: Double, amplitude: Double = 0.0625): Double {
        return when (step) {
            in 0.0..0.25 -> (step / 0.25) * -amplitude
            in 0.25..0.75 -> -amplitude + ((step - 0.25) / 0.5) * amplitude * 2
            else -> amplitude - ((step - 0.75) / 0.25) * amplitude
        }
    }

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.VLADOF.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.accuracy = Probability(0.983.modifyRandom(0.0075))
        properties.magazineSize = 1
        properties.ammoPerShot = 0
        properties.recoil = 1.0
        properties.pelletCount = 1
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        // Dva prefix gives 25% chance to fire twice.
        val assembly = execution.assembly as? PistolAssembly
        if (assembly?.accessory == PistolParts.Accessory.DOUBLE && Random.nextDouble() < 0.25) {
            handler.shootGun(player, execution, triggerAfterGunShot = false)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            divide(1.1, StatModifier.Property.FIRE_RATE)
            multiply(1.1, StatModifier.Property.BASE_DAMAGE)
        }
    }
}