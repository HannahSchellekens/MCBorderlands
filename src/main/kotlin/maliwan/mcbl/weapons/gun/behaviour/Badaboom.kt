package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.LauncherParts

/**
 * @author Hannah Schellekens
 */
open class Badaboom : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Badaboom"
    override var redText = "Multi-kill."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.BANDIT.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(2, StatModifier.Property.PELLET_COUNT)
            multiply(0.97, StatModifier.Property.BASE_DAMAGE)
            subtract(0.25, StatModifier.Property.ACCURACY)
        }
    }
}