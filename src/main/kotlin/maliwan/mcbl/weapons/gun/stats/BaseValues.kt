package maliwan.mcbl.weapons.gun.stats

import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass

/**
 * @author Hannah Schellekens
 */
object BaseValues {

    fun providerOf(weaponClass: WeaponClass) = when (weaponClass) {
        WeaponClass.PISTOL -> PistolBaseValues
        WeaponClass.SHOTGUN -> ShotgunBaseValues
        WeaponClass.SNIPER -> SniperBaseValues
        WeaponClass.SMG -> SmgBaseValues
        WeaponClass.ASSAULT_RIFLE -> AssaultRifleBaseValues
        WeaponClass.LAUNCHER -> LauncherBaseValues
    }
}

/**
 * @author Hannah Schellekens
 */
interface BaseValueProvider {

    fun <T> baseValue(manufacturer: Manufacturer, stat: Stat<T>): T
}