package maliwan.mcbl.weapons.gun.behaviour.shotgun

import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import kotlin.math.min
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Butcher : UniqueGun, PostGunShotBehaviour, PostGenerationBehaviour {

    override val baseName = "Butcher"
    override val redText = "Fresh meat!"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.pelletCount = when ((assembly as ShotgunAssembly).accessory) {
            ShotgunParts.Accessory.VERTICAL_GRIP -> 5
            else -> 3
        }
        properties.ammoPerShot = 1
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        if (Random.nextDouble() < 0.5) {
            val addAmmo = Random.nextInt(0, 3)
            execution.clip = min(execution.clip + addAmmo, execution.magazineSize)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(2.0, StatModifier.Property.ACCURACY)
            multiply(2.5, StatModifier.Property.FIRE_RATE)
            add(1, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}