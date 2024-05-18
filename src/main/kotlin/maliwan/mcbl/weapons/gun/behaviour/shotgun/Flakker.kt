package maliwan.mcbl.weapons.gun.behaviour.shotgun

import maliwan.mcbl.util.modifyAccuracy
import maliwan.mcbl.util.rotateRelative
import maliwan.mcbl.util.scheduleTask
import maliwan.mcbl.util.spawnBullet
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.BulletEffectBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import maliwan.mcbl.weapons.splashDamage
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Flakker : UniqueGun, PostGenerationBehaviour, BulletEffectBehaviour, PostGunShotBehaviour {

    override val baseName = "Flakker"
    override val redText = "Flak the world."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.extraInfoText += "+100% Weapon Fire Rate"
        properties.extraInfoText += "+${Random.nextInt(301, 360)}% Weapon Damage"
        properties.splashDamage = properties.baseDamage
        properties.elementalDamage[Elemental.EXPLOSIVE] = properties.baseDamage

        val shotgun = properties.assembly as ShotgunAssembly
        properties.pelletCount = when (shotgun.accessory) {
            ShotgunParts.Accessory.VERTICAL_GRIP -> 5
            else -> 3
        }
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        bullets.forEach {
            it.isVisibleByDefault = false
        }
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        handler.scheduleEffect(bullet, 315L) { _, projectile, meta ->
            val direction = projectile.velocity
            val speed = projectile.velocity.length()
            val world = projectile.world
            val childMeta = meta.copy(splashRadius = meta.splashRadius * 0.8)

            repeat(3) {
                val newDirection = direction.rotateRelative(
                    Random.nextDouble() * Math.PI * 2 - Math.PI,
                    Random.nextDouble() * Math.PI * 2 - Math.PI
                )
                val child = world.spawnBullet(projectile.location, newDirection, speed / 8.0)
                child.isVisibleByDefault = false
                handler.registerBullet(child, childMeta)

                // Child explosions
                handler.scheduleEffect(child, 220L + Random.nextLong(0L, 160L)) { _, pellet, pelletMeta ->
                    val location = pellet.location
                    val miniMeta = pelletMeta.copy()
                    repeat(4) {
                        handler.plugin.scheduleTask(it * 2L) {
                            val loc = location.toVector().modifyAccuracy(2.2)
                            splashDamage(handler.plugin, Location(location.world, loc.x, loc.y - 1.2, loc.z), miniMeta)
                        }
                    }
                    handler.unregisterBullet(child)
                    child.remove()
                }
            }

            println("$meta")
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(2.3, StatModifier.Property.BASE_DAMAGE)
            multiply(3, StatModifier.Property.SPLASH_RADIUS)
            divide(2, StatModifier.Property.FIRE_RATE)
            multiply(0.3, StatModifier.Property.PROJECTILE_SPEED)
            multiply(0.4, StatModifier.Property.ACCURACY)
            multiply(1.2, StatModifier.Property.RELOAD_SPEED)
        }
    }
}