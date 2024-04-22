package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class FibberCrit : UniqueGun, PostGenerationBehaviour, FibWeaponCard {

    override val baseName = "FibberC"
    override val redText = "Would I lie to you?"
    override val fibMultiplierBase = 0.25
    override val fibMultiplierFuzz = 0.1
    override val showGeneratedInfo = false

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.BANDIT.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.extraInfoText.clear()
        properties.extraInfoText += "Sustained fire increases accuracy"
        properties.extraInfoText += "+50% love"
        properties.extraInfoText += "+3000% Damage"
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.2, StatModifier.Property.BASE_DAMAGE)
            add(5, StatModifier.Property.BOUNCE_COUNT)
            add(7, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            multiply(0.55, StatModifier.Property.PROJECTILE_SPEED)
            multiply(4.2, StatModifier.Property.GRAVITY)
        }
    }
}