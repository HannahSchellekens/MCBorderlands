package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.entity.headLocation
import maliwan.mcbl.util.setLength
import maliwan.mcbl.weapons.BulletMeta
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * Updates the velocity of this bullet to home in on a certain target.
 *
 * @param bullet
 *          The bullet that must alter its trajectory.
 * @param meta
 *          Meta information about the bullet/weapon.
 * @param delta
 *          The tick interval in seconds (by defautlt 1/20 of a second).
 */
fun Entity.tickHomingBullet(bullet: Entity, meta: BulletMeta, delta: Double = 0.05) {
    if (meta.homingStrength <= 0.00001) return

    val bulletDirection = bullet.velocity
    val speed = bulletDirection.length()

    val searchRadius = meta.homingTargetRadius
    val targetLocation = location.add(bulletDirection.clone().multiply(speed * meta.homingTargetDistance * delta))
    val targetCandidates = world.getNearbyEntities(targetLocation, searchRadius, searchRadius, searchRadius)
    val target = targetCandidates.asSequence()
        .filterIsInstance(LivingEntity::class.java)
        .filter { it != meta.shooter }
        .minByOrNull { it.headLocation.distance(targetLocation) } ?: return

    val targetDirection = target.headLocation.toVector().subtract(bullet.location.toVector())

    // Instantly change to the desired course if strength is 100%, no need to make further calculations.
    if (meta.homingStrength >= 0.999999) {
        bullet.velocity = targetDirection.setLength(speed)
        return
    }

    val totalAngle = bulletDirection.angle(targetDirection)
    val toRotate = totalAngle * meta.homingStrength
    val rotationAxis = bulletDirection.clone().crossProduct(targetDirection)
    val newDirection = bulletDirection.rotateAroundNonUnitAxis(rotationAxis, toRotate)

    bullet.velocity = newDirection.setLength(speed)
}