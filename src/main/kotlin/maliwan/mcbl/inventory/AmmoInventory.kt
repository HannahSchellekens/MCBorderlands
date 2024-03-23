package maliwan.mcbl.inventory

import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.WeaponClasses
import kotlin.math.max

/**
 * @author Hannah Schellekens
 */
open class AmmoInventory {

    /**
     * The maximum amount of ammo allowed for each weapon class.
     */
    val maxAmmo: MutableMap<WeaponClass, Int>  = mutableMapOf(
        // Defaults based on green lvl BL2 amounts
        WeaponClasses.ASSAULT_RIFLE to 420,
        WeaponClasses.PISTOL to 300,
        WeaponClasses.LAUNCHER to 15,
        WeaponClasses.SHOTGUN to 100,
        WeaponClasses.SMG to 540,
        WeaponClasses.SNIPER to 60
    )

    /**
     * The amount of ammo available for each class.
     */
    val ammo: MutableMap<WeaponClass, Int> = maxAmmo.toMutableMap()

    /**
     * Resets back to maximum values.
     */
    fun resetAmmo() {
        maxAmmo.forEach { (weaponClass, amount) ->
            ammo[weaponClass] = amount
        }
    }

    /**
     * Removes ammo from the inventory.
     */
    fun removeAmmo(weaponClass: WeaponClass, amount: Int) {
        ammo[weaponClass] = max(0, (ammo[weaponClass] ?: error("No weapon class $weaponClass in inventory")) - amount)
    }

    /**
     * Get the ammo amounts per weapon class.
     *
     * @param maxValue `true` for the max ammo value of this weapon class, `false` for the current state.
     */
    operator fun get(weaponClass: WeaponClass, maxValue: Boolean = false): Int {
        return if (maxValue) {
            maxAmmo[weaponClass] ?: error("No weapon class $weaponClass in inventory.")
        }
        else ammo[weaponClass] ?: error("No weapon class $weaponClass in inventory.")
    }
}