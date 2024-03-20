package maliwan.mcbl.weapons

/**
 * @author Hannah Schellekens
 */
interface WeaponClass {

    val displayName: String
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
}