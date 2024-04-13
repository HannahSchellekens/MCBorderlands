package maliwan.mcbl.weapons.gun.names

import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.LauncherParts

/**
 * @author Hannah Schellekens
 */
object LauncherNames {

    private val nameTable = TabTable.fromResource(
        "/gun/names/launcher-names.csv",
        Manufacturer::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    private val accessoryTable = TabTable.fromResource(
        "/gun/names/launcher-prefixes.csv",
        LauncherParts.Accessory::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    private val maliwanElementTable = TabTable.fromResource(
        "/gun/names/launcher-maliwan-elements.csv",
        Elemental::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    fun nameOf(
        manufacturer: Manufacturer,
        barrel: LauncherParts.Barrel,
        accessory: LauncherParts.Accessory? = null,
        capacitor: Capacitor? = null
    ): String {
        val customName = barrel.customBaseName() ?: accessory?.customBaseName() ?: capacitor?.customBaseName()
        val baseName = customName ?: nameTable[barrel.manufacturer, manufacturer]
            ?: throw IllegalArgumentException("No name found for manufacturer $manufacturer and barrel $barrel")

        val prefix = if (accessory != null) {
            accessoryTable[accessory, manufacturer]
                ?: throw IllegalArgumentException("No prefix found for manufacturer $manufacturer and accessory $accessory")
        }
        // Maliwan has special prefixes for elements for launchers. Because they all start with P.
        else if (capacitor != null && manufacturer == Manufacturer.MALIWAN) {
            maliwanElementTable[capacitor.element, Manufacturer.MALIWAN]
                ?: throw IllegalArgumentException("No maliwan elemental prefix found for launcher with capacitor $capacitor")
        }
        else if (capacitor != null) {
            WeaponNames.elementTable[capacitor.element, manufacturer]
                ?: throw IllegalArgumentException("No prefix found for manufacturer $manufacturer and capacitor $capacitor")
        }
        else ""

        return "$prefix $baseName".trim() // Trim to remove empty prefixes.
    }
}