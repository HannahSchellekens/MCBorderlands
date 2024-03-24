package maliwan.mcbl.weapons.gun

import maliwan.mcbl.Chance
import kotlin.math.pow

/**
 * @author Hannah Schellekens
 */
open class GunExecution(

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
    properties.extraInfoText,
    properties.elements,
    properties.elementalChance,
    properties.elementalDuration,
    properties.elementalDamage,
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
    properties.gravity
) {

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

    override var accuracy: Chance
        get() {
            val now = System.currentTimeMillis()
            if (now - (lastShotTimestamp ?: 0) >= recoilTime) {
                consecutiveShots = 0
                return properties.accuracy
            }
            return Chance(properties.accuracy.chance * properties.recoil.pow(consecutiveShots))
        }
        set(value) { properties.accuracy = value }
}