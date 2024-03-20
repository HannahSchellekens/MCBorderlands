package maliwan.mcbl

import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Hannah Schellekens
 */
class MCBorderlandsPlugin : JavaPlugin() {

    override fun onEnable() {
        logger.info("Enabled!")
    }

    override fun onDisable() {
        logger.info("Disabled!")
    }
}