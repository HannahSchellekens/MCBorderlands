package maliwan.mcbl.weapons.gun.pattern

import org.bukkit.util.Vector

/**
 * @author Hannah Schellekens
 */
interface BulletPatternProcessor {

    /**
     * Rotates this vector in space to match with the next rotation in the bullet pattern.
     * Preserving length.
     *
     * Does not modify the base vector, creates a copy.
     *
     * @param base
     *          The vector direction to start rotation from.
     * @return A new vector object pointing in the direction of the next bullet pattern location preserving speed.
     */
    fun nextRotation(base: Vector): Vector
}

/**
 * Does not alter the base vector at all.
 * Produces copies to adhere to the interface contract.
 *
 * @author Hannah Schellekens
 */
object NoBulletPattern : BulletPatternProcessor {

    override fun nextRotation(base: Vector) = base.clone()
}