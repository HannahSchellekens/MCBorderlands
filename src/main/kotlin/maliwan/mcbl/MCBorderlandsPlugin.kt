package maliwan.mcbl

import maliwan.mcbl.commmand.McblCommands
import maliwan.mcbl.gui.Hud
import maliwan.mcbl.inventory.InventoryManager
import maliwan.mcbl.weapons.*
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Hannah Schellekens
 */
class MCBorderlandsPlugin : JavaPlugin() {

    /**
     * Handles all events related to weapon handling.
     */
    val weaponEventHandler = WeaponEventHandler(this)

    /**
     * Manages all ammo inventories.
     */
    val inventoryManager = InventoryManager()

    /**
     * Manages the heads-up display for each player.
     */
    val hud = Hud(this)

    private fun registerAllEvents() {
        server.pluginManager.apply {
            registerEvents(weaponEventHandler, this@MCBorderlandsPlugin)
            registerEvents(hud, this@MCBorderlandsPlugin)
        }
    }

    private fun registerCommands() {
        getCommand("mcbl")?.apply {
            val commandHandler = McblCommands(this@MCBorderlandsPlugin)
            setExecutor(commandHandler)
            tabCompleter = commandHandler
        }
    }

    override fun onEnable() {
        Keys.initialize(this)
        registerAllEvents()
        registerCommands()

        // Handle weapon physics.
        server.scheduler.scheduleSyncRepeatingTask(this, weaponEventHandler, 1L, 1L)

        // Update HUD
        server.scheduler.scheduleSyncRepeatingTask(this, hud, 1L, 1L)

        logger.info("Enabled!")
    }

    override fun onDisable() {
        weaponEventHandler.cleanup()
        hud.clearDisplays()

        logger.info("Disabled!")
    }
}