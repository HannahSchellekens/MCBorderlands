package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.SmgParts

/**
 * @author Hannah Schellekens
 */
open class GoodTouch : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, DefaultPrefixProvider {

    override val baseName = "Good Touch"
    override val redText = "...but when I'm bad, I'm better."
    override val defaultPrefix = "Miss Moxxi's"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.transfusion = 0.025
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always incendiary.
        return assembly.replaceCapacitor(Capacitor.INCENDIARY)
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.7, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            multiply(0.95, StatModifier.Property.BASE_DAMAGE)
            multiply(1.15, StatModifier.Property.ELEMENTAL_DAMAGE)
            subtract(0.006, StatModifier.Property.ELEMENTAL_CHANCE)
        }
    }
}