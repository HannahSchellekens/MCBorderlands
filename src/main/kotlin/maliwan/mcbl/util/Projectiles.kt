package maliwan.mcbl.util

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Projectile
import org.bukkit.util.BoundingBox

/**
 * Calculates the actual position a projectile hit the `target` entity.
 * Will look `traceRange` blocks ahead to find one.
 *
 * @return The location where the projectile hit the entity, or `null` when no such location
 * could be found.
 */
fun Projectile.determineHitLocation(target: Entity, traceRange: Double = 5.0): Location? {
    // Ray trace to the target to check where it hit.
    val result = target.boundingBox.rayTrace(location.toVector(), velocity, traceRange)
    val hitPos = result?.hitPosition ?: return null
    return Location(world, hitPos.x, hitPos.y, hitPos.z)
}

/**
 * Calculates the actual position a projectile hit the `target`.
 * Will look `traceRange` blocks ahead to find one.
 *
 * @return The location where the projectile hit the entity, or `null` when no such location
 * could be found.
 */
fun Projectile.determineHitLocation(target: BoundingBox, traceRange: Double = 5.0): Location? {
    // Ray trace to the target to check where it hit.
    val result = target.rayTrace(location.toVector(), velocity, traceRange)
    val hitPos = result?.hitPosition ?: return null
    return Location(world, hitPos.x, hitPos.y, hitPos.z)
}