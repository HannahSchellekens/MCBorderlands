package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts

/**
 * @author Hannah Schellekens
 */
open class DamnedCowboy : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Damned Cowboy"
    override val redText = "Speak Softly. Carry This."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Barrel.BANDIT.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.65, StatModifier.Property.BASE_DAMAGE)
            add(0.55, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            divide(2, StatModifier.Property.FIRE_RATE)
            add(0.025, StatModifier.Property.ACCURACY)
        }
    }
}