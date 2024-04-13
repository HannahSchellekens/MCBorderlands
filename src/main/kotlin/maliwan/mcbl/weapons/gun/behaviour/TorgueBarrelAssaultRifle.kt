package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.util.Chance
import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.gun.GunExecution
import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.WeaponAssembly
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class TorgueBarrelAssaultRifle : PostGenerationBehaviour, PostBulletLandBehaviour, PreGunShotBehaviour {

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        // Transfer 100% weapon damage to explosive damage.
        if (properties.elements.isEmpty()) {
            properties.elements.add(Elemental.EXPLOSIVE)
            properties.elementalChance[Elemental.EXPLOSIVE] = Chance.ONE
            properties.elementalDuration[Elemental.EXPLOSIVE] = Ticks(0)
            properties.elementalDamage[Elemental.EXPLOSIVE] = properties.baseDamage
        }
        properties.splashDamage = properties.baseDamage
    }

    override fun afterBulletLands(bullet: Entity, meta: BulletMeta) {
        bullet.world.createExplosion(bullet.location, 0f)
    }

    override fun beforeGunShot(execution: GunExecution, player: Player) {
        player.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.PLAYERS, 5.0f, 1.0f)
    }
}