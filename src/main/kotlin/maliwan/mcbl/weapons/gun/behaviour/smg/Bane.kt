package maliwan.mcbl.weapons.gun.behaviour.smg

import maliwan.mcbl.util.spigot.scheduleTask
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.SmgParts
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextLong

/**
 * @author Hannah Schellekens
 */
open class Bane : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour {

    override val baseName = "Bane"
    override val redText = "in Spain, stays mainly on the plain."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun afterGunShot(handler: WeaponEventHandler, execution: GunExecution, bullets: List<Entity>, player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 80, 3, true, false, false))

        player.playSound(player.location, Sound.ENTITY_HORSE_DEATH, 1f, Random.nextFloat() * 0.85f)

        val amount = Random.nextInt(0..3)
        repeat(amount) {
            val delay = Random.nextLong(2L..12L)
            handler.plugin.scheduleTask(it * delay) {
                player.playSound(player.location, Sound.ENTITY_HORSE_DEATH, 1f, Random.nextFloat() * 0.85f)
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.95, StatModifier.Property.BASE_DAMAGE)
            multiply(1.5, StatModifier.Property.MAGAZINE_SIZE)
            subtract(0.08, StatModifier.Property.ACCURACY)
        }
    }
}