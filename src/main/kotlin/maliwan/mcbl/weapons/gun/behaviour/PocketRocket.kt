package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.PistolParts
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

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