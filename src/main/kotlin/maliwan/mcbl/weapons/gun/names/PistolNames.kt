package maliwan.mcbl.weapons.gun.names

import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
object PistolNames {

    private val nameTable = TabTable.fromResource(
        "/gun/names/pistol-names.csv",
        Manufacturer::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    private val accessoryTable = TabTable.fromResource(
        "/gun/names/pistol-prefixes.csv",
        PistolParts.Accessory::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    fun nameOf(
        manufacturer: Manufacturer,
        barrel: PistolParts.Barrel,
        accessory: PistolParts.Accessory? = null,
        capacitor: Capacitor? = null
    ): String {
        val customName = barrel.customBaseName() ?: accessory?.customBaseName() ?: capacitor?.customBaseName()
        val baseName = customName ?: nameTable[barrel.manufacturer, manufacturer]
            ?: throw IllegalArgumentException("No name found for manufacturer $manufacturer and barrel $barrel")

        val default = barrel.defaultPrefix() ?: capacitor?.defaultPrefix() ?: accessory?.defaultPrefix()
        val prefix = if (accessory != null) {
            accessoryTable[accessory, manufacturer]
                ?: throw IllegalArgumentException("No prefix found for manufacturer $manufacturer and accessory $accessory")
        }
        else if (default != null) {
            default
        }
        else if (capacitor != null) {
            WeaponNames.elementTable[capacitor.element, manufacturer]
                ?: throw IllegalArgumentException("No prefix found for manufacturer $manufacturer and capacitor $capacitor")
        }
        else ""

        return "$prefix $baseName".trim() // Trim to remove empty prefixes.
    }
}