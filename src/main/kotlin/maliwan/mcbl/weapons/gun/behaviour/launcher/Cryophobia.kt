package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.util.spigot.showElementalParticle
import maliwan.mcbl.weapons.*
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.BulletEffectBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import org.bukkit.entity.Entity
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Cryophobia : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, BulletEffectBehaviour {

    override val baseName = "Cryophobia"
    override val redText = "No argument here."

    val delay = 200
    val interval = 6L * 1000L / 20L

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.elementalPolicy = ElementalStatusEffects.ApplyPolicy.ADD
        properties.isPiercing = true
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always cryo.
        return assembly.replaceCapacitor(Capacitor.CRYO)
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        repeat((3000 - delay) / interval.toInt()) {
            handler.scheduleEffect(bullet, delay + interval * it) { _, projectile, _ ->
                projectile.world.createExplosion(projectile.location, 0f)
                projectile.location.showElementalParticle(Elemental.CRYO.color, Random.nextInt(5, 6), spread = 1.5)
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