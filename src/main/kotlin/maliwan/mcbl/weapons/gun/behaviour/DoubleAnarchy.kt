package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.SmgParts

/**
 * @author Hannah Schellekens
 */
open class DoubleAnarchy : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Anarchy"
    override val redText = "I thought the HellFire was OP..."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.TEDIORE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        return assembly.replaceCapacitor(null)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(0.78, StatModifier.Property.BASE_DAMAGE)
            add(3, StatModifier.Property.PELLET_COUNT)
            add(1, StatModifier.Property.AMMO_PER_SHOT)
            subtract(0.275, StatModifier.Property.ACCURACY)
            multiply(1.5, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}