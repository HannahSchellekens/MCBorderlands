package maliwan.mcbl.weapons.gun.behaviour.sniper

import maliwan.mcbl.entity.checkOnGround
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
open class LongestYard : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour {

    override val baseName = "Longest Yard"
    override val redText = "Impressive."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.HYPERION.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.accuracy = Probability.ONE
        properties.magazineSize = 10
        properties.fireRate = 0.8

        properties.extraInfoText += "Deals 50% bonus damage when airborne"
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        if (player.checkOnGround().not()) {
            bullets.forEach {
                val meta = handler.bullets[it] ?: return@forEach
                meta.damage *= 1.5
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.2, StatModifier.Property.BASE_DAMAGE)
            add(1, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
        }
    }
}