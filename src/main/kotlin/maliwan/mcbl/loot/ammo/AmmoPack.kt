package maliwan.mcbl.loot.ammo

import maliwan.mcbl.Keys
import maliwan.mcbl.util.GSON
import maliwan.mcbl.util.updateItemMeta
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.GunProperties
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

/**
 * @author Hannah Schellekens
 */
data class AmmoPack(
    val weaponType: WeaponClass,
    val amount: Int = defaultAmount(weaponType)
) {

    val itemType = when (weaponType) {
        WeaponClass.SHOTGUN -> Material.MELON_SEEDS
        WeaponClass.SNIPER -> Material.ECHO_SHARD
        WeaponClass.ASSAULT_RIFLE -> Material.PRISMARINE_CRYSTALS
        WeaponClass.LAUNCHER -> Material.FIREWORK_ROCKET
        WeaponClass.SMG -> Material.GRAY_CANDLE
        else -> Material.IRON_NUGGET
    }

    val itemName = "${weaponType.displayName} Ammo"

    fun toItemStack(): ItemStack = ItemStack(itemType, amount).updateItemMeta {
        setDisplayName(itemName)
        persistentDataContainer.set(Keys.ammoDrop, PersistentDataType.STRING, this@AmmoPack.serialize())
    }

    /**
     * AmmoPack object -> json string.
     */
    fun serialize(): String = GSON.toJson(this)

    companion object {

        /**
         * Gun string -> GunProperties object.
         */
        fun deserialize(json: String): AmmoPack = GSON.fromJson(json, AmmoPack::class.java)

        fun defaultAmount(weaponType: WeaponClass) = when (weaponType) {
            WeaponClass.ASSAULT_RIFLE -> 28
            WeaponClass.PISTOL -> 24
            WeaponClass.LAUNCHER -> 4
            WeaponClass.SHOTGUN -> 12
            WeaponClass.SMG -> 36
            WeaponClass.SNIPER -> 8
        }
    }
}

/**
 * Check if this item is an ammo pack, and if so returns the ammo pack data.
 */
fun ItemStack.toAmmoPack(): AmmoPack? {
    val store = itemMeta?.persistentDataContainer ?: return null
    val packJson = store.get(Keys.ammoDrop, PersistentDataType.STRING) ?: return null
    return AmmoPack.deserialize(packJson)
}