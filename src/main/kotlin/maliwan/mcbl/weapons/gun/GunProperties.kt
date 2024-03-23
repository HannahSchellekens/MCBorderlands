package maliwan.mcbl.weapons.gun

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import maliwan.mcbl.Chance
import maliwan.mcbl.Damage
import maliwan.mcbl.Keys
import maliwan.mcbl.Ticks
import maliwan.mcbl.weapons.*
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

/**
 * @author Hannah Schellekens
 */
open class GunProperties(

    /**
     * The name of the gun.
     */
    var name: String = "Gun",

    /**
     * Damage per shot in half hearts.
     */
    var baseDamage: Damage = Damage(1.5),

    /**
     * Shot direction accuracy percentage.
     *
     * 100% is dead center. 0% is anywhere on screen.
     */
    var accuracy: Chance = Chance(0.985),

    /**
     * Amount of shots per second.
     */
    var fireRate: Double = 1.0,

    /**
     * How many ticks it costs for the gun to reload.
     */
    var reloadSpeed: Ticks = Ticks(20),

    /**
     * The amount of shots per magazine: before reloading.
     */
    var magazineSize: Int = 8,

    /**
     * How much ammo is consumed per shot.
     */
    var ammoPerShot: Int = 1,

    /**
     * Special red text to show on the weapon card, `null` for no text.
     */
    var redText: String? = null,

    /**
     * Extra lines of information shown on the weapon card.
     * Empty list for no information.
     */
    val extraInfoText: MutableList<String> = ArrayList(),

    /**
     * Which elements bullets fired with this gun apply to the target.
     * Order of application is the order in this list.
     */
    val elements: MutableList<Elemental> = ArrayList(),

    /**
     * The chance each element is applied to the target.
     */
    val elementalChance: MutableMap<Elemental, Chance> = HashMap(),

    /**
     * How many ticks each elemental effect lasts when applied.
     */
    val elementalDuration: MutableMap<Elemental, Ticks> = HashMap(),

    /**
     * How much damage each elemental effect deals per 0.5 seconds.
     */
    val elementalDamage: MutableMap<Elemental, Damage> = HashMap(),

    /**
     * How large the splash damage radius is, 0.0 for no splash damage.
     */
    var splashRadius: Double = 0.0,

    /**
     * How much splash damage to deal on impact.
     */
    var splashDamage: Damage = Damage(0.0),

    /**
     * How much to change the pitch after firing the gun.
     * Negative angle is upward, positive downward in range [-90,90].
     *
     * `null` for no recoil angle. Disabled by default because it is extremely jarring
     * server side. Only reserve for heavy guns with slow fire rates.
     */
    var recoilAngle: Double? = null,

    /**
     * The manufacturer of this gun.
     */
    var manufacturer: Manufacturer = Manufacturers.MALIWAN,

    /**
     * The rarity/grade of this weapon.
     */
    var rarity: Rarity = Rarities.COMMON,

    /**
     * Weapon class of this gun.
     * Determines base properties of each class combined with which ammo reserve to use.
     */
    var weaponClass: WeaponClass = WeaponClasses.PISTOL,

    /**
     * How many pellets to fire at once.
     */
    var pelletCount: Int = 1,

    /**
     * Speed of the bullets in blocks per second.
     */
    var bulletSpeed: Double = 90.0,

    /**
     * How much damage to apply when dealing melee damage with this gun.
     */
    var meleeDamage: Damage = Damage(1.0),

    /**
     * How many pellets to fire in burst (per shot).
     */
    var burstCount: Int = 1,

    /**
     * How much delay there must be between each burst pellet.
     */
    var burstDelay: Ticks = Ticks(2)
) {

    /**
     * Generates bullet meta for a bullet fired by a gun with these gun properties.
     */
    fun bulletMeta(): BulletMeta {
        return BulletMeta(baseDamage)
    }

    /**
     * Changes the item meta to reflect this gun's properties in the item info box.
     */
    fun applyToItem(itemStack: ItemStack) {
        val itemMeta = itemStack.itemMeta ?: return
        itemMeta.setDisplayName("${rarity.colourPrefix}$name")

        val lore = ArrayList<String>()
        lore += "${ChatColor.GRAY}${rarity.displayName} • ${weaponClass.displayName} • ${manufacturer.displayName}"

        val pelletPrefix = if (pelletCount == 1) "" else "${ChatColor.WHITE}$pelletCount${ChatColor.GRAY}×"
        lore += "${ChatColor.GRAY}Damage: $pelletPrefix${ChatColor.WHITE}${baseDamage.heartDisplay}"
        lore += "${ChatColor.GRAY}Accuracy: ${ChatColor.WHITE}${accuracy.percentageDisplay}"
        lore += "${ChatColor.GRAY}Fire Rate: ${ChatColor.WHITE}%.1f".format(fireRate)
        lore += "${ChatColor.GRAY}Reload Speed: ${ChatColor.WHITE}%.1f".format(reloadSpeed.seconds)
        lore += "${ChatColor.GRAY}Magazine Size: ${ChatColor.WHITE}%d".format(magazineSize)

        elements.forEach { element ->
            val damage = if (element in elementalDamage) " " + elementalDamage[element]!!.heartDisplay else ""
            lore += "${element.colourPrefix}${element.displayName} ${ChatColor.WHITE}(${elementalChance[element]?.percentageDisplay}$damage)"
        }

        var separator: () -> Unit = { lore += "" }
        fun placeSeparator() = run { separator(); separator = {} }

        redText?.let { text ->
            placeSeparator()
            text.split("\n").forEach { line ->
                lore += "${ChatColor.RED}$line"
            }
        }

        extraInfoText.forEach { line ->
            placeSeparator()
            lore += "${ChatColor.WHITE}$line"
        }

        // Melee damage bonus.
        if (meleeDamage.damage > 1.0001) {
            placeSeparator()
            lore += "${ChatColor.WHITE}+${meleeDamage.heartDisplay} Melee Damage"
        }

        if (ammoPerShot > 1) {
            placeSeparator()
            lore += "${ChatColor.WHITE}Consumes %d ammo per shot".format(ammoPerShot)
        }

        itemMeta.lore = lore
        setPersistantData(itemMeta)
        itemStack.setItemMeta(itemMeta)
    }

    fun setPersistantData(itemMeta: ItemMeta) = itemMeta.persistentDataContainer.apply {
        set(Keys.gunProperties, PersistentDataType.STRING, serialize())
    }

    /**
     * GunProperties object -> json string.
     */
    fun serialize(): String = GSON.toJson(this)

    companion object {

        val GSON: Gson = GsonBuilder()
            .registerTypeAdapter(Elemental::class.java, ElementalTypeAdapter)
            .registerTypeAdapter(Manufacturer::class.java, ManufacturerTypeAdapter)
            .registerTypeAdapter(Rarity::class.java, RarityTypeAdapter)
            .registerTypeAdapter(WeaponClass::class.java, WeaponClassTypeAdapter)
            .create()

        /**
         * Gun string -> GunProperties object.
         */
        fun deserialize(json: String): GunProperties = GSON.fromJson(json, GunProperties::class.java) /* TODO: Add type handlers */
    }
}

/**
 * Get the gun properties of the gun that the player holds in their hand.
 * `null` if no gun could be found.
 */
fun Player.gunProperties(): GunProperties? {
    val primary = inventory.itemInMainHand
    val secondary = inventory.itemInOffHand
    return primary.gunProperties() ?: secondary.gunProperties()
}

/**
 * Get the gun properties of the given item.
 * `null` if this item has no gun properties.
 */
fun ItemStack.gunProperties(): GunProperties? {
    val itemMeta = itemMeta ?: return null
    val dataStore = itemMeta.persistentDataContainer
    if (dataStore.has(Keys.gunProperties, PersistentDataType.STRING).not()) return null

    val gunPropertiesJson = dataStore.get(Keys.gunProperties, PersistentDataType.STRING) ?: return null
    return GunProperties.deserialize(gunPropertiesJson)
}