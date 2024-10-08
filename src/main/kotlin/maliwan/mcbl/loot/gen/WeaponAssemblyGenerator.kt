package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.LootPool
import maliwan.mcbl.weapons.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.WeaponAssembly
import java.util.*

/**
 * @author Hannah Schellekens
 */
interface WeaponAssemblyGenerator {

    /**
     * Generates a new weapon.
     */
    fun generate(rarity: Rarity): WeaponAssembly

    companion object {

        /**
         * Create a new weapon assembler for a given weapon class
         */
        fun forType(
            weaponClass: WeaponClass,
            manufacturers: Set<Manufacturer>,
            random: Random = Random()
        ) = when (weaponClass) {
            WeaponClass.PISTOL -> PistolAssemblyGenerator(manufacturers, random)
            WeaponClass.SHOTGUN -> ShotgunAssemblyGenerator(manufacturers, random)
            WeaponClass.SNIPER -> SniperAssemblyGenerator(manufacturers, random)
            WeaponClass.SMG -> SmgAssemblyGenerator(manufacturers, random)
            WeaponClass.ASSAULT_RIFLE -> AssaultRifleAssemblyGenerator(manufacturers, random)
            WeaponClass.LAUNCHER -> LauncherAssemblyGenerator(manufacturers, random)
        }
    }
}

/**
 * From which loot pool to determine the elemental capacitor.
 */
fun capacitorLootpool(manufacturer: Manufacturer, weaponClass: WeaponClass = WeaponClass.PISTOL): LootPool<Capacitor> {
    return when (weaponClass) {
        WeaponClass.SNIPER -> when (manufacturer) {
            Manufacturer.MALIWAN -> CapacitorTable.elementOnly
            Manufacturer.JAKOBS -> CapacitorTable.elementNoneButExplosive
            Manufacturer.TORGUE -> CapacitorTable.elementNone /* Explosive will come from body */
            else -> CapacitorTable.elementAll
        }
        else -> when (manufacturer) {
            Manufacturer.MALIWAN -> CapacitorTable.elementOnly
            Manufacturer.JAKOBS -> CapacitorTable.elementNoneButExplosive
            Manufacturer.TORGUE -> CapacitorTable.elementNone /* Explosive will come from body */
            else -> CapacitorTable.elementSome
        }
    }
}