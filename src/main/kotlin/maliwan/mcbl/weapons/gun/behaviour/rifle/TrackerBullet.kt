package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.GunExecution
import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.WeaponAssembly
import maliwan.mcbl.weapons.gun.behaviour.PostBulletLandBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * Guns where the first bullet of the magazine marks an enemy to track.
 *
 * @author Hannah Schellekens
 */
open class TrackerBullet : PostGenerationBehaviour, PostBulletLandBehaviour, PostGunShotBehaviour {

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        properties.extraInfoText += "First bullet per clip marks a target.\nBullets home in on this target."
    }

    override fun afterBulletLands(
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    ) {
        if (meta.isTrackerBullet) {
            targetEntity?.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 160, 0, true, false, false))
        }
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        val bullet = bullets.first()
        val meta = handler.bullets[bullet] ?: return

        if (execution.clip == execution.magazineSize - execution.ammoPerShot) {
            meta.isTrackerBullet = true
            meta.homingStrength = 0.0
        }
        else {
            meta.homingTargetRadius *= 2.5
        }
    }
}