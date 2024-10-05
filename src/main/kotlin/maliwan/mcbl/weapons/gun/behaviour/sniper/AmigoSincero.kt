package maliwan.mcbl.weapons.gun.behaviour.sniper

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.SniperParts

/**
 * @author Hannah Schellekens
 */
open class AmigoSincero : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Amigo Sincero"
    override val redText = "A true friend can penetrate any\nbarrier."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.armourPenetration = GunProperties.FULL_ARMOUR_PENETRATION
        properties.extraInfoText += "+150% damage"
    }

    companion object {

        val statModifiers = statModifierList {
            divide(2, StatModifier.Property.FIRE_RATE)
            multiply(2.5, StatModifier.Property.BASE_DAMAGE)
        }
    }
}