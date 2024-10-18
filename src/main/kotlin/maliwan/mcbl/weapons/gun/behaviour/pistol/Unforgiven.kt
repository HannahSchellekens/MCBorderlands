package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class Unforgiven : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Unforgiven"
    override val redText = "It's a helluva thing..."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.35, StatModifier.Property.BASE_DAMAGE)
            add(0.01, StatModifier.Property.ACCURACY)
            add(0.75, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
        }
    }
}