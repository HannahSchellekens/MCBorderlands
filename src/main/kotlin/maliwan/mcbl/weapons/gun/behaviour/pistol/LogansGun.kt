package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.util.plugin.Ticks
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class LogansGun : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Logan's Gun"
    override var redText = "Gun, Gunner!"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.HYPERION.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.directDamage = true
        properties.splashDamage = properties.baseDamage
        properties.splashRadius = 0.85
        properties.bonusCritMultiplier = -1.0

        properties.addElement(Elemental.EXPLOSIVE, properties.splashDamage, Ticks(0), Probability.ONE)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always incendiary.
        return assembly.replaceCapacitor(Capacitor.INCENDIARY)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.15, StatModifier.Property.BASE_DAMAGE)
            divide(2, StatModifier.Property.FIRE_RATE)
            multiply(1.65, StatModifier.Property.ELEMENTAL_DAMAGE)
            add(0.0420, StatModifier.Property.ELEMENTAL_CHANCE)
            add(1, StatModifier.Property.AMMO_PER_SHOT)
        }
    }
}