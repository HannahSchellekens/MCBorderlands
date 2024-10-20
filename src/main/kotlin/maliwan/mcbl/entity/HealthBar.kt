package maliwan.mcbl.entity

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.util.countMap
import maliwan.mcbl.weapons.Elemental
import org.bukkit.ChatColor
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

fun LivingEntity.showHealthBar(
    smallLength: Int = 10,
    largeLength: Int = 20,
    smallThreshold: Double = 19.5,
    color: ChatColor = ChatColor.RED,
    statusEffects: List<Elemental> = emptyList()
) {
    fun Elemental.letter() = when (this) {
        Elemental.INCENDIARY -> "${chatColor}F"
        Elemental.CORROSIVE -> "${chatColor}C"
        Elemental.SHOCK -> "${chatColor}E"
        Elemental.SLAG -> "${chatColor}S"
        Elemental.CRYO -> "${chatColor}I"
        else -> ""
    }

    val counts = statusEffects.countMap()
    val statusPrefix = statusEffects.sorted().distinct().joinToString("") {
        val count = if ((counts[it] ?: 0) > 1) counts[it]?.toString() ?: "" else ""
        it.chatColor + count + it.symbol
    }

    val maxHealth = getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue ?: return
    val barLength = if (maxHealth <= smallThreshold) smallLength else largeLength
    val percentage = health / maxHealth
    val bars = max(0, ceil(percentage * barLength).toInt())
    val grey = max(0, min(barLength, barLength - bars))

    val barText = if (health <= 0.0) {
        "${color}☠"
    }
    else "$statusPrefix ${ChatColor.GRAY}[$color${"|".repeat(bars)}${ChatColor.DARK_GRAY}${"|".repeat(grey)}${ChatColor.GRAY}] %.1f".format(health).trim()

    val title = enemyLevel()?.title
    val titlePrefix = title?.let { "${ChatColor.WHITE}$it " } ?: ""
    customName = "$titlePrefix$barText"
    isCustomNameVisible = true
}

fun LivingEntity.showHealthBar(
    plugin: MCBorderlandsPlugin,
    smallLength: Int = 10,
    largeLength: Int = 20,
    smallThreshold: Double = 19.5,
    color: ChatColor = ChatColor.RED
) {
    val effects = plugin.weaponEventHandler.elementalStatusEffects.activeEffects(this)
        .map { (effect, _) -> effect.elemental }
    showHealthBar(smallLength, largeLength, smallThreshold, color, effects)
}