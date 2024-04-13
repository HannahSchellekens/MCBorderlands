package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.ManufacturerTable
import maliwan.mcbl.loot.toUniformLootPool
import maliwan.mcbl.weapons.gun.PistolAssembly
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.AssaultRifleAssembly
import maliwan.mcbl.weapons.gun.ShotgunAssembly
import maliwan.mcbl.weapons.gun.SniperAssembly
import maliwan.mcbl.weapons.gun.names.SniperNames
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts
import maliwan.mcbl.weapons.gun.parts.PistolParts
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import maliwan.mcbl.weapons.gun.parts.SniperParts
import java.util.Random

/**
 * @author Hannah Schellekens
 */
open class AssaultRifleAssemblyGenerator(

    /**
     * Possible manufacturers for the generated assault rifle.
     */
    manufacturers: Set<Manufacturer> = Manufacturer.assaultRifleProducers,

    /**
     * The random object to use to generate random guns.
     */
    val random: Random = Random()

) : WeaponAssemblyGenerator {

    private val manufacturerPool = Manufacturer.assaultRifleProducers.toUniformLootPool().retainResults(manufacturers)

    override fun generate(rarity: Rarity): AssaultRifleAssembly {
        val manufacturer = manufacturerPool.roll(random)

        val barrel = if (rarity == Rarity.LEGENDARY) {
            AssaultRifleParts.Barrel.commonBarrels.filter { it.manufacturer == manufacturer }.random()
        }
        else AssaultRifleParts.Barrel.commonLootPool.roll(random)

        val grip = AssaultRifleParts.Grip.commonLootPool.roll(random)
        val stock = AssaultRifleParts.Stock.commonLootPool.roll(random)
        val accessory = if (random.nextDouble() < AccessoryTable.chanceByRarity(rarity)) {
            AssaultRifleParts.Accessory.commonLootPool.roll(random)
        }
        else null
        val capacitor = capacitorLootpool(manufacturer, WeaponClass.ASSAULT_RIFLE).roll(random).nullIfPhysical

        return AssaultRifleAssembly(manufacturer, barrel, grip, stock, accessory, capacitor)
    }
}