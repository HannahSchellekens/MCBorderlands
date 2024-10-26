package maliwan.mcbl.gui

import maliwan.mcbl.Keys
import maliwan.mcbl.util.average
import maliwan.mcbl.util.modifyRandom
import maliwan.mcbl.weapons.Elemental
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Display
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.TextDisplay
import org.bukkit.event.Listener
import org.bukkit.persistence.PersistentDataType
import org.bukkit.util.Vector

/**
 * Using
 *
 * @author Hannah Schellekens
 */
open class DamageParticles : Listener, Runnable {

    /**
     * Maps each active text display to the time when it should die.
     */
    private val displays = HashMap<TextDisplay, Long>()

    /**
     * All damage particles that must be shown on the next tick.
     */
    private val damageParticleQueue = ArrayDeque<DamageParticleEntry>()

    fun scheduleDisplay(entry: DamageParticleEntry) {
        damageParticleQueue.add(entry)
    }

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
            billboard = Display.Billboard.CENTER
            brightness = Display.Brightness(15, 15)
            persistentDataContainer.set(Keys.damageParticle, PersistentDataType.BOOLEAN, true)
        }
        apply(display)

        displays[display] = System.currentTimeMillis() + duration
    }

    /**
     * Turns all scheduled particles into actual display particles.
     */
    fun flushSchedule() {
        damageParticleQueue
            .groupBy { it.targetEntity }
            .forEach { (entity, entriesByEntity) ->
                entriesByEntity.groupBy { it.element }
                    .forEach { (elemental, entries) ->
                        val location = entries.map { it.location.toVector() }.average().toLocation(entity.world)
                        val totalDamage = entries.sumOf { it.damage }
                        val duration = entries.maxOf { it.duration }

                        when (elemental) {
                            Elemental.PHYSICAL -> showDamageDisplay(location.fuzz(), totalDamage, duration)
                            else -> {
                                showDotDamageDisplay(
                                    location.fuzz().add(Vector(0.0, (-0.4).modifyRandom(0.12), 0.0)),
                                    totalDamage,
                                    elemental,
                                    duration
                                )
                            }
                        }
                    }
            }

        damageParticleQueue.clear()
    }

    /**
     * Cleans up all memory: removes all text displays.
     */
    fun cleanup() {
        displays.clear()
        damageParticleQueue.clear()
    }

    override fun run() {
        flushSchedule()

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

    companion object {

        /**
         * Removes all damage particles from all worlds.
         */
        fun removeAllParticles() = Bukkit.getWorlds().forEach { world ->
            world .entities.asSequence()
                .filter { it.type == EntityType.TEXT_DISPLAY }
                .filter { it.persistentDataContainer.getOrDefault(Keys.damageParticle, PersistentDataType.BOOLEAN, false) }
                .forEach { it.remove() }
        }
    }

    /**
     * @author Hannah Schellekens
     */
    data class DamageParticleEntry(
        val targetEntity: Entity,
        val location: Location,
        val element: Elemental,
        val damage: Double,
        val duration: Long = 1000L
    )
}