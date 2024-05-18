package maliwan.mcbl.weapons.gun.behaviour.smg

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.SmgParts

/**
 * @author Hannah Schellekens
 */
open class Emperor : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Emperor"
    override var redText = "You know... for him."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.isPiercing = true
    }

    companion object {

        val statModifiers = statModifierList {
            add(1, StatModifier.Property.BURST_COUNT)
            multiply(1.5, StatModifier.Property.BASE_DAMAGE)
            subtract(0.02, StatModifier.Property.ACCURACY)
            multiply(0.8, StatModifier.Property.FIRE_RATE)
            multiply(1.4, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}