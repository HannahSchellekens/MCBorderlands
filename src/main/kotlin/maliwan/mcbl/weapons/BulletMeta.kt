package maliwan.mcbl.weapons

import maliwan.mcbl.Damage

/**
 * @author Hannah Schellekens
 */
data class BulletMeta(

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
    val createdAt: Long = System.currentTimeMillis()
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