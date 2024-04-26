package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts

/**
 * @author Hannah Schellekens
 */
open class Stomper : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Stomper"
    override val redText = "Oh, sorry, was that your head?"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(0.9, StatModifier.Property.BASE_DAMAGE)
            add(0.5, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
        }
    }
}