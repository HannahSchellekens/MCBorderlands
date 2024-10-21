package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.util.VECTOR_UP
import maliwan.mcbl.util.spigot.showFlameParticle
import maliwan.mcbl.util.spigot.spawnBullet
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.StatModifier.Property
import maliwan.mcbl.weapons.gun.behaviour.BulletEffectBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.splashDamage
import org.bukkit.entity.Entity
import kotlin.math.PI

/**
 * @author Hannah Schellekens
 */
open class Sawbar : UniqueGun, PostGenerationBehaviour, BulletEffectBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Sawbar"
    override val redText = "Surpresssing Fires!!!"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Barrel.BANDIT.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always incendiary.
        return assembly.replaceCapacitor(Capacitor.INCENDIARY)
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        handler.scheduleEffect(bullet, 150L) { _, projectile, meta ->
            val direction = projectile.velocity
            val speed = direction.length()
            val world = projectile.world
            val childMeta = meta.copy(splashRadius = meta.splashRadius * 1.5, splashDamage = meta.damage)

            val rotationAxis = direction.clone().crossProduct(VECTOR_UP).crossProduct(direction).normalize()

            repeat(3) {
                val angle = 0.09
                val child = world.spawnBullet(projectile.location, direction.clone().rotateAroundNonUnitAxis(rotationAxis, -angle), speed)
                handler.registerBullet(child, childMeta)

                if (it != 2) {
                    rotationAxis.rotateAroundNonUnitAxis(direction.normalize(), PI * 2 / 3)
                }

                handler.scheduleEffect(child, 150L) { _, childProjectile, childMeta ->
                    childProjectile.world.createExplosion(childProjectile.location, 0f)
                    childProjectile.location.showFlameParticle()
                    splashDamage(handler.plugin, childProjectile.location, childMeta)
                    handler.unregisterBullet(childProjectile)
                    childProjectile.remove()
                }
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.1, Property.BASE_DAMAGE)
            multiply(1.1, Property.ACCURACY)
            multiply(1.1, Property.ELEMENTAL_DAMAGE)
            multiply(1.1, Property.ELEMENTAL_CHANCE)
            multiply(1.1, Property.ELEMENTAL_DURATION)
            multiply(0.8, Property.PROJECTILE_SPEED)
        }
    }
}