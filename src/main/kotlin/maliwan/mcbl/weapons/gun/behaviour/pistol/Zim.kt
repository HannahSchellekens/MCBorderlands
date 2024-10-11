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
open class Zim : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Zim"
    override var redText = "Would you like to know more?"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.BANDIT.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always cryo.
        return assembly.replaceCapacitor(Capacitor.CRYO)
    }

    companion object {

        val statModifiers = statModifierList {
            subtract(10, StatModifier.Property.PROJECTILE_SPEED)
            add(0.008, StatModifier.Property.ACCURACY)
            multiply(1.5, StatModifier.Property.MAGAZINE_SIZE)
            multiply(1.5, StatModifier.Property.BASE_DAMAGE)
        }
    }
}