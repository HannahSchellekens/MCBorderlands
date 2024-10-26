package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.BulletEffectBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostBulletBounceBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts
import maliwan.mcbl.weapons.splashDamage
import org.bukkit.entity.Entity

/**
 * @author Hannah Schellekens
 */
open class Wanderlust : UniqueGun, PostGenerationBehaviour, BulletEffectBehaviour, PostBulletBounceBehaviour {

    override val baseName = "Wanderlust"
    override val redText = "You never know until you go."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.accuracy = Probability(0.01)
        properties.homingTargetDistance = 5.0
        properties.homingTargetRadius = 50.0
        properties.bounces = 2
        properties.splashDamage = properties.baseDamage
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        handler.scheduleEffect(bullet, 500L) { _, _, meta ->
            meta.homingStrength = 0.03
        }
    }

    override fun afterBulletBounce(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        splashDamage(handler.plugin, bullet.location, bulletMeta)
        bulletMeta.homingStrength += 0.03
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.3, StatModifier.Property.MAGAZINE_SIZE)
            divide(2.5, StatModifier.Property.PROJECTILE_SPEED)
            multiply(1.2, StatModifier.Property.ELEMENTAL_CHANCE)
            multiply(1.3, StatModifier.Property.BASE_DAMAGE)
        }
    }
}