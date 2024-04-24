package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class Dog : UniqueGun, PostGenerationBehaviour, DefaultPrefixProvider, PostGunShotBehaviour {

    override val baseName = "Dog"
    override val redText = "Because one barrel ain't enough,\nand two is too few."
    override val defaultPrefix = "Thre"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.BANDIT.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        player.world.playSound(player.location, sounds.random(), 0.4f, 1f)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.035, StatModifier.Property.BASE_DAMAGE)
            multiply(1.4, StatModifier.Property.FIRE_RATE)
            multiply(1.25, StatModifier.Property.MAGAZINE_SIZE)
        }

        val sounds = listOf(
            Sound.ENTITY_WOLF_GROWL,
            Sound.ENTITY_WOLF_HOWL,
            Sound.ENTITY_WOLF_PANT,
            Sound.ENTITY_WOLF_WHINE,
            Sound.ENTITY_WOLF_AMBIENT,
            Sound.ENTITY_WOLF_AMBIENT,
            Sound.ENTITY_WOLF_AMBIENT,
        )
    }
}