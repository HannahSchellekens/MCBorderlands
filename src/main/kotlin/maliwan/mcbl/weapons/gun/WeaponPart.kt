package maliwan.mcbl.weapons.gun

import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass

/**
 * @author Hannah Schellekens
 */
interface WeaponPart {

    /**
     * The type name of weapon part (Barrel, Stock, ...)
     */
    val partTypeName: String

    /**
     * The name of the weapon part.
     */
    val partName: String

    /**
     * For which weapon class this part is made.
     */
    val weaponClass: WeaponClass

    /**
     * Which manufacturer made this weapon part.
     */
    val manufacturer: Manufacturer

    /**
     * The stat modifiers that this weapon part provides to the weapon.
     */
    val statModifiers: List<StatModifier>

    /**
     * Which stat modifiers to apply to the weapon part if the manufacturer of the part matches the
     * manufacturer of the gun.
     */
    val manufacturerStatModifiers: List<StatModifier>
}

/**
 * Applies all stat modifiers of this weapon part to the given gun properties.
 *
 * Applies manufacturer stat modifiers whenever the manufacturer of the part matches the manufacturer of the gun.
 */
fun WeaponPart.applyStatModifiers(gunProperties: GunProperties) {
    statModifiers.applyAll(gunProperties)

    if (manufacturer == gunProperties.manufacturer) {
        manufacturerStatModifiers.applyAll(gunProperties)
    }
}