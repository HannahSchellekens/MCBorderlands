package maliwan.mcbl.gui

import maliwan.mcbl.MCBorderlandsPlugin
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Display
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.bukkit.event.Listener
import org.bukkit.util.Vector
import kotlin.math.max

/**
 * @author Hannah Schellekens
 */
open class Hud(val plugin: MCBorderlandsPlugin) : Listener, Runnable {

    private val hudAmmoElements = HashMap<Player, TextDisplay>()

    /**
     * Shows the text message that displays the ammo status of the gun that the player is currently holding.
     * `null` when there is no applicable ammo context (e.g. no gun held).
     */
    fun ammoMessage(player: Player): String? {
        val gunExecution = plugin.weaponEventHandler.obtainGunExecutionFromInventory(player) ?: return null
        val inventory = plugin.inventoryManager[player]

        val gunType = gunExecution.weaponClass.displayName
        val leftInClip = gunExecution.clip
        val totalAmmo = inventory[gunExecution.weaponClass]
        val ammoLeft = max(0, totalAmmo - leftInClip)

        return "${ChatColor.GRAY}$gunType: ${ChatColor.WHITE}$leftInClip ${ChatColor.GRAY}/ $ammoLeft"
    }

    fun showDisplay(player: Player, show: Boolean = true) {
        val displayPeak = hudAmmoElements[player]
        if (displayPeak == null && show.not()) return

        // Must hide element, delete it.
        if (show.not()) {
            hudAmmoElements.remove(player)
            displayPeak!!.remove()
            return
        }
        // Otherwise show element, i.e. create a new one.

        val location = player.eyeLocation
        val playerDirection = player.location.direction.clone()

        // Place the status in the top left corner of the screen (approximately).
        val newLocation = location.clone()
        newLocation.yaw -= 42.0f
        newLocation.pitch -= 22.0f
        val displayLocation = newLocation.add(newLocation.direction.normalize().multiply(9.0))

        val display = displayPeak ?: player.world.spawnEntity(displayLocation, EntityType.TEXT_DISPLAY) as TextDisplay
        hudAmmoElements[player] = display

        display.text = ammoMessage(player)
        display.isSeeThrough = true
        display.brightness = Display.Brightness(15, 15)
        display.isDefaultBackground = false
        displayLocation.setDirection(playerDirection.multiply(-1).normalize())
        display.teleport(displayLocation)
    }

    fun hideDisplay(player: Player) = showDisplay(player, false)

    fun clearDisplays() {
        hudAmmoElements.values.forEach { it.remove() }
        hudAmmoElements.clear()
    }

    override fun run() {
        Bukkit.getOnlinePlayers().forEach { showDisplay(it) }
    }

    companion object {

        private val UP = Vector(0.0, 1.0, 0.0)
    }
}