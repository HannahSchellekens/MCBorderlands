package maliwan.mcbl.weapons.gun

import maliwan.mcbl.util.modifyRandom
import org.bukkit.Location
import org.bukkit.entity.Arrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.util.Vector

/**
 * Shoots a bullet by `this` living entity `from` a given location with `properties` gun properties in `direction`.
 */
fun LivingEntity.shootBullet(from: Location, direction: Vector, properties: GunProperties): Projectile? {
    val world = from.world ?: return null

    val accuracyModifier = (1.0 - properties.accuracy.chance) * 0.375
    val newDirection = direction
        .setX(direction.x.modifyRandom(accuracyModifier))
        .setY(direction.y.modifyRandom(accuracyModifier))
        .setZ(direction.z.modifyRandom(accuracyModifier))

    val bullet = world.spawnEntity(from, EntityType.ARROW) as Arrow
    bullet.velocity = newDirection.normalize().multiply(properties.bulletSpeed / 20.0 /* blocks per second -> per tick */)
    bullet.setGravity(false)
    return bullet
}