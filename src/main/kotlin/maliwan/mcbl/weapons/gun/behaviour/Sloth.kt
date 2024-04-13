package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.SniperParts

/**
 * @author Hannah Schellekens
 */
open class Sloth : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Sloth"
    override val redText = "Yeah, booze'll do that to ya...."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.7, StatModifier.Property.BASE_DAMAGE)
            divide(2, StatModifier.Property.PROJECTILE_SPEED)
            divide(2, StatModifier.Property.GRAVITY)
            add(0.002, StatModifier.Property.ACCURACY)
        }
    }
}