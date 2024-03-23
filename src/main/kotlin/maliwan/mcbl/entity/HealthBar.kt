package maliwan.mcbl.entity

import org.bukkit.ChatColor
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * @author Hannah Schellekens
 */
fun LivingEntity.showHealthBar(
    smallLength: Int = 15,
    largeLength: Int = 25,
    smallThreshold: Double = 14.0,
    color: ChatColor = ChatColor.RED
) {
    val maxHealth = getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: return
    val barLength = if (maxHealth <= smallThreshold) smallLength else largeLength
    val percentage = health / maxHealth
    val bars = ceil(percentage * barLength).toInt()
    val grey = barLength - bars

    val barText = if (health <= 0.0) {
        "${color}X"
    }
    else "${ChatColor.GRAY}[$color${"|".repeat(bars)}${ChatColor.DARK_GRAY}${"|".repeat(grey)}${ChatColor.GRAY}] %.1f".format(health)

    // TODO: Change to display entity to not hijack the entity name.
    customName = barText
    isCustomNameVisible = true
}