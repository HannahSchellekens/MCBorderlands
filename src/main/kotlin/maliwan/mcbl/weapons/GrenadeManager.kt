package maliwan.mcbl.weapons

import maliwan.mcbl.MCBorderlandsPlugin
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

/**
 * @author Hannah Schellekens
 */
open class GrenadeManager(val plugin: MCBorderlandsPlugin) : Runnable {

    private val grenades = ArrayList<Grenade>()

    fun throwGrenade(grenade: Grenade) {
        grenades.add(grenade)
    }

    fun findsHit(grenade: Grenade): Boolean {
        val entity = grenade.itemDisplay
        val loc = entity.location.clone().add(0.0, 0.5, 0.0)
        return (loc.block.type.isSolid || entity.world.getNearbyEntities(loc, 0.1, 0.1, 0.1)
            .any { it is LivingEntity && it != grenade.source })
    }

    override fun run() {
        grenades.forEach { grenade ->
            val location = grenade.itemDisplay.location.clone()
            location.add(grenade.velocity.x, grenade.velocity.y, grenade.velocity.z)
            grenade.itemDisplay.teleport(location)

            grenade.velocity = grenade.velocity.add(Vector(0.0, -grenade.gravity, 0.0))
            grenade.itemDisplay.velocity = grenade.velocity

            if (findsHit(grenade)) {
                val splashLocation = grenade.itemDisplay.location.add(grenade.velocity.multiply(-1.0))

                grenade.itemDisplay.world.createExplosion(splashLocation, 0f)
                grenade.exploded = true

                grenade.bulletMeta?.let { meta ->
                    splashDamage(plugin, splashLocation, meta)
                }
            }
        }

        val now = System.currentTimeMillis()
        grenades.removeIf {
            val dead = it.exploded || now >= it.deathTime
            if (dead) {
                it.itemDisplay.remove()
            }
            dead
        }
    }

    /**
     * @author Hannah Schellekens
     */
    data class Grenade(

        /**
         * The item display that displays the grenade.
         */
        val itemDisplay: ItemDisplay,

        /**
         * The speed of the grenade
         */
        var velocity: Vector,

        /**
         * The gravity acceleration in blocks/tick^2.
         */
        val gravity: Double = 0.05,

        /**
         * Unix time when the grenade must despawn.
         */
        val deathTime: Long = System.currentTimeMillis() + 5000L,

        /**
         * Whether the grenade has exploded.
         */
        var exploded: Boolean = false,

        /**
         * Whatever, some data, hurray. Grenade = Bullet = Happy.
         */
        val bulletMeta: BulletMeta? = null,

        /**
         * Who shot the grenade.
         */
        val source: LivingEntity? = null
    )
}