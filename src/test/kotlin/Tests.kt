import maliwan.mcbl.loot.gen.WeaponGenerator
import maliwan.mcbl.loot.lootPoolOf
import maliwan.mcbl.weapons.*
import maliwan.mcbl.weapons.gun.GunProperties
import java.util.Random

fun main() {

    val generator = WeaponGenerator(
        weaponClassTable = lootPoolOf(WeaponClass.PISTOL to 10),
        random = Random(123)
    )

    val gun = generator.generate()

    val serialized = gun.serialize()

    println(serialized)

    val deserialized = GunProperties.deserialize(serialized)

    println(deserialized.serialize())
    println("Assembly: " + deserialized.assembly)
}