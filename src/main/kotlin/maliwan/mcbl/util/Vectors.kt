package maliwan.mcbl.util

import org.bukkit.util.Vector

/**
 * Unit up vector:
 * `(0, 1, 0)`
 */
val VECTOR_UP = Vector(0.0, 1.0, 0.0)

/**
 * Randomly changes the x, y and z values of this vector.
 * Max delta is in `[-maxModifier, maxModifier]` per dimension.
 * Creates a new vector and does not modify the original.
 */
fun Vector.modifyAccuracy(maxModifier: Double) = Vector(
    x.modifyRandom(maxModifier),
    y.modifyRandom(maxModifier),
    z.modifyRandom(maxModifier)
)