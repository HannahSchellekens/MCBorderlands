package maliwan.mcbl.loot

import maliwan.mcbl.weapons.Manufacturers

/**
 * @author Hannah Schellekens
 */
object ManufacturerTable {

    /**
     * Table for generating weapons.
     *
     * @author Hannah Schellekens
     */
    object Weapons {

        val generation = lootPoolOf(
            Manufacturers.MALIWAN to 12,
            Manufacturers.JAKOBS to 12,
            Manufacturers.TEDIORE to 12,
            Manufacturers.DAHL to 12,
            Manufacturers.VLADOF to 12,
            Manufacturers.TORGUE to 12,
            Manufacturers.HYPERION to 12,
            Manufacturers.BANDIT to 12,
        )
    }
}