package maliwan.mcbl.inventory

import maliwan.mcbl.util.spigot.updateItemMeta
import maliwan.mcbl.weapons.Rarity
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

/**
 * Storage deck upgrades: how much ammo capacity each thing has.
 *
 * @author Hannah Schellekens
 */
open class SDU(
    val rarity: Rarity,
    val assaultRifle: Int,
    val grenade: Int,
    val pistol: Int,
    val launcher: Int,
    val shotgun: Int,
    val smg: Int,
    val sniper: Int
) : Comparable<SDU> {

    companion object {

        val common = SDU(Rarity.COMMON, 420, 4, 300, 15, 100, 540, 60)
        val uncommon = SDU(Rarity.UNCOMMON, 560, 5, 400, 18, 120, 720, 72)
        val rare = SDU(Rarity.RARE, 700, 6, 500, 21, 140, 900, 84)
        val epic = SDU(Rarity.EPIC, 840, 7, 600, 24, 160, 1080, 96)
        val legendary = SDU(Rarity.LEGENDARY, 980, 8, 700, 27, 180, 1260, 108)
        val pearlescent = SDU(Rarity.PEARLESCENT, 1120, 9, 800, 30, 200, 1440, 120)

        val itemType = Material.LEATHER_HORSE_ARMOR
        val sdus = listOf(common, uncommon, rare, epic, legendary, pearlescent)

        fun sduByRarity(rarity: Rarity) = sdus.firstOrNull { it.rarity == rarity }

        fun sduByItem(itemStack: ItemStack?): SDU {
            if (itemStack == null) return common
            sdus.forEach {
                if (itemStack == it.item) {
                    return it
                }
            }
            return common
        }

        fun isSdu(itemStack: ItemStack?) = sdus.any { it.item == itemStack }
    }

    val item: ItemStack by lazy {
        ItemStack(itemType, 1).updateItemMeta {
            setDisplayName("${this@SDU.rarity.colourPrefix}Storage Deck Upgrade")
            lore = listOf(
                "${ChatColor.GRAY}${this@SDU.rarity.displayName} • SDU • Dahl",
                "",
                "${ChatColor.GRAY}Pistol: ${ChatColor.WHITE}$pistol",
                "${ChatColor.GRAY}Shotgun: ${ChatColor.WHITE}$shotgun",
                "${ChatColor.GRAY}Assault Rifle: ${ChatColor.WHITE}$assaultRifle",
                "${ChatColor.GRAY}Sniper: ${ChatColor.WHITE}$sniper",
                "${ChatColor.GRAY}SMG: ${ChatColor.WHITE}$smg",
                "${ChatColor.GRAY}Launcher: ${ChatColor.WHITE}$launcher",
                "${ChatColor.GRAY}Grenade: ${ChatColor.WHITE}$grenade",
            )
            addEnchant(Enchantment.UNBREAKING, this@SDU.rarity.ordinal + 1, true)
            addItemFlags(
                ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_UNBREAKABLE,
            )
        }
    }

    override fun compareTo(other: SDU): Int {
        return rarity.compareTo(other.rarity)
    }
}