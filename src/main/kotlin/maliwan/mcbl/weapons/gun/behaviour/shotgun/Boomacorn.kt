package maliwan.mcbl.weapons.gun.behaviour.shotgun

import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.util.plugin.Ticks
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import maliwan.mcbl.weapons.gun.stats.*
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

/**
 * @author Hannah Schellekens
 */
open class Boomacorn : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Boomacorn"
    override val redText = "Always, I want to be with you."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.pelletCount = 5
        properties.splashDamage = properties.baseDamage
        properties.ammoPerShot = 2
        properties.magazineSize = 2
        properties.reloadSpeed = Ticks(46)
        properties.extraInfoText += "+50% weapon damage"
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // No base element, always jakobs parts.
        return assembly
            .replaceCapacitor(null)
            .replacePart(ShotgunParts.Grip.JAKOBS)
            .replacePart(ShotgunParts.Stock.JAKOBS)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        player.playSound(player, Sound.ENTITY_HORSE_DEATH, SoundCategory.PLAYERS, 1.0f, 1.0f)

        bullets.forEach {
            val meta = handler.bullets[it] ?: return

            val element = Elemental.elementals.random()

            val eltDamage = ShotgunBaseValues.baseValue(Manufacturer.JAKOBS, Stat.elementalDamage)
            val modifier = ShotgunGradeModifiers.modifier(Rarity.RARE, Modifier.elementalDamage)
            val stdDev = ShotgunGradeModifiers.standardDeviation(Rarity.RARE, StandardDeviation.elementalDamage)

            val eltChance = ShotgunBaseValues.baseValue(Manufacturer.JAKOBS, Stat.elementalProbability)
            val chanceModifier = ShotgunGradeModifiers.modifier(Rarity.RARE, Modifier.elementalChance)

            val elementalDamage = eltDamage * modifier + Random().nextGaussian() * stdDev * element.noDotMultiplier
            meta.addElement(element, element.baseDotDuration, elementalDamage, Probability(eltChance.chance * chanceModifier))
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.5, StatModifier.Property.BASE_DAMAGE)
            add(0.025, StatModifier.Property.ACCURACY)
        }
    }
}