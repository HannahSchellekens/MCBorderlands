package maliwan.mcbl.util

import java.util.HashMap

/**
 * Counts how many instances of each element are present in this list.
 *
 * @return Map that maps Element -> Count.
 */
fun <T> Iterable<T>.countMap(): Map<T, Int> {
    val map = HashMap<T, Int>()
    forEach {
        val count = map.getOrDefault(it, 0)
        map[it] = count + 1
    }
    return map
}