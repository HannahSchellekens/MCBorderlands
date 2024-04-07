package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.util.Chance
import maliwan.mcbl.util.Damage
import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.*
import maliwan.mcbl.weapons.gun.GunExecution
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
        val direction = player.eyeLocation.direction.normalize().multiply(1.5)

        // Set explosive if no element is known:
        if (gunExecution.elements.isEmpty()) {
            gunExecution.elements.add(Elemental.EXPLOSIVE)
            gunExecution.elementalChance[Elemental.EXPLOSIVE] = Chance.ONE
            gunExecution.elementalDamage[Elemental.EXPLOSIVE] = Damage(10.0)
            gunExecution.elementalDuration[Elemental.EXPLOSIVE] = Ticks(0)
        }

        val shotgunBonus = if (gunExecution.weaponClass == WeaponClass.SHOTGUN) {
            when (val count = gunExecution.pelletCount) {
                0, 1, 2 -> 1.5
                3 -> 1.7
                else -> count * 0.4 + 0.5
            }
        }
        else 1.0

        val damage = gunExecution.baseDamage * (max(gunExecution.clip, 1) * shotgunBonus)

        val bulletMeta = BulletMeta(
            shooter = player,
            assembly = null,
            damage = damage,
            splashDamage = damage,
            elements = gunExecution.elements,
            elementalChance = gunExecution.elementalChance,
            elementalDuration = gunExecution.elementalDuration,
            elementalDamage = gunExecution.elementalDamage,
            elementalPolicy = gunExecution.elementalPolicy,
            splashRadius = 1.0,
        )

        val grenade = GrenadeManager.Grenade(display, direction, bulletMeta = bulletMeta, source = player)
        plugin.grenadeManager.throwGrenade(grenade)

        val inventory = plugin.inventoryManager[player]
        inventory.removeAmmo(gunExecution.weaponClass, gunExecution.clip)
    }
}