package maliwan.mcbl.inventory

import maliwan.mcbl.loot.ammo.AmmoPack
import maliwan.mcbl.weapons.WeaponClass
import kotlin.math.max
import kotlin.math.min

/**
 * @author Hannah Schellekens
 */
open class AmmoInventory {

    /**
     * The maximum amount of ammo allowed for each weapon class.
     */
    val maxAmmo: MutableMap<WeaponClass, Int>  = mutableMapOf(
        // Defaults based on green lvl BL2 SDU amounts
        WeaponClass.ASSAULT_RIFLE to 560,
        WeaponClass.PISTOL to 400,
        WeaponClass.LAUNCHER to 18,
        WeaponClass.SHOTGUN to 120,
        WeaponClass.SMG to 720,
        WeaponClass.SNIPER to 72
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
     * Adds the contents of this ammo pack to the inventory.
     *
     * @param ammoPack
     *          Ammo to add to the inventory.
     */
    fun add(ammoPack: AmmoPack) {
        val current = ammo[ammoPack.weaponType] ?: error("No weapon type <${ammoPack.weaponType}> in inventory.")
        val limit = maxAmmo[ammoPack.weaponType] ?: error("No weapon type <${ammoPack.weaponType}> in inventory.")
        ammo[ammoPack.weaponType] = min(current + ammoPack.amount, limit)
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