package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.StatModifier.Property
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts

/**
 * @author Hannah Schellekens
 */
open class HammerBuster : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Hammer Buster"
    override val redText = "Gar! Gorarr! My dad's a scientist!\nGWARRRR!!!!"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.65, Property.BONUS_CRIT_MULTIPLIER)
            multiply(1.6, Property.BASE_DAMAGE)
        }
    }
}