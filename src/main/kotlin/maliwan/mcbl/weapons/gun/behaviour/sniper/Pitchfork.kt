package maliwan.mcbl.weapons.gun.behaviour.sniper

import maliwan.mcbl.util.Ticks
import maliwan.mcbl.util.VECTOR_UP
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.SmgParts
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class Pitchfork : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour {

    override val baseName = "Pitchfork"
    override var redText = "Mainstream'd!"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.pelletCount = 5
        properties.burstDelay = Ticks(3)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        val base = bullets.first()
        val direction = base.velocity
        val shiftDirection = direction.clone().crossProduct(VECTOR_UP).normalize()

        val first = bullets[1]
        val second = bullets[2]
        val third = bullets[3]
        val fourth = bullets[4]

        first.teleport(first.location.add(shiftDirection.clone().multiply(-0.4)))
        second.teleport(second.location.add(shiftDirection.clone().multiply(-0.2)))
        third.teleport(third.location.add(shiftDirection.clone().multiply(0.2)))
        fourth.teleport(fourth.location.add(shiftDirection.clone().multiply(0.4)))

        first.velocity = direction
        second.velocity = direction
        third.velocity = direction
        fourth.velocity = direction
    }

    companion object {

        val statModifiers = statModifierList {
            add(1, StatModifier.Property.AMMO_PER_SHOT)
            multiply(1.5, StatModifier.Property.MAGAZINE_SIZE)
            add(0.012, StatModifier.Property.ACCURACY)
            multiply(0.8, StatModifier.Property.BASE_DAMAGE)
            subtract(1, StatModifier.Property.BURST_COUNT)
        }
    }
}