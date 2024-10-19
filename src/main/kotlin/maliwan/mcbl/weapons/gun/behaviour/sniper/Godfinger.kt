package maliwan.mcbl.weapons.gun.behaviour.sniper

import maliwan.mcbl.util.modifyAccuracy
import maliwan.mcbl.util.spawnBullet
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
open class Godfinger : UniqueGun, PostGenerationBehaviour, BulletEffectBehaviour {

    override val baseName = "Godfinger"
    override val redText = "Such a Cod Finger."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.isPiercing = true
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        repeat(9) {
            val bullets = if (it == 8) 4 else 1
            repeat(bullets) { _ ->
                handler.scheduleEffect(bullet, 200L * (it + 1L) + 90L) { _, projectile, meta ->
                    val direction = projectile.velocity
                    val speed = direction.length()
                    val world = projectile.world
                    val childMeta = meta.copy()

                    val newDirection = direction.modifyAccuracy(0.02)
                    val child = world.spawnBullet(projectile.location, newDirection, speed)
                    handler.registerBullet(child, childMeta)
                }
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.1, StatModifier.Property.BASE_DAMAGE)
            multiply(0.8, StatModifier.Property.PROJECTILE_SPEED)
            add(0.006, StatModifier.Property.ACCURACY)
        }
    }
}