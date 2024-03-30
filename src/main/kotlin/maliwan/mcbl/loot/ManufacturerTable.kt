package maliwan.mcbl.loot

import maliwan.mcbl.weapons.Manufacturer

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
            Manufacturer.MALIWAN to 12,
            Manufacturer.JAKOBS to 12,
            Manufacturer.TEDIORE to 12,
            Manufacturer.DAHL to 12,
            Manufacturer.VLADOF to 12,
            Manufacturer.TORGUE to 12,
            Manufacturer.HYPERION to 12,
            Manufacturer.BANDIT to 12,
        )
    }
}