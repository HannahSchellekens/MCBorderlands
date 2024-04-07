package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.toUniformLootPool
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.SmgAssembly
import maliwan.mcbl.weapons.gun.parts.SmgParts
import java.util.*

/**
 * @author Hannah Schellekens
 */
open class SmgAssemblyGenerator(

    /**
     * Possible manufacturers for the generated smg.
     */
    manufacturers: Set<Manufacturer> = Manufacturer.smgProducers,

    /**
     * The random object to use to generate random guns.
     */
    val random: Random = Random()

) : WeaponAssemblyGenerator {

    private val manufacturerPool = Manufacturer.smgProducers.toUniformLootPool().retainResults(manufacturers)

    override fun generate(rarity: Rarity): SmgAssembly {
        val manufacturer = manufacturerPool.roll(random)
        val barrel = SmgParts.Barrel.commonLootPool.roll(random)
        val grip = SmgParts.Grip.commonLootPool.roll(random)
        val stock = SmgParts.Stock.commonLootPool.roll(random)
        val accessory = if (random.nextDouble() < AccessoryTable.chanceByRarity(rarity)) {
            SmgParts.Accessory.commonLootPool.roll(random)
        }
        else null
        val capacitor = capacitorLootpool(manufacturer, WeaponClass.SMG).roll(random).nullIfPhysical

        return SmgAssembly(manufacturer, barrel, grip, stock, accessory, capacitor)
    }
}