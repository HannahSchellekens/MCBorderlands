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

    val epicParts: List<UniqueGunPart> = listOf(
        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.SMG, SmgParts.Barrel.BONE_SHREDDER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.SMG, SmgParts.Barrel.CRIT),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.SMG, SmgParts.Barrel.GOOD_TOUCH),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.SMG, SmgParts.Barrel.BAD_TOUCH),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.SMG, SmgParts.Barrel.ERIDIAN),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.SMG, SmgParts.Barrel.ERIDIAN),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.SMG, SmgParts.Barrel.ERIDIAN),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.SMG, SmgParts.Barrel.ERIDIAN),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TEDIORE, WeaponClass.SMG, SmgParts.Barrel.ERIDIAN),
    )

    val rareParts: List<UniqueGunPart> = listOf(
        UniqueGunPart.UniqueCapacitor(Manufacturer.BANDIT, WeaponClass.PISTOL, Capacitor.TINDERBOX),
        UniqueGunPart.UniqueCapacitor(Manufacturer.BANDIT, WeaponClass.LAUNCHER, Capacitor.ROASTER_SHOCK),
        UniqueGunPart.UniqueCapacitor(Manufacturer.BANDIT, WeaponClass.LAUNCHER, Capacitor.ROASTER_INCENDIARY),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.PISTOL, PistolParts.Barrel.GWENS_HEAD),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.PISTOL, PistolParts.Barrel.TEAPOT),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.PISTOL, PistolParts.Barrel.FIBBER_CRIT),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.PISTOL, PistolParts.Barrel.FIBBER_RICOCHET),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.PISTOL, PistolParts.Barrel.FIBBER_SHOTGUN),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.PISTOL, PistolParts.Barrel.LADY_FINGER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.PISTOL, PistolParts.Barrel.LADY_FIST),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.PISTOL, PistolParts.Barrel.GREED),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.PISTOL, PistolParts.Barrel.JUDGE),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.PISTOL, PistolParts.Barrel.LAW),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.PISTOL, PistolParts.Barrel.REX),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.PISTOL, PistolParts.Barrel.RUBI),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TORGUE, WeaponClass.PISTOL, PistolParts.Barrel.POCKET_ROCKET),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Grip.CHOPPER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.SCORPIO),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.DAMNED_COWBOY),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.STINKPOT),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.STOMPER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TORGUE, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.BOOM_PUPPY),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.VLADOF, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.HAIL),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.SHOTGUN, ShotgunParts.Barrel.DOG),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.SHOTGUN, ShotgunParts.Barrel.ROKSALT),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.SHOTGUN, ShotgunParts.Barrel.HEART_BREAKER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.SHOTGUN, ShotgunParts.Barrel.HYDRA),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.SHOTGUN, ShotgunParts.Barrel.ORPHAN_MAKER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TEDIORE, WeaponClass.SHOTGUN, ShotgunParts.Barrel.BLOCKHEAD),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.SNIPER, SniperParts.Barrel.SLOTH),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.SNIPER, SniperParts.Barrel.BUFFALO),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.SNIPER, SniperParts.Barrel.COBRA),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.SNIPER, SniperParts.Barrel.CHERE_AMIE),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.SMG, SmgParts.Barrel.BANE),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.SMG, SmgParts.Barrel.COMMERCE),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.SMG, SmgParts.Barrel.CHULAINN),

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