import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.*
import maliwan.mcbl.weapons.gun.PistolParts
import maliwan.mcbl.weapons.gun.names.PistolNames
import maliwan.mcbl.weapons.gun.stats.PistolBaseValues
import maliwan.mcbl.weapons.gun.stats.PistolGradeModifiers
import java.util.Random

fun main() {

    val gauss = Random().nextGaussian()
    val multiplier = gauss * 0.045 + 1.2


    print("Maliwan base damage: ")
    val damage = PistolBaseValues.baseValue(Manufacturer.MALIWAN, PistolBaseValues.Stat.baseDamage)
    println(damage)

    print("Epic base damage: ")
    println(PistolGradeModifiers.modifier(Rarity.EPIC, PistolGradeModifiers.Modifier.baseDamage))

    print("Epic std deviation: ")
    println(PistolGradeModifiers.standardDeviation(Rarity.EPIC, PistolGradeModifiers.StandardDeviation.baseDamage))
}