package maliwan.mcbl.weapons.gun.behaviour.smg

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor

/**
 * @author Hannah Schellekens
 */
open class Commerce : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Commerce"
    override val redText = "I have a Soldier, a Siren, two\nScooters and a Claptrap."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        // Tediore barrel damage penalty is not applied, the rest is via the companion object.
        statModifiers.applyAll(properties)

        properties.isPiercing = true
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always shock.
        return assembly.replaceCapacitor(Capacitor.SHOCK)
    }

    companion object {

        val statModifiers = statModifierList {
            divide(1.15, StatModifier.Property.RELOAD_SPEED)
            subtract(5, StatModifier.Property.PROJECTILE_SPEED)
            multiply(1.05, StatModifier.Property.BASE_DAMAGE)
        }
    }
}