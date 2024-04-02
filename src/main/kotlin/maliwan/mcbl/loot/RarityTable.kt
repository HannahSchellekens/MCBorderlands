package maliwan.mcbl.loot

import maliwan.mcbl.weapons.WeaponClass

/**
 * @author Hannah Schellekens
 */
object RarityTable {

    /**
     * Random world drops and enemy drops.
     *
     * @author Hannah Schellekens
     */
    object WorldDrops {

        val shitty = rarityLootPool(100000, 10000, 1000, 1, 0, 0)
        val regular = rarityLootPool(100000, 10000, 1000, 100, 10, 1)
        val badass = rarityLootPool(25000, 5000, 1000, 200, 20, 3)
        val superBadass = rarityLootPool(6250, 2500, 1000, 400, 40, 9)
        val ultimateBadass = rarityLootPool(625, 500, 500, 400, 80, 27)
        val chubby = rarityLootPool(63, 100, 250, 400, 160, 81)
    }

    /**
     * Random loot in chests.
     *
     * @author Hannah Schellekens
     */
    object Treasure {

        val shitty = rarityLootPool(100000, 27360, 3330, 465, 0, 0)
        val regular = rarityLootPool(25000, 13680, 3330, 930, 9, 1)
        val highQuality = rarityLootPool(2500, 17100, 11100, 9300, 90, 10)
        val stellar = rarityLootPool(250, 1710, 5550, 9300, 900, 200)
    }

    /**
     * Ammo drops.
     *
     * @author Hannah Schellekens
     */
    object Ammo {

        val regular = lootPoolOf(
            WeaponClass.PISTOL to 100,
            WeaponClass.ASSAULT_RIFLE to 100,
            WeaponClass.SMG to 100,
            WeaponClass.SHOTGUN to 100,
            WeaponClass.SNIPER to 60,
            WeaponClass.LAUNCHER to 5,
        )
    }
}