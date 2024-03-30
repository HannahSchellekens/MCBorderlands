package maliwan.mcbl.weapons.gun.stats

import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.Rarity

/**
 * @author Hannah Schellekens
 */
object PistolGradeModifiers {

    private val baseModifierTable = TabTable.fromResource(
        "/gun/base/pistol-grade-modifiers.csv",
        Rarity::valueOf,
        { it },
        { it }
    )

    private val modifierStandardDeviationTable = TabTable.fromResource(
        "/gun/base/pistol-grade-modifiers-std.csv",
        Rarity::valueOf,
        { it },
        { it.toDouble() }
    )

    /**
     * Get the base value of a specific stat for a given manufacturer.
     */
    fun <T> modifier(rarity: Rarity, modifier: Modifier<T>): T {
        val modifierRarity = when (rarity) {
            Rarity.LEGENDARY, Rarity.PEARLESCENT -> Rarity.EPIC
            else -> rarity
        }

        val stringValue = baseModifierTable[modifierRarity, modifier.key]
            ?: throw IllegalArgumentException("No stat modifier found for rarity $rarity and key ${modifier.key}")
        return modifier(stringValue)
    }

    /**
     * Get the standar ddeviation for a specific stat for a given rarity.
     */
    fun standardDeviation(rarity: Rarity, standardDeviation: StandardDeviation): Double {
        return modifierStandardDeviationTable[rarity, standardDeviation.key]
            ?: throw IllegalArgumentException("No standard deviation found for rarity $rarity and key ${standardDeviation.key}")
    }

    /**
     * @author Hannah Schellekens
     */
    class StandardDeviation(
        val key: String
    ) {

        fun parse(stringValue: String) = stringValue.toDouble()

        operator fun invoke(stringValue: String) = parse(stringValue)

        companion object {

            val baseDamage = StandardDeviation("baseDamage")
            val magazineSize = StandardDeviation("magazineSize")
            val elementalDamage = StandardDeviation("elementalDamage")
        }
    }

    /**
     * @author Hannah Schellekens
     */
    class Modifier<T>(
        val key: String,
        val parser: (String) -> T
    ) {

        fun parse(stringValue: String) = parser(stringValue)

        operator fun invoke(stringValue: String) = parse(stringValue)

        companion object {

            val baseDamage = Modifier("baseDamage") {
                it.toDoubleOrNull() ?: error("Invalid baseDamage double <$it>")
            }
            val magazineSize = Modifier("magazineSize") {
                it.toIntOrNull() ?: error("Invalid magazineSize int <$it>")
            }
            val reloadTime = Modifier("reloadTime") {
                it.toIntOrNull() ?: error("Invalid reloadTime int <$it>")
            }
            val fireRate = Modifier("fireRate") {
                it.toDoubleOrNull() ?: error("Invalid fireRate double <$it>")
            }
            val elementalChance = Modifier("elementalChance") {
                it.toDoubleOrNull() ?: error("Invalid elementalChance double <$it>")
            }
            val elementalDamage = Modifier("elementalDamage") {
                it.toDoubleOrNull() ?: error("Invalid elementalDamage double <$it>")
            }
            val accuracy = Modifier("accuracy") {
                it.toDoubleOrNull() ?: error("Invalid accuracy double <$it>")
            }
            val recoil = Modifier("recoil") {
                it.toDoubleOrNull() ?: error("Invalid recoil double <$it>")
            }
        }
    }
}