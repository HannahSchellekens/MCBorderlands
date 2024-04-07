package maliwan.mcbl.loot

import maliwan.mcbl.weapons.WeaponClass

/**
 * @author Hannah Schellekens
 */
object WeaponClassTable {

    val generation = lootPoolOf(
        WeaponClass.PISTOL to 100,
        WeaponClass.ASSAULT_RIFLE to 80,
        WeaponClass.SMG to 80,
        WeaponClass.SHOTGUN to 80,
        WeaponClass.SNIPER to 55,
//        WeaponClass.LAUNCHER to 20,
    )
}