package maliwan.mcbl.weapons.gun.behaviour.shotgun

import maliwan.mcbl.util.VECTOR_UP
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class Blockhead : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, PostGunShotBehaviour {

    override val baseName = "Blockhead"
    override val redText = "Also try Borderlands!"

    val bulletSpacing = 0.41

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.TEDIORE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.pelletCount = 9
        properties.splashDamage = properties.baseDamage
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always incendiary.
        return assembly.replaceCapacitor(Capacitor.INCENDIARY)
    }

    override fun afterGunShot(handler: WeaponEventHandler, execution: GunExecution, bullets: List<Entity>, player: Player) {
        val baseDirection = player.eyeLocation.direction.normalize()
        val speed = bullets.first().velocity.length()
        val newVelocity = baseDirection.clone().multiply(speed)
        val baseLocation = bullets.first().location

        val leftRight = baseDirection.clone().crossProduct(VECTOR_UP)
        val upDown = baseDirection.clone().crossProduct(leftRight)

        bullets.getOrNull(0)?.let {
            it.teleport(baseLocation)
            it.velocity = newVelocity.clone()
        }
        bullets.getOrNull(1)?.let {
            it.teleport(baseLocation.clone().add(leftRight.clone().normalize().multiply(bulletSpacing)))
            it.velocity = newVelocity.clone()
        }
        bullets.getOrNull(2)?.let {
            it.teleport(baseLocation.clone().add(leftRight.clone().normalize().multiply(-bulletSpacing)))
            it.velocity = newVelocity.clone()
        }
        bullets.getOrNull(3)?.let {
            it.teleport(baseLocation.clone().add(upDown.clone().normalize().multiply(bulletSpacing)))
            it.velocity = newVelocity.clone()
        }
        bullets.getOrNull(4)?.let {
            it.teleport(baseLocation.clone().add(upDown.clone().normalize().multiply(-bulletSpacing)))
            it.velocity = newVelocity.clone()
        }
        bullets.getOrNull(5)?.let {
            it.teleport(
                baseLocation.clone()
                    .add(leftRight.clone().normalize().multiply(bulletSpacing))
                    .add(upDown.clone().normalize().multiply(bulletSpacing))
            )
            it.velocity = newVelocity.clone()
        }
        bullets.getOrNull(6)?.let {
            it.teleport(
                baseLocation.clone()
                    .add(leftRight.clone().normalize().multiply(-bulletSpacing))
                    .add(upDown.clone().normalize().multiply(bulletSpacing))
            )
            it.velocity = newVelocity.clone()
        }
        bullets.getOrNull(7)?.let {
            it.teleport(
                baseLocation.clone()
                    .add(leftRight.clone().normalize().multiply(bulletSpacing))
                    .add(upDown.clone().normalize().multiply(-bulletSpacing))
            )
            it.velocity = newVelocity.clone()
        }
        bullets.getOrNull(8)?.let {
            it.teleport(
                baseLocation.clone()
                    .add(leftRight.clone().normalize().multiply(-bulletSpacing))
                    .add(upDown.clone().normalize().multiply(-bulletSpacing))
            )
            it.velocity = newVelocity.clone()
        }
    }

    companion object {

        val statModifiers = statModifierList {
            add(2, StatModifier.Property.BOUNCE_COUNT)
            multiply(0.4, StatModifier.Property.PROJECTILE_SPEED)
            multiply(0.4, StatModifier.Property.GRAVITY)
        }
    }
}