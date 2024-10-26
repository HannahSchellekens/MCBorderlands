package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.util.spigot.modifyRandomCircle
import maliwan.mcbl.util.spigot.scheduleTask
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostBulletLandBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import maliwan.mcbl.weapons.splashDamage
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Blizzard : UniqueGun, PostGenerationBehaviour, PostBulletLandBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Blizzard"
    override var redText = "You have to let it go"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        statModifiers.applyAll(properties)

        properties.splashDamage = properties.baseDamage
        properties.extraInfoText += "Extreme ${Elemental.CRYO.chatColor}freeze${ChatColor.WHITE} efficacy"
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        return assembly.replacePart(LauncherParts.Barrel.MALIWAN)
    }

    override fun afterBulletLands(
        handler: WeaponEventHandler,
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    ) {
        val baseLocation = hitLocation ?: bullet.location

        repeat(Random.nextInt(9, 20)) { it ->
            handler.plugin.scheduleTask(Random.nextLong(3L, 7L) * (it + 1)) {
                val explosionLocation = baseLocation.modifyRandomCircle(meta.splashRadius * 2)
                splashDamage(handler.plugin, explosionLocation, meta)
                explosionLocation.world?.playSound(explosionLocation, Sound.BLOCK_SNOW_STEP, 1f, 1f)
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(0.01, StatModifier.Property.BASE_DAMAGE)
            multiply(1.3, StatModifier.Property.SPLASH_RADIUS)
            multiply(1.3, StatModifier.Property.ELEMENTAL_CHANCE)
        }
    }
}