package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.toUniformLootPool
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.ShotgunAssembly
import maliwan.mcbl.weapons.gun.parts.PistolParts
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import java.util.*

/**
 * @author Hannah Schellekens
 */
open class ShotgunAssemblyGenerator(

    /**
     * Possible manufacturers for the generated shotgun.
     */
    manufacturers: Set<Manufacturer> = Manufacturer.shotgunProducers,

    /**
     * The random object to use to generate random guns.
     */
    val random: Random = Random()

) : WeaponAssemblyGenerator {

    private val manufacturerPool = Manufacturer.shotgunProducers.toUniformLootPool().retainResults(manufacturers)

    override fun generate(rarity: Rarity): ShotgunAssembly {
        val manufacturer = manufacturerPool.roll(random)

        val barrel = if (rarity == Rarity.LEGENDARY) {
            ShotgunParts.Barrel.commonBarrels.filter { it.manufacturer == manufacturer }.random()
        }
        else ShotgunParts.Barrel.commonLootPool.roll(random)

        val grip = ShotgunParts.Grip.commonLootPool.roll(random)
        val stock = ShotgunParts.Stock.commonLootPool.roll(random)
        val accessory = if (random.nextDouble() < AccessoryTable.chanceByRarity(rarity)) {
            ShotgunParts.Accessory.commonLootPool.roll(random)
        }
        else null
        val capacitor = capacitorLootpool(manufacturer, WeaponClass.SHOTGUN).roll(random)?.nullIfPhysical

        return ShotgunAssembly(manufacturer, barrel, grip, stock, accessory, capacitor)
    }
}