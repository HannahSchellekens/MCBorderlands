package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class Law : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Law"
    override val redText = "De Da."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        // No order shield effects yet as shields are TBA.
    }

    companion object {

        val statModifiers = statModifierList {
            add(6.0, StatModifier.Property.MELEE)
        }
    }
}