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
open class Law : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Law"
    override val redText = "De Da."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        // No order shield effects yet as shields are TBA.
    }

    companion object {

        val statModifiers = statModifierList {
            add(6.0, StatModifier.Property.MELEE)
        }
    }
}