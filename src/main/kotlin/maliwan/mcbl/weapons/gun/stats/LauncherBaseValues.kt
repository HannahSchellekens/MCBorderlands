package maliwan.mcbl.weapons.gun.stats

import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.gun.GunProperties

/**
 * @author Hannah Schellekens
 */
object LauncherBaseValues : BaseValueProvider {

    private val baseValueTable = TabTable.fromResource(
        "/gun/base/launcher-base-values.csv",
        Manufacturer::valueOf,
        { it },
        { it }
    )

    /**
     * Get the base value of a specific stat for a given manufacturer.
     */
    override fun <T> baseValue(manufacturer: Manufacturer, stat: Stat<T>): T {
        val stringValue = baseValueTable[manufacturer, stat.key]
            ?: throw IllegalArgumentException("No stat found for manufacturer $manufacturer and key ${stat.key}")
        return stat(stringValue)
    }

    /**
     * Creates a new [GunProperties] object for launchers that is populated by the base values of `manufacturer`.
     * Does not include elemental properties, they have to be applied when the specific element is applied.
     */
    fun newGunProperties(manufacturer: Manufacturer) = GunProperties(
        baseDamage = baseValue(manufacturer, Stat.baseDamage),
        magazineSize = baseValue(manufacturer, Stat.magazineSize),
        reloadSpeed = baseValue(manufacturer, Stat.reloadTime),
        fireRate = baseValue(manufacturer, Stat.fireRate),
        burstCount = baseValue(manufacturer, Stat.burstCount),
        accuracy = baseValue(manufacturer, Stat.accuracy),
        recoil = baseValue(manufacturer, Stat.recoil),
        gravity = baseValue(manufacturer, Stat.gravity),
        bulletSpeed = baseValue(manufacturer, Stat.bulletSpeed),
        bonusCritMultiplier = baseValue(manufacturer, Stat.bonusCritModifier),
        splashRadius = baseValue(manufacturer, Stat.splashRadius),
        ammoPerShot = baseValue(manufacturer, Stat.ammoPerShot),
        pelletCount = baseValue(manufacturer, Stat.projectileCount),
        recoilAngle = baseValue(manufacturer, Stat.recoilAngle),
        freeShotChance = baseValue(manufacturer, Stat.freeShotChance),
    )
}