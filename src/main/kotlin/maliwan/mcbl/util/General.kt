package maliwan.mcbl.util

import kotlin.random.Random

/**
 * Creates a list with only this element.
 */
fun <T> T.asList() = listOf(this)

fun Double.modifyRandom(max: Double) = this - max + 2 * Random.nextDouble() * max

/**
 * Formats a multiplier to a percentage string.
 */
fun Double.formatPercentage(decimals: Int = 0) = "%.${decimals}f".format(this * 100) + "%"