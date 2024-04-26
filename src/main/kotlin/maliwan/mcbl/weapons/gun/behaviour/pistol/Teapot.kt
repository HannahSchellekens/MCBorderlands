package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.DefaultPrefixProvider
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class Teapot : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, DefaultPrefixProvider {

    override val baseName = "Teapot"
    override val redText = "Havin' a tea Partay, drinkin' mah\nTeeea!"
    override val defaultPrefix = "Tiny Tina's"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.TEDIORE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.splashDamage = properties.baseDamage * 0.8
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always corrosive
        return assembly.replaceCapacitor(Capacitor.CORROSIVE)
    }

    companion object {

        val statModifiers = statModifierList {
            divide(1.02, StatModifier.Property.BASE_DAMAGE)
            add(0.025, StatModifier.Property.ACCURACY)
            multiply(0.75, StatModifier.Property.MAGAZINE_SIZE)
            divide(1.5, StatModifier.Property.ELEMENTAL_CHANCE)
            subtract(1, StatModifier.Property.BURST_COUNT)
        }
    }
}