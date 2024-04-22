package maliwan.mcbl.util

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Projectile
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

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

/**
 * Spawns a new gun bullet in the world.
 *
 * @param location
 *          Where the bullet spawns.
 * @param direction
 *          The direction the bullet moves in. Only influences direction, not speed.
 * @param speed
 *          How many blocks/tick the bullet should move.
 * @param type
 *          What type of entity must be used as the projectile type.
 *          Must be a [Projectile].
 */
fun World.spawnBullet(location: Location, direction: Vector, speed: Double, type: EntityType = EntityType.ARROW): Projectile {
    val bullet = spawnEntity(location, type) as? Projectile
        ?: error("Type $type is not a Projectile!")
    bullet.velocity = direction.normalize().multiply(speed)
    bullet.setGravity(false)
    return bullet
}