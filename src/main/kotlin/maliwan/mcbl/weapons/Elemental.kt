package maliwan.mcbl.weapons

import maliwan.mcbl.util.plugin.Ticks
import net.md_5.bungee.api.ChatColor
import org.bukkit.Color
import org.bukkit.entity.EntityType

/**
 * @author Hannah Schellekens
 */
enum class Elemental(
    val displayName: String,
    val chatColor: String,
    val symbol: String,
    val color: Color,
    val baseDotDuration: Ticks
) {

    /**
     * Regular damage.
     */
    PHYSICAL("", ChatColor.WHITE.toString(), "⚔", Color.WHITE, Ticks(0)),

    /**
     * Weaker against fish and armored targets. Neutral to the rest.
     */
    EXPLOSIVE("Explosive", ChatColor.YELLOW.toString(), "\uD83D\uDCA3", Color.YELLOW, Ticks(0)),

    /**
     * Strong against flesh and birds, weaker against the rest.
     */
    INCENDIARY("Incendiary", ChatColor.GOLD.toString(), "\uD83D\uDD25", Color.ORANGE, Ticks(100)),

    /**
     * Strong against armour, weak against flesh.
     */
    SHOCK("Shock", ChatColor.BLUE.toString(), "⚡", Color.fromRGB(37, 150, 190), Ticks(40)),

    /**
     * Strong against undead and armour, weak against flesh and bird.
     */
    CORROSIVE("Corrosive", ChatColor.GREEN.toString(), "☣", Color.LIME, Ticks(160)),

    /**
     * Slagged enemies take x2 damage from other damage sources.
     */
    SLAG("Slag", ChatColor.DARK_PURPLE.toString(), "⬇", Color.PURPLE, Ticks(160)),

    /**
     * Effect depends on amount of Cryo stacks:
     * - 1-2 stacks: +50% explosive/melee damage, Slowness I
     * - 3-4 stacks: +150% explosive/melee damage, Slowness II
     * - 5 stacks: +250% explosive/melee damage, Frozen Solid/no movement.
     */
    CRYO("Cryo", ChatColor.AQUA.toString(), "❄", Color.AQUA, Ticks(160))
    ;

    val nullIfPhysical: Elemental?
        get() = if (this == PHYSICAL) null else this

    /**
     * @return 0 when the element does not deal DoT damage.
     */
    val noDotMultiplier: Double
        get() = when (this) {
            PHYSICAL, SLAG, EXPLOSIVE, CRYO -> 0.0
            else -> 1.0
        }

    companion object {

        val elementals = setOf(INCENDIARY, SHOCK, CORROSIVE, SLAG, CRYO)
    }
}

/**
 * Groups of entities that share the same strengths/weaknesses to the elements.
 */
