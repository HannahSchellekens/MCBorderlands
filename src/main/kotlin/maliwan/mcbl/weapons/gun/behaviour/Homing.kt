package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.Keys
import maliwan.mcbl.entity.headLocation
import maliwan.mcbl.util.setLength
import maliwan.mcbl.util.spigot.simulateBulletArc
import maliwan.mcbl.weapons.BulletMeta
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.persistence.PersistentDataType

/**
 * Updates the velocity of this bullet to home in on a certain target.
 *
 * @param bullet
 *          The bullet that must alter its trajectory.
 * @param meta
 *          Meta information about the bullet/weapon.
 */
fun tickHomingBullet(bullet: Entity, meta: BulletMeta) {
    if (meta.homingStrength <= 0.00001) return

    val bulletDirection = bullet.velocity
    val speed = bulletDirection.length()

    if (meta.homingTarget == null || meta.homingTarget!!.isDead) {
        findHomingTarget(bullet, meta)
    }

    val target = meta.homingTarget ?: return
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

/**
 * Finds a target to home in on for the given bullet and updates the bullet meta accordingly.
 * [BulletMeta.homingTarget] will be the target, or `null` if there is none.
 */
fun findHomingTarget(bullet: Entity, meta: BulletMeta) {
    val bulletDirection = bullet.velocity
    val searchRadius = meta.homingTargetRadius

    // Simulate the bullet traversing its arc and find the target that lies closest to this arc.
    var target: LivingEntity? = null
    var distance: Double = Double.MAX_VALUE
    bullet.location.simulateBulletArc(bulletDirection, 60, meta.gravity) { it, i ->
        // Only look ahead the minimum target distance to pervent selecting targets around/behind the player.
        if (i < meta.homingTargetDistance) return@simulateBulletArc

        val loc = Location(bullet.world, it.x, it.y, it.z)
        val candidateTargets = loc.world?.getNearbyEntities(loc, searchRadius, searchRadius, searchRadius)?.asSequence()
            ?.filterIsInstance(LivingEntity::class.java)
            ?.filter { it != meta.shooter }
            ?.toList()
            ?: emptyList()

        val trackedTargets = candidateTargets.filter {
            val pdc = it.persistentDataContainer
            pdc.getOrDefault(Keys.homingTarget, PersistentDataType.BOOLEAN, false)
        }

        val closest = trackedTargets.ifEmpty { candidateTargets }
            .minByOrNull { it.headLocation.distance(loc) }

        if (closest != null) {
            val dist = closest.location.distance(loc)
            if (dist < distance) {
                distance = dist
                target = closest
            }
        }
    }

    meta.homingTarget = target
}