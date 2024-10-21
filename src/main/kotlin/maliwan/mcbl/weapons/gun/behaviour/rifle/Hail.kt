package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.loot.gen.CapacitorTable
import maliwan.mcbl.util.spigot.spawnBullet
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.*
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts
import maliwan.mcbl.weapons.splashDamage
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector

/**
 * @author Hannah Schellekens
 */
open class Hail : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, DefaultPrefixProvider,
    PostGunShotBehaviour, BulletEffectBehaviour {

    override val baseName = "Hail"
    override val redText = "What play thing can you offer me\ntoday?"
    override val defaultPrefix = "Miss Moxxi's"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.splashDamage = properties.baseDamage * 0.8
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Only elemental.
        val capacitor = CapacitorTable.elementOnly.roll()
        return assembly.replaceCapacitor(capacitor)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        bullets.forEach {
            val velocity = it.velocity
            val speed = velocity.length()
            it.velocity = Vector(velocity.x, velocity.y + 1.0, velocity.z).normalize().multiply(speed)
        }
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        handler.scheduleEffect(bullet, 360L) { _, projectile, meta ->
            val direction = projectile.velocity
            val speed = direction.length()
            val world = projectile.world
            val childMeta = BulletMeta(
                meta.shooter,
                meta.assembly,
                meta.damage,
                gravity = meta.gravity,
                splashRadius = meta.splashRadius,
                splashDamage = meta.damage,
                transfusion = meta.transfusion
            )

            splashDamage(handler.plugin, projectile.location, childMeta)

            val newDirection = Vector(direction.x, direction.y / 1.8, direction.z)
            val child = world.spawnBullet(projectile.location, newDirection, speed, type = bullet.type)
            handler.registerBullet(child, childMeta)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.3, StatModifier.Property.BASE_DAMAGE)
            add(0.03, StatModifier.Property.TRANSFUSION)
            add(1.5, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            multiply(1.2, StatModifier.Property.MAGAZINE_SIZE)
            add(0.04, StatModifier.Property.ACCURACY)
            multiply(0.5, StatModifier.Property.PROJECTILE_SPEED)
            multiply(4, StatModifier.Property.GRAVITY)
        }
    }
}