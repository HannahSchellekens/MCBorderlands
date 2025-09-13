package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.weapons.ElementalStatusEffects
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class Infection : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Infection"
    override val redText = "Itchy. Tasty."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.splashDamage *= 0.8
        properties.splashRadius *= 1.2
        properties.elementalPolicy = ElementalStatusEffects.ApplyPolicy.ADD

        val baseDamage = properties.baseDamage
        val elementalDamage = properties.elementalDamage.entries.first().value
        val elements = properties.elements
        properties.baseDamage = elementalDamage

        elements.forEach { elemental ->
            properties.elementalDamage[elemental] = baseDamage * 0.75
        }
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always corrosive.
        return assembly.replaceCapacitor(Capacitor.CORROSIVE)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.15, StatModifier.Property.MAGAZINE_SIZE)
            multiply(1.5, StatModifier.Property.ELEMENTAL_CHANCE)
            multiply(1.1, StatModifier.Property.GRAVITY)
        }
    }
}