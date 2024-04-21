package maliwan.mcbl.weapons.gun

import maliwan.mcbl.util.Chance
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * @author Hannah Schellekens
 */
class GunExecution(

    /**
     * The initial properties of the gun used.
     */
    val properties: GunProperties,

    /**
     * The amount of millis before the recoil ran out.
     */
    val recoilTime: Int = 650

) : GunProperties(
    properties.name,
    properties.baseDamage,
    properties.accuracy,
    properties.recoil,
    properties.fireRate,
    properties.reloadSpeed,
    properties.magazineSize,
    properties.ammoPerShot,
    properties.redText,
    properties.cyanText,
    properties.extraInfoText,
    properties.elements,
    properties.elementalChance,
    properties.elementalDuration,
    properties.elementalDamage,
    properties.elementalPolicy,
    properties.splashRadius,
    properties.splashDamage,
    properties.recoilAngle,
    properties.manufacturer,
    properties.rarity,
    properties.weaponClass,
    properties.pelletCount,
    properties.bulletSpeed,
    properties.meleeDamage,
    properties.burstCount,
    properties.burstDelay,
    properties.gravity,
    properties.bonusCritMultiplier,
    properties.assembly,
    properties.extraShotChance,
    properties.freeShotChance,
    properties.transfusion
) {

    val originalFireRate: Double = properties.fireRate

    /**
     * The amount of bullets left in the clip.
     */
    var clip = properties.magazineSize

    /**
     * How many shots are fired consecutively.
     */
    var consecutiveShots: Int = 0
        set(value) {
            val now = System.currentTimeMillis()
            if (value > 0) {
                lastShotTimestamp = now
            }
            field = if (now - (lastShotTimestamp ?: 0) >= recoilTime) {
                0
            } else {
                value
            }
        }

    /**
     * The last time as shot has been fired (unix timestamp).
     */
    var lastShotTimestamp: Long? = null

    /**
     * Accuracy multiplier for each consecutive shot.
     */
    private val recoilFactor: Double
        get() = when (assembly?.manufacturer) {
            Manufacturer.HYPERION -> when (assembly!!.weaponClass) {
                WeaponClass.SHOTGUN -> 1.075
                WeaponClass.SMG -> 1.009
                else -> 1.018
            }
            else -> properties.recoil
        }

    override var accuracy: Chance
        get() {
            val now = System.currentTimeMillis()
            if (now - (lastShotTimestamp ?: 0) >= recoilTime) {
                consecutiveShots = 0
                return properties.accuracy
            }
            val newChance = properties.accuracy.chance * recoilFactor.pow(consecutiveShots)
            return Chance(min(max(0.0, newChance), 1.0))
        }
        set(value) { properties.accuracy = value }
}