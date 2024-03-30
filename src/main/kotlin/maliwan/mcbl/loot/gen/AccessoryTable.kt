package maliwan.mcbl.loot.gen

import maliwan.mcbl.weapons.Rarity

/**
 * @author Hannah Schellekens
 */
object AccessoryTable {

    /**
     * The chance in [0,1] that a gun with the given rarity has an accessory (0=never, 1=always).
     */
    fun chanceByRarity(rarity: Rarity) = when (rarity) {
        Rarity.COMMON -> 0.01
        Rarity.UNCOMMON -> 0.25
        Rarity.RARE -> 0.5
        Rarity.EPIC -> 0.8
        Rarity.LEGENDARY, Rarity.PEARLESCENT -> 1.0
    }
}