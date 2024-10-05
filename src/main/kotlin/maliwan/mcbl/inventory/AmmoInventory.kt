package maliwan.mcbl.inventory

import maliwan.mcbl.loot.ammo.AmmoPack
import maliwan.mcbl.weapons.WeaponClass
import org.bukkit.entity.Entity
import org.bukkit.inventory.InventoryHolder
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * @author Hannah Schellekens
 */
open class AmmoInventory(val owner: Entity) {

    /**
     * The amount of ammo available for each class.
     */
    val ammo: MutableMap<WeaponClass, Int> = EnumMap(WeaponClass::class.java)

    /**
     * Scans the inventory of the entity for an SDU.
     */
    fun findSdu(): SDU = (owner as InventoryHolder).inventory
        .asSequence()
        .map { SDU.sduByItem(it) }
        .maxOrNull()
        ?: SDU.common

    /**
     * Get the maximum available ammo for the given weapon class.
     */
    fun maxAmmo(weaponClass: WeaponClass): Int {
        val sdu = findSdu()
        return when (weaponClass) {
            WeaponClass.PISTOL -> sdu.pistol
            WeaponClass.SMG -> sdu.smg
            WeaponClass.ASSAULT_RIFLE -> sdu.assaultRifle
            WeaponClass.SHOTGUN -> sdu.shotgun
            WeaponClass.LAUNCHER -> sdu.launcher
            WeaponClass.SNIPER -> sdu.sniper
        }
    }

    /**
     * Resets back to maximum values.
     */
    fun resetAmmo() {
        WeaponClass.entries.forEach {
            ammo[it] = maxAmmo(it)
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
        val limit = maxAmmo(ammoPack.weaponType)
        ammo[ammoPack.weaponType] = min(current + ammoPack.amount, limit)
    }

    /**
     * Get the ammo amounts per weapon class.
     *
     * @param maxValue `true` for the max ammo value of this weapon class, `false` for the current state.
     */
    operator fun get(weaponClass: WeaponClass, maxValue: Boolean = false): Int {
        if (maxValue) {
             return maxAmmo(weaponClass)
        }

        if (weaponClass !in ammo) {
            ammo[weaponClass] = maxAmmo(weaponClass)
        }
        return ammo.getOrDefault(weaponClass, 0)
    }
}