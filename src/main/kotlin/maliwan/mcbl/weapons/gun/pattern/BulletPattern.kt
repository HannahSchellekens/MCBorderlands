package maliwan.mcbl.weapons.gun.pattern

/**
 * @author Hannah Schellekens
 */
data class BulletPattern(

    /**
     * List of pair pattern rotations.
     * Each element is a rotation around the base direction.
     */
    val pitchesAndYaws: List<Pair<Double, Double>>
) {

    /**
     * How many bullet pattern options there are.
     */
    val count = pitchesAndYaws.size

    /**
     * Get a random (pitch, yaw) pair.
     */
    fun randomPitchYaw(): Pair<Double, Double> = pitchesAndYaws.random()

    companion object {

        fun of(vararg pitchAndYawPairs: Pair<Double, Double>) = BulletPattern(pitchAndYawPairs.toList())
        fun of(pitchAndYawPairs: List<Pair<Double, Double>>) = BulletPattern(pitchAndYawPairs)
    }
}