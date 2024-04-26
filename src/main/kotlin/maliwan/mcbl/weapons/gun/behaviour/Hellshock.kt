package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.PistolParts
import maliwan.mcbl.weapons.splashDamage
import org.bukkit.entity.Entity

/**
 * @author Hannah Schellekens
 */
open class Hellshock : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, PostBulletBounceBehaviour {

    override val baseName = "Hellshock"
    override val redText = "I had not thought death had undone\nso many."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.bounces = 1
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always start incendiary.
        return assembly.replaceCapacitor(Capacitor.INCENDIARY)
    }

    override fun afterBulletBounce(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        splashDamage(handler.plugin, bullet.location, bulletMeta)

        if (Elemental.INCENDIARY !in bulletMeta.elements) return

        bulletMeta.apply {
            elementalChance[Elemental.SHOCK] = elementalChance[Elemental.INCENDIARY]!!
            elementalDamage[Elemental.SHOCK] = elementalDamage[Elemental.INCENDIARY]!!
            elementalDuration[Elemental.SHOCK] = elementalDuration[Elemental.INCENDIARY]!!
            elements.add(Elemental.SHOCK)

            elementalChance.remove(Elemental.INCENDIARY)
            elementalDamage.remove(Elemental.INCENDIARY)
            elementalDuration.remove(Elemental.INCENDIARY)
            elements.remove(Elemental.INCENDIARY)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            add(1, StatModifier.Property.PELLET_COUNT)
            multiply(1.8, StatModifier.Property.MAGAZINE_SIZE)
            subtract(0.05, StatModifier.Property.ACCURACY)
        }
    }
}