package maliwan.mcbl.weapons.gun.pattern

import maliwan.mcbl.util.rotateRelative
import org.bukkit.util.Vector

/**
 * Goes through each bullet rotation in order.
 *
 * @author Hannah Schellekens
 */
open class SequentialBulletPattern(val pattern: BulletPattern) : BulletPatternProcessor {

    private var count = 0

    override fun nextRotation(base: Vector): Vector {
        val (pitch, yaw) = pattern.pitchesAndYaws[count++ % pattern.count]
        return base.rotateRelative(pitch, yaw)
    }

    companion object {

        fun of(vararg pitchAndYawPairs: Pair<Double, Double>): SequentialBulletPattern {
            return SequentialBulletPattern(BulletPattern(pitchAndYawPairs.toList()))
        }

        fun of(pitchAndYawPairs: List<Pair<Double, Double>>): SequentialBulletPattern {
            return SequentialBulletPattern(BulletPattern(pitchAndYawPairs))
        }
    }
}