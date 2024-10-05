package maliwan.mcbl.util

import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.Elemental
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * @author Hannah Schellekens
 */
enum class DamageClass(
    val protectiveEnchantment: Enchantment
) {

    /**
     * Projectiles.
     */
    BULLETS(Enchantment.PROJECTILE_PROTECTION),

    /**
     * Explosive element.
     */
    EXPLOSIVE(Enchantment.BLAST_PROTECTION),

    /**
     * Incendiary element.
     */
    FIRE(Enchantment.FIRE_PROTECTION)
    ;

    companion object {

        /**
         * Get all applicable damage classes for a bullet fired with the given bullet meta.
         */
        fun damageClassesOf(bulletMeta: BulletMeta): List<DamageClass> {
            val result = ArrayList<DamageClass>()
            if (Elemental.INCENDIARY in bulletMeta.elements) {
                result += FIRE
            }
            if (bulletMeta.splashDamage.damage > 0.0001 || Elemental.EXPLOSIVE in bulletMeta.elements) {
                result += EXPLOSIVE
            }
            result += BULLETS
            return result
        }
    }
}

/**
 * Get the total amount of EPF points for all enchanted armour on the entity.
 * This can exceed the damage calculation cap of 20.0 points.
 *
 * @return The total enchantment protection factor points that the entity wears.
 */
fun Entity.enchantmentProtectionFactor(vararg damageClasses: DamageClass): Double {
    return enchantmentProtectionFactor(damageClasses.toSet())
}

/**
 * Get the total amount of EPF points for all enchanted armour on the entity.
 * This can exceed the damage calculation cap of 20.0 points.
 *
 * @return The total enchantment protection factor points that the entity wears.
 */
fun Entity.enchantmentProtectionFactor(damageClasses: Iterable<DamageClass>): Double {
    val equipment = (this as? LivingEntity)?.equipment ?: return 0.0
    val helm = equipment.helmet
    val chestplate = equipment.chestplate
    val leggings = equipment.leggings
    val boots = equipment.boots

    var totalPoints = 0.0

    totalPoints += helm?.getEnchantmentLevel(Enchantment.PROTECTION)?.toDouble() ?: 0.0
    totalPoints += chestplate?.getEnchantmentLevel(Enchantment.PROTECTION)?.toDouble() ?: 0.0
    totalPoints += leggings?.getEnchantmentLevel(Enchantment.PROTECTION)?.toDouble() ?: 0.0
    totalPoints += boots?.getEnchantmentLevel(Enchantment.PROTECTION)?.toDouble() ?: 0.0

    damageClasses.forEach { damageClass ->
        totalPoints += helm?.getEnchantmentLevel(damageClass.protectiveEnchantment)?.toDouble() ?: 0.0
        totalPoints += chestplate?.getEnchantmentLevel(damageClass.protectiveEnchantment)?.toDouble() ?: 0.0
        totalPoints += leggings?.getEnchantmentLevel(damageClass.protectiveEnchantment)?.toDouble() ?: 0.0
        totalPoints += boots?.getEnchantmentLevel(damageClass.protectiveEnchantment)?.toDouble() ?: 0.0
    }

    return totalPoints
}