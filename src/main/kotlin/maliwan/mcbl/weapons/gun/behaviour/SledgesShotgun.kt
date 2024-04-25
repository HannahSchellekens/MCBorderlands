package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.util.scheduleTask
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class SledgesShotgun : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour, PostBulletLandBehaviour {

    override val baseName = "Sledge's Shotgun"
    override var redText = "The Legend Lives"

    val knockbackStrength = 0.25

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        handler.plugin.scheduleTask(5L) {
            handler.shootGun(player, execution, triggerAfterGunShot = false)
        }
    }

    override fun afterBulletLands(bullet: Entity, meta: BulletMeta, targetEntity: LivingEntity?) {
        targetEntity?.let {
            it.velocity = it.velocity.add(bullet.velocity.multiply(knockbackStrength))
        }
    }

    companion object {

        val statModifiers = statModifierList {
            add(2, StatModifier.Property.PELLET_COUNT)
            multiply(0.92, StatModifier.Property.BASE_DAMAGE)
            add(0.002, StatModifier.Property.RECOIL)
        }
    }
}