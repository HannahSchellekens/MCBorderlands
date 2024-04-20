package maliwan.mcbl

import org.bukkit.NamespacedKey

/**
 * @author Hannah Schellekens
 */
object Keys {

    lateinit var gunProperties: NamespacedKey
        private set

    lateinit var ammoDrop: NamespacedKey
        private set

    lateinit var enemyLevel: NamespacedKey
        private set

    fun initialize(plugin: MCBorderlandsPlugin) {
        gunProperties = NamespacedKey(plugin, "gun-properties")
        ammoDrop = NamespacedKey(plugin, "ammo-drop")
        enemyLevel = NamespacedKey(plugin, "enemy-level")
    }
}