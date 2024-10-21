package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.util.spigot.scheduleTask
import maliwan.mcbl.util.spigot.showSmokeParticle
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostBulletLandBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import maliwan.mcbl.weapons.splashDamageExplosive
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Tunguska : UniqueGun, PostGenerationBehaviour, PostBulletLandBehaviour, PostGunShotBehaviour {

    override val baseName = "Tunguska"
    override val redText = "It will split the sky in two."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        bullets.forEach {
            val meta = handler.bullets[it] ?: return@forEach
            meta.splashDamage *= 0.5
            meta.damage *= 0.5
        }
    }

    override fun afterBulletLands(
        handler: WeaponEventHandler,
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    ) {
        val baseLocation = hitLocation?.clone() ?: bullet.location

        // Particles every other tick.
        repeat(12) {
            handler.plugin.scheduleTask(it + 1L) {
                val percentage = it / 12.0
                val location = baseLocation.clone().add(0.0, 0.5 + 5.5 * percentage, 0.0)
                location.showSmokeParticle()
            }
        }

        handler.plugin.scheduleTask(12L) {
            val location = baseLocation.clone().add(0.0, 2.0, 0.0)

            val radius = meta.splashRadius * 2.0

            repeat(12) {
                val polar = Random.nextDouble() * Math.PI * 2
                val alpha = Random.nextDouble() * Math.PI * 2

                val x = radius * sin(polar) * cos(alpha)
                val y = radius * sin(polar) * sin(alpha)
                val z = radius * cos(polar)

                val particleLocation = location.clone().add(x, y, z)
                particleLocation.world?.spawnParticle(Particle.EXPLOSION, particleLocation, 1)
            }

            location.world?.createExplosion(location, 0f)
            splashDamageExplosive(
                handler.plugin,
                location,
                meta.copy(damage = meta.damage * 2.0, splashDamage = meta.splashDamage * 2.0, splashRadius = meta.splashRadius * 3.0)
            )
        }
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.036, StatModifier.Property.ACCURACY)
            add(1, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}