package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.util.VECTOR_UP
import maliwan.mcbl.util.spawnBullet
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.PistolParts
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import kotlin.math.PI

/**
 * @author Hannah Schellekens
 */
open class FibberRicochet : UniqueGun, PostGenerationBehaviour, FibWeaponCard, PostBulletBounceBehaviour {

    override val baseName = "Fibber"
    override val redText = "Would I lie to you?"
    override val fibMultiplierBase = 3.3
    override val fibMultiplierFuzz = 0.84
    override val showGeneratedInfo = false

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.BANDIT.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.extraInfoText.clear()
        properties.extraInfoText += "Sustained fire increases accuracy"
        properties.extraInfoText += "+50% love"
        properties.extraInfoText += "+3000% Damage"

        properties.bounces = 1
    }

    override fun afterBulletBounce(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        fun spawnBullet(newDirection: Vector) {
            val child = bullet.world.spawnBullet(bullet.location, newDirection, bullet.velocity.length())
            handler.registerBullet(child, bulletMeta.copy())
        }

        val dir = bullet.velocity.normalize()
        val rotationAxis = dir.clone().crossProduct(dir.clone().crossProduct(VECTOR_UP))

        repeat(8) {
            spawnBullet(dir.clone().rotateAroundNonUnitAxis(rotationAxis, PI / 50))
            rotationAxis.rotateAroundNonUnitAxis(dir, PI / 4)
        }
        spawnBullet(dir)

        handler.unregisterBullet(bullet)
        bullet.remove()
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(0.8, StatModifier.Property.BASE_DAMAGE)
        }
    }
}