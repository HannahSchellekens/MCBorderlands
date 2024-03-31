package maliwan.mcbl.weapons.gun.stats

import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.GunProperties

/**
 * Create new [GunProperties] of weapon type `weaponClass` for a given manufacturer.
 */
fun newBaseValueProperties(manufacturer: Manufacturer, weaponClass: WeaponClass) = when (weaponClass) {
    WeaponClass.PISTOL -> PistolBaseValues.newGunProperties(manufacturer)
    else -> error("Weapon class <$weaponClass> is not supported.")
}