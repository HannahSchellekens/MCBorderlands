package maliwan.mcbl.util.spigot

import org.bukkit.plugin.java.JavaPlugin

fun JavaPlugin.scheduleTask(delay: Long, runnable: Runnable) {
    server.scheduler.scheduleSyncDelayedTask(this, runnable, delay)
}