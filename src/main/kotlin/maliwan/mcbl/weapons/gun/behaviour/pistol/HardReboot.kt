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
open class HardReboot : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Hard Reboot"
    override var redText = "Have you tried turning it off\nand on again?"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always shock.
        return assembly.replaceCapacitor(Capacitor.SHOCK)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.1, StatModifier.Property.BASE_DAMAGE)
            multiply(1.5, StatModifier.Property.ELEMENTAL_DAMAGE)
            multiply(1.04, StatModifier.Property.ELEMENTAL_CHANCE)
            multiply(3, StatModifier.Property.SPLASH_RADIUS)
        }
    }
}