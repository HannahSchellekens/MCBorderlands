package maliwan.mcbl.commmand

import maliwan.mcbl.*
import maliwan.mcbl.weapons.Elements
import maliwan.mcbl.weapons.Manufacturers
import maliwan.mcbl.weapons.Rarities
import maliwan.mcbl.weapons.WeaponClasses
import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.gunProperties
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Hannah Schellekens
 */
open class McblCommands(val plugin: MCBorderlandsPlugin) : CommandExecutor, TabCompleter {

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String>? {
        if (args.size == 2 && "update".equals(args.first(), ignoreCase = true)) {
            return listOf(
                "name",
                "baseDamage",
                "accuracy",
                "recoil",
                "fireRate",
                "reloadSpeed",
                "magazineSize",
                "ammoPerShot",
                "redText",
                "redText:none",
                "extraInfoText:empty",
                "extraInfoText:add",
                "extraInfoText:removeLast",
                "element:none",
                "element:add",
                "splashRadius",
                "splashDamage",
                "recoilAngle:none",
                "recoilAngle",
                "manufacturer",
                "rarity",
                "weaponClass",
                "pelletCount",
                "bulletSpeed",
                "meleeDamage",
                "burstCount",
                "burstDelay",
                "gravity",
            ).filter { args[1].isBlank() || it.startsWith(args[1]) }.toMutableList()
        }
        else if (args.size < 2) {
            return mutableListOf("update")
        }
        else if ("manufacturer".equals(args[1], ignoreCase = true)) {
            return Manufacturers.entries.map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }.toMutableList()
        }
        else if ("rarity".equals(args[1], ignoreCase = true)) {
            return Rarities.entries.map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }.toMutableList()
        }
        else if ("weaponClass".equals(args[1], ignoreCase = true)) {
            return WeaponClasses.entries.map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }.toMutableList()
        }
        else if ("element:add".equals(args[1], ignoreCase = true)) {
            return Elements.entries.map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }.toMutableList()
        }
        else {
            return null
        }
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val player = sender as? Player ?: return false

        if (args.getOrNull(0) == "update") {
            if (args.size >= 2 && args[1].endsWith(":none") || args[1].endsWith(":removeLast")) {
                updateBowProperties(player, args[1], emptyList())
                return true
            }

            if (args.size < 3) {
                sender.sendMessage("${ChatColor.RED}Usage: /mcbl update <property> <value>")
                return false
            }

            updateBowProperties(player, args[1], args.toList().subList(2, args.size))
            return true
        }

        debug(player)
        return true
    }

    fun updateBowProperties(player: Player, property: String, values: List<String>) {
        val mainHand = player.inventory.itemInMainHand
        val gun = mainHand.gunProperties() ?: run {
            player.sendMessage("${ChatColor.RED}No gun in main hand")
            return
        }
        val value = values.joinToString(" ")

        /** Updates the properties of the gun and automatically update the item in hand */
        fun update(update: GunProperties.() -> Unit) {
            gun.update()
            gun.applyToItem(mainHand)
        }

        when (property) {
            "name" -> update { name = value }
            "baseDamage" -> update { baseDamage = Damage(value.toDoubleOrNull() ?: error("No double: $value")) }
            "accuracy" -> update { accuracy = Chance(value.toDoubleOrNull() ?: error("No double: $value")) }
            "recoil" -> update { recoil = value.toDoubleOrNull() ?: error("No double: $value") }
            "fireRate" -> update { fireRate = value.toDoubleOrNull() ?: error("No double: $value") }
            "reloadSpeed" -> update { reloadSpeed = Ticks(value.toIntOrNull() ?: error("No int/ticks: $value")) }
            "magazineSize" -> update { magazineSize = value.toIntOrNull() ?: error("No int: $value") }
            "ammoPerShot" -> update { ammoPerShot = value.toIntOrNull() ?: error("No int: $value") }
            "redText" -> update { redText = value.replace("\\n", "\n") }
            "redText:none" -> update { redText = null }
            "extraInfoText:none" -> update { extraInfoText.clear() }
            "extraInfoText:add" -> update { extraInfoText.add(value) }
            "extraInfoText:removeLast" -> update { extraInfoText.removeLast() }
            "element:none" -> update {
                elements.clear()
                elementalChance.clear()
                elementalDuration.clear()
                elementalDamage.clear()
            }
            "element:add" -> update {
                val element = Elements.valueOf(values[0])
                val chance = Chance(values[1].toDoubleOrNull() ?: error("Invalid double: ${values[1]}"))
                val duration = Ticks(values[2].toIntOrNull() ?: error("Invalid int: ${values[2]}"))
                val damage = Damage(values[3].toDoubleOrNull() ?: error("Invalid double: ${values[3]}"))
                elements.add(element)
                elementalChance[element] = chance
                elementalDuration[element] = duration
                elementalDamage[element] = damage
            }
            "splashRadius" -> update { splashRadius = value.toDoubleOrNull() ?: error("No double: $value") }
            "splashDamage" -> update { splashDamage = Damage(value.toDoubleOrNull() ?: error("No double: $value")) }
            "recoilAngle:none" -> update { recoilAngle = null }
            "recoilAngle" -> update { recoilAngle = value.toDoubleOrNull() ?: error("No double: $value") }
            "manufacturer" -> update { manufacturer = Manufacturers.valueOf(value.uppercase()) }
            "rarity" -> update { rarity = Rarities.valueOf(value.uppercase()) }
            "weaponClass" -> update { weaponClass = WeaponClasses.valueOf(value.uppercase()) }
            "pelletCount" -> update { pelletCount = value.toIntOrNull() ?: error("No int: $value") }
            "bulletSpeed" -> update { bulletSpeed = value.toDoubleOrNull() ?: error("No double: $value") }
            "meleeDamage" -> update { meleeDamage = Damage(value.toDoubleOrNull() ?: error("No double: $value")) }
            "burstCount" -> update { burstCount = value.toIntOrNull() ?: error("No int: $value") }
            "burstDelay" -> update { burstDelay = Ticks(value.toIntOrNull() ?: error("No int/ticks: $value")) }
            "gravity" -> update { gravity = value.toDoubleOrNull() ?: error("No double: $value") }
        }
    }

    fun debug(player: Player) {
        val gunItem = ItemStack(Material.BOW, 1)
        val gunProperties = GunProperties(
            name = "Anarchist",
            rarity = Rarities.UNCOMMON,
            weaponClass = WeaponClasses.PISTOL,
            manufacturer = Manufacturers.VLADOF,
            bulletSpeed = 90.0,
            accuracy = Chance(0.862),
            fireRate = 10.4,
            magazineSize = 38,
            baseDamage = Damage(0.8),
        )

        gunProperties.applyToItem(gunItem)
        player.inventory.addItem(gunItem)
    }
}