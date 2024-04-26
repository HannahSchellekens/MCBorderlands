package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.util.scheduleTask
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Scorpio : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour {

    override val baseName = "Scorpio"
    override val redText = "Say not in grief: \"He is no more.\" but\nlive in thankfulness that he was."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        if (Random.nextDouble() < 0.15 && execution.clip > 0) {
            handler.plugin.scheduleTask(2L) {
                handler.shootGun(player, execution)
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.02, StatModifier.Property.BASE_DAMAGE)
            add(1, StatModifier.Property.BURST_COUNT)
            add(0.002, StatModifier.Property.RECOIL)
            multiply(1.25, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}