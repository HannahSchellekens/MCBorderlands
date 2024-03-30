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
    val parts: List<WeaponPart>
) {

    /**
     * The weapon name.
     */
    abstract val gunName: String

    constructor(weaponClass: WeaponClass, manufacturer: Manufacturer, vararg parts: WeaponPart?)
            : this(weaponClass, manufacturer, parts.filterNotNull())

    /**
     * Applies the stat modifiers of all weapon parts of this gun to the given gun properties.
     */
    open fun applyToGun(properties: GunProperties): GunProperties {
        parts.forEach {
            it.statModifiers.applyAll(properties)
            if (it.manufacturer == manufacturer) {
                it.manufacturerStatModifiers.applyAll(properties)
            }
        }
        return properties
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : WeaponPart> partOfType(klass: KClass<T>): T? {
        return parts.find { it::class.isSubclassOf(klass) } as? T
    }
}

/**
 * @author Hannah Schellekens
 */
data class PistolAssembly(
    val body: Manufacturer,
    val barrel: PistolParts.Barrel,
    val grip: PistolParts.Grip,
    val accessory: PistolParts.Accessory? = null,
    val capacitor: Elemental? = null
) : WeaponAssembly(WeaponClass.PISTOL, body, barrel, grip, accessory) {

    override val gunName = PistolNames.nameOf(manufacturer, barrel, accessory, capacitor)
}