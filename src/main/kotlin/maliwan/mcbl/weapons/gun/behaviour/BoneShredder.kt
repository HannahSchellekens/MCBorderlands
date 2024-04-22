package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.SmgParts

/**
 * @author Hannah Schellekens
 */
open class BoneShredder : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Bone Shredder"
    override val redText = "The Lead Wind Blows!"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.BANDIT.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            add(2, StatModifier.Property.PELLET_COUNT)
            add(1, StatModifier.Property.AMMO_PER_SHOT)
            multiply(0.8, StatModifier.Property.BASE_DAMAGE)
            subtract(0.15, StatModifier.Property.ACCURACY)
        }
    }
}