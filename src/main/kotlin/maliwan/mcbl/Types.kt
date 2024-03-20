package maliwan.mcbl

/**
 * 20 ticks per second.
 */
@JvmInline
value class Ticks(val ticks: Int) {

    val seconds: Double
        get() = ticks.toDouble() / 20.0
}

@JvmInline
value class Seconds(val seconds: Double) {

    val ticks: Int
        get() = (20.0 / seconds).toInt()
}

@JvmInline
value class Chance(val chance: Double) {

    init {
        require(chance in 0.0..1.0) { "Chance $chance must be between 0.0 and 1.0, got <$chance>" }
    }
}

@JvmInline
value class Damage(val damage: Double)