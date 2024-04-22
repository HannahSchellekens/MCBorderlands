package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.loot.gen.CapacitorTable
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.gun.*
import org.bukkit.entity.EntityType

/**
 * @author Hannah Schellekens
 */
open class PlasmaCaster : CustomBaseNameProvider, PostGenerationBehaviour, UpdateAssemblyBehaviour, BulletTypeProvider {

    override val baseName = "Plasma Caster"
    override val bulletType = EntityType.SNOWBALL

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        when (properties.manufacturer) {
            Manufacturer.BANDIT -> banditStatModifiers.applyAll(properties)
            Manufacturer.HYPERION -> hyperionStatModifiers.applyAll(properties)
            else -> Unit
        }

        properties.splashDamage = properties.baseDamage * 0.5
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always have a non-explosive element.
        val lootTable = CapacitorTable.elementOnlyNoExplosive
        return assembly.replaceCapacitor(lootTable.roll())
    }

    companion object {

        val banditStatModifiers = statModifierList {
            multiply(0.778, StatModifier.Property.BASE_DAMAGE)
            multiply(0.857, StatModifier.Property.ELEMENTAL_DAMAGE)
            multiply(1.1, StatModifier.Property.FIRE_RATE)
        }

        val hyperionStatModifiers = statModifierList {
            subtract(0.03, StatModifier.Property.ACCURACY)
        }
    }
}