package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.util.spigot.showFlameParticle
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.ElementalStatusEffects
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.BulletEffectBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import maliwan.mcbl.weapons.splashDamage
import org.bukkit.entity.Entity

/**
 * @author Hannah Schellekens
 */
open class Pyrophobia : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, BulletEffectBehaviour {

    override val baseName = "Pyrophobia"
    override val redText = "It's actually a fairly rational fear in\nthis case."

    val delay = 200
    val interval = 6L * 1000L / 20L

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.elementalPolicy = ElementalStatusEffects.ApplyPolicy.ADD
        properties.isPiercing = true
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always incendiary.
        return assembly.replaceCapacitor(Capacitor.INCENDIARY)
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        repeat((3000 - delay) / interval.toInt()) {
            handler.scheduleEffect(bullet, delay + interval * it) { _, projectile, _ ->
                projectile.world.createExplosion(projectile.location, 0f)
                projectile.location.showFlameParticle()
                splashDamage(handler.plugin, projectile.location, bulletMeta)
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.06, StatModifier.Property.BASE_DAMAGE)
            multiply(0.9, StatModifier.Property.RELOAD_SPEED)
            subtract(10, StatModifier.Property.PROJECTILE_SPEED)
        }
    }
}