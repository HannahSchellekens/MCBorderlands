package maliwan.mcbl.loot.gen

import maliwan.mcbl.weapons.Rarities
import maliwan.mcbl.weapons.Rarity

/**
 * @author Hannah Schellekens
 */
object AccessoryTable {

    /**
     * The chance in [0,1] that a gun with the given rarity has an accessory (0=never, 1=always).
     */
    fun chanceByRarity(rarity: Rarity) = when (rarity) {
        Rarities.COMMON -> 0.01
        Rarities.UNCOMMON -> 0.25
        Rarities.RARE -> 0.5
        Rarities.EPIC -> 0.8
        Rarities.LEGENDARY, Rarities.PEARLESCENT -> 1.0
        else -> 0.0
    }
}