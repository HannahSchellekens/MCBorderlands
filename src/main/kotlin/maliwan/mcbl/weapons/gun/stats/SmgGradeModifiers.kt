package maliwan.mcbl.weapons.gun.stats

import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.Rarity

/**
 * @author Hannah Schellekens
 */
object SmgGradeModifiers : ModifierProvider, StandardDeviationProvider {

    private val baseModifierTable = TabTable.fromResource(
        "/gun/base/smg-grade-modifiers.csv",
        Rarity::valueOf,
        { it },
        { it }
    )

    private val modifierStandardDeviationTable = TabTable.fromResource(
        "/gun/base/smg-grade-modifiers-std.csv",
        Rarity::valueOf,
        { it },
        { it.toDouble() }
    )

    /**
     * Get the base value of a specific stat for a given manufacturer.
     */
    override fun <T> modifier(rarity: Rarity, modifier: Modifier<T>): T {
        val modifierRarity = when (rarity) {
            Rarity.LEGENDARY, Rarity.PEARLESCENT -> Rarity.EPIC
            else -> rarity
        }

        val stringValue = baseModifierTable[modifierRarity, modifier.key]
            ?: throw IllegalArgumentException("No stat modifier found for rarity $rarity and key ${modifier.key}")
        return modifier(stringValue)
    }

    /**
     * Get the standard deviation for a specific stat for a given rarity.
     */
    override fun standardDeviation(rarity: Rarity, standardDeviation: StandardDeviation): Double {
        val stdRarity = when (rarity) {
            Rarity.LEGENDARY, Rarity.PEARLESCENT -> Rarity.EPIC
            else -> rarity
        }

        return modifierStandardDeviationTable[stdRarity, standardDeviation.key]
            ?: throw IllegalArgumentException("No standard deviation found for rarity $stdRarity and key ${standardDeviation.key}")
    }
}