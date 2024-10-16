package maliwan.mcbl.util

import org.bukkit.util.Vector

/**
 * Unit up vector:
 * `(0, 1, 0)`
 * DO NOT MODIFY.
 */
val VECTOR_UP = Vector(0.0, 1.0, 0.0)

/**
 * Null vector:
 * `(0, 0, 0)`
 * DO NOT MODIFY.
 */
val VECTOR_ZERO = Vector(0.0, 0.0, 0.0)

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

/**
 * Keeps the direction, but sets the length of the vector.
 */
fun Vector.setLength(length: Double): Vector {
    return normalize().multiply(length)
}

/**
 * Rotates this vector around its base.
 * Creates a copy.
 */
fun Vector.rotateRelative(pitch: Double, yaw: Double, up: Vector = VECTOR_UP): Vector {
    val pitchAxis = clone().crossProduct(up)
    val yawAxis = clone().crossProduct(pitchAxis)

    return clone().apply {
        rotateAroundNonUnitAxis(pitchAxis, pitch)
        rotateAroundNonUnitAxis(yawAxis, yaw)
    }
}

/**
 * Pointwise addition of vectors.
 */
fun Iterable<Vector>.sum(): Vector = fold(Vector()) { a, b -> a.add(b) }

/**
 * Pointwise average of vectors.
 */
fun Collection<Vector>.average(): Vector = sum() / size.toDouble()

/**
 * Adds this vector to the other vector, returns a clone and does not modify the original.
 */
operator fun Vector.plus(other: Vector): Vector = clone().add(other)

/**
 * Multiplies the vector with a scalar, returns a clone of this vector.
 */
operator fun Vector.times(scalar: Double): Vector = clone().multiply(scalar)

/**
 * Divides the vector by a scalar, returns a clone of this vector.
 */
operator fun Vector.div(scalar: Double): Vector = this * (1.0 / scalar)