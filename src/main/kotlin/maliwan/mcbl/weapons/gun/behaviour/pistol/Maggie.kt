package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.StatModifier.Property
import maliwan.mcbl.weapons.gun.WeaponAssembly
import maliwan.mcbl.weapons.gun.applyAll
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.statModifierList

/**
 * @author Hannah Schellekens
 */
open class Maggie : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Maggie"
    override val redText = "Monty's wife don't take no guff."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            add(5, Property.PELLET_COUNT)
            subtract(0.06, Property.ACCURACY)
            divide(1.65, Property.BASE_DAMAGE)
            subtract(0.007, Property.RECOIL)
            divide(1.3, Property.MAGAZINE_SIZE)
            divide(1.0, Property.FIRE_RATE)
        }
    }
}