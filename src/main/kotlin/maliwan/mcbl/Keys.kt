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

    lateinit var recipeSduRare: NamespacedKey
        private set

    lateinit var recipeSduEpic: NamespacedKey
        private set

    lateinit var recipeSduLegendary: NamespacedKey
        private set

    lateinit var recipeSduPearlescent: NamespacedKey
        private set

    lateinit var homingTarget: NamespacedKey
        private set

    fun initialize(plugin: MCBorderlandsPlugin) {
        gunProperties = NamespacedKey(plugin, "gun-properties")
        ammoDrop = NamespacedKey(plugin, "ammo-drop")
        enemyLevel = NamespacedKey(plugin, "enemy-level")
        recipeSduRare = NamespacedKey(plugin, "recipe-sdu-rare")
        recipeSduEpic = NamespacedKey(plugin, "recipe-sdu-epic")
        recipeSduLegendary = NamespacedKey(plugin, "recipe-sdu-legendary")
        recipeSduPearlescent = NamespacedKey(plugin, "recipe-sdu-pearlescent")
        homingTarget = NamespacedKey(plugin, "homing-target")
    }
}