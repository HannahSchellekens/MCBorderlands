package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.ShotgunParts

/**
 * @author Hannah Schellekens
 */
open class RokSalt : UniqueGun, PostGenerationBehaviour {

    override val baseName = "RokSalt"
    override val redText = "Don't retreat. Instead, reload!"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.06, StatModifier.Property.BASE_DAMAGE)
            add(1, StatModifier.Property.PELLET_COUNT)
            multiply(1.3, StatModifier.Property.ACCURACY)
            multiply(1.2, StatModifier.Property.FIRE_RATE)
            multiply(0.6, StatModifier.Property.RELOAD_SPEED)
        }
    }
}