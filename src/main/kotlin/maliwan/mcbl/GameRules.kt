package maliwan.mcbl

import maliwan.mcbl.entity.showHealthBar
import org.bukkit.ChatColor
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.EntitySpawnEvent
import kotlin.math.max
import kotlin.math.min

/**
 * @author Hannah Schellekens
 */
open class GameRules(val plugin: MCBorderlandsPlugin) : Listener {

    @EventHandler
    fun moreBaseHealth(event: EntitySpawnEvent) {
        // Make all entities a bit more beefy to compensate for elemental damage modifiers.
        val entity = event.entity as? LivingEntity ?: return
        val maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return
        val newBaseValue = max(0.0, min(2024.0, maxHealth.baseValue * 1.8))
        maxHealth.baseValue = newBaseValue
        entity.health = newBaseValue
        entity.showHealthBar(plugin)
    }

    @EventHandler(priority = EventPriority.LOW)
    fun healthBarUpdate(event: EntityDamageEvent) {
        val entity = event.entity as? LivingEntity ?: return
        entity.showHealthBar(plugin)

    }

    @EventHandler(priority = EventPriority.LOW)
    fun healthBarUpdateWhenHealed(event: EntityRegainHealthEvent) {
        val entity = event.entity as? LivingEntity ?: return
        entity.showHealthBar(plugin)
    }
}