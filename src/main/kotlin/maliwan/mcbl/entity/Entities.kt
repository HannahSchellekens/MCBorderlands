package maliwan.mcbl.entity

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.util.scheduleTask
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Cow
import org.bukkit.entity.Donkey
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min

/**
 * [Attribute.GENERIC_MAX_HEALTH]
 */
val LivingEntity.genericMaxHealth: Double
    get() = getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 0.0

/**
 * How many armor points the given entity has: 0 if not applicable.
 */
val Entity.armorPoints: Double
    get() = (this as? LivingEntity)?.getAttribute(Attribute.GENERIC_ARMOR)?.value ?: 0.0

/**
 * Heals the target with the given amount of health.
 * Will never apply a negative amount of health or set the health above the health limit.
 */
fun LivingEntity.heal(amount: Double) {
    val minHealth = 0.0
    val maxHealth = genericMaxHealth
    val newAmount = health + amount

    health = min(max(newAmount, minHealth), maxHealth)
}

/**
 * Set knockback resistance (scale 0.0 for no resistance, to 1.0 for full resistance).
 * `null` is reset.
 */
fun LivingEntity.setKnockbackResistance(knockbackValue: Double? = null) {
    val knockback = getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)
    if (knockback != null) {
        knockback.baseValue = knockbackValue ?: knockback.defaultValue
    }
}

/**
 * Temporarily disable knockback.
 */
fun LivingEntity.temporarilyDisableKnockback(plugin: MCBorderlandsPlugin) {
    setKnockbackResistance(1.0)
    plugin.scheduleTask(1L) {
        setKnockbackResistance()
    }
}

/**
 * Executes the action with iframes disabled for the living entity.
 * Iframes get disabled on the next server tick.
 */
fun LivingEntity.temporarilyDisableIframes(plugin: MCBorderlandsPlugin) {
    val oldNoDamageTicks = noDamageTicks
    val oldNoDamageTicksMax = maximumNoDamageTicks
    noDamageTicks = 0
    maximumNoDamageTicks = 0

    plugin.scheduleTask(1L) {
        noDamageTicks = oldNoDamageTicks
        maximumNoDamageTicks = oldNoDamageTicksMax
    }
}

/**
 * Calculates for each type of living entity what the head position of the entity is.
 *
 * This is not the same as eye location: that is (I guess) the origin of the head from which the head rotation
 * is calculated. Creepers have it dead center in their head, but some mobs do not.
 *
 * E.g. pigs have their eye location at the center of their core body, so they require some projection.
 *
 * This value will calculate this projection.
 */
