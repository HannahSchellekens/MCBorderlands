package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.util.modifyRandom
import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.util.rotateRelative
import maliwan.mcbl.util.spigot.spawnBullet
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.BulletEffectBehaviour
import maliwan.mcbl.weapons.gun.behaviour.BulletPatternProvider
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts
import maliwan.mcbl.weapons.gun.pattern.SequentialBulletPattern
import org.bukkit.entity.Entity
import kotlin.math.min

/**
 * @author Hannah Schellekens
 */
open class UnkemptHarold : UniqueGun, PostGenerationBehaviour, BulletEffectBehaviour, BulletPatternProvider {

    override val baseName = "Unkempt Harold"
    override val redText = "Did I fire six shots, or only five?\nThree? Seven. Whatever."

    override val bulletPatternProcessor = SequentialBulletPattern.of(
        0.0 to -0.04,
        0.0 to 0.0,
        0.0 to 0.04
    )

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.splashDamage = properties.baseDamage
        properties.gravity = 0.0
        properties.accuracy = Probability(min(1.0, 0.98.modifyRandom(0.05)))
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        val childMeta = bulletMeta.copy(damage = bulletMeta.damage * 0.7)

        handler.scheduleEffect(bullet, 250L) { _, projectile, _ ->
            val direction = projectile.velocity
            val left = direction.rotateRelative(0.0, -0.015)
            val right = direction.rotateRelative(0.0, 0.015)

            val leftChild = projectile.world.spawnBullet(projectile.location, left, direction.length())
            val rightChild = projectile.world.spawnBullet(projectile.location, right, direction.length())

            handler.registerBullet(leftChild, childMeta)
            handler.registerBullet(rightChild, childMeta)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            add(2, StatModifier.Property.PELLET_COUNT)
            add(2, StatModifier.Property.AMMO_PER_SHOT)
            add(1, StatModifier.Property.MAGAZINE_SIZE)
            subtract(25, StatModifier.Property.PROJECTILE_SPEED)
        }
    }
}