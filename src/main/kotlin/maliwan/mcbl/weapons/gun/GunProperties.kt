package maliwan.mcbl.weapons.gun

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import maliwan.mcbl.Chance
import maliwan.mcbl.Damage
import maliwan.mcbl.Keys
import maliwan.mcbl.Ticks
import maliwan.mcbl.weapons.*
import org.bukkit.ChatColor
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
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
    open var name: String = "Gun",

    /**
     * Damage per shot in half hearts.
     */
    open var baseDamage: Damage = Damage(1.0),

    /**
     * Shot direction accuracy percentage.
     *
     * 100% is dead center. 0% is anywhere on screen.
     */
    open var accuracy: Chance = Chance(0.985),

    /**
     * With how much to multiply the accuracy after each shot.
     */
    open var recoil: Double = 0.995,

    /**
     * Amount of shots per second.
     */
    open var fireRate: Double = 2.8,

    /**
     * How many ticks it costs for the gun to reload.
     */
    open var reloadSpeed: Ticks = Ticks(20),

    /**
     * The amount of shots per magazine: before reloading.
     */
    open var magazineSize: Int = 8,

    /**
     * How much ammo is consumed per shot.
     */
    open var ammoPerShot: Int = 1,

    /**
     * Special red text to show on the weapon card, `null` for no text.
     */
    open var redText: String? = null,

    /**
     * Extra lines of information shown on the weapon card.
     * Empty list for no information.
     */
    open val extraInfoText: MutableList<String> = ArrayList(),

    /**
     * Which elements bullets fired with this gun apply to the target.
     * Order of application is the order in this list.
     */
    open val elements: MutableList<Elemental> = ArrayList(),

    /**
     * The chance each element is applied to the target.
     */
    open val elementalChance: MutableMap<Elemental, Chance> = HashMap(),

    /**
     * How many ticks each elemental effect lasts when applied.
     */
    open val elementalDuration: MutableMap<Elemental, Ticks> = HashMap(),

    /**
     * How much damage each elemental effect deals per 0.5 seconds.
     */
    open val elementalDamage: MutableMap<Elemental, Damage> = HashMap(),

    /**
     * What to do when the target already has an elemental effect of the same type.
     */
    open var elementalPolicy: ElementalStatusEffects.ApplyPolicy = ElementalStatusEffects.ApplyPolicy.REPLACE,

    /**
     * How large the splash damage radius is, 0.0 for no splash damage.
     */
    open var splashRadius: Double = 0.0,

    /**
     * How much splash damage to deal on impact.
     */
    open var splashDamage: Damage = Damage(0.0),

    /**
     * How much to change the pitch after firing the gun.
     * Negative angle is upward, positive downward in range [-90,90].
     *
     * `null` for no recoil angle. Disabled by default because it is extremely jarring
     * server side. Only reserve for heavy guns with slow fire rates.
     */
    open var recoilAngle: Double? = null,

    /**
     * The manufacturer of this gun.
     */
    open var manufacturer: Manufacturer = Manufacturers.MALIWAN,

    /**
     * The rarity/grade of this weapon.
     */
    open var rarity: Rarity = Rarities.COMMON,

    /**
     * Weapon class of this gun.
     * Determines base properties of each class combined with which ammo reserve to use.
     */
    open var weaponClass: WeaponClass = WeaponClasses.PISTOL,

    /**
     * How many pellets to fire at once.
     */
    open var pelletCount: Int = 1,

    /**
     * Speed of the bullets in blocks per second.
     */
    open var bulletSpeed: Double = 90.0,

    /**
     * How much damage to apply when dealing melee damage with this gun.
     */
    open var meleeDamage: Damage = Damage(1.0),

    /**
     * How many pellets to fire in burst (per shot).
     */
    open var burstCount: Int = 1,

    /**
     * How much delay there must be between each burst pellet.
     */
    open var burstDelay: Ticks = Ticks(2),

    /**
     * Downward acceleration (+ value is downward, - is upward) of the bullet.
     */
    open var gravity: Double = 0.016,
) {

    /**
     * Generates bullet meta for a bullet fired by a gun with these gun properties.
     */
    fun bulletMeta(owner: LivingEntity): BulletMeta {
        return BulletMeta(
            owner,
            baseDamage,
            elements = elements,
            elementalChance = elementalChance,
            elementalDuration = elementalDuration,
            elementalDamage = elementalDamage,
            elementalPolicy = elementalPolicy,
            splashRadius = splashRadius,
            splashDamage = splashDamage,
            gravity = gravity
        )
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
            var text = line
            Elements.entries.forEach {
                text = text.replace(it.name.lowercase(), it.colourPrefix + it.name.lowercase() + ChatColor.WHITE)
            }
            lore += "${ChatColor.WHITE}$text"
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GunProperties) return false

        if (name != other.name) return false
        if (baseDamage != other.baseDamage) return false
        if (accuracy != other.accuracy) return false
        if (fireRate != other.fireRate) return false
        if (reloadSpeed != other.reloadSpeed) return false
        if (magazineSize != other.magazineSize) return false
        if (ammoPerShot != other.ammoPerShot) return false
        if (redText != other.redText) return false
        if (extraInfoText != other.extraInfoText) return false
        if (elements != other.elements) return false
        if (elementalChance != other.elementalChance) return false
        if (elementalDuration != other.elementalDuration) return false
        if (elementalDamage != other.elementalDamage) return false
        if (elementalPolicy != other.elementalPolicy) return false
        if (splashRadius != other.splashRadius) return false
        if (splashDamage != other.splashDamage) return false
        if (recoilAngle != other.recoilAngle) return false
        if (manufacturer != other.manufacturer) return false
        if (rarity != other.rarity) return false
        if (weaponClass != other.weaponClass) return false
        if (pelletCount != other.pelletCount) return false
        if (bulletSpeed != other.bulletSpeed) return false
        if (meleeDamage != other.meleeDamage) return false
        if (burstCount != other.burstCount) return false
        if (burstDelay != other.burstDelay) return false
        if (gravity != other.gravity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + baseDamage.hashCode()
        result = 31 * result + accuracy.hashCode()
        result = 31 * result + fireRate.hashCode()
        result = 31 * result + reloadSpeed.hashCode()
        result = 31 * result + magazineSize
        result = 31 * result + ammoPerShot
        result = 31 * result + (redText?.hashCode() ?: 0)
        result = 31 * result + extraInfoText.hashCode()
        result = 31 * result + elements.hashCode()
        result = 31 * result + elementalChance.hashCode()
        result = 31 * result + elementalDuration.hashCode()
        result = 31 * result + elementalDamage.hashCode()
        result = 31 * result + splashRadius.hashCode()
        result = 31 * result + splashDamage.hashCode()
        result = 31 * result + (recoilAngle?.hashCode() ?: 0)
        result = 31 * result + manufacturer.hashCode()
        result = 31 * result + rarity.hashCode()
        result = 31 * result + weaponClass.hashCode()
        result = 31 * result + pelletCount
        result = 31 * result + bulletSpeed.hashCode()
        result = 31 * result + meleeDamage.hashCode()
        result = 31 * result + burstCount
        result = 31 * result + burstDelay.hashCode()
        return result
    }

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

/**
 * Get the gun properties of the given item emtotu.
 * `null` if this item has no gun properties.
 */
fun Item.gunProperties(): GunProperties? {
    return itemStack.gunProperties()
}