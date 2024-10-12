package maliwan.mcbl.weapons.gun.names

import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.CustomBaseNameProvider
import maliwan.mcbl.weapons.gun.behaviour.DefaultPrefixProvider
import maliwan.mcbl.weapons.gun.behaviour.GunBehaviour
import maliwan.mcbl.weapons.gun.behaviour.forEachType
import maliwan.mcbl.weapons.gun.parts.Capacitor

/**
 * @author Hannah Schellekens
 */
object WeaponNames {

    val elementTable = TabTable.fromResource(
        "/gun/names/weapon-elements.csv",
        Elemental::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    fun nameOf(weaponAssembly: WeaponAssembly) = with(weaponAssembly) {
        when (this) {
            is PistolAssembly -> PistolNames.nameOf(manufacturer, barrel, accessory, capacitor)
            is ShotgunAssembly -> ShotgunNames.nameOf(manufacturer, barrel, accessory, capacitor)
            is SniperAssembly -> SniperNames.nameOf(manufacturer, barrel, accessory, capacitor, grip)
            is SmgAssembly -> SmgNames.nameOf(manufacturer, barrel, accessory, capacitor, grip)
            is AssaultRifleAssembly -> AssaultRifleNames.nameOf(manufacturer, barrel, accessory, capacitor, grip.customBaseName())
            is LauncherAssembly -> LauncherNames.nameOf(manufacturer, barrel, accessory, capacitor)
        }
    }
}

/**
 * Checks this weapon part provides a custom base name.
 * Returns this name, or `null` when there is no custom base name.
 */
fun WeaponPart.customBaseName() = customBaseName(behaviours)

/**
 * Checks this capacitor provides a custom base name.
 * Returns this name, or `null` when there is no custom base name.
 */
fun Capacitor.customBaseName() = customBaseName(behaviours)

/**
 * Checks if one of these gun behaviours provides a custom base name.
 * Returns this name, or `null` when there is no custom base name.
 */
fun customBaseName(behaviours: List<GunBehaviour>): String? {
    val names = ArrayList<String>()
    behaviours.forEachType<CustomBaseNameProvider> {
        names += it.baseName
    }
    return if (names.isEmpty()) {
        null
    }
    else names.joinToString(" ")
}

/**
 * Checks this weapon part provides a default prefix.
 * Returns this name, or `null` when there is no default prefix.
 */
fun WeaponPart.defaultPrefix() = defaultPrefix(behaviours)

/**
 * Checks this capacitor provides a default prefix.
 * Returns this name, or `null` when there is no default prefix.
 */
fun Capacitor.defaultPrefix() = defaultPrefix(behaviours)

/**
 * Checks if one of these gun behaviours provides a default gun prefix.
 * Returns this prefix, or `null` when there is no default prefix.
 */
fun defaultPrefix(behaviours: List<GunBehaviour>): String? {
    val names = ArrayList<String>()
    behaviours.forEachType<DefaultPrefixProvider> {
        names += it.defaultPrefix
    }
    return if (names.isEmpty()) {
        null
    }
    else names.joinToString(" ").trim().ifBlank { null }
}