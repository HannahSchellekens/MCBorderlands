package maliwan.mcbl.util

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.util.BoundingBox

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