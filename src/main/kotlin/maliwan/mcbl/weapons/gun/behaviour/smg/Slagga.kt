package maliwan.mcbl.weapons.gun.behaviour.smg

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.SmgParts

/**
 * @author Hannah Schellekens
 */
open class Slagga : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Slagga"
    override var redText = "blagaga"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.BANDIT.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always slag.
        return assembly.replaceCapacitor(Capacitor.SLAG)
    }

    companion object {

        val statModifiers = statModifierList {
            add(2, StatModifier.Property.PELLET_COUNT)
            multiply(0.6, StatModifier.Property.BASE_DAMAGE)
            subtract(0.15, StatModifier.Property.ACCURACY)
            divide(0.875, StatModifier.Property.FIRE_RATE)
            multiply(1.3, StatModifier.Property.MAGAZINE_SIZE)
            multiply(1.6, StatModifier.Property.ELEMENTAL_CHANCE)
        }
    }
}