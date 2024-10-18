package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.util.Damage
import maliwan.mcbl.util.scheduleTask
import maliwan.mcbl.util.showElementalParticle
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import maliwan.mcbl.weapons.splashDamage
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Hive : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, PostGunShotBehaviour,
    PostBulletLandBehaviour {

    override val baseName = "Hive"
    override var redText = "Full of bees."

    private var handler: WeaponEventHandler? = null

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.splashDamage = Damage.ZERO
        properties.splashRadius = 0.0
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always corrosive.
        return assembly.replaceCapacitor(Capacitor.CORROSIVE)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        this.handler = handler

        bullets.forEach { bullet ->
            val meta = handler.bullets[bullet] ?: return@forEach
            // Don't do damage on initial projectile.
            // Put it on splashDamage, because the child damage is based on the base damage.
            // Rocket launchers do not do direct damage.
            meta.splashDamage = Damage.ZERO

            handler.scheduleEffect(bullet, 500L) { _, _, _ ->
                spawnHive(bullet)
            }
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
        if (meta.splashDamage.damage <= 0.0001) {
            spawnHive(bullet)
        }
    }

    private fun spawnHive(bullet: Entity) {
        val handler = this.handler ?: return
        val hiveMeta = handler.bullets[bullet] ?: return
        hiveMeta.setLifespan(600L)

        val hiveLocation = bullet.location

        // Remove source location.
        handler.unregisterBullet(bullet)
        bullet.remove()

        // After all children have been spawned, explode one last time.
        handler.plugin.scheduleTask(144L) {
            splashDamage(handler.plugin, hiveLocation, hiveMeta)
            hiveLocation.world?.createExplosion(hiveLocation, 0f)
        }

        repeat(18) { index ->
            // Spawn 18 child bullets with 8 ticks delay. Total
            handler.plugin.scheduleTask(0L + 8L * index) {

                // Children do 20% splash damage and home in on targets.
                val childMeta = hiveMeta.copy(
                    damage = hiveMeta.damage * 0.1,
                    splashDamage = hiveMeta.damage * 0.1,
                    splashRadius = 1.2,
                    gravity = 0.0,
                    homingStrength = 0.01,
                    homingTargetDistance = 5.0,
                    homingTargetRadius = 24.0,
                    bouncesLeft = 4,
                )
                childMeta.setLifespan(6000L + 400L * index)

                // Bee immersion.
                bullet.world.playSound(
                    bullet.location,
                    Sound.ENTITY_BEE_LOOP_AGGRESSIVE,
                    SoundCategory.PLAYERS,
                    1f,
                    1.0f
                )

                // Shoot child in random direction.
                val angle = Random.nextDouble() * Math.PI * 2
                val x = sin(angle)
                val z = cos(angle)
                val y = Random.nextDouble()

                val childRocket = bullet.world.spawnArrow(hiveLocation, Vector(x, y, z), 0.7f, 0f)
                childRocket.setGravity(false)

                handler.addBulletEffect(1L, childRocket, childMeta) { tick ->
                    // Slowly increasing homing strength makes the children zoom around more before hitting
                    // the target.
                    childMeta.homingStrength += 0.003

                    if (tick % 2 == 0L) {
                        // Give particle effects to the hive while it is shooting.
                        hiveLocation.showElementalParticle(
                            Elemental.CORROSIVE.color, Random.nextInt(0, 2), 1f, 0.6
                        )
                    }

                    if (tick % 5 == 0L) {
                        childRocket.world.playSound(
                            childRocket.location,
                            Sound.ENTITY_BEE_LOOP_AGGRESSIVE,
                            SoundCategory.PLAYERS,
                            0.7f,
                            1.0f
                        )
                    }

                    if (tick > 120) {
                        stopEffect()
                    }
                }
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(0.6, StatModifier.Property.FIRE_RATE)
            multiply(1.2, StatModifier.Property.RELOAD_SPEED)
            add(2, StatModifier.Property.MAGAZINE_SIZE)
            add(2, StatModifier.Property.AMMO_PER_SHOT)
            multiply(0.5, StatModifier.Property.PROJECTILE_SPEED)
        }
    }
}