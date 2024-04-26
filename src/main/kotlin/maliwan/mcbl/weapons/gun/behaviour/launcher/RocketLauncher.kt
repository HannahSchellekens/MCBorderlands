package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.util.Chance
import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.gun.GunExecution
import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.WeaponAssembly
import maliwan.mcbl.weapons.gun.behaviour.PostBulletLandBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PreGunShotBehaviour
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class RocketLauncher : PostGenerationBehaviour, PreGunShotBehaviour, PostBulletLandBehaviour {

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        // Transfer 100% weapon damage to explosive damage.
        if (properties.elements.isEmpty()) {
            properties.elements.add(Elemental.EXPLOSIVE)
            properties.elementalChance[Elemental.EXPLOSIVE] = Chance.ONE
            properties.elementalDuration[Elemental.EXPLOSIVE] = Ticks(0)
            properties.elementalDamage[Elemental.EXPLOSIVE] = properties.baseDamage
        }

        // Launchers deal +100% splash damage, and only splash damage.
        properties.splashDamage += properties.baseDamage
    }

    override fun afterBulletLands(bullet: Entity, meta: BulletMeta, targetEntity: LivingEntity?) {
        val particle = if (meta.splashRadius > 2.75) Particle.EXPLOSION_HUGE else Particle.EXPLOSION_LARGE
        bullet.world.spawnParticle(particle, bullet.location, 1)
        bullet.world.playSound(bullet.location, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 12f, 1f)
    }

    override fun beforeGunShot(execution: GunExecution, player: Player) {
        player.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 5.0f, 1.0f)
    }
}