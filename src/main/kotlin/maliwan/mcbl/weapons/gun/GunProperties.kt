package maliwan.mcbl.weapons.gun

import maliwan.mcbl.Keys
import maliwan.mcbl.util.*
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
    open var recoil: Double = 0.994,

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
     * Special cyan text to show on the weapon card, `null` for no text.
     */
    open var cyanText: String? = null,

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
    open val elementalChance: LinkedHashMap<Elemental, Chance> = LinkedHashMap(),

    /**
     * How many ticks each elemental effect lasts when applied.
     */
    open val elementalDuration: LinkedHashMap<Elemental, Ticks> = LinkedHashMap(),

    /**
     * How much damage each elemental effect deals per 0.5 seconds.
     */
    open val elementalDamage: LinkedHashMap<Elemental, Damage> = LinkedHashMap(),

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
    open var manufacturer: Manufacturer = Manufacturer.MALIWAN,

    /**
     * The rarity/grade of this weapon.
     */
    open var rarity: Rarity = Rarity.COMMON,

    /**
     * Weapon class of this gun.
     * Determines base properties of each class combined with which ammo reserve to use.
     */
    open var weaponClass: WeaponClass = WeaponClass.PISTOL,

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

    /**
     * Extra bonus critical hit multiplier that gets ADDED to the original multiplier.
     * E.g. a value of 1.0 means that the multiplier will be +1.0 greater.
     *
     * Normally a critical is x2 damage. The bonus crit multiplier multiplies this multiplier.
     * `null` for no bonus crit multiplier.
     */
    open var bonusCritMultiplier: Double? = null,

    /**
     * The separate parts of the gun.
     */
    var assembly: WeaponAssembly? = null,
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
            gravity = gravity,
            bonusCritMultiplier = bonusCritMultiplier
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
            val chance = if (element == Elemental.EXPLOSIVE) "" else "${elementalChance[element]?.percentageDisplay}"
            val damage = if ((elementalDamage[element]?.damage ?: 0.0) > 0.01) " " + elementalDamage[element]!!.heartDisplay else ""
            val combined = "$chance$damage".trim()
            lore += "${element.chatColor}${element.displayName} ${ChatColor.GRAY}(${ChatColor.WHITE}$combined${ChatColor.GRAY})"
        }

        var separator: () -> Unit = { lore += "" }
        fun placeSeparator() = run { separator(); separator = {} }

        redText?.let { text ->
            placeSeparator()
            text.split("\n").forEach { line ->
                lore += "${ChatColor.RED}$line"
            }
        }

        cyanText?.let { text ->
            placeSeparator()
            text.split("\n").forEach { line ->
                lore += "${ChatColor.DARK_AQUA}$line"
            }
        }

        // Critical damage bonus.
        if (bonusCritMultiplier != null && bonusCritMultiplier!! > 0.0001) {
            placeSeparator()
            lore += "${ChatColor.WHITE}• +${bonusCritMultiplier!!.formatPercentage(0)} Critical Hit Damage"
        }

        // Melee damage bonus.
        if (meleeDamage.damage > 1.0001) {
            placeSeparator()
            lore += "${ChatColor.WHITE}• +${meleeDamage.heartDisplay} Melee Damage"
        }

        // Consumes ammo per shot.
        if (ammoPerShot > 1) {
            placeSeparator()
            lore += "${ChatColor.WHITE}• Consumes %d ammo per shot".format(ammoPerShot)
        }

        // Bonus elemental damage.
        if (elementalDamage.values.any { it.damage > 0.01 } && splashDamage.damage > 0.0001) {
            placeSeparator()
            lore += "${ChatColor.WHITE}• Deals ${splashDamage.heartDisplay} bonus elemental damage"
        }

        // Custom information.
        extraInfoText.forEach { line ->
            placeSeparator()
            var text = line
            Elemental.entries.forEach {
                text = text.replace(it.name.lowercase(), it.chatColor + it.name.lowercase() + ChatColor.WHITE)
            }
            lore += "${ChatColor.WHITE}• $text"
        }

        // Elemental data info.
        elements.forEach { element ->
            when (element) {
                Elemental.CORROSIVE -> {
                    placeSeparator()
                    lore += "${ChatColor.WHITE}•${element.chatColor} Highly effective vs Undead"
                }
                Elemental.INCENDIARY -> {
                    placeSeparator()
                    lore += "${ChatColor.WHITE}•${element.chatColor} Highly effective vs Flesh"
                }
                Elemental.SHOCK -> {
                    placeSeparator()
                    lore += "${ChatColor.WHITE}•${element.chatColor} Highly effective vs Armor & Fish"
                }
                Elemental.SLAG -> {
                    placeSeparator()
                    lore += "${ChatColor.WHITE}•${element.chatColor} Slagged entities take additional"
                    lore += "${element.chatColor}non-slag damage"
                }
                else -> {}
            }
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
        if (recoil != other.recoil) return false
        if (fireRate != other.fireRate) return false
        if (reloadSpeed != other.reloadSpeed) return false
        if (magazineSize != other.magazineSize) return false
        if (ammoPerShot != other.ammoPerShot) return false
        if (redText != other.redText) return false
        if (cyanText != other.cyanText) return false
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
        if (bonusCritMultiplier != other.bonusCritMultiplier) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 12289 * result + baseDamage.hashCode()
        result = 12289 * result + accuracy.hashCode()
        result = 12289 * result + recoil.hashCode()
        result = 12289 * result + fireRate.hashCode()
        result = 12289 * result + reloadSpeed.hashCode()
        result = 12289 * result + magazineSize
        result = 12289 * result + ammoPerShot
        result = 12289 * result + (redText?.hashCode() ?: 0)
        result = 12289 * result + (cyanText?.hashCode() ?: 0)
        result = 12289 * result + extraInfoText.hashCode()
        result = 12289 * result + elements.hashCode()
        result = 12289 * result + elementalChance.hashCode()
        result = 12289 * result + elementalDuration.hashCode()
        result = 12289 * result + elementalDamage.hashCode()
        result = 12289 * result + elementalPolicy.hashCode()
        result = 12289 * result + splashRadius.hashCode()
        result = 12289 * result + splashDamage.hashCode()
        result = 12289 * result + (recoilAngle?.hashCode() ?: 0)
        result = 12289 * result + manufacturer.hashCode()
        result = 12289 * result + rarity.hashCode()
        result = 12289 * result + weaponClass.hashCode()
        result = 12289 * result + pelletCount
        result = 12289 * result + bulletSpeed.hashCode()
        result = 12289 * result + meleeDamage.hashCode()
        result = 12289 * result + burstCount
        result = 12289 * result + burstDelay.hashCode()
        result = 12289 * result + gravity.hashCode()
        result = 12289 * result + (bonusCritMultiplier?.hashCode() ?: 0)
        return result
    }

    companion object {

        /**
         * Gun string -> GunProperties object.
         */
        fun deserialize(json: String): GunProperties = GSON.fromJson(json, GunProperties::class.java)
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