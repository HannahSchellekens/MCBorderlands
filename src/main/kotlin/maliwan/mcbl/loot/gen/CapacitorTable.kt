package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.LootPool
import maliwan.mcbl.loot.lootPoolOf
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.gun.Capacitor

/**
 * @author Hannah Schellekens
 */
object CapacitorTable {

    /**
     * Equal chance for all elements or NE to show up, explosive is an exception as it is somewhat rarer.
     */
    val elementAll = capacitorLootPool(100, 100, 100, 100, 100, 10)

    /**
     * Can only roll elemental capacitors.
     */
    val elementOnly = capacitorLootPool(0, 100, 100, 100, 100, 10)

    /**
     * Will roll elemental capacitors most of the time.
     */
    val elementOften = capacitorLootPool(10, 100, 100, 100, 100, 1)

    /**
     * Will only roll elemental capacitors sometimes.
     */
    val elementSome = capacitorLootPool(100, 10, 10, 10, 10, 1)

    /**
     * Will never roll elemental capacitors, but sometimes explosive.
     */
    val elementNoneButExplosive = capacitorLootPool(100, 0, 0, 0, 0, 10)

    /**
     * Will never roll any elemental capacitor.
     */
    val elementNone = capacitorLootPool(100, 0, 0, 0, 0, 0)

    /**
     * Creates a new loot pool for rarities based on the given weights.
     */
    fun capacitorLootPool(
        nonElemental: Int,
        incendiary: Int,
        shock: Int,
        corrosive: Int,
        slag: Int,
        explosive: Int
    ): LootPool<Capacitor> = lootPoolOf(
        Capacitor(Elemental.PHYSICAL) to nonElemental,
        Capacitor(Elemental.INCENDIARY) to incendiary,
        Capacitor(Elemental.SHOCK) to shock,
        Capacitor(Elemental.CORROSIVE) to corrosive,
        Capacitor(Elemental.SLAG) to slag,
        Capacitor(Elemental.EXPLOSIVE) to explosive
    )
}