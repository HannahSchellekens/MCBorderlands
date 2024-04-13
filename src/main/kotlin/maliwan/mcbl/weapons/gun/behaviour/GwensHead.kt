package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.PistolParts
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * @author Hannah Schellekens
 */
open class GwensHead : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Gwen's Head"
    override val redText = "Thinking outside the box."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.HYPERION.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.burstCount = 7
        // Fire rate of 1 makes it burst 1 time per second.
        properties.fireRate = 1.0
        properties.burstDelay = Ticks(1)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always TEDIORE grip.
        return assembly.replacePart(PistolParts.Grip.TEDIORE)
    }

    companion object {

        val statModifiers = statModifierList {
            // No multiplicative crit bonus.
            add(0.375, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            multiply(1.4, StatModifier.Property.MAGAZINE_SIZE)
            multiply(1.2, StatModifier.Property.BASE_DAMAGE)
            add(0.005, StatModifier.Property.ACCURACY)
        }
    }
}