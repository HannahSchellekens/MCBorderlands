package maliwan.mcbl.weapons.gun.stats

import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.WeaponClass

/**
 * @author Hannah Schellekens
 */
object Modifiers {

    fun providerOf(weaponClass: WeaponClass): ModifierProvider = when (weaponClass) {
        WeaponClass.PISTOL -> PistolGradeModifiers
        WeaponClass.SHOTGUN -> ShotgunGradeModifiers
        else -> error("Weapon class <$weaponClass> is not supported.")
    }
}

/**
 * @author Hannah Schellekens
 */
interface ModifierProvider {

    fun <T> modifier(rarity: Rarity, modifier: Modifier<T>): T
}

/**
 * @author Hannah Schellekens
 */
class Modifier<T>(
    val key: String,
    val parser: (String) -> T
) {

    fun parse(stringValue: String) = parser(stringValue)

    operator fun invoke(stringValue: String) = parse(stringValue)

    companion object {

        val baseDamage = Modifier("baseDamage") {
            it.toDoubleOrNull() ?: error("Invalid baseDamage double <$it>")
        }
        val magazineSize = Modifier("magazineSize") {
            it.toDoubleOrNull() ?: error("Invalid magazineSize double <$it>")
        }
        val reloadTime = Modifier("reloadTime") {
            it.toIntOrNull() ?: error("Invalid reloadTime int <$it>")
        }
        val fireRate = Modifier("fireRate") {
            it.toDoubleOrNull() ?: error("Invalid fireRate double <$it>")
        }
        val elementalChance = Modifier("elementalChance") {
            it.toDoubleOrNull() ?: error("Invalid elementalChance double <$it>")
        }
        val elementalDamage = Modifier("elementalDamage") {
            it.toDoubleOrNull() ?: error("Invalid elementalDamage double <$it>")
        }
        val accuracy = Modifier("accuracy") {
            it.toDoubleOrNull() ?: error("Invalid accuracy double <$it>")
        }
        val recoil = Modifier("recoil") {
            it.toDoubleOrNull() ?: error("Invalid recoil double <$it>")
        }
    }
}