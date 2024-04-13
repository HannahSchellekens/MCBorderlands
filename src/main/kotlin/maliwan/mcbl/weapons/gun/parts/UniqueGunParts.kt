package maliwan.mcbl.weapons.gun.parts

import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.WeaponPart

/**
 * @author Hannah Schellekens
 */
object UniqueGunParts {

    val legendaryParts: List<UniqueGunPart> = listOf(
        UniqueGunPart.UniqueCapacitor(Manufacturer.MALIWAN, WeaponClass.SMG, Capacitor.HELLFIRE),
        UniqueGunPart.UniqueCapacitor(Manufacturer.MALIWAN, WeaponClass.PISTOL, Capacitor.DEFILER),
    )

    val uniqueParts: List<UniqueGunPart> = listOf(
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.PISTOL, PistolParts.Barrel.LADY_FINGER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.PISTOL, PistolParts.Barrel.LADY_FIST),
    )
}

/**
 * @author Hannah Schellekens
 */
sealed class UniqueGunPart(
    val manufacturer: Manufacturer,
    val weaponClass: WeaponClass
) {

    class UniqueWeaponPart(
        manufacturer: Manufacturer,
        weaponClass: WeaponClass,
        val part: WeaponPart
    ) : UniqueGunPart(manufacturer, weaponClass)

    class UniqueCapacitor(
        manufacturer: Manufacturer,
        weaponClass: WeaponClass,
        val capacitor: Capacitor
    ) : UniqueGunPart(manufacturer, weaponClass)
}