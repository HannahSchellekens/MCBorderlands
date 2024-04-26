package maliwan.mcbl.weapons.gun.behaviour.smg

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.SmgParts

/**
 * @author Hannah Schellekens
 */
open class Anarchy : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Anarchy"
    override val redText = "This gun has no red text."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.TEDIORE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        return assembly.replaceCapacitor(null)
    }

    companion object {

        val statModifiers = statModifierList {
            divide(1.04, StatModifier.Property.BASE_DAMAGE)
            add(1, StatModifier.Property.PELLET_COUNT)
            subtract(0.20, StatModifier.Property.ACCURACY)
            multiply(1.3, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}