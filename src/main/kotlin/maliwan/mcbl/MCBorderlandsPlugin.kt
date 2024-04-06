package maliwan.mcbl

import maliwan.mcbl.commmand.McblCommands
import maliwan.mcbl.entity.ItemParticles
import maliwan.mcbl.gui.DamageParticles
import maliwan.mcbl.gui.Hud
import maliwan.mcbl.inventory.InventoryManager
import maliwan.mcbl.loot.ammo.AmmoObtain
import maliwan.mcbl.loot.chests.LootChestPopulator
import maliwan.mcbl.loot.drops.AmmoDropOnDeath
import maliwan.mcbl.loot.drops.WeaponDropOnDeath
import maliwan.mcbl.weapons.WeaponEventHandler
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
     * Manages general play behaviour, like e.g. increasing default health pools.
     */
    val gameRules = GameRules(this)

    /**
     * Manages all ammo inventories.
     */
    val inventoryManager = InventoryManager()

    /**
     * Manages the heads-up display for each player.
     */
    val hud = Hud(this)

    /**
     * Shows particles when enemies get damaged.
     */
    val damageParticles = DamageParticles()

    /**
     * Shows rarity particles above the gun items on the ground.
     */
    val itemParticles = ItemParticles(this)

    private fun registerAllEvents() {
        val plugin = this@MCBorderlandsPlugin
        server.pluginManager.apply {
            registerEvents(weaponEventHandler, plugin)
            registerEvents(gameRules, plugin)
            registerEvents(hud, plugin)
            registerEvents(AmmoDropOnDeath(plugin), plugin)
            registerEvents(AmmoObtain(plugin), plugin)
            registerEvents(WeaponDropOnDeath(plugin), plugin)
            registerEvents(LootChestPopulator(plugin), plugin)
            registerEvents(damageParticles, plugin)
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
        // Show rarity particles on items.
        server.scheduler.scheduleSyncRepeatingTask(this, itemParticles, 1L, 1L)
        // Damage particle
        server.scheduler.scheduleSyncRepeatingTask(this, damageParticles, 1L, 1L)

        logger.info("Enabled!")
    }

    override fun onDisable() {
        weaponEventHandler.cleanup()
        hud.clearAll()

        logger.info("Disabled!")
    }
}