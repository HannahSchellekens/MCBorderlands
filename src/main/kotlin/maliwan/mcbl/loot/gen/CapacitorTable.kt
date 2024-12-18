package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.LootPool
import maliwan.mcbl.loot.lootPoolOf
import maliwan.mcbl.weapons.gun.parts.Capacitor

/**
 * @author Hannah Schellekens
 */
object CapacitorTable {

    /**
     * Equal chance for all elements or NE to show up, explosive is an exception as it is somewhat rarer.
     */
    val elementAll = capacitorLootPool(100, 100, 100, 100, 100, 10, 50)

    /**
     * Can only roll elemental capacitors.
     */
    val elementOnly = capacitorLootPool(0, 100, 100, 100, 100, 10, 50)

    /**
     * Will roll elemental capacitors most of the time.
     */
    val elementOften = capacitorLootPool(10, 100, 100, 100, 100, 1, 50)

    /**
     * Will only roll elemental capacitors sometimes.
     */
    val elementSome = capacitorLootPool(100, 10, 10, 10, 10, 1, 5)

    /**
     * Equal chance for all elements to show up except explosive.
     */
    val elementOnlyNoExplosive = capacitorLootPool(0, 100, 100, 100, 100, 0, 50)

    /**
     * Will never roll elemental capacitors, but sometimes explosive.
     */
    val elementNoneButExplosive = capacitorLootPool(100, 0, 0, 0, 0, 10, 0)

    /**
     * Will never roll any elemental capacitor.
     */
    val elementNone = capacitorLootPool(100, 0, 0, 0, 0, 0, 0)

    /**
     * Creates a new loot pool for rarities based on the given weights.
     */
    fun capacitorLootPool(
        nonElemental: Int,
        incendiary: Int,
        shock: Int,
        corrosive: Int,
        slag: Int,
        explosive: Int,
        cryo: Int,
    ): LootPool<Capacitor> = lootPoolOf(
        Capacitor.NONE to nonElemental,
        Capacitor.INCENDIARY to incendiary,
        Capacitor.SHOCK to shock,
        Capacitor.CORROSIVE to corrosive,
        Capacitor.SLAG to slag,
        Capacitor.EXPLOSIVE to explosive,
        Capacitor.CRYO to cryo
    )
}