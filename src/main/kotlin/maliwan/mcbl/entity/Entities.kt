package maliwan.mcbl.entity

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * How many armor points the given entity has: 0 if not applicable.
 */
val Entity.armorPoints: Double
    get() = (this as? LivingEntity)?.getAttribute(Attribute.GENERIC_ARMOR)?.value ?: 0.0
