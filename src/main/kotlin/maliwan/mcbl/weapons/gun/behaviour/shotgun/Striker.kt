package maliwan.mcbl.weapons.gun.behaviour.shotgun

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.StatModifier.Property
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.ShotgunParts

/**
 * @author Hannah Schellekens
 */
open class Striker : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Striker"
    override val redText = "Fandir? Thirteen."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.5, Property.BONUS_CRIT_MULTIPLIER)
            multiply(1.06, Property.BASE_DAMAGE)
            multiply(1.5, Property.ACCURACY)
            add(1, Property.MAGAZINE_SIZE)
        }
    }
}