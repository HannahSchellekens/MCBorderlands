package maliwan.mcbl.weapons.gun.names

import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.gun.*

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

    fun nameOf(weaponAssembly: WeaponAssembly) = with(weaponAssembly) {
        when (this) {
            is PistolAssembly -> PistolNames.nameOf(manufacturer, barrel, accessory, capacitor)
            is ShotgunAssembly -> ShotgunNames.nameOf(manufacturer, barrel, accessory, capacitor)
            is SniperAssembly -> SniperNames.nameOf(manufacturer, barrel, accessory, capacitor)
            is SmgAssembly -> SmgNames.nameOf(manufacturer, barrel, accessory, capacitor)
            is AssaultRifleAssembly -> AssaultRifleNames.nameOf(manufacturer, barrel, accessory, capacitor)
        }
    }
}