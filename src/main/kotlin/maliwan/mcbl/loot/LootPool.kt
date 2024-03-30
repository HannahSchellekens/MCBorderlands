package maliwan.mcbl.loot

import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.Rarity
import java.util.*

/**
 * @author Hannah Schellekens
 */
open class LootPool<Result> {

    /**
     * In increasing order contains the end of the ranges that determine which parts
     * of interval (0,1) is designated to each loot drop.
     *
     * For example (A:20%, B:70%, C:10%) results in the following list:
     * - 0.2 -> A
     * - 0.9 -> B
     * - 1.0 -> C
     */
    private val intervals: List<Pair<Double, Result>>

    private constructor(intervals: List<Pair<Double, Result>>) {
        this.intervals = intervals
    }

    /**
     * Maps each result type to the weight in the loot table.
     */
    constructor(weightTable: Map<Result, Int>) {
        val totalWeight = weightTable.values.sum().toDouble()
        var cumulativeChance = 0.0
        intervals = ArrayList<Pair<Double, Result>>(weightTable.size).apply {
            weightTable.forEach { (result, weight) ->
                val chance = weight / totalWeight
                add((cumulativeChance + chance) to result)
                cumulativeChance += chance
            }
        }
    }

    /**
     * Each item in the list has the same probability.
     */
    constructor(uniformList: Collection<Result>) {
        val chancePerItem = 1.0 / uniformList.size
        intervals = uniformList.mapIndexed { index, result ->
            Pair((index + 1) * chancePerItem, result)
        }
    }

    /**
     * Generate a random element from this loot pool.
     *
     * @param random
     *          The random object to use for generating a random item.
     *          Defaults to a generic default shared random object of [LootPool].
     */
    fun roll(random: Random = defaultRandom): Result {
        val roll = random.nextDouble()

        return intervals.find { (interval, _) ->
            roll < interval
        }?.second ?: intervals.last().second
    }

    /**
     * Creates a new LootPool where certain results are removed from.
     */
    fun removeResults(toRemove: Set<Result>, precision: Int = 1_000_000): LootPool<Result> {
        if (toRemove.isEmpty()) return this

        // Total share of probability to remove.
        var totalToRemove = 0.0

        // Loop over all intervals and compare them to the previous entry
        // sum the parts that need to be removed.
        // Need to store each individual value to later use it to calculate new weights.
        val individualValues = HashMap<Result, Double>()
        var lastValue = 0.0

        intervals.forEach { (rangeEnd, result) ->
            val individualValue = (rangeEnd - lastValue)

            if (result in toRemove) {
                totalToRemove += individualValue
            }
            else individualValues[result] = individualValue

            lastValue = rangeEnd
        }

        // Calculate new weights and build new loot pool:
        val totalLeft = 1.0 - totalToRemove
        return individualValues
            .map { (result, value) -> result to (totalToRemove * (value / totalLeft) * precision).toInt() }
            .toMap()
            .toLootPool()
    }

    /**
     * Creates a new Loot Pool where only certain results are kept, the rest is removed.
     */
    fun retainResults(toRetain: Set<Result>, precision: Int = 1_000_000): LootPool<Result> {
        val difference = intervals.map { (_, result) -> result } subtract toRetain
        return removeResults(difference, precision)
    }

    /**
     * Creates a new LootPool where certain results are removed from.
     */
    fun removeResults(toRemove: Iterable<Result>, precision: Int = 1_000_000) = removeResults(toRemove.toSet(), precision)

    /**
     * Creates a new LootPool where certain results are removed from.
     */
    fun removeResults(vararg toRemove: Result, precision: Int = 1_000_000) = removeResults(toRemove.toSet(), precision)

    companion object {

        private val defaultRandom = Random()
    }
}

/**
 * Creates a new loot pool based on given weights as pairs Pair(result, int).
 */
fun <Result> lootPoolOf(vararg weightList: Pair<Result, Int>) = mapOf(*weightList).toLootPool()

/**
 * Creates a new loot pool for rarities based on the given weights.
 */
fun rarityLootPool(
    commonWeight: Int,
    uncommonWeight: Int,
    rareWeight: Int,
    epicWeight: Int,
    legendaryWeight: Int,
    pearlescentWeight: Int
): LootPool<Rarity> = lootPoolOf(
    Rarity.COMMON to commonWeight,
    Rarity.UNCOMMON to uncommonWeight,
    Rarity.RARE to rareWeight,
    Rarity.EPIC to epicWeight,
    Rarity.LEGENDARY to legendaryWeight,
    Rarity.PEARLESCENT to pearlescentWeight
)

/**
 * Creates a new loot pool for rarities based on the given weights.
 */
fun elementalLootPool(
    nonElemental: Int,
    incendiary: Int,
    shock: Int,
    corrosive: Int,
    slag: Int,
    explosive: Int
): LootPool<Elemental> = lootPoolOf(
    Elemental.PHYSICAL to nonElemental,
    Elemental.INCENDIARY to incendiary,
    Elemental.SHOCK to shock,
    Elemental.CORROSIVE to corrosive,
    Elemental.SLAG to slag,
    Elemental.EXPLOSIVE to explosive
)

/**
 * Creates a new LootPool based on the weights defined by this map.
 */
fun <Result> Map<Result, Int>.toLootPool() = LootPool(this)

/**
 * Creates a new LootPool where each item in this collection has equal weight.
 */
fun <Result> Collection<Result>.toUniformLootPool() = LootPool(this)