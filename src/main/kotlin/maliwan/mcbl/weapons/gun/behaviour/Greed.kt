package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * @author Hannah Schellekens
 */
open class Greed : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, PostGunShotBehaviour {

    override val baseName = "Greed"
    override val redText = "Thief."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        statModifiers.applyAll(properties)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always corrosive
        return assembly.replaceCapacitor(Capacitor.INCENDIARY)
    }

    override fun afterGunShot(execution: GunExecution, player: Player) {
        // Running while shooting is not possible to simulate.
        // However, granting a small short speed boost on use is in line with the theme.
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 60, 0, true, false, false))
    }

    companion object {

        val statModifiers = statModifierList {
            add(7, StatModifier.Property.RELOAD_SPEED)
            subtract(0.018, StatModifier.Property.ACCURACY)
            multiply(1.5, StatModifier.Property.ELEMENTAL_CHANCE)
        }
    }
}