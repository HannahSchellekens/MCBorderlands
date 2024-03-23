package maliwan.mcbl.weapons

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import net.md_5.bungee.api.ChatColor

/**
 * @author Hannah Schellekens
 */
interface Elemental {

    val id: String
    val displayName: String
    val colourPrefix: String
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
    override val colourPrefix: String
) : Elemental {

    NONE("", ChatColor.WHITE.toString()),
    EXPLOSIVE("Explosive", ChatColor.YELLOW.toString()),
    INCENDIARY("Incendiary", ChatColor.GOLD.toString()),
    SHOCK("Shock", ChatColor.BLUE.toString()),
    CORROSIVE("Corrosive", ChatColor.GREEN.toString()),
    SLAG("Slag", ChatColor.DARK_PURPLE.toString())
    ;

    override val id: String
        get() = name
}