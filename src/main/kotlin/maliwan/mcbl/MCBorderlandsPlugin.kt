package maliwan.mcbl

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Hannah Schellekens
 */
class MCBorderlandsPlugin : JavaPlugin() {

    override fun onEnable() {
        val a: Player = 3 as Player

        logger.info("Enabled!")
    }

    override fun onDisable() {
        logger.info("Disabled!")
    }
}