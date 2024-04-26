package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.DefaultPrefixProvider
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.LauncherParts

/**
 * @author Hannah Schellekens
 */
open class Roaster(val element: Elemental) : UniqueGun, PostGenerationBehaviour, DefaultPrefixProvider {

    override val baseName = "Roaster"

    override var redText = when (element) {
        Elemental.INCENDIARY -> "Gonna cook someone today"
        else -> "Toasty!"
    }

    override var defaultPrefix = when (element) {
        Elemental.SHOCK -> "Zappy"
        else -> "Toasty"
    }

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.TEDIORE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.5, StatModifier.Property.ELEMENTAL_CHANCE)
            multiply(1.5, StatModifier.Property.ELEMENTAL_DAMAGE)
        }
    }
}