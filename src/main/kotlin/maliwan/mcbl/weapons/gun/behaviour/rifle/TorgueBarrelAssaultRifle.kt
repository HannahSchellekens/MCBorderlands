package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.util.plugin.Ticks
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.GunExecution
import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.WeaponAssembly
import maliwan.mcbl.weapons.gun.behaviour.PostBulletLandBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PreGunShotBehaviour
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class TorgueBarrelAssaultRifle : PostGenerationBehaviour, PostBulletLandBehaviour, PreGunShotBehaviour {

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        // Transfer 100% weapon damage to explosive damage.
        if (properties.elements.isEmpty()) {
            properties.elements.add(Elemental.EXPLOSIVE)
            properties.elementalProbability[Elemental.EXPLOSIVE] = Probability.ONE
            properties.elementalDuration[Elemental.EXPLOSIVE] = Ticks(0)
            properties.elementalDamage[Elemental.EXPLOSIVE] = properties.baseDamage
        }
        properties.splashDamage = properties.baseDamage
    }

    override fun afterBulletLands(
        handler: WeaponEventHandler,
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    ) {
        bullet.world.createExplosion(bullet.location, 0f)
    }

    override fun beforeGunShot(execution: GunExecution, player: Player) {
        player.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 5.0f, 1.0f)
    }
}