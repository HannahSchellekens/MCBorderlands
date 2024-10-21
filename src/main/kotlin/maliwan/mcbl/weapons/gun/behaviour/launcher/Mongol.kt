package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.util.VECTOR_UP
import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.util.plugin.Ticks
import maliwan.mcbl.util.spigot.spawnBullet
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.BulletEffectBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import org.bukkit.entity.Entity
import kotlin.math.PI
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Mongol : UniqueGun, PostGenerationBehaviour, BulletEffectBehaviour {

    override val baseName = "Mongol"
    override var redText = "The Horde will always return!"

    val interval = 1000L / 20L
    val spawnProbability = Probability(0.4)

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.VLADOF.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.extraInfoText += "(Just kidding, no it doesn't.)"
        properties.gravity = 0.0
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        val childMeta = bulletMeta.copy(
            gravity = 0.025,
            damage = bulletMeta.damage * 0.6,
            splashRadius = bulletMeta.splashRadius * 1.4,
            elementalDuration = mutableMapOf(Elemental.EXPLOSIVE to Ticks(0)),
            elementalDamage = mutableMapOf(Elemental.EXPLOSIVE to bulletMeta.damage * 0.6),
            elementalProbability = mutableMapOf(Elemental.EXPLOSIVE to Probability(0.0)),
            elements = mutableListOf(Elemental.EXPLOSIVE),
        )

        repeat(3000 / interval.toInt()) {
            if (spawnProbability.roll().not()) return@repeat

            handler.scheduleEffect(bullet, interval * it) { _, projectile, _ ->
                val mainDirection = projectile.velocity.normalize()
                val rotationAxis = mainDirection.clone().crossProduct(mainDirection.clone().crossProduct(VECTOR_UP))
                val randomAngle = Random.nextDouble() * 2 * PI
                val finalRotationAxis = rotationAxis.rotateAroundNonUnitAxis(mainDirection, randomAngle)
                val finalDirection = mainDirection.clone().rotateAroundNonUnitAxis(finalRotationAxis, PI / 20)

                val child = bullet.world.spawnBullet(projectile.location, finalDirection, projectile.velocity.length())
                handler.registerBullet(child, childMeta)
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.06, StatModifier.Property.BASE_DAMAGE)
            divide(2, StatModifier.Property.FIRE_RATE)
            subtract(1, StatModifier.Property.MAGAZINE_SIZE)
            subtract(15, StatModifier.Property.PROJECTILE_SPEED)
            add(1, StatModifier.Property.AMMO_PER_SHOT)
        }
    }
}