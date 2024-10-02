package maliwan.mcbl.weapons.gun.behaviour.smg

import maliwan.mcbl.util.Probability
import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.SmgParts
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class Emptiness : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour {

    override val baseName = "Emptiness"
    override val redText = "Goin' around like a revolver, it's\nbeen decided how we lose"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Grip.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.extraInfoText += "Bonus damage and elemental chance\nfor the last last bullet in a clip"
        properties.extraInfoText += "Lower magazine size"
    }

    override fun afterGunShot(handler: WeaponEventHandler, execution: GunExecution, bullets: List<Entity>, player: Player) {
        if (execution.clip > 0) return

        bullets.forEach {
            val meta = handler.bullets[it] ?: return@forEach
            meta.damage *= 10.0
            meta.splashRadius *= 1.5

            meta.elements.forEach { elt ->
                meta.elementalProbability[elt] = Probability.ONE
            }

            meta.addElement(Elemental.EXPLOSIVE, Ticks(0), meta.splashDamage, Probability.ONE)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(0.6, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}