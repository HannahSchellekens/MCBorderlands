package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.elementalLootPool

/**
 * @author Hannah Schellekens
 */
object CapacitorTable {

    /**
     * Equal chance for all elements or NE to show up, explosive is an exception as it is somewhat rarer.
     */
    val elementAll = elementalLootPool(100, 100, 100, 100, 100, 10)

    /**
     * Can only roll elemental capacitors.
     */
    val elementOnly = elementalLootPool(0, 100, 100, 100, 100, 10)

    /**
     * Will roll elemental capacitors most of the time.
     */
    val elementOften = elementalLootPool(10, 100, 100, 100, 100, 1)

    /**
     * Will only roll elemental capacitors sometimes.
     */
    val elementSome = elementalLootPool(100, 10, 10, 10, 10, 1)

    /**
     * Will never roll elemental capacitors, but sometimes explosive.
     */
    val elementNoneButExplosive = elementalLootPool(100, 0, 0, 0, 0, 10)

    /**
     * Will never roll any elemental capacitor.
     */
    val elementNone = elementalLootPool(100, 0, 0, 0, 0, 0)
}