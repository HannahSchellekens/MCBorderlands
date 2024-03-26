import com.google.gson.Gson
import maliwan.mcbl.util.Chance
import maliwan.mcbl.util.Damage
import maliwan.mcbl.weapons.Elements
import maliwan.mcbl.weapons.Manufacturers
import maliwan.mcbl.weapons.gun.*

fun main() {

    val gunProperties = GunProperties(manufacturer = Manufacturers.MALIWAN)

    gunProperties.elements.add(Elements.SHOCK)
    gunProperties.elements.add(Elements.INCENDIARY)

    gunProperties.elementalDamage[Elements.SHOCK] = Damage(1.0)
    gunProperties.elementalDamage[Elements.INCENDIARY] = Damage(2.0)

    gunProperties.elementalChance[Elements.SHOCK] = Chance(0.5)
    gunProperties.elementalChance[Elements.INCENDIARY] = Chance(0.7)

    // Before stat mods.
    println(gunProperties.serialize())

    // We're going to apply modifiers.
    val barrel = PistolParts.Barrel.MALIWAN
    val grip = PistolParts.Grip.MALIWAN

    barrel.applyStatModifiers(gunProperties)
    grip.applyStatModifiers(gunProperties)

    // Has it changed?
    println(gunProperties.serialize())
}