package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class HectorsParadise : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Hector's Paradise"
    override var redText = "The whip at the New Pandoran's\nbacks."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.splashDamage = properties.baseDamage * 0.8
        properties.name = baseName /* No prefixes */
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always shock and bladed.
        return assembly.replaceCapacitor(Capacitor.SHOCK).replacePart(PistolParts.Accessory.BAYONET)
    }

    companion object {

        val statModifiers = statModifierList {
            add(1, StatModifier.Property.BURST_COUNT)
            divide(1.03, StatModifier.Property.BASE_DAMAGE)
            subtract(0.083, StatModifier.Property.ACCURACY)
            multiply(1.25, StatModifier.Property.ELEMENTAL_CHANCE)
        }
    }
}