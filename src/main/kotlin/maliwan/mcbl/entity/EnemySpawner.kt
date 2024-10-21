package maliwan.mcbl.entity

import maliwan.mcbl.Keys
import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.util.spigot.isInDungeon
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.persistence.PersistentDataType
import kotlin.math.max
import kotlin.math.min

/**
 * @author Hannah Schellekens
 */
open class EnemySpawner(val plugin: MCBorderlandsPlugin) : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun badassEnemies(event: EntitySpawnEvent) {
        val living = event.entity as? LivingEntity ?: return
        val baseLevel = EnemyLevel.enemyLevelPool.roll()
        val inDungeon = living.location.isInDungeon()

        val enemyLevel = if (inDungeon) baseLevel.nextLevel else baseLevel
        if (enemyLevel <= EnemyLevel.REGULAR) return

        val multiplier = enemyLevel.healthMultiplier * if (inDungeon) 0.5 else 1.0

        val maxHealth = living.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return
        val newBaseValue = max(0.0, min(2024.0, maxHealth.baseValue * multiplier))
        maxHealth.baseValue = newBaseValue
        living.health = newBaseValue
        living.persistentDataContainer.apply {
            set(Keys.enemyLevel, PersistentDataType.STRING, enemyLevel.name)
        }

        living.setScale(enemyLevel.mobSizeScale)
        living.showHealthBar(plugin)
    }
}