import maliwan.mcbl.loot.gen.WeaponGenerator
import maliwan.mcbl.loot.lootPoolOf
import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.*
import maliwan.mcbl.weapons.gun.PistolParts
import maliwan.mcbl.weapons.gun.names.PistolNames
import maliwan.mcbl.weapons.gun.stats.PistolBaseValues
import maliwan.mcbl.weapons.gun.stats.PistolGradeModifiers
import java.util.Random

fun main() {

    val generator = WeaponGenerator(weaponClassTable = lootPoolOf(WeaponClass.PISTOL to 10))

    repeat(100) {
        println(generator.generate().serialize())
    }
}