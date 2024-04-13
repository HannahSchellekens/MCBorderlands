package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.ElementalStatusEffects
import maliwan.mcbl.weapons.gun.*

/**
 * @author Hannah Schellekens
 */
open class Hellfire : UniqueGun, PostGenerationBehaviour {

    override val baseName = "HellFire"
    override val redText = "We don't need no water..."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        statModifiers.applyAll(properties)

        // 50% splash damage: also enables extra proc chance.
        properties.splashDamage = properties.baseDamage * 0.5

        // Justice for BL1 HellFire.
        properties.elementalPolicy = ElementalStatusEffects.ApplyPolicy.ADD
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.025, StatModifier.Property.ELEMENTAL_CHANCE)
            multiply(1.15, StatModifier.Property.MAGAZINE_SIZE)
            subtract(0.015, StatModifier.Property.ACCURACY)
            divide(1.01, StatModifier.Property.BASE_DAMAGE)
        }
    }
}