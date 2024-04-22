package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.SmgParts

/**
 * @author Hannah Schellekens
 */
open class BadTouch : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, ReloadBehaviour, DefaultPrefixProvider {

    override val baseName = "Bad Touch"
    override val redText = "When I'm good, I'm very good..."
    override val defaultPrefix = "Miss Moxxi's"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.transfusion = 0.02
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always corrosive.
        return assembly.replaceCapacitor(Capacitor.CORROSIVE)
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.7, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            multiply(0.95, StatModifier.Property.BASE_DAMAGE)
        }
    }
}