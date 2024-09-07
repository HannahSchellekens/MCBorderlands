package maliwan.mcbl.weapons.gun.behaviour.sniper

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.SniperParts

/**
 * @author Hannah Schellekens
 */
open class Longbow : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Longbow"
    override val redText = "Ceci n'est pas une sniper rifle!"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.HYPERION.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always incendiary.
        return assembly.replaceCapacitor(Capacitor.INCENDIARY)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.45, StatModifier.Property.BASE_DAMAGE)
            add(0.015, StatModifier.Property.ACCURACY)
            add(0.045, StatModifier.Property.ELEMENTAL_CHANCE)
            add(0.5, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            multiply(2, StatModifier.Property.FIRE_RATE)
            divide(2, StatModifier.Property.PROJECTILE_SPEED)
        }
    }
}