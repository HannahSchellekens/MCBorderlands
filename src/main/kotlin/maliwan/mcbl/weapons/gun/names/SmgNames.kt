package maliwan.mcbl.weapons.gun.names

import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.gun.Capacitor
import maliwan.mcbl.weapons.gun.parts.SmgParts
import maliwan.mcbl.weapons.gun.parts.SniperParts

/**
 * @author Hannah Schellekens
 */
object SmgNames {

    private val nameTable = TabTable.fromResource(
        "/gun/names/smg-names.csv",
        Manufacturer::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    private val accessoryTable = TabTable.fromResource(
        "/gun/names/smg-prefixes.csv",
        SmgParts.Accessory::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    /**
     * SMG names differ from name based on the element of the maliwan barrel.
     * The names in the .csv are separated by |, this is the index in this |-separated list for this elemental type.
     */
    private val Elemental?.maliwanBaseNameIndex: Int
        get() = when (this) {
            Elemental.INCENDIARY -> 1
            Elemental.SHOCK -> 2
            Elemental.CORROSIVE -> 3
            Elemental.SLAG -> 4
            else -> 0
        }

    fun nameOf(
        manufacturer: Manufacturer,
        barrel: SmgParts.Barrel,
        accessory: SmgParts.Accessory? = null,
        capacitor: Capacitor? = null
    ): String {
        val candidateBaseNames = nameTable[barrel.manufacturer, manufacturer]?.split("|")
            ?: throw IllegalArgumentException("No name found for manufacturer $manufacturer and barrel $barrel")

        // Maliwan Barrel has 5 different names, based on the elemental type.
        val index = capacitor?.element.maliwanBaseNameIndex
        val baseName = if (capacitor != null && barrel == SmgParts.Barrel.MALIWAN) {
            candidateBaseNames[index]
        }
        else candidateBaseNames.first()

        val prefix = if (accessory != null) {
            accessoryTable[accessory, manufacturer]
                ?: throw IllegalArgumentException("No prefix found for manufacturer $manufacturer and accessory $accessory")
        }
        else if (capacitor != null && barrel != SmgParts.Barrel.MALIWAN /* Maliwan barrels have element baked in base name */) {
            WeaponNames.elementTable[capacitor.element, manufacturer]
                ?: throw IllegalArgumentException("No prefix found for manufacturer $manufacturer and capacitor $capacitor")
        }
        else ""

        return "$prefix $baseName".trim() // Trim to remove empty prefixes.
    }
}