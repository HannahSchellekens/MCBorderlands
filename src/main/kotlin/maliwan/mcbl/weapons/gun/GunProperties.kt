package maliwan.mcbl.weapons.gun

import maliwan.mcbl.Keys
import maliwan.mcbl.util.*
import maliwan.mcbl.weapons.*
import maliwan.mcbl.weapons.gun.behaviour.FibWeaponCard
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
    open var accuracy: Probability = Probability(0.985),

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
    open val elementalProbability: LinkedHashMap<Elemental, Probability> = LinkedHashMap(),

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

    /**
     * The chance for firing an extra shot.
     */
    var extraShotProbability: Probability = Probability.ZERO,

    /**
     * The chance for a shot to not consume ammo.
     */
    var freeShotProbability: Probability = Probability.ZERO,

    /**
     * The percentage of damage that must be converted to healing for the shooter.
     * Negative values damage the user.
     * A value of 0.0 means that the bullet does not apply transfusion healing.
     */
    var transfusion: Double = 0.0,

    /**
     * How many times the bullet will bounce off surfaces.
     */
    var bounces: Int = 0,

    /**
     * Whether bullets pierce enemies.
     */
    var isPiercing: Boolean = false,

    /**
     * Whether the bullets themselves will deal the listed damage on the weapon card.
     */
    var directDamage: Boolean = true,

    /**
     * How many ticks to look forward for potential targets to home in on.
     */
    var homingTargetDistance: Double = 5.0,

    /**
     * In how many blocks radius to look for possible targets after traversing [homingTargetDistance].
     */
    var homingTargetRadius: Double = 5.0,

    /**
     * How quickly to home in on targets.
     * 0.0 means no homing.
     * 1.0 means instant homing.
     */
    var homingStrength: Double = 0.0,

    /**
     * How bullets affect armour damage reduction.
     * 1.0 means that no armour reduction is applied.
     * 0.0-1.0 means that only part of the armour reduction is applied.
     * 0.0 means that the regular armour reduction is applied.
     */
    var armourPenetration: Double = 0.0,
) {

    /**
     * Generates bullet meta for a bullet fired by a gun with these gun properties.
     */
    fun bulletMeta(owner: LivingEntity): BulletMeta {
        return BulletMeta(
            owner,
            assembly,
            baseDamage,
            elements = ArrayList(elements),
            elementalProbability = HashMap(elementalProbability),
            elementalDuration = HashMap(elementalDuration),
            elementalDamage = HashMap(elementalDamage),
            elementalPolicy = elementalPolicy,
            splashRadius = splashRadius,
            splashDamage = splashDamage,
            gravity = gravity,
            bonusCritMultiplier = bonusCritMultiplier,
            transfusion = transfusion,
            bouncesLeft = bounces,
            isPiercing = isPiercing,
            directDamage = directDamage,
            homingTargetDistance = homingTargetDistance,
            homingTargetRadius = homingTargetRadius,
            homingStrength = homingStrength,
            armourPenetration = armourPenetration,
        )
    }

    /**
     * Adds an elemental effect to this gun.
     */
    fun addElement(elemental: Elemental, damage: Damage, duration: Ticks, probability: Probability) {
        elements.add(elemental)
        elementalDamage[elemental] = damage
        elementalDuration[elemental] = duration
        elementalProbability[elemental] = probability
    }

    /**
     * Changes the item meta to reflect this gun's properties in the item info box.
     */
    fun applyToItem(itemStack: ItemStack) {
        val itemMeta = itemStack.itemMeta ?: return
        itemMeta.setDisplayName("${rarity.colourPrefix}$name")

        val fib = assembly?.behaviours?.firstOrNull { it is FibWeaponCard } as? FibWeaponCard

        val lore = ArrayList<String>()
        lore.addGunTypeToCard()
        lore.addBaseStatsToCard(fib)
        lore.addElementStatsToCard(fib)

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

        // Manufacturer gimmicks.
        if (fib == null || fib.showGeneratedInfo) {
            when (manufacturer) {
                Manufacturer.HYPERION -> {
                    placeSeparator()
                    lore += "${ChatColor.WHITE}• Sustained fire increases accuracy"
                }

                Manufacturer.TEDIORE -> {
                    placeSeparator()
                    lore += "${ChatColor.WHITE}• Explodes like a grenade when reloaded"
                }
                else -> Unit
            }
        }

        // Critical damage bonus.
        if (bonusCritMultiplier != null && bonusCritMultiplier!! > 0.0001 && (fib == null || fib.showGeneratedInfo)) {
            placeSeparator()
            lore += "${ChatColor.WHITE}• +${bonusCritMultiplier!!.formatPercentage(0)} Critical Hit Damage"
        }

        // Melee damage bonus.
        if (meleeDamage.damage > 1.0001 && (fib == null || fib.showGeneratedInfo)) {
            placeSeparator()
            lore += "${ChatColor.WHITE}• +${meleeDamage.heartDisplay} Melee Damage"
        }

        // Bonus elemental damage.
        if (splashDamage.damage > 0.0001 && (fib == null || fib.showGeneratedInfo)) {
            placeSeparator()
            val bonus = if (weaponClass == WeaponClass.LAUNCHER) "" else "bonus "
            lore += "${ChatColor.WHITE}• Deals ${splashDamage.heartDisplay} ${bonus}elemental damage"
        }

        // Transfusion.
        if (transfusion > 0.0001 && (fib == null || fib.showGeneratedInfo)) {
            placeSeparator()
            lore += "${ChatColor.WHITE}• Heals ${"%.1f".format(transfusion * 100)}% of damage dealt"
        }

        // Transfusion.
        if (homingStrength > 0.00001 && (fib == null || fib.showGeneratedInfo)) {
            placeSeparator()
            lore += "${ChatColor.WHITE}• Bullets home in on targets"
        }

        // Armour penetration.
        if (armourPenetration in 0.99999..1.0001) {
            placeSeparator()
            lore += "${ChatColor.WHITE}• Damage penetrates armour"
        }
        else if (armourPenetration > 0.00001 && armourPenetration < 0.99999) {
            placeSeparator()
            lore += "${ChatColor.WHITE}• Damage partially penetrates armour"
        }

        // Consumes ammo per shot.
        if (ammoPerShot > 1 && (fib == null || fib.showGeneratedInfo)) {
            placeSeparator()
            lore += "${ChatColor.WHITE}• Consumes %d ammo per shot".format(ammoPerShot)
        }

        // Reduced ammo per shot.
        if (freeShotProbability.chance > 0.01 && (fib == null || fib.showGeneratedInfo)) {
            placeSeparator()
            lore += "${ChatColor.WHITE}• Consumes reduced ammo per shot"
        }

        // Custom information.
        extraInfoText.forEach { line ->
            placeSeparator()
            var text = line
            Elemental.entries.forEach {
                text = text.replace(it.name.lowercase(), it.chatColor + it.name.lowercase() + ChatColor.WHITE)
            }

            val lines = text.split("\n")
            lore += "${ChatColor.WHITE}• ${lines.first()}"
            lines.drop(1).forEach {
                lore += "${ChatColor.WHITE}$it"
            }
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

        // Weapon parts.
        assembly?.let { ass ->
            lore += "\n"
            val partStrings = ass.parts
                ?.map { "${ChatColor.GRAY}${it.partTypeName}: ${it.partName}" }
                ?.toMutableList() ?: ArrayList()

            ass.capacitor?.let {
                partStrings += "${ChatColor.GRAY}Capacitor: ${it.partName}"
            }

            partStrings.windowed(2, 2, partialWindows = true).forEach { window ->
                lore += window.joinToString(" ${ChatColor.GRAY}• ")
            }
        }

        itemMeta.lore = lore
        setPersistantData(itemMeta)
        itemStack.setItemMeta(itemMeta)
    }

    private fun MutableList<String>.addGunTypeToCard() {
        this += "${ChatColor.GRAY}${rarity.displayName} • ${weaponClass.displayName} • ${manufacturer.displayName}"
    }

    private fun MutableList<String>.addBaseStatsToCard(fib: FibWeaponCard?) {
        val showDamage = baseDamage * (fib?.let { it.fibMultiplierBase.modifyRandom(it.fibMultiplierFuzz) } ?: 1.0)
        val showAccuracy = 100 * accuracy.chance * (fib?.let { it.fibMultiplierBase.modifyRandom(it.fibMultiplierFuzz) } ?: 1.0)
        val showFireRate = fireRate * (fib?.let { it.fibMultiplierBase.modifyRandom(it.fibMultiplierFuzz) } ?: 1.0)
        val showReloadSpeed = reloadSpeed.seconds * (fib?.let { it.fibMultiplierBase.modifyRandom(it.fibMultiplierFuzz) } ?: 1.0)
        val showMagSize = magazineSize * (fib?.let { it.fibMultiplierBase.modifyRandom(it.fibMultiplierFuzz) } ?: 1.0)

        val pelletPrefix = if (pelletCount == 1 || fib != null) "" else "${ChatColor.WHITE}$pelletCount${ChatColor.GRAY}×"
        this += "${ChatColor.GRAY}Damage: ♥$pelletPrefix${ChatColor.WHITE}${showDamage.heartDisplay}"
        this += "${ChatColor.GRAY}Accuracy: \uD83C\uDFAF${ChatColor.WHITE}%.1f".format(showAccuracy) + "%"
        this += "${ChatColor.GRAY}Fire Rate: \uD83D\uDD2B${ChatColor.WHITE}%.1f".format(showFireRate)
        this += "${ChatColor.GRAY}Reload Speed: ♻${ChatColor.WHITE}%.1f".format(showReloadSpeed)
        this += "${ChatColor.GRAY}Magazine Size: □${ChatColor.WHITE}%d".format(showMagSize.toInt())
    }

    private fun MutableList<String>.addElementStatsToCard(fib: FibWeaponCard?) {
        elements.forEach { element ->
            val parts = ArrayList<String>(2)

            // Chance
            if (element != Elemental.EXPLOSIVE) {
                val chance = 100 * (elementalProbability[element]?.chance ?: 0.0) * (fib?.let { it.fibMultiplierBase.modifyRandom(it.fibMultiplierFuzz) } ?: 1.0)
                parts += "%.1f".format(chance) + "%"
            }

            // Damage
            val damage = elementalDamage[element]?.damage ?: 0.0
            val showDamage = damage * (fib?.let { it.fibMultiplierBase.modifyRandom(it.fibMultiplierFuzz) } ?: 1.0)

            val perSecond = if (element.noDotMultiplier < 0.001) "" else "/sec"
            if (element != Elemental.EXPLOSIVE && showDamage > 0.01) {
                parts += Damage(showDamage * 2.0).heartDisplay + perSecond
            }

            val suffix = if (parts.isEmpty()) "" else parts.joinToString(
                separator ="${ChatColor.GRAY} • ",
                prefix = " ${ChatColor.GRAY}(",
                postfix = "${ChatColor.GRAY})"
            ) { "${ChatColor.WHITE}$it" }

            this += "${element.chatColor}${element.symbol} ${element.displayName}$suffix"
        }
    }

    fun setPersistantData(itemMeta: ItemMeta) = itemMeta.persistentDataContainer.apply {
        set(Keys.gunProperties, PersistentDataType.STRING, serialize())
    }

    /**
     * GunProperties object -> json string.
     */
    fun serialize(): String = GSON.toJson(this)

    override fun equals(other: Any?): Boolean {
        // Do not include assembly in equality check, otherwise equal gun executions won't be
        // recognised as equal.
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
        if (elementalProbability != other.elementalProbability) return false
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
        if (extraShotProbability != other.extraShotProbability) return false
        if (freeShotProbability != other.freeShotProbability) return false
        if (transfusion != other.transfusion) return false
        if (bounces != other.bounces) return false
        if (isPiercing != other.isPiercing) return false
        if (directDamage != other.directDamage) return false
        if (homingTargetDistance != other.homingTargetDistance) return false
        if (homingTargetRadius != other.homingTargetRadius) return false
        if (homingStrength != other.homingStrength) return false
        if (armourPenetration != other.armourPenetration) return false

        return true
    }

    override fun hashCode(): Int {
        // Do not include assembly in equality check, otherwise equal gun executions won't be
        // recognised as equal.
        var result = name.hashCode()
        result = 31 * result + baseDamage.hashCode()
        result = 31 * result + accuracy.hashCode()
        result = 31 * result + recoil.hashCode()
        result = 31 * result + fireRate.hashCode()
        result = 31 * result + reloadSpeed.hashCode()
        result = 31 * result + magazineSize
        result = 31 * result + ammoPerShot
        result = 31 * result + (redText?.hashCode() ?: 0)
        result = 31 * result + (cyanText?.hashCode() ?: 0)
        result = 31 * result + extraInfoText.hashCode()
        result = 31 * result + elements.hashCode()
        result = 31 * result + elementalProbability.hashCode()
        result = 31 * result + elementalDuration.hashCode()
        result = 31 * result + elementalDamage.hashCode()
        result = 31 * result + elementalPolicy.hashCode()
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
        result = 31 * result + gravity.hashCode()
        result = 31 * result + (bonusCritMultiplier?.hashCode() ?: 0)
        result = 31 * result + extraShotProbability.hashCode()
        result = 31 * result + freeShotProbability.hashCode()
        result = 31 * result + transfusion.hashCode()
        result = 31 * result + bounces
        result = 31 * result + isPiercing.hashCode()
        result = 31 * result + directDamage.hashCode()
        result = 31 * result + homingTargetDistance.hashCode()
        result = 31 * result + homingTargetRadius.hashCode()
        result = 31 * result + homingStrength.hashCode()
        result = 31 * result + armourPenetration.hashCode()
        return result
    }

    companion object {

        /**
         * Armour reduction is applied as usual. See [armourPenetration].
         */
        const val NO_ARMOUR_PENETRATION = 0.0

        /**
         * Armour reduction is not applied. See [armourPenetration].
         */
        const val FULL_ARMOUR_PENETRATION = 1.0

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