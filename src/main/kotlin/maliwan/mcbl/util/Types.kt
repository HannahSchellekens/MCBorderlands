package maliwan.mcbl.util

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * 20 ticks per second.
 */
@JvmInline
value class Ticks(val ticks: Int) {

    /**
     * Converted to seconds.
     */
    val seconds: Double
        get() = ticks.toDouble() / 20.0

    /**
     * Long value.
     */
    val long: Long
        get() = ticks.toLong()

    /**
     * Converted to milliseconds.
     */
    val millis: Long
        get() = long * 50L

    operator fun times(other: Number) = Ticks((ticks * other.toDouble()).toInt())
    operator fun plus(other: Number) = Ticks((ticks + other.toDouble()).toInt())
}

@JvmInline
value class Seconds(val seconds: Double) {

    val ticks: Int
        get() = (20.0 / seconds).toInt()
}

@JvmInline
value class Probability(val chance: Double) {

    init {
        require(chance in 0.0..1.0) { "Chance $chance must be between 0.0 and 1.0, got <$chance>" }
    }

    val percentageDisplay: String
        get() = "${"%.1f".format(chance * 100)}%"

    fun roll() = Random.nextDouble() <= chance

    companion object {

        val ONE = Probability(1.0)
        val ZERO = Probability(0.0)
    }
}

/**
 * Automatically fits to the range [0.0,1.0], negative values become 0.0, larger than 1 becomes 1.0.
 */
fun Double.toChance() = Probability(min(max(0.0, this), 1.0))

operator fun Double.compareTo(probability: Probability): Int = this.compareTo(probability.chance)

@JvmInline
value class Damage(val damage: Double) {

    val heartDisplay: String
        get() {
            return "%.2f♥".format(damage)
        }

    val display: String
        get() {
            return "%.2f".format(damage)
        }

    operator fun times(other: Double) = Damage(damage * other)
    operator fun plus(other: Double) = Damage(damage + other)
    operator fun plus(other: Damage) = Damage(damage + other.damage)

    companion object {

        val ZERO = Damage(0.0)
    }
}