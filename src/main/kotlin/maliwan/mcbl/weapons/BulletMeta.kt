package maliwan.mcbl.weapons

import maliwan.mcbl.Chance
import maliwan.mcbl.Damage
import maliwan.mcbl.Ticks
import org.bukkit.entity.LivingEntity

/**
 * @author Hannah Schellekens
 */
data class BulletMeta(

    /**
     * Who shot the bullet.
     */
    val shooter: LivingEntity,

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
    val lifespanSeconds: Double = 3.0,

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
    val elementalChance: MutableMap<Elemental, Chance> = HashMap(),

    /**
     * How many ticks each elemental effect lasts when applied.
     */
    val elementalDuration: MutableMap<Elemental, Ticks> = HashMap(),

    /**
     * How much damage each elemental effect deals per 0.5 seconds.
     */
    val elementalDamage: MutableMap<Elemental, Damage> = HashMap(),

    /**
     * How large the splash damage radius is, 0.0 for no splash damage.
     */
    var splashRadius: Double = 0.0,

    /**
     * How much splash damage to deal on impact.
     */
    var splashDamage: Damage = Damage(0.0),
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