val LivingEntity.headLocation: Location
    get() {
        val headOrigin = eyeLocation.clone()
        val direction = headOrigin.direction.clone().normalize()

        return when (type) {
            // Values are obtained by trial and error.
            EntityType.COW -> headOrigin.add(direction.multiply(0.72))
            EntityType.CREEPER -> headOrigin
            EntityType.GUARDIAN -> headOrigin.add(direction.multiply(0.4))
            EntityType.PIG -> headOrigin.add(direction.multiply(0.7))
            EntityType.SPIDER -> headOrigin.add(direction.multiply(0.52))
            EntityType.DONKEY -> headOrigin.add(direction.multiply(0.82))
            EntityType.MULE -> headOrigin.add(direction.multiply(0.82))
            EntityType.EVOKER -> headOrigin
            EntityType.VINDICATOR -> headOrigin
            EntityType.ILLUSIONER -> headOrigin
            EntityType.CAVE_SPIDER -> headOrigin.add(direction.multiply(0.3))
            EntityType.SILVERFISH -> headOrigin.add(direction.multiply(0.16))
            EntityType.ENDER_DRAGON -> headOrigin.add(direction.multiply(-6.3).add(Vector(0.0, -4.3, 0.0)))
            EntityType.WITCH -> headOrigin
            EntityType.SHEEP -> headOrigin.add(direction.multiply(0.72))
            EntityType.CHICKEN -> headOrigin.add(direction.multiply(0.29))
            EntityType.WOLF -> headOrigin.add(direction.multiply(0.5))
            EntityType.MUSHROOM_COW -> headOrigin.add(direction.multiply(0.72))
            EntityType.SNOWMAN -> headOrigin
            EntityType.OCELOT -> headOrigin.add(direction.multiply(0.57))
            EntityType.HORSE -> headOrigin.add(direction.multiply(0.9).add(Vector(0.0, 0.3, 0.0)))
            EntityType.RABBIT -> headOrigin.add(direction.multiply(0.15))
            EntityType.POLAR_BEAR -> headOrigin.add(direction.multiply(1.3).add(Vector(0.0, -0.15, 0.0)))
            EntityType.LLAMA -> headOrigin.add(direction.multiply(0.8))
            EntityType.VILLAGER -> headOrigin
            EntityType.CAT -> headOrigin.add(direction.multiply(0.47))
            EntityType.PANDA -> headOrigin.add(direction.multiply(1.1).add(Vector(0.0, -0.2, 0.0)))
            EntityType.PILLAGER -> headOrigin
            EntityType.RAVAGER -> headOrigin.add(direction.multiply(1.2).add(Vector(0.0, -0.23, 0.0)))
            EntityType.TRADER_LLAMA -> headOrigin.add(direction.multiply(0.8))
            EntityType.WANDERING_TRADER -> headOrigin
            EntityType.FOX -> headOrigin.add(direction.multiply(0.35))
            EntityType.GOAT -> headOrigin.add(direction.multiply(0.7))
            EntityType.FROG -> headOrigin.add(direction.multiply(0.2))
            EntityType.WARDEN -> headOrigin
            EntityType.CAMEL -> headOrigin.add(direction.multiply(1.3).add(Vector(0.0, 0.2, 0.0)))
            EntityType.SNIFFER -> headOrigin.add(direction.multiply(1.7).add(Vector(0.0, -0.2, 0.0)))
            EntityType.PLAYER -> headOrigin.add(direction.multiply(0.0))
            EntityType.ELDER_GUARDIAN -> headOrigin.add(direction.multiply(1.0))
            EntityType.SQUID -> headOrigin.add(direction.multiply(-0.6))
            EntityType.COD -> headOrigin.add(direction.multiply(0.3))
            EntityType.SALMON -> headOrigin.add(direction.multiply(0.4))
            EntityType.PUFFERFISH -> headOrigin
            EntityType.TROPICAL_FISH -> headOrigin
            EntityType.DOLPHIN -> headOrigin.add(direction.multiply(0.7))
            EntityType.AXOLOTL -> headOrigin.add(direction.multiply(0.4))
            EntityType.GLOW_SQUID -> headOrigin.add(direction.multiply(-0.6))
            EntityType.TADPOLE -> headOrigin.add(direction.multiply(0.12))
            EntityType.TURTLE -> headOrigin.add(direction.multiply(0.62).add(Vector(0.0, -0.15, 0.0)))
            EntityType.BAT -> headOrigin.add(direction.multiply(0.0).add(Vector(0.0, 0.1, 0.0)))
            EntityType.PARROT -> headOrigin.add(direction.multiply(0.22))
            EntityType.PHANTOM -> headOrigin.add(direction.multiply(0.5))
            EntityType.BEE -> headOrigin.add(direction.multiply(0.25))
            EntityType.VEX -> headOrigin
            EntityType.SLIME -> headOrigin
            EntityType.GHAST -> headOrigin.add(direction.multiply(2.35))
            EntityType.ENDERMAN -> headOrigin
            EntityType.ENDERMITE -> headOrigin.add(direction.multiply(0.2))
            EntityType.SHULKER -> headOrigin
            EntityType.ALLAY -> headOrigin.add(direction.multiply(0.0))
            EntityType.ZOMBIFIED_PIGLIN -> headOrigin
            EntityType.BLAZE -> headOrigin
            EntityType.MAGMA_CUBE -> headOrigin
            EntityType.HOGLIN -> headOrigin.add(direction.multiply(1.3))
            EntityType.PIGLIN -> headOrigin
            EntityType.STRIDER -> headOrigin.add(direction.multiply(0.55).add(Vector(0.0, -0.27, 0.0)))
            EntityType.ZOGLIN -> headOrigin.add(direction.multiply(1.3))
            EntityType.PIGLIN_BRUTE -> headOrigin
            EntityType.WITHER_SKELETON -> headOrigin
            EntityType.STRAY -> headOrigin
            EntityType.HUSK -> headOrigin
            EntityType.ZOMBIE_VILLAGER -> headOrigin
            EntityType.SKELETON_HORSE -> headOrigin.add(direction.multiply(0.9).add(Vector(0.0, 0.3, 0.0)))
            EntityType.ZOMBIE_HORSE -> headOrigin.add(direction.multiply(0.9).add(Vector(0.0, 0.3, 0.0)))
            EntityType.SKELETON -> headOrigin
            EntityType.GIANT -> headOrigin.add(direction.multiply(1.0).add(Vector(0.0, 0.7, 0.0)))
            EntityType.ZOMBIE -> headOrigin
            EntityType.WITHER -> headOrigin
            EntityType.IRON_GOLEM -> headOrigin.add(direction.multiply(0.22).add(Vector(0.0, 0.1, 0.0)))
            EntityType.DROWNED -> headOrigin

            else -> headOrigin
        }
    }

