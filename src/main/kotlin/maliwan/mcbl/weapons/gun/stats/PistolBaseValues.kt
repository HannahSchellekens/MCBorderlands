package maliwan.mcbl.weapons.gun.stats

import maliwan.mcbl.util.Chance
import maliwan.mcbl.util.Damage
import maliwan.mcbl.util.TabTable
import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.gun.GunProperties

/**
 * @author Hannah Schellekens
 */
object PistolBaseValues {

    private val baseValueTable = TabTable.fromResource(
        "/gun/base/pistol-base-values.csv",
        Manufacturer::valueOf,
        { it },
        { it }
    )

    /**
     * Get the base value of a specific stat for a given manufacturer.
     */
    fun <T> baseValue(manufacturer: Manufacturer, stat: Stat<T>): T {
        val stringValue = baseValueTable[manufacturer, stat.key]
            ?: throw IllegalArgumentException("No stat found for manufacturer $manufacturer and key ${stat.key}")
        return stat(stringValue)
    }

    /**
     * Creates a new [GunProperties] object for pistols that is populated by the base values of `manufacturer`.
     * Does not include elemental properties, they have to be applied when the specific element is applied.
     */
    fun newGunProperties(manufacturer: Manufacturer) = GunProperties(
        baseDamage = baseValue(manufacturer, Stat.baseDamage),
        magazineSize = baseValue(manufacturer, Stat.magazineSize),
        reloadSpeed = baseValue(manufacturer, Stat.reloadTime),
        fireRate = baseValue(manufacturer, Stat.fireRate),
        burstCount = baseValue(manufacturer, Stat.burstCount),
        accuracy = baseValue(manufacturer, Stat.accuracy),
        recoil = baseValue(manufacturer, Stat.recoil),
        gravity = baseValue(manufacturer, Stat.gravity),
        bulletSpeed = baseValue(manufacturer, Stat.bulletSpeed),
        bonusCritMultiplier = baseValue(manufacturer, Stat.bonusCritModifier),
        splashRadius = baseValue(manufacturer, Stat.splashRadius),
        ammoPerShot = baseValue(manufacturer, Stat.ammoPerShot)
    )

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
            val elementalChance = Stat("elementalChance") {
                Chance(it.toDoubleOrNull() ?: error("Invalid elementalChance double <$it>"))
            }
            val elementalDamage = Stat("elementalDamage") {
                Damage((it.toDoubleOrNull() ?: error("Invalid elementalDamage double <$it>")) / 2.0)
            }
            val accuracy = Stat("accuracy") {
                Chance(it.toDoubleOrNull() ?: error("Invalid accuracy double <$it>"))
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
        }
    }
}