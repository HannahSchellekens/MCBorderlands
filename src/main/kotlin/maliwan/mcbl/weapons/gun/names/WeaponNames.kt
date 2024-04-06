package maliwan.mcbl.weapons.gun.names

import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.gun.PistolAssembly
import maliwan.mcbl.weapons.gun.ShotgunAssembly
import maliwan.mcbl.weapons.gun.SniperAssembly
import maliwan.mcbl.weapons.gun.WeaponAssembly

/**
 * @author Hannah Schellekens
 */
object WeaponNames {

    val elementTable = TabTable.fromResource(
        "/gun/names/weapon-elements.csv",
        Elemental::valueOf,
        Manufacturer::valueOf,
        { it }
    )

    fun nameOf(weaponAssembly: WeaponAssembly) = when (weaponAssembly) {
        is PistolAssembly -> PistolNames.nameOf(
            weaponAssembly.manufacturer,
            weaponAssembly.barrel,
            weaponAssembly.accessory,
            weaponAssembly.capacitor
        )
        is ShotgunAssembly -> ShotgunNames.nameOf(
            weaponAssembly.manufacturer,
            weaponAssembly.barrel,
            weaponAssembly.accessory,
            weaponAssembly.capacitor
        )
        is SniperAssembly -> SniperNames.nameOf(
            weaponAssembly.manufacturer,
            weaponAssembly.barrel,
            weaponAssembly.accessory,
            weaponAssembly.capacitor
        )
    }
}