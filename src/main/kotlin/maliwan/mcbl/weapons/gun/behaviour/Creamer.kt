package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.util.modifyRandom
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import maliwan.mcbl.weapons.gun.parts.PistolParts
import org.bukkit.Bukkit
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.util.Vector

/**
 * @author Hannah Schellekens
 */
open class Creamer : UniqueGun, PostGenerationBehaviour, DefaultPrefixProvider, BulletEffectBehaviour {

    override val baseName = "Creamer"
    override val redText = "non-dairy"
    override val defaultPrefix = "Miss Moxxi's"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.transfusion = 0.02
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        handler.scheduleEffect(bullet, 120L) { it, projectile, meta ->
            val direction = projectile.velocity
            val speed = direction.length()
            val world = projectile.world
            val childMeta = BulletMeta(
                meta.shooter,
                meta.assembly,
                meta.damage * 0.8,
                gravity = meta.gravity,
                splashRadius = meta.splashRadius,
                splashDamage = meta.damage * 0.8,
                transfusion = meta.transfusion
            )

            repeat(2) {
                val accuracyModifier = 0.425
                val newDirection = Vector(
                    direction.x.modifyRandom(accuracyModifier),
                    direction.y.modifyRandom(accuracyModifier),
                    direction.z.modifyRandom(accuracyModifier)
                )
                val child = world.spawnEntity(projectile.location, EntityType.ARROW) as Arrow
                child.velocity = newDirection.normalize().multiply(speed)
                child.setGravity(false)

                handler.registerBullet(child, childMeta)
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.2, StatModifier.Property.BASE_DAMAGE)
            subtract(10, StatModifier.Property.PROJECTILE_SPEED)
        }
    }
}