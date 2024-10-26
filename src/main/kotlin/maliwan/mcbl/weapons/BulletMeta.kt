package maliwan.mcbl.weapons

import maliwan.mcbl.util.plugin.Damage
import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.util.plugin.Ticks
import maliwan.mcbl.weapons.gun.WeaponAssembly
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import java.util.*

/**
 * @author Hannah Schellekens
 */
data class BulletMeta(

    /**
     * Who shot the bullet.
     */
    val shooter: LivingEntity,

    /**
     * The assembly of the weapon that shot this bullet.
     */
    val assembly: WeaponAssembly?,

    /**
     * How much damage to deal by this bullet.
     */
    var damage: Damage,

    /**
     * Downward acceleration (+ value is downward, - is upward) of the bullet.
     */
    var gravity: Double = 0.016,

    /**
     * How long the bullet stays alive.
     */
    val lifespanSeconds: Double = 6.5,

    /**
     * When the bullet is created.
     */
    val createdAt: Long = System.currentTimeMillis(),

    /**
     * Which elements bullets fired with this gun apply to the target.
     * Order of application is the order in this list.
     */
    val elements: MutableList<Elemental> = ArrayList(),

    /**
     * The chance each element is applied to the target.
     */
    val elementalProbability: MutableMap<Elemental, Probability> = EnumMap(Elemental::class.java),

    /**
     * How many ticks each elemental effect lasts when applied.
     */
    val elementalDuration: MutableMap<Elemental, Ticks> = EnumMap(Elemental::class.java),

    /**
     * How much damage each elemental effect deals per 0.5 seconds.
     */
    val elementalDamage: MutableMap<Elemental, Damage> = EnumMap(Elemental::class.java),

    /**
     * What to do when the target already has the same elemental effect.
     */
    val elementalPolicy: ElementalStatusEffects.ApplyPolicy = ElementalStatusEffects.ApplyPolicy.REPLACE,

    /**
     * How large the splash damage radius is, 0.0 for no splash damage.
     */
    var splashRadius: Double = 0.0,

    /**
     * How much splash damage to deal on impact.
     */
    var splashDamage: Damage = Damage(0.0),

    /**
     * Extra bonus critical hit multiplier.
     * Normally a critical is x2 damage. The bonus crit multiplier multiplies this multiplier.
     * `null` for no bonus crit multiplier.
     */
    var bonusCritMultiplier: Double? = null,

    /**
     * The percentage of damage that must be converted to healing for the shooter.
     * Negative values damage the user.
     * A value of 0.0 means that the bullet does not apply transfusion healing.
     */
    val transfusion: Double = 0.0,

    /**
     * How many times the bullet can still bounce.
     */
    var bouncesLeft: Int = 0,

    /**
     * Whether the bullet should pierce entities or not.
     */
    var isPiercing: Boolean = false,

    /**
     * Whether the bullets themselves will deal the listed damage on the weapon card.
     */
    var directDamage: Boolean = true,

    /**
     * How many ticks to look forward for potential targets to home in on.
     */
    var homingTargetDistance: Double = 0.0,

    /**
     * In how many blocks radius to look for possible targets after traversing [homingTargetDistance].
     */
    var homingTargetRadius: Double = 0.0,

    /**
     * How quickly to home in on targets.
     * 0.0 means no homing.
     * 1.0 means instant homing.
     */
    var homingStrength: Double = 0.0,

    /**
     * The target to home in to.
     */
    var homingTarget: LivingEntity? = null,

    /**
     * How bullets affect armour damage reduction.
     * 1.0 means that no armour reduction is applied.
     * 0.0-1.0 means that only part of the armour reduction is applied.
     * 0.0 means that the regular armour reduction is applied.
     */
    var armourPenetration: Double = 0.0,

    /**
     * Whether this bullet can score critical hits (`true`) or not (`false`).
     */
    var canCrit: Boolean = true,

    /**
     * How many particles to show per tick during flight.
     */
    var particleCount: () -> Int =  { 1 },

    /**
     * The original location of the bullet (where it was spawned).
     * `null` if irrelevant or not specified.
     */
    var originLocation: Location? = null,

    /**
     * Whether hits with this bullet should mark targets as tracked.
     */
    var isTrackerBullet: Boolean = false
) {

    /**
     * The unix timestamp where the bullet should die.
     */
    var deathTimestamp: Long = createdAt + (lifespanSeconds * 1000).toLong()

    /**
     * Checks whether the bullet has exceeded its lifespan.
     */
    fun isDead(): Boolean = System.currentTimeMillis() >= deathTimestamp

    /**
     * Adds an element to this bullet.
     */
    fun addElement(
        elemental: Elemental,
        duration: Ticks,
        damage: Damage,
        effectchance: Probability,
    ) {
        elements.add(elemental)
        elementalDuration[elemental] = duration
        elementalDamage[elemental] = damage
        elementalProbability[elemental] = effectchance
    }

    /**
     * How long the bullet is allowed to be alive before it despawns.
     */
    fun setLifespan(millis: Long) {
        deathTimestamp = System.currentTimeMillis() + millis
    }
}