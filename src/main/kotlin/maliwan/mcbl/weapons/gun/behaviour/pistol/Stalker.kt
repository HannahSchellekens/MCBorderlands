package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class Stalker : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Stinger"
    override val redText = "You can run, but you can't hide."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(0.33, StatModifier.Property.PROJECTILE_SPEED)
            add(5, StatModifier.Property.BOUNCE_COUNT)
            divide(5, StatModifier.Property.GRAVITY)
            multiply(1.2, StatModifier.Property.BASE_DAMAGE)
            multiply(0.85, StatModifier.Property.ACCURACY)
            multiply(1.35, StatModifier.Property.MAGAZINE_SIZE)
            multiply(1.3, StatModifier.Property.FIRE_RATE)
        }
    }
}