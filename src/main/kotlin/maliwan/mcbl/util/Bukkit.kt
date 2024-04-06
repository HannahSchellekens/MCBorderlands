package maliwan.mcbl.util

import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

fun JavaPlugin.scheduleTask(delay: Long, runnable: Runnable) {
    server.scheduler.scheduleSyncDelayedTask(this, runnable, delay)
}