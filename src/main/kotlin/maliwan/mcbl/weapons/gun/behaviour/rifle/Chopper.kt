package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.util.scheduleTask
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import kotlin.random.Random
import kotlin.random.nextLong

/**
 * @author Hannah Schellekens
 */
open class Chopper : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, PostGunShotBehaviour {

    override val baseName = "CHOPPER"
    override val redText = "Get to it."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        statModifiers.applyAll(properties)

        properties.recoil = 0.9995
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        return assembly.replacePart(AssaultRifleParts.Barrel.VLADOF_MINIGUN)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        val currentGun = player.gunProperties()
        if (currentGun?.assembly?.parts?.any { it == AssaultRifleParts.Grip.CHOPPER } == true) {
            if (execution.clip > 0) {
                handler.plugin.scheduleTask(1L + Random.nextLong(0L..1L)) {
                    handler.shootGun(player, execution)
                }
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(8.9, StatModifier.Property.MAGAZINE_SIZE)
            add(3, StatModifier.Property.PELLET_COUNT)
            add(3, StatModifier.Property.AMMO_PER_SHOT)
            multiply(1.2, StatModifier.Property.FIRE_RATE)
        }
    }
}