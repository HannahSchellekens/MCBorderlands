package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.ElementalStatusEffects
import maliwan.mcbl.weapons.gun.*

/**
 * @author Hannah Schellekens
 */
open class Defiler : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Defiler"
    override val redText = "Give Sick"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        statModifiers.applyAll(properties)

        properties.splashRadius *= 1.5
        properties.reloadSpeed = Ticks((properties.reloadSpeed.ticks * 1.2).toInt())
    }

    companion object {

        val statModifiers = statModifierList {
            // +15% damage on base. Negate the -20% base damage penalty.
            multiply(1.32, StatModifier.Property.BASE_DAMAGE)
            add(1.0, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            divide(1.8, StatModifier.Property.MAGAZINE_SIZE)
            add(0.05, StatModifier.Property.ELEMENTAL_CHANCE)
            multiply(1.1, StatModifier.Property.ELEMENTAL_DAMAGE)
        }
    }
}