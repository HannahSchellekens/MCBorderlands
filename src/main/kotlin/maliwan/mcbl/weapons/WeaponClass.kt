package maliwan.mcbl.weapons

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/**
 * @author Hannah Schellekens
 */
enum class WeaponClass(
    val displayName: String
) {

    PISTOL("Pistol"),
    SHOTGUN("Shotgun"),
    ASSAULT_RIFLE("Assault Rifle"),
    SNIPER("Sniper"),
    SMG("SMG"),
    LAUNCHER("Launcher")
    ;
}