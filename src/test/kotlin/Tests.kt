import maliwan.mcbl.util.TabTable
import maliwan.mcbl.weapons.*
import maliwan.mcbl.weapons.gun.PistolParts
import maliwan.mcbl.weapons.gun.names.PistolNames
import java.util.Random

fun main() {

    val gauss = Random().nextGaussian()
    val multiplier = gauss * 0.045 + 1.2

    val table = TabTable.fromResource(
        "/gun/base/pistol-base-values.csv",
        { Manufacturer.valueOf(it) },
        { it },
        { it }
    )

    println(table)

    val name = PistolNames.nameOf(Manufacturer.MALIWAN, PistolParts.Barrel.VLADOF, null, Elemental.EXPLOSIVE)
    println(name)
}