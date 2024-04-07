package maliwan.mcbl.weapons.gun.stats

import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.GunProperties

/**
 * Create new [GunProperties] of weapon type `weaponClass` for a given manufacturer.
 */
fun newBaseValueProperties(manufacturer: Manufacturer, weaponClass: WeaponClass) = when (weaponClass) {
    WeaponClass.PISTOL -> PistolBaseValues.newGunProperties(manufacturer)
    WeaponClass.SHOTGUN -> ShotgunBaseValues.newGunProperties(manufacturer)
    WeaponClass.SNIPER -> SniperBaseValues.newGunProperties(manufacturer)
    WeaponClass.SMG -> SmgBaseValues.newGunProperties(manufacturer)
    WeaponClass.ASSAULT_RIFLE -> AssaultRifleBaseValues.newGunProperties(manufacturer)
    WeaponClass.LAUNCHER -> LauncherBaseValues.newGunProperties(manufacturer)
}