package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.SniperParts

/**
 * @author Hannah Schellekens
 */
open class Buffalo : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Buffalo"
    override val redText = "Bison Bison had had had had had\nBison Bison Bison shi shi shi."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            subtract(0.007, StatModifier.Property.ACCURACY)
            multiply(1.5, StatModifier.Property.BASE_DAMAGE)
            divide(2, StatModifier.Property.FIRE_RATE)
        }
    }
}