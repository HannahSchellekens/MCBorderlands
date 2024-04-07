package maliwan.mcbl.gui

import maliwan.mcbl.util.modifyRandom
import maliwan.mcbl.weapons.Elemental
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Display
import org.bukkit.entity.EntityType
import org.bukkit.entity.TextDisplay
import org.bukkit.event.Listener
import org.bukkit.util.Vector

/**
 * @author Hannah Schellekens
 */
open class DamageParticles : Listener, Runnable {

    /**
     * Maps each active text display to the time when it should die.
     */
    private val displays = HashMap<TextDisplay, Long>()

    fun showCritDisplay(location: Location, duration: Long = 1000L) {
        makeNewDisplay(location.fuzz().add(Vector(0.0, 0.45, 0.0)), duration) {
            it.text = "${ChatColor.RED}Critical"
        }
    }

    fun showDamageDisplay(location: Location, damage: Double, duration: Long = 1000L) {
        makeNewDisplay(location.fuzz(), duration) {
            it.text = "${ChatColor.WHITE}%.1f".format(damage)
        }
    }

    fun showDotDamageDisplay(location: Location, damage: Double, element: Elemental, duration: Long = 1000L) {
        makeNewDisplay(location.fuzz().add(Vector(0.0, (-0.4).modifyRandom(0.12), 0.0)), duration) {
            it.text = "${element.chatColor}%.1f".format(damage)
        }
    }

    private fun makeNewDisplay(location: Location, duration: Long, apply: (TextDisplay) -> Unit) {
        val display = location.world?.spawnEntity(location, EntityType.TEXT_DISPLAY) as? TextDisplay ?: return

        display.apply {
            isDefaultBackground = false
            isSeeThrough = true
            alignment = TextDisplay.TextAlignment.CENTER
            billboard = Display.Billboard.VERTICAL
            brightness = Display.Brightness(15, 15)
        }
        apply(display)

        displays[display] = System.currentTimeMillis() + duration
    }

    /**
     * Cleans up all memory: removes all text displays.
     */
    fun cleanup() {
        displays.clear()
    }

    override fun run() {
        displays.forEach { (display, _) ->
            display.teleport(display.location.add(0.0, 0.02, 0.0))
        }

        displays.entries.removeIf { (display, time) ->
            val expired = System.currentTimeMillis() >= time
            if (expired) {
                display.remove()
            }
            expired
        }
    }

    private fun Location.fuzz(amount: Double = 0.18) = add(0.0.modifyRandom(amount), 0.0, 0.0.modifyRandom(amount))
}