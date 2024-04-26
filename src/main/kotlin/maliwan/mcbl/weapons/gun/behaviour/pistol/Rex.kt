package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class Rex : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Rex"
    override val redText = "Basically, it's a big gun."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.fireRate = 0.8
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(2.2, StatModifier.Property.BASE_DAMAGE)
            add(0.4, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            subtract(0.006, StatModifier.Property.ACCURACY)
            multiply(1.3, StatModifier.Property.RELOAD_SPEED)
        }
    }
}