package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class PocketRocket : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Pocket Rocket"
    override val redText = "Did you ever have the feeling..."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.splashRadius *= 1.5
        properties.splashDamage = properties.baseDamage
    }

    companion object {

        val statModifiers = statModifierList {
            add(1, StatModifier.Property.AMMO_PER_SHOT)
            add(5, StatModifier.Property.PROJECTILE_SPEED)
            divide(0.8, StatModifier.Property.MAGAZINE_SIZE)
            multiply(1.3, StatModifier.Property.BASE_DAMAGE)
        }
    }
}