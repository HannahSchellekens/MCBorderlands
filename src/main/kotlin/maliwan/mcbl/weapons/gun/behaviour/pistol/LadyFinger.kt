package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class LadyFinger : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Lady Finger"
    override val redText = "Omnia vincit amor"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            add(1, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            add(0.015, StatModifier.Property.ACCURACY)
            multiply(1.1, StatModifier.Property.BASE_DAMAGE)
        }
    }
}