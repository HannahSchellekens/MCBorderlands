package maliwan.mcbl.loot.gen

import maliwan.mcbl.weapons.gun.WeaponAssembly
import maliwan.mcbl.weapons.Rarity

/**
 * @author Hannah Schellekens
 */
interface WeaponGenerator {

    /**
     * Generates a new weapon.
     */
    fun generate(rarity: Rarity): WeaponAssembly
}