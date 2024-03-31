package maliwan.mcbl.loot.ammo

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.weapons.gun.gunProperties
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

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
}