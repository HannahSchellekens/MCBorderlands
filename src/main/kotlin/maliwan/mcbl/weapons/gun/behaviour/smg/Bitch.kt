package maliwan.mcbl.weapons.gun.behaviour.smg

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.SmgParts

/**
 * @author Hannah Schellekens
 */
open class Bitch : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Bitch"
    override val redText = "yup. back."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.HYPERION.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.15, StatModifier.Property.BASE_DAMAGE)
            multiply(1.35, StatModifier.Property.MAGAZINE_SIZE)
            add(0.50, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
        }
    }
}