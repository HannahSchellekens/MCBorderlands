package maliwan.mcbl.loot.ammo

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.weapons.gun.gunProperties
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * @author Hannah Schellekens
 */
open class AmmoObtain(val plugin: MCBorderlandsPlugin) : Listener {

    @EventHandler
    fun pickupAmmo(event: EntityPickupItemEvent) {
        val player = event.entity
        val item = event.item
        val ammo = item.itemStack.toAmmoPack() ?: return
        val inventory = plugin.inventoryManager.inventories[player] ?: return
        inventory.add(ammo)
        event.item.remove()
        event.isCancelled = true
        player.world.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1f, 1f)
    }

    @EventHandler
    fun grabFromChest(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val item = event.currentItem ?: return
        val ammo = item.toAmmoPack() ?: return
        event.clickedInventory?.removeItem(item)
        event.isCancelled = true

        val ammoInventory = plugin.inventoryManager[player]
        ammoInventory.add(ammo)
        player.playSound(player.location, Sound.BLOCK_GRAVEL_HIT, SoundCategory.PLAYERS, 1.0f, 1.0f)
    }
}