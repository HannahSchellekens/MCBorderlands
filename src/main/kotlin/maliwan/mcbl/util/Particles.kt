package maliwan.mcbl.util

import org.bukkit.Color
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Particle

fun Location.showElementalParticle(color: Color, amount: Int, size: Float = 0.5f, spread: Double = 0.0) {
    showElementalParticle(color.red, color.green, color.blue, amount, size, spread)
}

fun Location.showElementalParticle(r: Int, g: Int, b: Int, amount: Int, size: Float = 0.5f, spread: Double = 0.0) {
    repeat(times = amount) { _ ->
        val color = Color.fromRGB(r, g, b)
        val options = Particle.DustOptions(color, size)

        val location = Location(
            world,
            x.modifyRandom(spread),
            y.modifyRandom(spread),
            z.modifyRandom(spread)
        )

        world?.spawnParticle(Particle.DUST, location, 0, spread, spread, spread, spread, options)
    }
}

fun Location.showFlameParticle() = clone().add(0.5, 0.5, 0.5).let {
    world?.playEffect(it, Effect.MOBSPAWNER_FLAMES, 0)
}