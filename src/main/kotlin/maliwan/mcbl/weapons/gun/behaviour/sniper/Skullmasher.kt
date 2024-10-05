package maliwan.mcbl.weapons.gun.behaviour.sniper

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.SniperParts

/**
 * @author Hannah Schellekens
 */
open class Skullmasher : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Skullmasher"
    override val redText = "Makes your brain hurt."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(0.35, StatModifier.Property.BASE_DAMAGE)
            add(4, StatModifier.Property.PELLET_COUNT)
            multiply(0.92, StatModifier.Property.ACCURACY)
        }
    }
}