package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts

/**
 * @author Hannah Schellekens
 */
open class Shredifier : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Shredifier"
    override val redText = "Speed Kills."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Barrel.VLADOF_MINIGUN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.03, StatModifier.Property.BASE_DAMAGE)
            add(0.05, StatModifier.Property.ACCURACY)
            multiply(1.2, StatModifier.Property.FIRE_RATE)
            multiply(1.45, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}