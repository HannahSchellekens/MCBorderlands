package maliwan.mcbl.loot

import maliwan.mcbl.weapons.WeaponClasses

/**
 * @author Hannah Schellekens
 */
object WeaponClassTable {

    val generation = lootPoolOf(
        WeaponClasses.PISTOL to 100,
        WeaponClasses.ASSAULT_RIFLE to 80,
        WeaponClasses.SMG to 80,
        WeaponClasses.SHOTGUN to 80,
        WeaponClasses.SNIPER to 55,
        WeaponClasses.LAUNCHER to 20,
    )
}