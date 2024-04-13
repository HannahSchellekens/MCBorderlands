package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * @author Hannah Schellekens
 */
open class Judge : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Judge"
    override val redText = "I am free now."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.3, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
        }
    }
}