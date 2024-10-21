package maliwan.mcbl.weapons.gun.behaviour.sniper

import maliwan.mcbl.util.VECTOR_UP
import maliwan.mcbl.util.spigot.spawnBullet
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.BulletEffectBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.SniperParts
import org.bukkit.entity.Entity

/**
 * @author Hannah Schellekens
 */
open class Lyudmila : UniqueGun, PostGenerationBehaviour, BulletEffectBehaviour {

    override val baseName = "Lyudmila"
    override val redText = "Man killer."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.VLADOF.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        handler.scheduleEffect(bullet, 120L) { _, projectile, meta ->
            val direction = projectile.velocity
            val speed = direction.length()
            val world = projectile.world
            val childMeta = bulletMeta.copy(damage = meta.damage * 0.75)

            val rotationAxis = direction.clone().crossProduct(VECTOR_UP).crossProduct(direction).normalize()

            val angle = 0.08
            val childLeft = world.spawnBullet(projectile.location, direction.clone().rotateAroundNonUnitAxis(rotationAxis, -angle), speed)
            val childRight = world.spawnBullet(projectile.location, direction.clone().rotateAroundNonUnitAxis(rotationAxis, angle), speed)
            handler.registerBullet(childLeft, childMeta)
            handler.registerBullet(childRight, childMeta)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.08, StatModifier.Property.BASE_DAMAGE)
            add(0.15, StatModifier.Property.ACCURACY)
            add(0.25, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            add(0.001, StatModifier.Property.RECOIL)
        }
    }
}