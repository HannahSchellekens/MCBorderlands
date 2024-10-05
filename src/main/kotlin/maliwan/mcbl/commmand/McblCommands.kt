package maliwan.mcbl.commmand

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.loot.ManufacturerTable
import maliwan.mcbl.loot.RarityTable
import maliwan.mcbl.loot.WeaponClassTable
import maliwan.mcbl.loot.gen.WeaponGenerator
import maliwan.mcbl.loot.lootPoolOf
import maliwan.mcbl.util.Damage
import maliwan.mcbl.util.Probability
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
                "gen"
            )
        } else if (args.size >= 2 && "gen".equals(args.first(), ignoreCase = true)) {
            return genArguments.toMutableList()
        } else if (args.size == 2 && "update".equals(args.first(), ignoreCase = true)) {
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
                "transfusion",
                "bounces",
                "isPiercing",
                "homingTargetDistance",
                "homingTargetRadius",
                "homingStrength",
                "armourPenetration"
            ).filter { args[1].isBlank() || it.startsWith(args[1]) }.toMutableList()
        } else if (args.size < 2) {
            return mutableListOf("update")
        } else if ("manufacturer".equals(args[1], ignoreCase = true)) {
            return Manufacturer.entries.map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }
                .toMutableList()
        } else if ("rarity".equals(args[1], ignoreCase = true)) {
            return Rarity.entries.map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }.toMutableList()
        } else if ("weaponClass".equals(args[1], ignoreCase = true)) {
            return WeaponClass.entries.map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }
                .toMutableList()
        } else if ("element:add".equals(args[1], ignoreCase = true)) {
            return Elemental.entries.map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }
                .toMutableList()
        } else if ("element:policy".equals(args[1], ignoreCase = true)) {
            return ElementalStatusEffects.ApplyPolicy.entries
                .map { it.name }.filter { args[2].isBlank() || it.startsWith(args[2]) }.toMutableList()
        } else {
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

            "gen" -> generateWeaponFromArguments(player, args.asList().subList(1, args.size))
            else -> Unit
        }

        return true
    }

    fun generateWeaponFromArguments(player: Player, args: List<String>) {
        val number = args.firstNotNullOfOrNull { it.toIntOrNull() } ?: 1
        val unique = args.any {
            "unique".equals(it, ignoreCase = true) || "legendary".equals(it, ignoreCase = true) ||
                    "pearlescent".equals(it, ignoreCase = true)
        }

        val weaponClass = WeaponClass.entries.firstOrNull { wc -> args.any { wc.name.equals(it, ignoreCase = true) } }
        val rarity = Rarity.entries.firstOrNull { r -> args.any { r.name.equals(it, ignoreCase = true) } }
        val manufacturer =
            Manufacturer.entries.firstOrNull { manu -> args.any { manu.name.equals(it, ignoreCase = true) } }

        generateWeapon(player, weaponClass, rarity, manufacturer, unique = unique, amount = number)
    }

    fun generateWeapon(
        player: Player,
        weaponClass: WeaponClass? = null,
        rarity: Rarity? = null,
        manufacturer: Manufacturer? = null,
        unique: Boolean = false,
        amount: Int = 1
    ) = repeat(amount) {
        val gunItem = ItemStack(Material.BOW, 1)

        val rarityTable = rarity?.let { lootPoolOf(it to 1) } ?: RarityTable.Treasure.regular
        val weaponClassTable = weaponClass?.let { lootPoolOf(it to 1) } ?: WeaponClassTable.generation
        val manufacturerTable = manufacturer?.let { lootPoolOf(it to 1) } ?: ManufacturerTable.Weapons.generation
        val generator = WeaponGenerator(rarityTable, weaponClassTable, manufacturerTable)

        try {
            val gunProperties = if (unique && rarity == Rarity.PEARLESCENT) {
                generator.generatePearlescent(weaponClassTable.roll(), manufacturerTable.roll())
            }
            else if (unique && rarity == Rarity.LEGENDARY) {
                generator.generateLegendary(weaponClassTable.roll(), manufacturerTable.roll())
            }
            else if (unique) {
                generator.generateUnique(rarityTable.roll(), weaponClassTable.roll(), manufacturerTable.roll())
            }
            else generator.generate()

            gunProperties.applyToItem(gunItem)
            player.inventory.addItem(gunItem)
        }
        catch (e: NoSuchElementException) {
            player.sendMessage("Could not generate weapon. Maybe an unsupported weapon class for the given manufacturer was rolled.")
        }
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
            "accuracy" -> update { accuracy = Probability(value.toDoubleOrNull() ?: error("No double: $value")) }
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
                elementalProbability.clear()
                elementalDuration.clear()
                elementalDamage.clear()
            }

            "element:add" -> update {
                val element = Elemental.valueOf(values[0])
                val probability = Probability(values[1].toDoubleOrNull() ?: error("Invalid double: ${values[1]}"))
                val duration = Ticks(values[2].toIntOrNull() ?: error("Invalid int: ${values[2]}"))
                val damage = Damage(values[3].toDoubleOrNull() ?: error("Invalid double: ${values[3]}"))
                elements.add(element)
                elementalProbability[element] = probability
                elementalDuration[element] = duration
                elementalDamage[element] = damage
            }

            "element:policy" -> update {
                elementalPolicy = ElementalStatusEffects.ApplyPolicy.valueOf(value.uppercase())
            }

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
            "bonusCritMultiplier" -> update {
                bonusCritMultiplier = value.toDoubleOrNull() ?: error("No double: $value")
            }

            "extraShotChance" -> update {
                extraShotProbability = Probability(value.toDoubleOrNull() ?: error("No double: $value"))
            }
            "freeShotChance" -> update { freeShotProbability = Probability(value.toDoubleOrNull() ?: error("No double: $value")) }
            "transfusion" -> update { transfusion = value.toDoubleOrNull() ?: error("No double: $value") }
            "bounces" -> update { bounces = value.toIntOrNull() ?: error("No int: $value") }
            "isPiercing" -> update { isPiercing = value.toBoolean() }
            "homingTargetDistance" -> update { homingTargetDistance = value.toDoubleOrNull() ?: error("No double: $value") }
            "homingTargetRadius" -> update { homingTargetRadius = value.toDoubleOrNull() ?: error("No double: $value") }
            "homingStrength" -> update { homingStrength = value.toDoubleOrNull() ?: error("No double: $value") }
            "armourPenetration" -> update { armourPenetration = value.toDoubleOrNull() ?: error("No double: $value") }
        }
    }

    companion object {

        val genArguments = WeaponClass.entries.map { it.name.lowercase() } +
                Rarity.entries.map { it.name.lowercase() } +
                Manufacturer.entries.map { it.name.lowercase() } +
                listOf("unique")
    }
}