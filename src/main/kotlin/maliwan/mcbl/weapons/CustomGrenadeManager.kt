package maliwan.mcbl.weapons

import maliwan.mcbl.MCBorderlandsPlugin
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector

/**
 * @author Hannah Schellekens
 */
open class CustomGrenadeManager(val plugin: MCBorderlandsPlugin) : Runnable {

    private val grenades = ArrayList<CustomGrenade>()

    fun throwGrenade(customGrenade: CustomGrenade) {
        grenades.add(customGrenade)
    }

    fun findsHit(customGrenade: CustomGrenade): Boolean {
        val entity = customGrenade.itemDisplay
        val loc = entity.location.clone().add(0.0, 0.5, 0.0)
        return (loc.block.type.isSolid || entity.world.getNearbyEntities(loc, 0.2, 0.2, 0.2)
            .any { it is LivingEntity && it != customGrenade.source })
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
     * Removes all active grenades (deletes entities and clears memory).
     */
    fun cleanup() {
        grenades.forEach { it.itemDisplay.remove() }
        grenades.clear()
    }

    /**
     * @author Hannah Schellekens
     */
    data class CustomGrenade(

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