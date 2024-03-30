package maliwan.mcbl.weapons.gun.names

import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.gun.PistolParts

/**
 * @author Hannah Schellekens
 */
object PistolNames {

    private val nameTable = TabTable.fromResource(
        "/gun/names/pistol/pistol-names.csv",
        Manufacturer::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    private val accessoryTable = TabTable.fromResource(
        "/gun/names/pistol/pistol-prefixes.csv",
        PistolParts.Accessory::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    private val elementTable = TabTable.fromResource(
        "/gun/names/pistol/pistol-elements.csv",
        Elemental::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    fun nameOf(
        manufacturer: Manufacturer,
        barrel: PistolParts.Barrel,
        accessory: PistolParts.Accessory? = null,
        elemental: Elemental? = null
    ): String {
        val baseName = nameTable[barrel.manufacturer, manufacturer]
            ?: throw IllegalArgumentException("No name found for manufacturer $manufacturer and barrel $barrel")

        val prefix = if (accessory != null) {
            accessoryTable[accessory, manufacturer]
                ?: throw IllegalArgumentException("No prefix found for manufacturer $manufacturer and accessory $accessory")
        }
        else if (elemental != null) {
            elementTable[elemental, manufacturer]
                ?: throw IllegalArgumentException("No prefix found for manufacturer $manufacturer and element $elemental")
        }
        else ""

        return "$prefix $baseName".trim() // Trim to remove empty prefixes.
    }
}