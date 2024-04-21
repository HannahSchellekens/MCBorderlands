package maliwan.mcbl.weapons.gun.names

import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts

/**
 * @author Hannah Schellekens
 */
object AssaultRifleNames {

    private val nameTable = TabTable.fromResource(
        "/gun/names/assault-rifle-names.csv",
        AssaultRifleParts.Barrel::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    private val accessoryTable = TabTable.fromResource(
        "/gun/names/assault-rifle-prefixes.csv",
        AssaultRifleParts.Accessory::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    fun nameOf(
        manufacturer: Manufacturer,
        barrel: AssaultRifleParts.Barrel,
        accessory: AssaultRifleParts.Accessory? = null,
        capacitor: Capacitor? = null
    ): String {
        val customName = barrel.customBaseName() ?: accessory?.customBaseName() ?: capacitor?.customBaseName()
        val baseName = customName ?: nameTable[barrel, manufacturer]
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