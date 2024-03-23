package maliwan.mcbl.inventory

import org.bukkit.entity.Entity

/**
 * @author Hannah Schellekens
 */
open class InventoryManager {

    /**
     * Maps the owner to their inventory.
     */
    val inventories: MutableMap<Entity, AmmoInventory> = HashMap()

    /**
     * Get the inventory of the given player.
     * Automatically creates a new instance if the owner has no inventory.
     */
    operator fun get(owner: Entity): AmmoInventory {
        val inventory = inventories.getOrDefault(owner, AmmoInventory())
        inventories[owner] = inventory
        return inventory
    }
}