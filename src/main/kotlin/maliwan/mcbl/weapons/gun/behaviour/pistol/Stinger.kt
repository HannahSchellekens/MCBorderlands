package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.util.modifyAccuracy
import maliwan.mcbl.util.spigot.spawnBullet
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostBulletBounceBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts
import org.bukkit.entity.Entity
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Stinger : UniqueGun, PostGenerationBehaviour, PostBulletBounceBehaviour {

    override val baseName = "Stinger"
    override val redText = "It's like butter, baby."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.VLADOF.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.bounces = 1
    }

    override fun afterBulletBounce(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        // Give it 20% extra chance to spawn extra bullet on ricochet.
        if (Random.nextDouble() < 0.2) {
            val direction = bullet.velocity
            val child = bullet.world.spawnBullet(bullet.location, direction.clone().modifyAccuracy(0.1), direction.length())
            handler.registerBullet(child, bulletMeta.copy())
        }
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.015, StatModifier.Property.ACCURACY)
        }
    }
}