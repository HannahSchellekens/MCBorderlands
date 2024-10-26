package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.util.modifyRandom
import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.*
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts
import maliwan.mcbl.weapons.gun.pattern.SequentialBulletPattern
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class TheMonarch(scale: Double = 1.5) : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, PostGunShotBehaviour,
    BulletPatternProvider {

    override val baseName = "The Monarch"
    override var redText = "The deadly sting of the monarch!."
    override val bulletPatternProcessor = SequentialBulletPattern.of(
        -0.02 * scale to -0.04 * scale,
        0.02 * scale to -0.04 * scale,
        -0.02 * scale to -0.02 * scale,
        0.01 * scale to -0.02 * scale,
        -0.02 * scale to 0.0 * scale,
        0.02 * scale to 0.0 * scale,
        -0.02 * scale to 0.02 * scale,
        0.01 * scale to 0.02 * scale,
        -0.02 * scale to 0.04 * scale,
        0.02 * scale to 0.04 * scale
    )

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Barrel.VLADOF.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.accuracy = Probability(0.952.modifyRandom(0.03))
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always bladed.
        return assembly.replacePart(AssaultRifleParts.Accessory.BAYONET)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        val crouching = player.isSneaking
        val bulletCount = Random.nextInt(3, 11) * if (crouching) 2 else 1

        repeat(bulletCount) {
            handler.shootGunBullet(player, execution)
        }

        if (crouching) {
            handler.removeAmmo(player, execution)
            player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 30, 3, true, false))
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.3 * 0.25, StatModifier.Property.BASE_DAMAGE)
            add(0.004, StatModifier.Property.RECOIL)
        }
    }
}