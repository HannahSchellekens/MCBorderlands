package maliwan.mcbl.util

import kotlin.math.max
import kotlin.math.min

/**
 * Armour damage reduction multiplier.
 *
 * @param damage Total amount of damage to inflict before armour modifiers.
 * @param defensePoints How many armour points the entity has: this is the armor bar on the HUD.
 * @param toughness Extra toughness modifier for armor types. 0 by default, 8 for a full diamond set, 12 for a
 * full netherite set.
 */
fun armorDamageMultiplier(
    damage: Double,
    defensePoints: Double,
    toughness: Double = 0.0,
    enchantmentProtectionFactor: Double = 0.0
): Double {
    val baseMultiplier = 1 - (max(defensePoints / 5.0, defensePoints - (4 * damage / toughness + 8))) / 25.0
    val epf = min(enchantmentProtectionFactor, 20.0)
    val enchantmentReduction = 1 - epf / 25.0
    return baseMultiplier * enchantmentReduction
}