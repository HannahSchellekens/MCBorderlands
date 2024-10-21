package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.util.modifyAccuracy
import maliwan.mcbl.util.spigot.spawnBullet
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.BulletEffectBehaviour
import maliwan.mcbl.weapons.gun.behaviour.DefaultPrefixProvider
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import org.bukkit.entity.Entity

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
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        handler.scheduleEffect(bullet, 250L) { _, projectile, meta ->
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
                val newDirection = direction.modifyAccuracy(0.425)
                val child = world.spawnBullet(projectile.location, newDirection, speed)
                handler.registerBullet(child, childMeta)
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.2, StatModifier.Property.BASE_DAMAGE)
            subtract(10, StatModifier.Property.PROJECTILE_SPEED)
            add(0.02, StatModifier.Property.TRANSFUSION)
        }
    }
}