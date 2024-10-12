package maliwan.mcbl.weapons.gun

import maliwan.mcbl.util.Probability
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.behaviour.BulletPatternProvider
import maliwan.mcbl.weapons.gun.behaviour.first
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import maliwan.mcbl.weapons.gun.pattern.BulletPatternProcessor
import maliwan.mcbl.weapons.gun.pattern.NoBulletPattern
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.reflect.KClass

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
    properties.cyanText,
    properties.extraInfoText,
    properties.elements,
    properties.elementalProbability,
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
    properties.extraShotProbability,
    properties.freeShotProbability,
    properties.transfusion,
    properties.bounces,
    properties.isPiercing,
    properties.directDamage,
    properties.homingTargetDistance,
    properties.homingTargetRadius,
    properties.homingStrength,
    properties.armourPenetration
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
     * How the bullets should spread during execution.
     */
    val bulletPattern = assembly?.behaviours?.first<BulletPatternProvider, BulletPatternProcessor> {
        it.bulletPatternProcessor
    } ?: NoBulletPattern

    /**
     * Accuracy multiplier for each consecutive shot.
     */
    private val recoilFactor: Double
        get() {
            val base = when (assembly?.manufacturer) {
                Manufacturer.HYPERION -> when (assembly!!.weaponClass) {
                    WeaponClass.SHOTGUN -> 1.06
                    WeaponClass.SMG -> 1.009
                    else -> 1.018
                }
                else -> properties.recoil
            }

            val extra = if (assembly?.weaponClass == WeaponClass.SHOTGUN) {
                val shotgun = assembly as ShotgunAssembly
                if (shotgun.barrel == ShotgunParts.Barrel.BUTCHER) {
                    -0.052
                }
                else 0.0
            }
            else 0.0

            return base + extra
        }

    override var accuracy: Probability
        get() {
            val now = System.currentTimeMillis()
            if (now - (lastShotTimestamp ?: 0) >= recoilTime) {
                consecutiveShots = 0
                return properties.accuracy
            }
            val newChance = properties.accuracy.chance * recoilFactor.pow(consecutiveShots)
            return Probability(min(max(0.0, newChance), 1.0))
        }
        set(value) { properties.accuracy = value }

    /**
     * Contains extra execution data about the gun.
     * Can be used for custom gun properties that are too niche to actually make generic.
     */
    val executionData: MutableMap<KClass<*>, Any> = HashMap()

    inline fun <reified T> executionData(): T? {
        return executionData[T::class] as? T
    }

    inline fun <reified T> setExecutionData(data: T) {
        executionData[T::class] = data as Any
    }
}