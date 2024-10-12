package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostBulletLandBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author Hannah Schellekens
 */
open class Nukem : UniqueGun, PostGenerationBehaviour, PostBulletLandBehaviour {

    override val baseName = "Nukem"
    override val redText = "Name Dropper."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun afterBulletLands(
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    ) {
        val location = hitLocation ?: bullet.location

        val radius = meta.splashRadius - 2.8
        val world = bullet.world
        repeat(8) {
            val angle = PI / 4 * it
            val x = radius * cos(angle)
            val z = radius * sin(angle)
            /* TODO: No large explosion in 1.21, how to solve this? */
            world.spawnParticle(Particle.EXPLOSION, Location(world, location.x + x, location.y + 0.5, location.z + z), 1)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.4, StatModifier.Property.BASE_DAMAGE)
            multiply(0.7, StatModifier.Property.FIRE_RATE)
            add(1, StatModifier.Property.AMMO_PER_SHOT)
            multiply(0.8, StatModifier.Property.MAGAZINE_SIZE)
            multiply(4, StatModifier.Property.GRAVITY)
            subtract(10, StatModifier.Property.PROJECTILE_SPEED)
            multiply(2.5, StatModifier.Property.SPLASH_RADIUS)
        }
    }
}