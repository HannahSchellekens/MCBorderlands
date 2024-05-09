package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Infinity : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour {

    override val baseName = "Infinity"
    override val redText = "It's closer than you think!\n(no it isn't)"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.VLADOF.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.magazineSize = 1
        properties.ammoPerShot = 0
        properties.recoil = 1.0
        properties.pelletCount = 1
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        // Dva prefix gives 25% chance to fire twice.
        val assembly = execution.assembly as? PistolAssembly
        if (assembly?.accessory == PistolParts.Accessory.DOUBLE && Random.nextDouble() < 0.25) {
            handler.shootGun(player, execution, triggerAfterGunShot = false)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            divide(1.25, StatModifier.Property.FIRE_RATE)
            add(0.015, StatModifier.Property.ACCURACY)
        }
    }
}