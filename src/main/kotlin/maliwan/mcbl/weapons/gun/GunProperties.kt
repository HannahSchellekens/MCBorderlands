package maliwan.mcbl.weapons.gun

import maliwan.mcbl.Chance
import maliwan.mcbl.Damage
import maliwan.mcbl.Ticks
import maliwan.mcbl.weapons.*

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
    var baseDamage: Double = 0.0,

    /**
     * Shot direction accuracy percentage.
     *
     * 100% is dead center. 0% is anywhere on screen.
     */
    var accuracy: Double = 1.0,

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
     */
    var recoilAngle: Double = -4.0,

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
    var bulletSpeed: Double = 35.0,

    /**
     * Maximum differentiation in pitch/yaw angle of each pellet from the shot direction.
     * Applied after accuracy for the initial direction.
     */
    var weaponSpread: Double = 0.0,

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
)