/**
 * How many blocks a hit may be from the [headLocation] in order for the hit to count as a critical hit.
 */
val LivingEntity.headshotRange: Double
    get() = when(type) {
        EntityType.COW -> 0.34
        EntityType.CREEPER -> 0.37
        EntityType.GUARDIAN -> 0.25
        EntityType.PIG -> 0.27
        EntityType.SPIDER -> 0.27
        EntityType.DONKEY -> 0.35
        EntityType.MULE -> 0.35
        EntityType.EVOKER -> 0.35
        EntityType.VINDICATOR -> 0.35
        EntityType.ILLUSIONER -> 0.35
        EntityType.CAVE_SPIDER -> 0.19
        EntityType.SILVERFISH -> 0.13
        EntityType.ENDER_DRAGON -> 2.0 /* TODO: TEST */
        EntityType.WITCH -> 0.37
        EntityType.SHEEP -> 0.41
        EntityType.CHICKEN -> 0.18
        EntityType.WOLF -> 0.26
        EntityType.MUSHROOM_COW -> 0.34
        EntityType.SNOWMAN -> 0.45
        EntityType.OCELOT -> 0.32
        EntityType.HORSE -> 0.5
        EntityType.RABBIT -> 0.15
        EntityType.POLAR_BEAR -> 0.62
        EntityType.LLAMA -> 0.5
        EntityType.VILLAGER -> 0.45
        EntityType.CAT -> 0.2
        EntityType.PANDA -> 0.5
        EntityType.PILLAGER -> 0.35
        EntityType.RAVAGER -> 0.35
        EntityType.TRADER_LLAMA -> 0.8
        EntityType.WANDERING_TRADER -> 0.35
        EntityType.FOX -> 0.2
        EntityType.GOAT -> 0.33
        EntityType.FROG -> 0.18
        EntityType.WARDEN -> 0.5
        EntityType.CAMEL -> 0.54
        EntityType.SNIFFER -> 0.82
        EntityType.PLAYER -> 0.35
        EntityType.ELDER_GUARDIAN -> 0.35
        EntityType.SQUID -> 0.36
        EntityType.COD -> 0.2
        EntityType.SALMON -> 0.39
        EntityType.PUFFERFISH -> 0.22
        EntityType.TROPICAL_FISH -> 0.3
        EntityType.DOLPHIN -> 0.3
        EntityType.AXOLOTL -> 0.2
        EntityType.GLOW_SQUID -> 0.36
        EntityType.TADPOLE -> 0.21
        EntityType.TURTLE -> 0.22
        EntityType.BAT -> 0.31
        EntityType.PARROT -> 0.2
        EntityType.PHANTOM -> 0.25
        EntityType.BEE -> 0.2
        EntityType.VEX -> 0.25
        EntityType.SLIME -> 0.0 /* Cannot be crit */
        EntityType.GHAST -> 1.15
        EntityType.ENDERMAN -> 0.33
        EntityType.ENDERMITE -> 0.1
        EntityType.SHULKER -> 0.0 /* Cannot be crit */
        EntityType.ALLAY -> 0.24
        EntityType.ZOMBIFIED_PIGLIN -> 0.35
        EntityType.BLAZE -> 0.35
        EntityType.MAGMA_CUBE -> 0.0 /* Cannot be crit */
        EntityType.HOGLIN -> 0.71
        EntityType.PIGLIN -> 0.35
        EntityType.STRIDER -> 0.35
        EntityType.ZOGLIN -> 0.71
        EntityType.PIGLIN_BRUTE -> 0.35
        EntityType.WITHER_SKELETON -> 0.4
        EntityType.STRAY -> 0.35
        EntityType.HUSK -> 0.35
        EntityType.ZOMBIE_VILLAGER -> 0.35
        EntityType.SKELETON_HORSE -> 0.5
        EntityType.ZOMBIE_HORSE -> 0.5
        EntityType.SKELETON -> 0.35
        EntityType.GIANT -> 1.05
        EntityType.ZOMBIE -> 0.35
        EntityType.WITHER -> 0.57
        EntityType.IRON_GOLEM -> 0.77
        EntityType.DROWNED -> 0.35

        else -> 0.0
    }