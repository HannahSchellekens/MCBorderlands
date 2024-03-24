package maliwan.mcbl.weapons

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.ChatColor
import org.bukkit.Color

/**
 * @author Hannah Schellekens
 */
interface Rarity {

    val id: String
    val displayName: String
    val colourPrefix: String
    val color: Color
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
    override val colourPrefix: String,
    override val color: Color
) : Rarity {

    COMMON("Common", ChatColor.WHITE.toString(), Color.WHITE),
    UNCOMMON("Uncommon", ChatColor.GREEN.toString(), Color.fromRGB(5635925)),
    RARE("Rare", ChatColor.BLUE.toString(), Color.fromRGB(5592575)),
    EPIC("Epic", ChatColor.LIGHT_PURPLE.toString(), Color.fromRGB(16733695)),
    LEGENDARY("Legendary", ChatColor.GOLD.toString(), Color.fromRGB(16755200)),
    PEARLESCENT("Pearlescent", ChatColor.AQUA.toString(), Color.fromRGB(5636095))
    ;

    override val id: String
        get() = name
}