package maliwan.mcbl.weapons.gun.pattern

import maliwan.mcbl.util.rotateRelative
import org.bukkit.util.Vector

/**
 * Picks a random element from the bullet pattern.
 *
 * @author Hannah Schellekens
 */
open class RandomBulletPattern(val pattern: BulletPattern) : BulletPatternProcessor {

    override fun nextRotation(base: Vector): Vector {
        val (pitch, yaw) = pattern.randomPitchYaw()
        return base.rotateRelative(pitch, yaw)
    }

    companion object {

        fun of(vararg pitchAndYawPairs: Pair<Double, Double>): RandomBulletPattern {
            return RandomBulletPattern(BulletPattern(pitchAndYawPairs.toList()))
        }

        fun of(pitchAndYawPairs: List<Pair<Double, Double>>): RandomBulletPattern {
            return RandomBulletPattern(BulletPattern(pitchAndYawPairs))
        }
    }
}