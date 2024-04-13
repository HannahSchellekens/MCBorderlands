package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.ManufacturerTable
import maliwan.mcbl.loot.toUniformLootPool
import maliwan.mcbl.weapons.gun.PistolAssembly
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.ShotgunAssembly
import maliwan.mcbl.weapons.gun.SniperAssembly
import maliwan.mcbl.weapons.gun.names.SniperNames
import maliwan.mcbl.weapons.gun.parts.PistolParts
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import maliwan.mcbl.weapons.gun.parts.SniperParts
import java.util.Random

/**
 * @author Hannah Schellekens
 */
open class SniperAssemblyGenerator(

    /**
     * Possible manufacturers for the generated sniper.
     */
    manufacturers: Set<Manufacturer> = Manufacturer.sniperProducers,

    /**
     * The random object to use to generate random guns.
     */
    val random: Random = Random()

) : WeaponAssemblyGenerator {

    private val manufacturerPool = Manufacturer.sniperProducers.toUniformLootPool().retainResults(manufacturers)

    override fun generate(rarity: Rarity): SniperAssembly {
        val manufacturer = manufacturerPool.roll(random)

        val barrel = if (rarity == Rarity.LEGENDARY) {
            SniperParts.Barrel.commonBarrels.filter { it.manufacturer == manufacturer }.random()
        }
        else SniperParts.Barrel.commonLootPool.roll(random)

        val grip = SniperParts.Grip.commonLootPool.roll(random)
        val stock = SniperParts.Stock.commonLootPool.roll(random)
        val accessory = if (random.nextDouble() < AccessoryTable.chanceByRarity(rarity)) {
            SniperParts.Accessory.commonLootPool.roll(random)
        }
        else null
        val capacitor = capacitorLootpool(manufacturer, WeaponClass.SNIPER).roll(random)?.nullIfPhysical

        return SniperAssembly(manufacturer, barrel, grip, stock, accessory, capacitor)
    }
}