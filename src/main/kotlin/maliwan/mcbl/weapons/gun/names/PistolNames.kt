package maliwan.mcbl.weapons.gun.names

import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.Elements
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.Manufacturers
import maliwan.mcbl.weapons.gun.PistolParts
import java.io.InputStreamReader

/**
 * @author Hannah Schellekens
 */
object PistolNames {

    /**
     * Maps `(Manufacturer -> (Barrel -> String/name))`.
     */
    private val nameMap: Map<Manufacturer, Map<PistolParts.Barrel, String>>

    /**
     * Maps `(Manufacturer -> (Accessory -> Prefix))`.
     */
    private val accessoryMap: Map<Manufacturer, Map<PistolParts.Accessory, String>>

    /**
     * Maps `(Manufacturer -> (Elemental -> Prefix))`.
     */
    private val elementalMap: Map<Manufacturer, Map<Elemental, String>>

    init {
        // Read input and initialize results.
        val pistolNames = javaClass.getResourceAsStream("/gun/names/pistol/pistol-names.csv")
            ?: error("Pistol name csv not found")
        val pistolLines = InputStreamReader(pistolNames).readLines()
        val nameMap = HashMap<Manufacturer, Map<PistolParts.Barrel, String>>()

        val prefixNames = javaClass.getResourceAsStream("/gun/names/pistol/pistol-prefixes.csv")
            ?: error("Pistol prefix csv not found")
        val prefixLines = InputStreamReader(prefixNames).readLines()
        val prefixMap = HashMap<Manufacturer, Map<PistolParts.Accessory, String>>()

        val elementalNames = javaClass.getResourceAsStream("/gun/names/pistol/pistol-elements.csv")
            ?: error("Pistol prefix csv not found")
        val elementalLines = InputStreamReader(elementalNames).readLines()
        val elementalMap = HashMap<Manufacturer, Map<Elemental, String>>()

        // Read parts.
        val barrels = pistolLines[0].split("\t").drop(1).map { PistolParts.Barrel.valueOf(it) }
        val accessories = prefixLines[0].split("\t").drop(1).map { PistolParts.Accessory.valueOf(it) }
        val elements = elementalLines[0].split("\t").drop(1).map { Elements.valueOf(it) }

        // Process base names.
        for (i in 1 until pistolLines.size) {
            val line = pistolLines[i].split("\t")
            val manufacturer = Manufacturers.valueOf(line[0])
            val barrelToNameMap = HashMap<PistolParts.Barrel, String>()
            for (j in 1 until line.size) {
                barrelToNameMap[barrels[j - 1]] = line[j]
            }
            nameMap[manufacturer] = barrelToNameMap
        }
        this.nameMap = nameMap

        // Process accessory names.
        for (i in 1 until prefixLines.size) {
            val line = prefixLines[i].split("\t")
            val manufacturer = Manufacturers.valueOf(line[0])
            val accessoryToNameMap = HashMap<PistolParts.Accessory, String>()
            for (j in 1 until line.size) {
                accessoryToNameMap[accessories[j - 1]] = line[j]
            }
            prefixMap[manufacturer] = accessoryToNameMap
        }
        this.accessoryMap = prefixMap

        // Process elemental names.
        for (i in 1 until elementalLines.size) {
            val line = elementalLines[i].split("\t")
            val manufacturer = Manufacturers.valueOf(line[0])
            val elementToNameMap = HashMap<Elemental, String>()
            for (j in 1 until line.size) {
                elementToNameMap[elements[j - 1]] = line[j]
            }
            elementalMap[manufacturer] = elementToNameMap
        }
        this.elementalMap = elementalMap
    }

    fun nameOf(
        manufacturer: Manufacturer,
        barrel: PistolParts.Barrel,
        accessory: PistolParts.Accessory? = null,
        elemental: Elemental? = null
    ): String {
        val baseName = nameMap[manufacturer]?.get(barrel)
            ?: throw IllegalArgumentException("No name found for manufacturer $manufacturer and barrel $barrel")

        val prefix = if (accessory != null) {
            accessoryMap[manufacturer]?.get(accessory)
                ?: throw IllegalArgumentException("No prefix found for manufacturer $manufacturer and accessory $accessory")
        }
        else if (elemental != null) {
            elementalMap[manufacturer]?.get(elemental)
                ?: throw IllegalArgumentException("No prefix found for manufacturer $manufacturer and element $elemental")
        }
        else ""

        return "$prefix $baseName".trim() // Trim to remove empty prefixes.
    }
}