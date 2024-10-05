package maliwan.mcbl.inventory

import maliwan.mcbl.Keys
import maliwan.mcbl.MCBorderlandsPlugin
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Hannah Schellekens
 */
open class SduRecipeDiscovery(val plugin: MCBorderlandsPlugin) : Listener {

    @EventHandler
    fun discoverRecipes(event: InventoryClickEvent) {
        val item = event.currentItem ?: return
        val player = event.whoClicked as? Player ?: return

        if (SDU.isSdu(item)) {
            player.discoverRecipes()
        }
    }

    fun Player.discoverRecipes() {
        discoverRecipe(Keys.recipeSduRare)
        discoverRecipe(Keys.recipeSduEpic)
        discoverRecipe(Keys.recipeSduLegendary)
        discoverRecipe(Keys.recipeSduPearlescent)
    }
}