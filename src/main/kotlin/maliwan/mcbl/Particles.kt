package maliwan.mcbl

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle

fun Location.showElementalParticle(color: Color, amount: Int, size: Float = 0.5f) = showElementalParticle(color.red, color.green, color.blue, amount, size)

fun Location.showElementalParticle(r: Int, g: Int, b: Int, amount: Int, size: Float = 0.5f) {
    repeat(times = amount) { _ ->
        val color = Color.fromRGB(r, g, b)
        val options = Particle.DustOptions(color, size)
        world?.spawnParticle(Particle.REDSTONE, this, 0, 0.0, 0.0, 0.0, 4.0, options)
    }
}