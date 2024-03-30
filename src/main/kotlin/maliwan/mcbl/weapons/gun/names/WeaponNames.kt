package maliwan.mcbl.weapons.gun.names

import maliwan.mcbl.weapons.gun.PistolAssembly
import maliwan.mcbl.weapons.gun.WeaponAssembly

/**
 * @author Hannah Schellekens
 */
object WeaponNames {

    fun nameOf(weaponAssembly: WeaponAssembly) = when (weaponAssembly) {
        is PistolAssembly -> PistolNames.nameOf(
            weaponAssembly.manufacturer,
            weaponAssembly.barrel,
            weaponAssembly.accessory,
            weaponAssembly.capacitor
        )
    }
}