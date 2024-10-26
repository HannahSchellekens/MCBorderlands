package maliwan.mcbl.util.spigot

import maliwan.mcbl.util.modifyRandom
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.util.BoundingBox
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

fun Location.nearbyEntities(radius: Double): Collection<Entity> {
    return world?.getNearbyEntities(this.clone().add(0.0, 0.75, 0.0), radius, radius, radius) ?: emptyList()
}

/**
 * Executes for each block in a radius of `radius` around this location in order: x, z, y.
 */
inline fun Location.forEachBlockAround(radius: Int, crossinline blockAction: (block: Block) -> Unit) {
    for (x in (blockX - radius)..(blockX + radius)) {
        for (z in (blockZ - radius)..(blockZ + radius)) {
            for (y in (blockY - radius)..(blockY + radius)) {
                world?.getBlockAt(x, y, z)?.let {
                    blockAction(it)
                }
            }
        }
    }
}

/**
 * Creates a new location object that randomises the x, y and z location with a deviation of at most `maxModifier`.
 */
fun Location.modifyRandom(maxModifier: Double) = Location(
    world,
    x.modifyRandom(maxModifier),
    y.modifyRandom(maxModifier),
    z.modifyRandom(maxModifier)
)

/**
 * Generates a random location in a horizontal circle around this location.
 *
 * @param maxRadius The maximum distance from the source location the new location can generate.
 */
fun Location.modifyRandomCircle(maxRadius: Double): Location {
    val radius = Random.nextDouble() * maxRadius
    val angle = Random.nextDouble() * Math.PI * 2

    val newX = x + radius * cos(angle)
    val newZ = z + radius * sin(angle)

    return Location(world, newX, y, newZ)
}

/**
 * Iterate over all block locations in this bounding box in order: x, z, y.
 */
inline fun BoundingBox.forEachBlockCoord(blockAction: (x: Int, y: Int, z: Int) -> Unit) {
    for (x in minX.toInt()..maxX.toInt()) {
        for (z in minZ.toInt()..maxZ.toInt()) {
            for (y in minY.toInt()..maxY.toInt()) {
                blockAction(x, y, z)
            }
        }
    }
}

/**
 * Iterate over all blocks in this bounding box in order: x, z, y
 */
inline fun BoundingBox.forEachBlock(world: World, crossinline blockAction: (block: Block) -> Unit) {
    forEachBlockCoord { x, y, z ->
        val block = world.getBlockAt(x, y, z)
        blockAction(block)
    }
}

/**
 * Checks whether this location is inside a dungeon.
 */
fun Location.isInDungeon(): Boolean {
    // First check dungeons. They do not get detected by getStructures.
    var foundSpawner = false
    var foundMossy = false
    forEachBlockAround(3) {
        if (it.type == Material.MOSSY_COBBLESTONE) {
            foundMossy = true
        }
        if (it.type == Material.SPAWNER) {
            foundSpawner = true
        }
    }
    return foundSpawner && foundMossy
}