package maliwan.mcbl.util.spigot

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * @author Hannah Schellekens
 */
fun ItemStack.updateDisplayName(newName: String) {
    updateItemMeta {
        setDisplayName(newName)
    }
}

/**
 * Updates the item meta of the given item stack.
 */
inline fun ItemStack.updateItemMeta(updater: ItemMeta.() -> Unit): ItemStack {
    val meta = itemMeta ?: return this
    meta.updater()
    itemMeta = meta
    return this
}