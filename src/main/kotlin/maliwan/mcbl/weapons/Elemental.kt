package maliwan.mcbl.weapons

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import net.md_5.bungee.api.ChatColor
import org.bukkit.Color
import org.bukkit.entity.EntityType

/**
 * @author Hannah Schellekens
 */
interface Elemental {

    val id: String
    val displayName: String
    val colourPrefix: String
    val color: Color
}

/**
 * @author Hannah Schellekens
 */
object ElementalTypeAdapter : TypeAdapter<Elemental>() {

    override fun write(writer: JsonWriter, elemental: Elemental) {
        writer.value(elemental.id)
    }

    override fun read(reader: JsonReader): Elemental {
        return Elements.valueOf(reader.nextString())
    }
}

/**
 * @author Hannah Schellekens
 */
enum class Elements(
    override val displayName: String,
    override val colourPrefix: String,
    override val color: Color
) : Elemental {

    PHYSICAL("", ChatColor.WHITE.toString(), Color.WHITE),
    EXPLOSIVE("Explosive", ChatColor.YELLOW.toString(), Color.YELLOW),
    INCENDIARY("Incendiary", ChatColor.GOLD.toString(), Color.ORANGE),
    SHOCK("Shock", ChatColor.BLUE.toString(), Color.fromRGB(37, 150, 190)),
    CORROSIVE("Corrosive", ChatColor.GREEN.toString(), Color.LIME),
    SLAG("Slag", ChatColor.DARK_PURPLE.toString(), Color.PURPLE)
    ;

    override val id: String
        get() = name
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
            EntityType.MUSHROOM_COW,
            EntityType.SNOWMAN, /* Not flesh, but melts anyway */
            EntityType.OCELOT,
            EntityType.HORSE,
            EntityType.RABBIT,
            EntityType.POLAR_BEAR,
            EntityType.LLAMA,
            EntityType.VILLAGER,
            EntityType.TURTLE,
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
            Elements.PHYSICAL to 1.0,
            Elements.EXPLOSIVE to 1.0,
            Elements.INCENDIARY to 1.5,
            Elements.SHOCK to 0.6,
            Elements.CORROSIVE to 0.8,
            Elements.SLAG to 1.0,
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
        ),
        mapOf(
            Elements.PHYSICAL to 1.0,
            Elements.EXPLOSIVE to 0.8,
            Elements.INCENDIARY to 0.5,
            Elements.SHOCK to 1.5,
            Elements.CORROSIVE to 1.0,
            Elements.SLAG to 1.0,
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
            Elements.PHYSICAL to 1.0,
            Elements.EXPLOSIVE to 1.0,
            Elements.INCENDIARY to 1.5,
            Elements.SHOCK to 1.5,
            Elements.CORROSIVE to 0.8,
            Elements.SLAG to 1.0,
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
            Elements.PHYSICAL to 1.0,
            Elements.EXPLOSIVE to 1.0,
            Elements.INCENDIARY to 1.0,
            Elements.SHOCK to 1.0,
            Elements.CORROSIVE to 1.0,
            Elements.SLAG to 1.0,
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
            Elements.PHYSICAL to 1.0,
            Elements.EXPLOSIVE to 1.0,
            Elements.INCENDIARY to 0.5,
            Elements.SHOCK to 1.2,
            Elements.CORROSIVE to 1.0,
            Elements.SLAG to 1.0,
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
            Elements.PHYSICAL to 1.0,
            Elements.EXPLOSIVE to 1.0,
            Elements.INCENDIARY to 0.5,
            Elements.SHOCK to 1.0,
            Elements.CORROSIVE to 1.5,
            Elements.SLAG to 1.0,
        )
    ),

    /**
     * Entities wearing armor.
     * At 20 armor points the full penalty must be applied, at 1 armor point only 5% of the penalty must be applied.
     */
    ARMORED(
        emptySet() /* depends on armor points */,
        mapOf(
            Elements.PHYSICAL to 0.8,
            Elements.EXPLOSIVE to 0.8,
            Elements.INCENDIARY to 0.8,
            Elements.SHOCK to 2.0,
            Elements.CORROSIVE to 1.2,
            Elements.SLAG to 1.0,
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