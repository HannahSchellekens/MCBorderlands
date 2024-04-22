package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.SniperParts

/**
 * @author Hannah Schellekens
 */
open class ChereAmie : UniqueGun, PostGenerationBehaviour, DefaultPrefixProvider {

    override val baseName = "Chère-Amie"
    override val redText = "Je suis enchante, Ou est le\nbibliotheque?"
    override val defaultPrefix = "Miss Moxxi's"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.HYPERION.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.15, StatModifier.Property.TRANSFUSION)
            add(0.2, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
        }
    }
}