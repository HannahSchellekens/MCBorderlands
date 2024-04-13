package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.ManufacturerTable
import maliwan.mcbl.loot.toUniformLootPool
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
    manufacturers: Set<Manufacturer> = Manufacturer.pistolProducers,

    /**
     * The random object to use to generate random guns.
     */
    val random: Random = Random()

) : WeaponAssemblyGenerator {

    private val manufacturerPool = Manufacturer.pistolProducers.toUniformLootPool().retainResults(manufacturers)

    override fun generate(rarity: Rarity): PistolAssembly {
        val manufacturer = manufacturerPool.roll(random)
        val barrel = PistolParts.Barrel.commonLootPool.roll(random)
        val grip = PistolParts.Grip.commonLootPool.roll(random)
        val accessory = if (random.nextDouble() < AccessoryTable.chanceByRarity(rarity)) {
            accessoryPool(manufacturer).toUniformLootPool().roll(random)
        }
        else null
        val capacitor = capacitorLootpool(manufacturer, WeaponClass.PISTOL).roll(random).nullIfPhysical

        return PistolAssembly(manufacturer, barrel, grip, accessory, capacitor)
    }

    private fun accessoryPool(manufacturer: Manufacturer) = when (manufacturer) {
        Manufacturer.JAKOBS -> PistolParts.Accessory.commonJakobsAccessories
        else -> PistolParts.Accessory.commonAccessories
    }
}