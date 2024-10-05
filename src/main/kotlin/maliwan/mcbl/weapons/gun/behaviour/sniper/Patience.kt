package maliwan.mcbl.weapons.gun.behaviour.sniper

import maliwan.mcbl.util.Probability
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.SniperParts
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class Patience : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour {

    override val baseName = "Patience"
    override val redText = "Most people overestimate what they can\ndo in one year, and underestimate what\nthey can do in ten years."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.HYPERION.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.gravity = 0.0
        properties.fireRate = 0.25
        properties.bulletSpeed = 3.0
        properties.accuracy = Probability.ONE

        properties.extraInfoText += "+1000% damage"
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        bullets.forEach {
            val meta = handler.bullets[it]
            // Lengten the time the arrows are alive. They travel slowly.
            meta?.deathTimestamp = System.currentTimeMillis() + 60L * 1000L
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(11, StatModifier.Property.BASE_DAMAGE)
        }
    }
}