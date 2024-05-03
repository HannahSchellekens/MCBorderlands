package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.ElementalStatusEffects
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.LauncherParts

/**
 * @author Hannah Schellekens
 */
open class AvatarState : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Avatar State"
    override var redText = "Everything changed when the Vault\nHunters attacked."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.elementalPolicy = ElementalStatusEffects.ApplyPolicy.ADD

        val damage = properties.elementalDamage[Elemental.SHOCK]!!
        val chance = properties.elementalProbability[Elemental.SHOCK]!!

        // Shock already added (Water).
        // Corrosion (Earth).
        properties.addElement(Elemental.CORROSIVE, damage, Elemental.CORROSIVE.baseDotDuration * 1.8, chance)
        // Incendiary (Fire).
        properties.addElement(Elemental.INCENDIARY, damage, Elemental.INCENDIARY.baseDotDuration, chance)
        // Air (Explosive).
        properties.addElement(Elemental.EXPLOSIVE, damage, Elemental.EXPLOSIVE.baseDotDuration, chance)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Start with shock only, add the rest onFinishGeneration.
        return assembly.replaceCapacitor(Capacitor.SHOCK)
    }

    companion object {

        val statModifiers = statModifierList {
            add(1, StatModifier.Property.MAGAZINE_SIZE)
            add(1, StatModifier.Property.AMMO_PER_SHOT)
            multiply(0.85, StatModifier.Property.BASE_DAMAGE)
            multiply(1.5, StatModifier.Property.ELEMENTAL_DURATION)
            multiply(1.2, StatModifier.Property.SPLASH_RADIUS)
        }
    }
}