package maliwan.mcbl.weapons.gun

import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.names.PistolNames
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * An amalgamation of weapon parts into a single gun.
 *
 * @author Hannah Schellekens
 */
sealed class WeaponAssembly(

    /**
     * Type of weapon.
     */
    val weaponClass: WeaponClass,

    /**
     * The main manufacturer of the weapon: i.e. the body.
     */
    val manufacturer: Manufacturer,

    /**
     * All weapon parts of this gun.
     */
    @Transient
    open val parts: List<WeaponPart>?,

    /**
     * The elemental capacitor of the gun.
     */
    val capacitor: Capacitor? = null
) {

    /**
     * The weapon name.
     */
    abstract val gunName: String

    constructor(weaponClass: WeaponClass, manufacturer: Manufacturer, capacitor: Capacitor?, vararg parts: WeaponPart?)
            : this(weaponClass, manufacturer, parts.filterNotNull(), capacitor)

    /**
     * Applies the stat modifiers of all weapon parts of this gun to the given gun properties.
     */
    open fun applyToGun(properties: GunProperties): GunProperties {
        parts?.forEach {
            it.statModifiers.applyAll(properties)
            if (it.manufacturer == manufacturer) {
                it.manufacturerStatModifiers.applyAll(properties)
            }
        }
        return properties
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> partOfType(klass: KClass<*>): T? {
        if (klass == Capacitor::class) {
            return capacitor as T?
        }
        return parts?.find { it::class.isSubclassOf(klass) } as? T
    }
}

/**
 * @author Hannah Schellekens
 */
class PistolAssembly(
    val body: Manufacturer,
    val barrel: PistolParts.Barrel,
    val grip: PistolParts.Grip,
    val accessory: PistolParts.Accessory? = null,
    capacitor: Capacitor? = null
) : WeaponAssembly(WeaponClass.PISTOL, body, capacitor, barrel, grip, accessory) {

    override val parts: List<WeaponPart>
        get() = listOfNotNull(barrel, grip, accessory)

    override val gunName: String
        get() = PistolNames.nameOf(manufacturer, barrel, accessory, capacitor)

    override fun toString(): String {
        return "PistolAssembly(body=$body, barrel=$barrel, grip=$grip, accessory=$accessory)"
    }
}