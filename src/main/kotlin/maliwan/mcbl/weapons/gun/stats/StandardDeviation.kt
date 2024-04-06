package maliwan.mcbl.weapons.gun.stats

import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.WeaponClass

/**
 * @author Hannah Schellekens
 */
object StandardDeviations {

    fun providerOf(weaponClass: WeaponClass): StandardDeviationProvider = when (weaponClass) {
        WeaponClass.PISTOL -> PistolGradeModifiers
        WeaponClass.SHOTGUN -> ShotgunGradeModifiers
        WeaponClass.SNIPER -> SniperGradeModifiers
        else -> error("Weapon class <$weaponClass> is not supported.")
    }
}

/**
 * @author Hannah Schellekens
 */
interface StandardDeviationProvider {

    fun standardDeviation(rarity: Rarity, standardDeviation: StandardDeviation): Double
}

/**
 * @author Hannah Schellekens
 */
class StandardDeviation(
    val key: String
) {

    fun parse(stringValue: String) = stringValue.toDouble()

    operator fun invoke(stringValue: String) = parse(stringValue)

    companion object {

        val baseDamage = StandardDeviation("baseDamage")
        val magazineSize = StandardDeviation("magazineSize")
        val elementalDamage = StandardDeviation("elementalDamage")
    }
}