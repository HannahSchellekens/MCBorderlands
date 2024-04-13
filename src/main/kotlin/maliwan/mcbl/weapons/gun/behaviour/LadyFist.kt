package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*

/**
 * @author Hannah Schellekens
 */
open class LadyFist : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Lady Fist"
    override val redText = "Love is a Lady Finger. True love is\na Lady Fist."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            add(8, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            multiply(0.75, StatModifier.Property.BASE_DAMAGE)
            add(3, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}