package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.ManufacturerTable
import maliwan.mcbl.weapons.gun.PistolAssembly
import maliwan.mcbl.weapons.Manufacturers
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.gun.PistolParts
import java.util.Random

/**
 * @author Hannah Schellekens
 */
open class PistolGenerator(

    /**
     * Possible manufacturers for the generated pistol.
     */
    manufacturers: Set<Manufacturers> = defaultManufacturers,

    /**
     * The random object to use to generate random guns.
     */
    val random: Random = Random()

) : WeaponGenerator{

    private val manufacturerPool = ManufacturerTable.Weapons.generation.retainResults(manufacturers)

    override fun generate(rarity: Rarity): PistolAssembly {
        val manufacturer = manufacturerPool.roll(random)
        val barrel = PistolParts.Barrel.commonLootPool.roll(random)
        val grip = PistolParts.Grip.commonLootPool.roll(random)
        val accessory = if (random.nextDouble() < AccessoryTable.chanceByRarity(rarity)) {
            PistolParts.Accessory.commonLootPool.roll(random)
        }
        else null

        return PistolAssembly(manufacturer, barrel, grip, accessory)
    }

    companion object {

        val defaultManufacturers = setOf(
            Manufacturers.MALIWAN,
            Manufacturers.BANDIT,
            Manufacturers.DAHL,
            Manufacturers.HYPERION,
            Manufacturers.JAKOBS,
            Manufacturers.TEDIORE,
            Manufacturers.TORGUE,
            Manufacturers.VLADOF,
        )
    }
}