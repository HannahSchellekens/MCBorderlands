package maliwan.mcbl.util

import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity

/**
 * Set the scale of the living entity where 1.0 is the regular size.
 */
fun LivingEntity.setScale(scale: Double) {
    val attribute = getAttribute(Attribute.GENERIC_SCALE)
    attribute?.baseValue = scale
}