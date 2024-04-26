package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class Tinderbox : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Tinderbox"
    override val redText = "Good for starting fires."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.splashDamage = properties.baseDamage
        properties.name = properties.name.replace("Fire Fire", "Flynt's")
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.5, StatModifier.Property.GRAVITY)
            add(1, StatModifier.Property.AMMO_PER_SHOT)
            multiply(0.85, StatModifier.Property.BASE_DAMAGE)
            multiply(2, StatModifier.Property.ELEMENTAL_CHANCE)
            multiply(0.78, StatModifier.Property.FIRE_RATE)
        }
    }
}