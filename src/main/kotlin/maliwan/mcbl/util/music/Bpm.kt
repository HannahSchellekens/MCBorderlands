package maliwan.mcbl.util.music

/**
 * @author Hannah Schellekens
 */
@JvmInline
value class Bpm(val bpm: Int) {

    companion object {

        val sluggish = Bpm(50)
        val evenSlower = Bpm(60)
        val verySlower = Bpm(75)
        val verySlow = Bpm(80)
        val slow = Bpm(100)
        val normal = Bpm(120)
        val fast = Bpm(150)
        val veryFast = Bpm(200)
        val blazingFast = Bpm(240)
    }
}