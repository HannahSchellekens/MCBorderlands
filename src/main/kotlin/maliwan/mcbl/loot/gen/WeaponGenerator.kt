package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.LootPool
import maliwan.mcbl.weapons.*
import maliwan.mcbl.weapons.gun.WeaponAssembly

/**
 * @author Hannah Schellekens
 */
interface WeaponGenerator {

    /**
     * Generates a new weapon.
     */
    fun generate(rarity: Rarity): WeaponAssembly
}

/**
 * From which loot pool to determine the elemental capacitor.
 */
fun capacitorLootpool(manufacturer: Manufacturer, weaponClass: WeaponClass = WeaponClass.PISTOL): LootPool<Elemental> {
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