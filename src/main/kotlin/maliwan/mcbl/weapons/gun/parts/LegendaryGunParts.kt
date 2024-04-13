package maliwan.mcbl.weapons.gun.parts

import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.WeaponPart

/**
 * @author Hannah Schellekens
 */
object LegendaryGunParts {

    val parts: List<LegendaryGunPart> = listOf(
        LegendaryGunPart.LegendaryCapacitor(Manufacturer.MALIWAN, WeaponClass.SMG, Capacitor.HELLFIRE)
    )
}

/**
 * @author Hannah Schellekens
 */
sealed class LegendaryGunPart(
    val manufacturer: Manufacturer,
    val weaponClass: WeaponClass
) {

    class LegendaryWeaponPart(
        manufacturer: Manufacturer,
        weaponClass: WeaponClass,
        val part: WeaponPart
    ) : LegendaryGunPart(manufacturer, weaponClass)

    class LegendaryCapacitor(
        manufacturer: Manufacturer,
        weaponClass: WeaponClass,
        val capacitor: Capacitor
    ) : LegendaryGunPart(manufacturer, weaponClass)
}