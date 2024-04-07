package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.toUniformLootPool
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.LauncherAssembly
import maliwan.mcbl.weapons.gun.SmgAssembly
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import maliwan.mcbl.weapons.gun.parts.SmgParts
import java.util.*

/**
 * @author Hannah Schellekens
 */
open class LauncherAssemblyGenerator(

    /**
     * Possible manufacturers for the generated launcher.
     */
    manufacturers: Set<Manufacturer> = Manufacturer.launcherProducers,

    /**
     * The random object to use to generate random guns.
     */
    val random: Random = Random()

) : WeaponAssemblyGenerator {

    private val manufacturerPool = Manufacturer.launcherProducers.toUniformLootPool().retainResults(manufacturers)

    override fun generate(rarity: Rarity): LauncherAssembly {
        val manufacturer = manufacturerPool.roll(random)
        val barrel = LauncherParts.Barrel.commonLootPool.roll(random)
        val grip = LauncherParts.Grip.commonLootPool.roll(random)
        val exhaust = LauncherParts.Exhaust.commonLootPool.roll(random)
        val accessory = if (random.nextDouble() < AccessoryTable.chanceByRarity(rarity)) {
            LauncherParts.Accessory.commonLootPool.roll(random)
        }
        else null
        val capacitor = capacitorLootpool(manufacturer, WeaponClass.LAUNCHER).roll(random).nullIfPhysical

        return LauncherAssembly(manufacturer, barrel, grip, exhaust, accessory, capacitor)
    }
}