package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.util.*
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostBulletLandBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class Barrage : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour, PostBulletLandBehaviour {

    override val baseName = "Barrage"
    override var redText = "Justice rains from above"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.VLADOF.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.accuracy = Probability.ONE
        properties.directDamage = true
        properties.splashDamage = Damage.ZERO

        properties.fireRate = 0.5
    }

    override fun afterBulletLands(
        handler: WeaponEventHandler,
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    ) {
        val target = hitLocation?.nearbyEntities(meta.splashRadius * 3)
            ?.filterIsInstance<LivingEntity>()
            ?.minByOrNull { it.location.distance(hitLocation) }

        if (meta.isTrackerBullet && target != null) {
            repeat(20) {
                handler.plugin.scheduleTask(it.toLong()) {
                    val childMeta = meta.copy(
                        damage = meta.damage,
                        splashDamage = meta.damage * 10.0,
                        gravity = 0.0,
                        homingStrength = 0.14,
                        homingTargetDistance = 5.0,
                        homingTargetRadius = 24.0,
                        homingTarget = target,
                        bouncesLeft = 2,
                        isTrackerBullet = false,
                        directDamage = false
                    )
                    childMeta.elementalDamage[Elemental.EXPLOSIVE] = childMeta.splashDamage

                    val direction = meta.shooter.eyeLocation.direction
                    val childRocket = bullet.world.spawnArrow(
                        meta.shooter.eyeLocation,
                        direction,
                        1.0f,
                        0f
                    )
                    childRocket.setGravity(false)

                    val newDirection = direction
                        .setX(direction.x.modifyRandom(1.7))
                        .setY(direction.y.modifyRandom(1.7))
                        .setZ(direction.z.modifyRandom(1.7))
                    childRocket.velocity = newDirection.normalize()

                    handler.registerBullet(childRocket, childMeta)
                }
            }
        }
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        val bullet = bullets.firstOrNull() ?: return
        val meta = handler.bullets[bullet] ?: return

        meta.isTrackerBullet = true
        meta.homingStrength = 0.0
        meta.damage *= 0.01
        meta.splashDamage *= 0.01
        meta.directDamage = true
    }

    companion object {

        val statModifiers = statModifierList {
            divide(1.6, StatModifier.Property.MAGAZINE_SIZE)
            multiply(1.1, StatModifier.Property.RELOAD_SPEED)
            add(1, StatModifier.Property.AMMO_PER_SHOT)
        }
    }
}