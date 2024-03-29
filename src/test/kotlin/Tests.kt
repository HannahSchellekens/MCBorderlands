import maliwan.mcbl.loot.gen.PistolGenerator
import maliwan.mcbl.weapons.*

fun main() {


    val gen = PistolGenerator()
    repeat(100) {
        val gun = gen.generate(Rarities.RARE)
        println("${gun.gunName}: $gun")
    }
}