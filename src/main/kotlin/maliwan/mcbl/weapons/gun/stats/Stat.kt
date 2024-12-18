package maliwan.mcbl.weapons.gun.stats

import maliwan.mcbl.util.plugin.Damage
import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.util.plugin.Ticks

/**
 * @author Hannah Schellekens
 */
class Stat<T>(
    val key: String,
    val parser: (String) -> T
) {

    fun parse(stringValue: String) = parser(stringValue)

    operator fun invoke(stringValue: String) = parse(stringValue)

    companion object {

        val baseDamage = Stat("baseDamage") {
            Damage(it.toDoubleOrNull() ?: error("Invalid baseDamage double <$it>"))
        }
        val magazineSize = Stat("magazineSize") {
            it.toIntOrNull() ?: error("Invalid magazineSize int <$it>")
        }
        val reloadTime = Stat("reloadTime") {
            Ticks(it.toIntOrNull() ?: error("Invalid reloadTime int <$it>"))
        }
        val fireRate = Stat("fireRate") {
            it.toDoubleOrNull() ?: error("Invalid fireRate double <$it>")
        }
        val burstCount = Stat("burstCount") {
            it.toIntOrNull() ?: error("Invalid burstCount int <$it>")
        }
        val elementalProbability = Stat("elementalChance") {
            Probability(it.toDoubleOrNull() ?: error("Invalid elementalChance double <$it>"))
        }
        val elementalDamage = Stat("elementalDamage") {
            Damage((it.toDoubleOrNull() ?: error("Invalid elementalDamage double <$it>")) / 2.0)
        }
        val accuracy = Stat("accuracy") {
            Probability(it.toDoubleOrNull() ?: error("Invalid accuracy double <$it>"))
        }
        val recoil = Stat("recoil") {
            it.toDoubleOrNull() ?: error("Invalid recoil double <$it>")
        }
        val gravity = Stat("gravity") {
            it.toDoubleOrNull() ?: error("Invalid gravity double <$it>")
        }
        val bulletSpeed = Stat("bulletSpeed") {
            it.toDoubleOrNull() ?: error("Invalid bulletSpeed double <$it>")
        }
        val bonusCritModifier = Stat("bonusCritModifier") {
            it.toDoubleOrNull() ?: error("Invalid bonusCritModifier double <$it>")
        }
        val splashRadius = Stat("splashRadius") {
            it.toDoubleOrNull() ?: error("Invalid splashRadius double <$it>")
        }
        val ammoPerShot = Stat("ammoPerShot") {
            it.toIntOrNull() ?: error("Invalid ammoPerShot int <$it>")
        }
        val projectileCount = Stat("projectileCount") {
            it.toIntOrNull() ?: error("Invalid projectileCount int <$it>")
        }
        val recoilAngle = Stat("recoilAngle") {
            it.toDoubleOrNull() ?: error("Invalid recoilAngle double <$it>")
        }
        val freeShotProbability = Stat("freeShotChance") {
            Probability(it.toDoubleOrNull() ?: error("Invalid freeShotChance double <$it>"))
        }
        val homingStrength = Stat("homingStrength") {
            it.toDoubleOrNull() ?: error("Invalid homingStrength double <$it>")
        }
    }
}