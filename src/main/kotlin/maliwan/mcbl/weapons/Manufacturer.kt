package maliwan.mcbl.weapons

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * @author Hannah Schellekens
 */
interface Manufacturer {

    val id: String
    val displayName: String
}

/**
 * @author Hannah Schellekens
 */
object ManufacturerTypeAdapter : TypeAdapter<Manufacturer>() {

    override fun write(writer: JsonWriter, elemental: Manufacturer) {
        writer.value(elemental.id)
    }

    override fun read(reader: JsonReader): Manufacturer {
        return Manufacturers.valueOf(reader.nextString())
    }
}

/**
 * @author Hannah Schellekens
 */
enum class Manufacturers(
    override val displayName: String
) : Manufacturer {

    MALIWAN("Maliwan"),
    S_AND_S_MUNITIONS("S&S Munitions"),
    ATLAS("Atlas"),
    HYPERION("Hyperion"),
    VLADOF("Vladof"),
    TEDIORE("Tediore"),
    BANDIT("Bandit"),
    DAHL("Dahl"),
    TORGUE("TORGUE"),
    ERIDIAN("Eridian"),
    JAKOBS("Jakobs"),
    ANSHIN("Anshin"),
    PANGOLIN("Pangolin")
    ;

    override val id: String
        get() = name
}