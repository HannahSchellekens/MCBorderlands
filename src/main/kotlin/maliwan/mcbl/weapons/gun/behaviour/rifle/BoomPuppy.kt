package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.*
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts
import maliwan.mcbl.weapons.splashDamage
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.util.Vector

/**
 * @author Hannah Schellekens
 */
open class BoomPuppy : UniqueGun, PostGenerationBehaviour, DefaultPrefixProvider, PostBulletBounceBehaviour,
    PostGunShotBehaviour, BulletTypeProvider {

    override val baseName = "Boom Puppy"
    override val redText = "Boom! Boom! Boom! BOOM!\nBOOOM!!!! GOOD DOGGY!!!"
    override val defaultPrefix = "Tiny Tina's"
    override val bulletType = EntityType.SNOWBALL

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.isPiercing = true
        properties.bounces = 6
        properties.splashDamage = properties.baseDamage
        properties.directDamage = false
    }

    override fun afterGunShot(handler: WeaponEventHandler, execution: GunExecution, bullets: List<Entity>, player: Player) {
        bullets.forEach {
            // Make the bullet launch slightly upward to get a better bounce when aiming forward.
            // Otherwise, the bullets will hit the ground too early.
            val velocity = it.velocity
            it.velocity = Vector(velocity.x, velocity.y + 0.15, velocity.z)
        }
    }

    override fun afterBulletBounce(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        splashDamage(handler.plugin, bullet.location, bulletMeta)

        val velocity = bullet.velocity
        bullet.velocity = Vector(velocity.x, velocity.y + 0.1, velocity.z)
    }

    companion object {

        val statModifiers = statModifierList {
            divide(1.2, StatModifier.Property.FIRE_RATE)
            subtract(2, StatModifier.Property.AMMO_PER_SHOT)
            multiply(2.25, StatModifier.Property.SPLASH_RADIUS)
            multiply(0.35, StatModifier.Property.PROJECTILE_SPEED)
            multiply(3.5, StatModifier.Property.GRAVITY)
        }
    }
}