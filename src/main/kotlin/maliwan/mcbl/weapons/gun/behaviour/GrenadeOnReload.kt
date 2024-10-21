package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.util.plugin.Damage
import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.util.plugin.Ticks
import maliwan.mcbl.util.spigot.scheduleTask
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.CustomGrenadeManager.CustomGrenade
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.GunExecution
import maliwan.mcbl.weapons.gun.forEachBehaviour
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Display
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.max

/**
 * @author Hannah Schellekens
 */
open class GrenadeOnReload : ReloadBehaviour {

    private val plugin = Bukkit.getPluginManager().getPlugin("MCBorderlands") as? MCBorderlandsPlugin
        ?: error("No MCBorderlands plugin found")

    override fun beforeReload(player: Player, gunExecution: GunExecution) {
        val display = player.world.spawn(player.eyeLocation, ItemDisplay::class.java).apply {
            itemStack = ItemStack(Material.BOW, 1)
            billboard = Display.Billboard.VERTICAL
            brightness = Display.Brightness(15, 15)
        }
        val direction = player.eyeLocation.direction.normalize().multiply(
            1.5 * when (gunExecution.weaponClass) {
                WeaponClass.LAUNCHER -> 1.25
                else -> 1.0
            }
        )

        val elements = ArrayList<Elemental>(gunExecution.elements)
        val elementalProbability = HashMap<Elemental, Probability>(gunExecution.elementalProbability)
        val elementalDuration = HashMap<Elemental, Ticks>(gunExecution.elementalDuration)
        val elementalDamage = HashMap<Elemental, Damage>(gunExecution.elementalDamage)

        // Set explosive if no element is known:
        if (elements.isEmpty()) {
            elements.add(Elemental.EXPLOSIVE)
            elementalProbability[Elemental.EXPLOSIVE] = Probability.ONE
            elementalDamage[Elemental.EXPLOSIVE] = Damage(10.0)
            elementalDuration[Elemental.EXPLOSIVE] = Ticks(0)
        }

        val damage = when (gunExecution.weaponClass) {
            WeaponClass.LAUNCHER -> javelinDamage(gunExecution)
            else -> grenadeDamage(gunExecution)
        }

        val gravity = when (gunExecution.weaponClass) {
            WeaponClass.LAUNCHER -> 0.001
            else -> 0.016
        }

        val bulletMeta = BulletMeta(
            shooter = player,
            assembly = gunExecution.assembly,
            damage = damage,
            splashDamage = damage,
            elements = elements,
            elementalProbability = elementalProbability,
            elementalDuration = elementalDuration,
            elementalDamage = elementalDamage,
            elementalPolicy = gunExecution.elementalPolicy,
            splashRadius = when (gunExecution.weaponClass) {
                WeaponClass.LAUNCHER -> gunExecution.splashRadius * 1.1
                else -> 1.5
            },
        )

        val customGrenade = CustomGrenade(display, direction, bulletMeta = bulletMeta, source = player, gravity = gravity)

        bulletMeta.assembly?.forEachBehaviour<UpdateReloadGrenadeBehaviour> {
            it.updateReloadGrenade(customGrenade)
        }

        // Have a short delay before throwing a tediore grenade.
        // Having it be thrown immediately makes it less obvious the player threw it and less satisfying.
        plugin.scheduleTask(when (gunExecution.weaponClass) {
            WeaponClass.LAUNCHER -> 7L
            else -> 3L
        }) {
            plugin.customGrenadeManager.throwGrenade(customGrenade)
        }

        if (gunExecution.weaponClass != WeaponClass.LAUNCHER) {
            val inventory = plugin.inventoryManager[player]
            inventory.removeAmmo(gunExecution.weaponClass, gunExecution.clip)
        }
    }

    private fun javelinDamage(gunExecution: GunExecution): Damage {
        return gunExecution.baseDamage * 0.5
    }

    private fun grenadeDamage(gunExecution: GunExecution): Damage {
        val shotgunBonus = if (gunExecution.weaponClass == WeaponClass.SHOTGUN) {
            when (val count = gunExecution.pelletCount) {
                0, 1, 2 -> 1.5
                3 -> 1.7
                else -> count * 0.4 + 0.5
            }
        } else 1.0

        return gunExecution.baseDamage * (max(gunExecution.clip, 1) * shotgunBonus)
    }
}