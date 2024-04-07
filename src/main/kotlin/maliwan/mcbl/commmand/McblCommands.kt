package maliwan.mcbl.commmand

import maliwan.mcbl.*
import maliwan.mcbl.loot.gen.WeaponGenerator
import maliwan.mcbl.loot.lootPoolOf
import maliwan.mcbl.loot.toUniformLootPool
import maliwan.mcbl.util.Chance
import maliwan.mcbl.util.Damage
import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.*
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
        if (args.size == 1) {
            return mutableListOf(
                "update",
                "pistol",
                "shotgun",
                "sniper",
                "smg",
                "assaultRifle",
                "launcher",
            )
        }
        else if (args.size == 2 && "update".equals(args.first(), ignoreCase = true)) {
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
                "cyanText",
                "cyanText:none",
                "extraInfoText:empty",
                "extraInfoText:add",
                "extraInfoText:removeLast",
                "element:none",
                "element:add",
                "element:policy",
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
                "bonusCritMultiplier",
                "extraShotChance",
                "freeShotChance",
            ).filter { args[1].isBlank() || it.startsWith(args[1]) }.toMutableList()
        }
        else if (args.size < 2) {
            return mutableListOf("update")
        }
        else if ("manufacturer".equals(args[1], ignoreCase = true)) {
            return Manufacturer.entries.map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }.toMutableList()
        }
        else if ("rarity".equals(args[1], ignoreCase = true)) {
            return Rarity.entries.map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }.toMutableList()
        }
        else if ("weaponClass".equals(args[1], ignoreCase = true)) {
            return WeaponClass.entries.map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }.toMutableList()
        }
        else if ("element:add".equals(args[1], ignoreCase = true)) {
            return Elemental.entries.map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }.toMutableList()
        }
        else if ("element:policy".equals(args[1], ignoreCase = true)) {
            return ElementalStatusEffects.ApplyPolicy.entries
                .map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }.toMutableList()
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
        val subCommand = args.getOrNull(0)
        val amount = args.getOrNull(1)?.toIntOrNull() ?: 1

        when (subCommand) {
            "update" -> {
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
            "pistol" -> debug(player, WeaponClass.PISTOL, amount = amount)
            "shotgun" -> debug(player, WeaponClass.SHOTGUN, amount = amount)
            "sniper" -> debug(player, WeaponClass.SNIPER, amount = amount)
            "smg" -> debug(player, WeaponClass.SMG, amount = amount)
            "assaultRifle" -> debug(player, WeaponClass.ASSAULT_RIFLE, amount = amount)
            "launcher" -> debug(player, WeaponClass.LAUNCHER, amount = amount)
            else -> debug(player, amount = 1)
        }

        return true
    }

    fun debug(player: Player, weaponClass: WeaponClass? = null, amount: Int = 1) = repeat(amount) {
        val gunItem = ItemStack(Material.BOW, 1)

        val generator = weaponClass?.let {
            WeaponGenerator(weaponClassTable = lootPoolOf(it to 1))
        } ?: WeaponGenerator()

        val gunProperties = generator.generate()
        gunProperties.applyToItem(gunItem)
        player.inventory.addItem(gunItem)
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
            "cyanText" -> update { cyanText = value.replace("\\n", "\n") }
            "cyanText:none" -> update { cyanText = null }
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
                val element = Elemental.valueOf(values[0])
                val chance = Chance(values[1].toDoubleOrNull() ?: error("Invalid double: ${values[1]}"))
                val duration = Ticks(values[2].toIntOrNull() ?: error("Invalid int: ${values[2]}"))
                val damage = Damage(values[3].toDoubleOrNull() ?: error("Invalid double: ${values[3]}"))
                elements.add(element)
                elementalChance[element] = chance
                elementalDuration[element] = duration
                elementalDamage[element] = damage
            }
            "element:policy" -> update { elementalPolicy = ElementalStatusEffects.ApplyPolicy.valueOf(value.uppercase()) }
            "splashRadius" -> update { splashRadius = value.toDoubleOrNull() ?: error("No double: $value") }
            "splashDamage" -> update { splashDamage = Damage(value.toDoubleOrNull() ?: error("No double: $value")) }
            "recoilAngle:none" -> update { recoilAngle = null }
            "recoilAngle" -> update { recoilAngle = value.toDoubleOrNull() ?: error("No double: $value") }
            "manufacturer" -> update { manufacturer = Manufacturer.valueOf(value.uppercase()) }
            "rarity" -> update { rarity = Rarity.valueOf(value.uppercase()) }
            "weaponClass" -> update { weaponClass = WeaponClass.valueOf(value.uppercase()) }
            "pelletCount" -> update { pelletCount = value.toIntOrNull() ?: error("No int: $value") }
            "bulletSpeed" -> update { bulletSpeed = value.toDoubleOrNull() ?: error("No double: $value") }
            "meleeDamage" -> update { meleeDamage = Damage(value.toDoubleOrNull() ?: error("No double: $value")) }
            "burstCount" -> update { burstCount = value.toIntOrNull() ?: error("No int: $value") }
            "burstDelay" -> update { burstDelay = Ticks(value.toIntOrNull() ?: error("No int/ticks: $value")) }
            "gravity" -> update { gravity = value.toDoubleOrNull() ?: error("No double: $value") }
            "bonusCritMultiplier" -> update { bonusCritMultiplier = value.toDoubleOrNull() ?: error("No double: $value") }
            "extraShotChance" -> update { extraShotChance = Chance(value.toDoubleOrNull() ?: error("No double: $value")) }
            "freeShotChance" -> update { freeShotChance = Chance(value.toDoubleOrNull() ?: error("No double: $value")) }
        }
    }
}