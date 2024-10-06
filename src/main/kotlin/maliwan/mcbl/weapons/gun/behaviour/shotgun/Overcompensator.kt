package maliwan.mcbl.weapons.gun.behaviour.shotgun

import maliwan.mcbl.util.Probability
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.*
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import kotlin.math.min
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Overcompensator : UniqueGun, PostGunShotBehaviour, PostGenerationBehaviour, UpdateAssemblyBehaviour,
    TalkWhenFiring, TalkWhenReloading {

    override val baseName = "Overcompensator"
    override val redText = "You can either surf, or you can fight!"

    override val talkChanceFiring = Probability(0.1)
    override val talkMessagesFiring = listOf(
        "Boom!",
        "Die, brah!",
        "Bang bang, brah!",
        "Get wasted!",
        "(Explosion noise) Awesome."
    )

    override val talkChanceReloading = Probability.ONE
    override val talkMessagesReloading = listOf(
        "Primo ammo, brah!",
        "Lovin' it!",
        "Oh, nice reload, brah!",
        "Yeah, more bullets!",
        "Choice."
    )

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.pelletCount = 3
        properties.ammoPerShot = 1
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        return assembly.replacePart(ShotgunParts.Accessory.SHELL)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        if (Random.nextDouble() < 0.4) {
            val addAmmo = Random.nextInt(0, 3)
            execution.clip = min(execution.clip + addAmmo, execution.magazineSize)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(2.25, StatModifier.Property.ACCURACY)
            multiply(2, StatModifier.Property.FIRE_RATE)
            add(2, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}