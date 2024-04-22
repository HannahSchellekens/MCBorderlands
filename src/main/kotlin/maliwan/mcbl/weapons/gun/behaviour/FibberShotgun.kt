package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class FibberShotgun : UniqueGun, PostGenerationBehaviour, FibWeaponCard {

    override val baseName = "FibberS"
    override val redText = "Would I lie to you?"
    override val fibMultiplierBase = 1.75
    override val fibMultiplierFuzz = 0.35
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
            add(7, StatModifier.Property.PELLET_COUNT)
            add(1, StatModifier.Property.AMMO_PER_SHOT)
            multiply(0.7, StatModifier.Property.BASE_DAMAGE)
            divide(3.5, StatModifier.Property.PROJECTILE_SPEED)
            divide(3, StatModifier.Property.GRAVITY)
            divide(2, StatModifier.Property.FIRE_RATE)
        }
    }
}