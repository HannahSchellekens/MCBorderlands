package maliwan.mcbl.loot

import java.util.Random

/**
 * @author Hannah Schellekens
 */
open class LootPool<Result>(

    /**
     * Maps each result type to the weight in the loot table.
     */
    weightTable: Map<Result, Int>
) {

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

    init {
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
     * Generate a random element from this loot pool.
     *
     * @param random
     *          The random object to use for generating a random item.
     *          Defaults to a generic default shared random object of [LootPool].
     */
    fun roll(random: Random = defaultRandom): Result {
        val roll = random.nextDouble()

        return intervals.find { (interval, result) ->
            roll < interval
        }?.second ?: intervals.last().second
    }

    companion object {

        private val defaultRandom = Random()
    }
}

/**
 * Creates a new LootPool based on the weights defined by this map.
 */
fun <Result> Map<Result, Int>.toLootPool() = LootPool(this)