package maliwan.mcbl.weapons.gun.behaviour.shotgun

import maliwan.mcbl.util.scheduleTask
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostBulletLandBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import org.bukkit.Location
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

    override fun afterBulletLands(
        handler: WeaponEventHandler,
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    ) {
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