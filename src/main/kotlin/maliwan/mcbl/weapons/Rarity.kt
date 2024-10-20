package maliwan.mcbl.weapons

import org.bukkit.ChatColor
import org.bukkit.Color

/**
 * @author Hannah Schellekens
 */
enum class Rarity(
    val displayName: String,
    val colourPrefix: String,
    val color: Color
) : Comparable<Rarity> {

    COMMON("Common", ChatColor.WHITE.toString(), Color.WHITE),
    UNCOMMON("Uncommon", ChatColor.GREEN.toString(), Color.fromRGB(5635925)),
    RARE("Rare", ChatColor.BLUE.toString(), Color.fromRGB(5592575)),
    EPIC("Epic", ChatColor.LIGHT_PURPLE.toString(), Color.fromRGB(16733695)),
    LEGENDARY("Legendary", ChatColor.GOLD.toString(), Color.fromRGB(16755200)),
    PEARLESCENT("Pearlescent", ChatColor.AQUA.toString(), Color.fromRGB(5636095))
    ;
}