package maliwan.mcbl.weapons.gun.behaviour.smg

import maliwan.mcbl.util.Chance
import maliwan.mcbl.util.Damage
import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.*
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.SmgParts
import maliwan.mcbl.weapons.gun.stats.*
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Hannah Schellekens
 */
open class Chulainn : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Chulainn"
    override val redText = "Riastrad!"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.TEDIORE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        val eltDamage = SmgBaseValues.baseValue(Manufacturer.MALIWAN, Stat.elementalDamage)
        val modifier = SmgGradeModifiers.modifier(Rarity.RARE, Modifier.elementalDamage)
        val stdDev = SmgGradeModifiers.standardDeviation(Rarity.RARE, StandardDeviation.elementalDamage)

        val eltChance = SmgBaseValues.baseValue(Manufacturer.MALIWAN, Stat.elementalChance)
        val chanceModifier = SmgGradeModifiers.modifier(Rarity.RARE, Modifier.elementalChance)

        val elementalDamage = eltDamage * modifier + Random().nextGaussian() * stdDev
        properties.addElement(Elemental.SHOCK, elementalDamage, Elemental.SHOCK.baseDotDuration, Chance(eltChance.chance * chanceModifier))

        properties.splashDamage = properties.baseDamage * 0.5
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always slag in combination with shock (applied after generation).
        return assembly.replaceCapacitor(Capacitor.SLAG)
    }

    override fun afterGunShot(handler: WeaponEventHandler, execution: GunExecution, bullets: List<Entity>, player: Player) {
        // Self-slag.
        handler.elementalStatusEffects.applyEffect(player, ElementalStatusEffect(
            Elemental.SLAG,
            Ticks(80),
            Damage(0.0),
            inflictedBy = player
        ))
    }

    companion object {

        val statModifiers = statModifierList {
            divide(1.06, StatModifier.Property.BASE_DAMAGE)
            subtract(0.12, StatModifier.Property.ACCURACY)
            multiply(1.2, StatModifier.Property.FIRE_RATE)
            multiply(0.9, StatModifier.Property.RELOAD_SPEED)
            multiply(1.3, StatModifier.Property.MAGAZINE_SIZE)
            multiply(1.7, StatModifier.Property.ELEMENTAL_CHANCE)
        }
    }
}