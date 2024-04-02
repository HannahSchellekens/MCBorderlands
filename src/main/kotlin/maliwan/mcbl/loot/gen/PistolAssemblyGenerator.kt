package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.ManufacturerTable
import maliwan.mcbl.weapons.gun.PistolAssembly
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.parts.PistolParts
import java.util.Random

/**
 * @author Hannah Schellekens
 */
open class PistolAssemblyGenerator(

    /**
     * Possible manufacturers for the generated pistol.
     */
    manufacturers: Set<Manufacturer> = defaultManufacturers,

    /**
     * The random object to use to generate random guns.
     */
    val random: Random = Random()

) : WeaponAssemblyGenerator {

    private val manufacturerPool = ManufacturerTable.Weapons.generation.retainResults(manufacturers)

    override fun generate(rarity: Rarity): PistolAssembly {
        val manufacturer = manufacturerPool.roll(random)
        val barrel = PistolParts.Barrel.commonLootPool.roll(random)
        val grip = PistolParts.Grip.commonLootPool.roll(random)
        val accessory = if (random.nextDouble() < AccessoryTable.chanceByRarity(rarity)) {
            PistolParts.Accessory.commonLootPool.roll(random)
        }
        else null
        val capacitor = capacitorLootpool(manufacturer, WeaponClass.PISTOL).roll(random).nullIfPhysical

        return PistolAssembly(manufacturer, barrel, grip, accessory, capacitor)
    }

    companion object {

        val defaultManufacturers = setOf(
            Manufacturer.MALIWAN,
            Manufacturer.BANDIT,
            Manufacturer.DAHL,
            Manufacturer.HYPERION,
            Manufacturer.JAKOBS,
            Manufacturer.TEDIORE,
            Manufacturer.TORGUE,
            Manufacturer.VLADOF,
        )
    }
}