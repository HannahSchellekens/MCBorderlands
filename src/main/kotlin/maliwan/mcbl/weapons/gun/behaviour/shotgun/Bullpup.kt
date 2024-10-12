package maliwan.mcbl.weapons.gun.behaviour.shotgun

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.ShotgunParts

/**
 * @author Hannah Schellekens
 */
open class Bullpup : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Bullpup"
    override val redText = "One bad pup!"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.HYPERION.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        return assembly.replacePart(ShotgunParts.Grip.HYPERION)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.2, StatModifier.Property.MAGAZINE_SIZE)
            add(0.5, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            add(0.02, StatModifier.Property.ACCURACY)
            multiply(1.1, StatModifier.Property.FIRE_RATE)
            divide(1.1, StatModifier.Property.RELOAD_SPEED)
        }
    }
}