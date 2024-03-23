package maliwan.mcbl

import com.google.gson.Gson
import maliwan.mcbl.commmand.McblCommands
import maliwan.mcbl.weapons.*
import maliwan.mcbl.weapons.gun.GunProperties
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.log

/**
 * @author Hannah Schellekens
 */
class MCBorderlandsPlugin : JavaPlugin() {

    /**
     * Handles all events related to weapon handling.
     */
    private val weaponEventHandler = WeaponEventHandler(this)

    private fun registerAllEvents() {
        server.pluginManager.registerEvents(weaponEventHandler, this)
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

        logger.info("Enabled!")
    }

    override fun onDisable() {
        logger.info("Disabled!")
    }
}