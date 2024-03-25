package maliwan.mcbl.util

import org.bukkit.Location
import org.bukkit.entity.Entity

fun Location.nearbyEntities(radius: Double): Collection<Entity> {
    return world?.getNearbyEntities(this.clone().add(0.0, 0.75, 0.0), radius, radius, radius) ?: emptyList()
}