package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class Rubi : UniqueGun, PostGenerationBehaviour, DefaultPrefixProvider {

    override val baseName = "Rubi"
    override val redText = "Whenever I'm caught between two\nevils, I take the one I've never tried."
    override val defaultPrefix = "Miss Moxxi's"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.12, StatModifier.Property.TRANSFUSION)
            multiply(0.95, StatModifier.Property.BASE_DAMAGE)
        }
    }
}