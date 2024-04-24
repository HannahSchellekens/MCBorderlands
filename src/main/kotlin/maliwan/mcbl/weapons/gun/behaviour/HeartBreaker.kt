package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.ShotgunParts

/**
 * @author Hannah Schellekens
 */
open class HeartBreaker : UniqueGun, PostGenerationBehaviour, DefaultPrefixProvider, UpdateAssemblyBehaviour {

    override val baseName = "Heart Breaker"
    override val redText = "I don't want to set the world on\nfire..."
    override val defaultPrefix = "Miss Moxxi's"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.HYPERION.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        // TODO: Add Heart shaped bullet pattern.
        properties.pelletCount = 10
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always incendiary.
        return assembly.replaceCapacitor(Capacitor.INCENDIARY)
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.02, StatModifier.Property.TRANSFUSION)
            add(0.5, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            multiply(0.92, StatModifier.Property.BASE_DAMAGE)
        }
    }
}