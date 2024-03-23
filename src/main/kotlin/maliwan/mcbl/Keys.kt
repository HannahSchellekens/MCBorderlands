package maliwan.mcbl

import org.bukkit.NamespacedKey

/**
 * @author Hannah Schellekens
 */
object Keys {

    lateinit var gunProperties: NamespacedKey
        private set

    fun initialize(plugin: MCBorderlandsPlugin) {
        gunProperties = NamespacedKey(plugin, "gun-properties")
    }
}