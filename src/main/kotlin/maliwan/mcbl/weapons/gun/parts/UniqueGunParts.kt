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
        UniqueGunPart.UniqueCapacitor(Manufacturer.BANDIT, WeaponClass.PISTOL, Capacitor.TINDERBOX),
        UniqueGunPart.UniqueCapacitor(Manufacturer.BANDIT, WeaponClass.LAUNCHER, Capacitor.ROASTER_SHOCK),
        UniqueGunPart.UniqueCapacitor(Manufacturer.BANDIT, WeaponClass.LAUNCHER, Capacitor.ROASTER_INCENDIARY),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.PISTOL, PistolParts.Barrel.GWENS_HEAD),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.PISTOL, PistolParts.Barrel.TEAPOT),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.PISTOL, PistolParts.Barrel.LADY_FINGER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.PISTOL, PistolParts.Barrel.LADY_FIST),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.PISTOL, PistolParts.Barrel.GREED),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.PISTOL, PistolParts.Barrel.JUDGE),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.PISTOL, PistolParts.Barrel.LAW),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.PISTOL, PistolParts.Barrel.REX),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TORGUE, WeaponClass.PISTOL, PistolParts.Barrel.POCKET_ROCKET),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.SNIPER, SniperParts.Barrel.SLOTH),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.SNIPER, SniperParts.Barrel.BUFFALO),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.SNIPER, SniperParts.Barrel.COBRA),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.TORGUE, WeaponClass.LAUNCHER, LauncherParts.Barrel.CREAMER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TORGUE, WeaponClass.LAUNCHER, LauncherParts.Barrel.TWELVE_POUNDER),
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