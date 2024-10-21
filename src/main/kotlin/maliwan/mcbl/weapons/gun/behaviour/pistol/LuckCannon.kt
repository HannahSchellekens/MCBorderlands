package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.util.plugin.Ticks
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PreBulletLandBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * @author Hannah Schellekens
 */
open class LuckCannon(

    val minimumChance: Probability = Probability(0.1),
    val maximumChance: Probability = Probability(0.6),
    val minimumDistance: Double = 1.0,
    val maximumDistance: Double = 30.0

) : UniqueGun, PostGenerationBehaviour, PreBulletLandBehaviour {

    override val baseName = "Luck Cannon"
    override val redText = "Better lucky than good!"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.magazineSize = 1
    }

    override fun beforeBulletLands(
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        wouldBeCritical: Boolean
    ) {
        val origin = meta.originLocation
        val distance = if (hitLocation == null || origin == null) 0.0 else hitLocation.distance(origin)

        val chance = if (distance <= minimumDistance) {
            minimumChance
        }
        else if (distance >= maximumDistance) {
            maximumChance
        }
        else {
            val probabilityRange = maximumChance.chance - minimumChance.chance
            val normalizedRange = maximumDistance - minimumDistance
            val normalizedDistance = distance - minimumDistance
            val percentage = normalizedDistance / normalizedRange
            Probability(minimumChance.chance + (probabilityRange * percentage))
        }

        if (chance.roll()) {
            meta.splashDamage = meta.damage * 4.0
            meta.addElement(Elemental.EXPLOSIVE, Ticks(0), meta.splashDamage, Probability.ONE)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.6, StatModifier.Property.BASE_DAMAGE)
            multiply(0.9, StatModifier.Property.RELOAD_SPEED)
            add(0.015, StatModifier.Property.ACCURACY)
        }
    }
}