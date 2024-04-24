package maliwan.mcbl.weapons

import maliwan.mcbl.util.Chance
import maliwan.mcbl.util.Damage
import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.gun.WeaponAssembly
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
    val damage: Damage,

    /**
     * Downward acceleration (+ value is downward, - is upward) of the bullet.
     */
    val gravity: Double = 0.016,

    /**
     * How long the bullet stays alive.
     */
    val lifespanSeconds: Double = 4.5,

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
    val elementalChance: MutableMap<Elemental, Chance> = EnumMap(Elemental::class.java),

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
    var directDamage: Boolean = true
) {

    /**
     * The unix timestamp where the bullet should die.
     */
    val deathTimestamp: Long = createdAt + (lifespanSeconds * 1000).toLong()

    /**
     * Checks whether the bullet has exceeded its lifespan.
     */
    fun isDead(): Boolean = System.currentTimeMillis() >= deathTimestamp
}