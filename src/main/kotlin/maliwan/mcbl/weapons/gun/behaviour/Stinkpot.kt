package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts
import maliwan.mcbl.weapons.gun.parts.Capacitor
import org.bukkit.entity.EntityType

/**
 * @author Hannah Schellekens
 */
open class Stinkpot : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, BulletTypeProvider {

    override val baseName = "Stinkpot"
    override val redText = "We're pirates. We don't follow the\nrules"
    override val bulletType = EntityType.LLAMA_SPIT

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.bounces = 1
        properties.directDamage = false
        properties.splashDamage = properties.baseDamage
        properties.splashRadius = 0.85
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always corrosive.
        return assembly.replaceCapacitor(Capacitor.CORROSIVE)
    }

    companion object {

        val statModifiers = statModifierList {
            add(1, StatModifier.Property.BOUNCE_COUNT)
            multiply(1.12, StatModifier.Property.BASE_DAMAGE)
        }
    }
}