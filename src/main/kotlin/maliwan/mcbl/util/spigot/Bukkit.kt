package maliwan.mcbl.util.spigot

import org.bukkit.plugin.java.JavaPlugin

/**
 * Schedules a task to be executed later.
 *
 * @param delay Schedule for over `delay` ticks.
 * @param runnable The action to execute.
 */
fun JavaPlugin.scheduleTask(delay: Long, runnable: Runnable) {
    server.scheduler.scheduleSyncDelayedTask(this, runnable, delay)
}