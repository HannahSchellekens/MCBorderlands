package maliwan.mcbl.weapons

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * @author Hannah Schellekens
 */
interface WeaponClass {

    val id: String
    val displayName: String
}

/**
 * @author Hannah Schellekens
 */
object WeaponClassTypeAdapter : TypeAdapter<WeaponClass>() {

    override fun write(writer: JsonWriter, elemental: WeaponClass) {
        writer.value(elemental.id)
    }

    override fun read(reader: JsonReader): WeaponClass {
        return WeaponClasses.valueOf(reader.nextString())
    }
}

/**
 * @author Hannah Schellekens
 */
enum class WeaponClasses(
    override val displayName: String
) : WeaponClass {

    PISTOL("Pistol"),
    SHOTGUN("Shotgun"),
    ASSAULT_RIFLE("Assault Rifle"),
    SNIPER("Sniper"),
    SMG("SMG"),
    LAUNCHER("Launcher")
    ;

    override val id: String
        get() = name
}