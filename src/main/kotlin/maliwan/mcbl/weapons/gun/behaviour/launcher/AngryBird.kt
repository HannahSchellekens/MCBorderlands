package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.entity.checkOnGround
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import maliwan.mcbl.weapons.splashDamageExplosive
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Chicken
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * @author Hannah Schellekens
 */
open class AngryBird : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour {

    override val baseName = "ANGRY BIRD"
    override var redText = "You can put wings on a pig,\nbut you don't make it an eagle."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.splashDamage *= 0.6
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        bullets.forEach {
            val meta = handler.bullets[it] ?: return@forEach

            // Chicks fall a little slower, so dont despawn them too early.
            meta.setLifespan(15000L)

            val chick = it.world.spawn(it.location, Chicken::class.java)
            chick.velocity = it.velocity.multiply(1.8)
            chick.lootTable = null
            chick.isCustomNameVisible = false
            chick.isInvulnerable = true
            chick.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, -1, 200, true, false, false))

            it.world.playSound(it.location, Sound.ENTITY_CHICKEN_DEATH, SoundCategory.PLAYERS, 1.0f, 1.0f)

            handler.unregisterBullet(it)
            it.remove()

            handler.addBulletEffect(1L, chick, meta) { _ ->
                val yVelocity = chick.velocity.y
                if (yVelocity in -0.09..-0.07 && chick.checkOnGround()) {
                    val location = chick.location
                    it.world.playSound(location, Sound.ENTITY_CHICKEN_DEATH, SoundCategory.PLAYERS, 1.0f, 1.0f)
                    chick.remove()
                    splashDamageExplosive(handler.plugin, location, meta)
                    stopEffect()
                }
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(0.6, StatModifier.Property.BASE_DAMAGE)
            add(1, StatModifier.Property.PELLET_COUNT)
            multiply(1.1, StatModifier.Property.SPLASH_RADIUS)
        }
    }
}