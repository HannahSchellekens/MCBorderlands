package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.PistolParts
import maliwan.mcbl.weapons.gun.parts.SniperParts
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * @author Hannah Schellekens
 */
open class Cobra : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Cobra"
    override val redText = "Found out about this, I was like,\n\"DAAAMN, I'm bringing that gun BACK!\""

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.splashDamage = properties.baseDamage * 0.8
        properties.splashRadius = 0.5
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always explosive.
        return assembly.replaceCapacitor(Capacitor.EXPLOSIVE)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(0.4, StatModifier.Property.FIRE_RATE)
            multiply(1.15 * 1.1, StatModifier.Property.BASE_DAMAGE)
        }
    }
}