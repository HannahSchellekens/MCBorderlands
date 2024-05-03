package maliwan.mcbl.weapons.gun.behaviour.shotgun

import maliwan.mcbl.util.Probability
import maliwan.mcbl.util.VECTOR_UP
import maliwan.mcbl.util.modifyAccuracy
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import kotlin.math.PI

/**
 * @author Hannah Schellekens
 */
open class Hydra : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour {

    override val baseName = "Hydra"
    override val redText = "Five heads of Death!"

    val angle = 14.5

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.BANDIT.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.accuracy = Probability(0.975)
        properties.pelletCount = 20
    }

    override fun afterGunShot(handler: WeaponEventHandler, execution: GunExecution, bullets: List<Entity>, player: Player) {
        val baseDirection = player.eyeLocation.direction.normalize()
        val speed = bullets.first().velocity.length()

        val dir = baseDirection.clone()
        val rotationAxis = dir.clone().crossProduct(dir.clone().crossProduct(VECTOR_UP))

        repeat(4) {
            val bullet = bullets[it]
            bullet.velocity = dir.clone()
                .modifyAccuracy(0.025 /* Fixed accuracy */)
                .multiply(speed)
        }
        repeat(4) {
            val bullet = bullets[it + 4]
            bullet.velocity = dir.clone()
                .rotateAroundNonUnitAxis(rotationAxis, PI / angle)
                .modifyAccuracy(0.025 /* Fixed accuracy */)
                .multiply(speed)
        }
        repeat(4) {
            val bullet = bullets[it + 8]
            bullet.velocity = dir.clone()
                .rotateAroundNonUnitAxis(rotationAxis, -PI / angle)
                .modifyAccuracy(0.025 /* Fixed accuracy */)
                .multiply(speed)
        }
        repeat(4) {
            val bullet = bullets[it + 12]
            bullet.velocity = dir.clone()
                .rotateAroundNonUnitAxis(rotationAxis, PI / angle * 2)
                .modifyAccuracy(0.025 /* Fixed accuracy */)
                .multiply(speed)
        }
        repeat(4) {
            val bullet = bullets[it + 16]
            bullet.velocity = dir.clone()
                .rotateAroundNonUnitAxis(rotationAxis, -PI / angle * 2)
                .modifyAccuracy(0.025 /* Fixed accuracy */)
                .multiply(speed)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.2, StatModifier.Property.BASE_DAMAGE)
        }
    }
}