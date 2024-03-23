package maliwan.mcbl

import kotlin.random.Random

fun Double.modifyRandom(max: Double) = this - max + 2 * Random.nextDouble() * max