package maliwan.mcbl.weapons

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.ChatColor

/**
 * @author Hannah Schellekens
 */
interface Rarity {

    val id: String
    val displayName: String
    val colourPrefix: String
}

/**
 * @author Hannah Schellekens
 */
object RarityTypeAdapter : TypeAdapter<Rarity>() {

    override fun write(writer: JsonWriter, elemental: Rarity) {
        writer.value(elemental.id)
    }

    override fun read(reader: JsonReader): Rarity {
        return Rarities.valueOf(reader.nextString())
    }
}

/**
 * @author Hannah Schellekens
 */
enum class Rarities(
    override val displayName: String,
    override val colourPrefix: String
) : Rarity {

    COMMON("Common", ChatColor.WHITE.toString()),
    UNCOMMON("Uncommon", ChatColor.GREEN.toString()),
    RARE("Rare", ChatColor.BLUE.toString()),
    EPIC("Epic", ChatColor.LIGHT_PURPLE.toString()),
    LEGENDARY("Legendary", ChatColor.GOLD.toString()),
    PEARLESCENT("Pearlescent", ChatColor.AQUA.toString())
    ;

    override val id: String
        get() = name
}