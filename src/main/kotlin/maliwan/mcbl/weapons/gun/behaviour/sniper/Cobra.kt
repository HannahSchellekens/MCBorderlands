package maliwan.mcbl.weapons.gun.behaviour.sniper

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.DefaultPrefixProvider
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.SniperParts

/**
 * @author Hannah Schellekens
 */
open class Cobra : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, DefaultPrefixProvider {

    override val baseName = "Cobra"
    override val redText = "Found out about this, I was like,\n\"DAAAMN, I'm bringing that gun BACK!\""
    override val defaultPrefix = "Tiny Tina's"

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