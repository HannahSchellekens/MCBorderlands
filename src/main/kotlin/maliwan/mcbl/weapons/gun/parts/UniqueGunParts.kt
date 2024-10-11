package maliwan.mcbl.weapons.gun.parts

import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.WeaponPart

/**
 * @author Hannah Schellekens
 */
object UniqueGunParts {

    val pearlescentParts: List<UniqueGunPart> = listOf(
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.LAUNCHER, LauncherParts.Barrel.AVATAR_STATE),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.SHOTGUN, ShotgunParts.Barrel.BUTCHER),
    )

    val legendaryParts: List<UniqueGunPart> = listOf(
        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.PISTOL, PistolParts.Barrel.GUB),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.SHOTGUN, ShotgunParts.Barrel.SLEDGES_SHOTGUN),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.SMG, SmgParts.Barrel.SLAGGA),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.LAUNCHER, LauncherParts.Barrel.BADABOOM),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.PISTOL, PistolParts.Barrel.HECTORS_PARADISE),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.PISTOL, PistolParts.Barrel.HORNET),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.VERUC),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.SMG, SmgParts.Barrel.EMPEROR),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.SNIPER, SniperParts.Barrel.PITCHFORK),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.PISTOL, PistolParts.Barrel.LOGANS_GUN),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.SHOTGUN, ShotgunParts.Barrel.OVERCOMPENSATOR),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.SMG, SmgParts.Barrel.BITCH),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.SNIPER, SniperParts.Barrel.LONGBOW),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.SNIPER, SniperParts.Barrel.PATIENCE),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.PISTOL, PistolParts.Accessory.MAGGIE),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.HAMMER_BUSTER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.SHOTGUN, ShotgunParts.Barrel.STRIKER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.SNIPER, SniperParts.Barrel.AMIGO_SINCERO),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.SNIPER, SniperParts.Barrel.SKULLMASHER),

        UniqueGunPart.UniqueCapacitor(Manufacturer.MALIWAN, WeaponClass.PISTOL, Capacitor.DEFILER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.PISTOL, PistolParts.Barrel.HELLSHOCK),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.PISTOL, PistolParts.Barrel.THUNDERBALL_FISTS),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.SMG, SmgParts.Grip.EMPTINESS),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.SMG, SmgParts.Barrel.ENDOTHERMIC_BLASTER),
        UniqueGunPart.UniqueCapacitor(Manufacturer.MALIWAN, WeaponClass.SMG, Capacitor.HELLFIRE),
        UniqueGunPart.UniqueCapacitor(Manufacturer.MALIWAN, WeaponClass.SNIPER, Capacitor.VOLCANO),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.LAUNCHER, LauncherParts.Barrel.PYROPHOBIA),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.TEDIORE, WeaponClass.PISTOL, PistolParts.Barrel.GUN_GUN),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.TORGUE, WeaponClass.PISTOL, PistolParts.Barrel.UNKEMPT_HAROLD),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TORGUE, WeaponClass.SHOTGUN, ShotgunParts.Barrel.FLAKKER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TORGUE, WeaponClass.LAUNCHER, LauncherParts.Barrel.NUKEM),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.VLADOF, WeaponClass.PISTOL, PistolParts.Barrel.INFINITY),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.VLADOF, WeaponClass.PISTOL, PistolParts.Barrel.STINGER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.VLADOF, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.SHREDIFIER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.VLADOF, WeaponClass.SNIPER, SniperParts.Barrel.LYUDMILA),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.VLADOF, WeaponClass.LAUNCHER, LauncherParts.Barrel.MONGOL),
    )

    val epicParts: List<UniqueGunPart> = listOf(
        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.SMG, SmgParts.Barrel.BONE_SHREDDER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.SMG, SmgParts.Barrel.CRIT),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.SMG, SmgParts.Barrel.GOOD_TOUCH),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.SMG, SmgParts.Barrel.BAD_TOUCH),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TEDIORE, WeaponClass.SMG, SmgParts.Barrel.DOUBLE_ANARCHY),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.SMG, SmgParts.Barrel.ERIDIAN),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.SMG, SmgParts.Barrel.ERIDIAN),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.HYPERION, WeaponClass.SMG, SmgParts.Barrel.ERIDIAN),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.SMG, SmgParts.Barrel.ERIDIAN),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TEDIORE, WeaponClass.SMG, SmgParts.Barrel.ERIDIAN),
    )

    val rareParts: List<UniqueGunPart> = listOf(
        UniqueGunPart.UniqueCapacitor(Manufacturer.BANDIT, WeaponClass.PISTOL, Capacitor.TINDERBOX),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.PISTOL, PistolParts.Barrel.GWENS_HEAD),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.PISTOL, PistolParts.Barrel.MAYDAY),
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
        UniqueGunPart.UniqueWeaponPart(Manufacturer.MALIWAN, WeaponClass.PISTOL, PistolParts.Barrel.WANDERLUST),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TORGUE, WeaponClass.PISTOL, PistolParts.Barrel.POCKET_ROCKET),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Grip.CHOPPER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.DAHL, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.SCORPIO),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.DAMNED_COWBOY),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.STINKPOT),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.JAKOBS, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.STOMPER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TORGUE, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.BOOM_PUPPY),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.VLADOF, WeaponClass.ASSAULT_RIFLE, AssaultRifleParts.Barrel.HAIL),

        UniqueGunPart.UniqueWeaponPart(Manufacturer.BANDIT, WeaponClass.SHOTGUN, ShotgunParts.Barrel.BOGANELLA),
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
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TEDIORE, WeaponClass.SMG, SmgParts.Barrel.ANARCHY),

        UniqueGunPart.UniqueCapacitor(Manufacturer.BANDIT, WeaponClass.LAUNCHER, Capacitor.ROASTER_SHOCK),
        UniqueGunPart.UniqueCapacitor(Manufacturer.BANDIT, WeaponClass.LAUNCHER, Capacitor.ROASTER_INCENDIARY),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TORGUE, WeaponClass.LAUNCHER, LauncherParts.Barrel.CREAMER),
        UniqueGunPart.UniqueWeaponPart(Manufacturer.TORGUE, WeaponClass.LAUNCHER, LauncherParts.Barrel.TWELVE_POUNDER),
    )

    /**
     * Get all unique gun parts that match the given parameters.
     *
     * @param rarity
     *          The rarity of the gun part.
     * @param weaponClass
     *          `null` for all classes, or select just parts for a specific weapon class.
     * @param manufacturer
     *          `null` for all manufacturers, or select just parts for a specific manufacturer.
     */
    fun partsFor(
        rarity: Rarity,
        weaponClass: WeaponClass? = null,
        manufacturer: Manufacturer? = null
    ): List<UniqueGunPart> {
        val baseParts = when (rarity) {
            Rarity.PEARLESCENT -> pearlescentParts
            Rarity.LEGENDARY -> legendaryParts
            Rarity.EPIC -> epicParts
            Rarity.RARE -> rareParts
            else -> emptyList()
        }

        if (weaponClass == null && manufacturer == null) return baseParts

        return baseParts.filter {
            (weaponClass == null || it.weaponClass == weaponClass) &&
                    (manufacturer == null || it.manufacturer == manufacturer)
        }
    }
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