enum class EffectivenessType(

    /**
     * All entity types that fall under this type.
     */
    val entityTypes: Set<EntityType>,

    /**
     * The damage multipliers for elemental attacks.
     */
    val damageMultipliers: Map<Elemental, Double>
) {

    /**
     * Entities that are made of flesh, but fall not in other categories.
     */
    FLESH(
        setOf(
            EntityType.DONKEY,
            EntityType.MULE,
            EntityType.EVOKER,
            EntityType.VINDICATOR,
            EntityType.ILLUSIONER,
            EntityType.SPIDER,
            EntityType.CAVE_SPIDER,
            EntityType.SILVERFISH,
            EntityType.ENDER_DRAGON,
            EntityType.WITCH,
            EntityType.PIG,
            EntityType.SHEEP,
            EntityType.COW,
            EntityType.CHICKEN,
            EntityType.WOLF,
            EntityType.MOOSHROOM,
            EntityType.SNOW_GOLEM, /* Not flesh, but melts anyway */
            EntityType.OCELOT,
            EntityType.HORSE,
            EntityType.RABBIT,
            EntityType.POLAR_BEAR,
            EntityType.LLAMA,
            EntityType.VILLAGER,
            EntityType.CAT,
            EntityType.PANDA,
            EntityType.PILLAGER,
            EntityType.RAVAGER,
            EntityType.TRADER_LLAMA,
            EntityType.WANDERING_TRADER,
            EntityType.FOX,
            EntityType.GOAT,
            EntityType.FROG,
            EntityType.WARDEN,
            EntityType.CAMEL,
            EntityType.SNIFFER,
            EntityType.PLAYER,
        ),
        mapOf(
            Elemental.PHYSICAL to 1.0,
            Elemental.EXPLOSIVE to 1.0,
            Elemental.INCENDIARY to 1.5,
            Elemental.SHOCK to 0.6,
            Elemental.CORROSIVE to 0.8,
            Elemental.SLAG to 1.0,
            Elemental.CRYO to 1.0
        )
    ),

    /**
     * Swimming/wet entities.
     */
    FISH(
        setOf(
            EntityType.GUARDIAN,
            EntityType.ELDER_GUARDIAN,
            EntityType.SQUID,
            EntityType.COD,
            EntityType.SALMON,
            EntityType.PUFFERFISH,
            EntityType.TROPICAL_FISH,
            EntityType.DOLPHIN,
            EntityType.AXOLOTL,
            EntityType.GLOW_SQUID,
            EntityType.TADPOLE,
            EntityType.TURTLE
        ),
        mapOf(
            Elemental.PHYSICAL to 1.0,
            Elemental.EXPLOSIVE to 0.8,
            Elemental.INCENDIARY to 0.5,
            Elemental.SHOCK to 1.5,
            Elemental.CORROSIVE to 1.0,
            Elemental.SLAG to 1.0,
            Elemental.CRYO to 1.5
        )
    ),

    /**
     * Flying entities.
     */
    BIRD(
        setOf(
            EntityType.BAT,
            EntityType.PARROT,
            EntityType.PHANTOM,
            EntityType.BEE,
            //EntityType.BREEZE,
        ),
        mapOf(
            Elemental.PHYSICAL to 1.0,
            Elemental.EXPLOSIVE to 1.0,
            Elemental.INCENDIARY to 1.5,
            Elemental.SHOCK to 1.5,
            Elemental.CORROSIVE to 0.8,
            Elemental.SLAG to 1.0,
            Elemental.CRYO to 1.0
        )
    ),

    /**
     * Mystical/magical creatures, like creepers.
     */
    NON_FLESH(
        setOf(
            EntityType.VEX,
            EntityType.CREEPER,
            EntityType.SLIME,
            EntityType.GHAST,
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.SHULKER,
            EntityType.ALLAY,
        ),
        mapOf(
            Elemental.PHYSICAL to 1.0,
            Elemental.EXPLOSIVE to 1.0,
            Elemental.INCENDIARY to 1.0,
            Elemental.SHOCK to 1.0,
            Elemental.CORROSIVE to 1.0,
            Elemental.SLAG to 1.0,
            Elemental.CRYO to 0.8
        )
    ),

    /**
     * Flesh creatures that live in the nether and are accustomed to heat.
     */
    FLESH_NETHER(
        setOf(
            EntityType.ZOMBIFIED_PIGLIN,
            EntityType.BLAZE,
            EntityType.MAGMA_CUBE,
            EntityType.HOGLIN,
            EntityType.PIGLIN,
            EntityType.STRIDER,
            EntityType.ZOGLIN,
            EntityType.PIGLIN_BRUTE,
        ),
        mapOf(
            Elemental.PHYSICAL to 1.0,
            Elemental.EXPLOSIVE to 1.0,
            Elemental.INCENDIARY to 0.5,
            Elemental.SHOCK to 1.2,
            Elemental.CORROSIVE to 1.0,
            Elemental.SLAG to 1.0,
            Elemental.CRYO to 1.5
        )
    ),

    /**
     * Undead entities.
     */
    UNDEAD(
        setOf(
            EntityType.WITHER_SKELETON,
            EntityType.STRAY,
            EntityType.HUSK,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.SKELETON_HORSE,
            EntityType.ZOMBIE_HORSE,
            EntityType.SKELETON,
            EntityType.GIANT,
            EntityType.ZOMBIE,
            EntityType.WITHER,
            EntityType.IRON_GOLEM,
            EntityType.DROWNED,
        ),
        mapOf(
            Elemental.PHYSICAL to 1.0,
            Elemental.EXPLOSIVE to 1.0,
            Elemental.INCENDIARY to 0.5,
            Elemental.SHOCK to 1.0,
            Elemental.CORROSIVE to 1.5,
            Elemental.SLAG to 1.0,
            Elemental.CRYO to 1.0
        )
    ),

    /**
     * Entities wearing armor.
     * At 20 armor points the full penalty must be applied, at 1 armor point only 5% of the penalty must be applied.
     */
    ARMORED(
        emptySet() /* depends on armor points */,
        mapOf(
            Elemental.PHYSICAL to 0.8,
            Elemental.EXPLOSIVE to 0.8,
            Elemental.INCENDIARY to 0.8,
            Elemental.SHOCK to 2.0,
            Elemental.CORROSIVE to 1.2,
            Elemental.SLAG to 1.0,
            Elemental.CRYO to 1.0
        )
    )
    ;

    fun damageMultiplier(elemental: Elemental, armorPoints: Int = 0): Double {
        val baseMultiplier = damageMultipliers[elemental] ?: 1.0

        if (this == ARMORED) {
            val difference = baseMultiplier - 1.0
            val scaledDifference = (armorPoints / 20.0) * difference
            return 1.0 + scaledDifference
        }
        else {
            return baseMultiplier
        }
    }

    companion object {

        fun typeOf(entityType: EntityType) = entries.first { entityType in it.entityTypes }
    }
}