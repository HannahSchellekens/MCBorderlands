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
open class Globber : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Globber"
    override val redText = "Lob those globs!"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.splashDamage = properties.baseDamage
        properties.bounces = 3
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always corrosive.
        return assembly.replaceCapacitor(Capacitor.CORROSIVE)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.08, StatModifier.Property.BASE_DAMAGE)
            subtract(0.003, StatModifier.Property.ACCURACY)
            multiply(0.8, StatModifier.Property.FIRE_RATE)
            subtract(2, StatModifier.Property.MAGAZINE_SIZE)
            multiply(2, StatModifier.Property.ELEMENTAL_CHANCE)
            multiply(0.3, StatModifier.Property.PROJECTILE_SPEED)
            multiply(2, StatModifier.Property.GRAVITY)
        }
    }
}