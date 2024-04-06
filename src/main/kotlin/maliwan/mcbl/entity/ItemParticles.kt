package maliwan.mcbl.entity

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.util.scheduleTask
import maliwan.mcbl.util.showElementalParticle
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.gun.gunProperties
import org.bukkit.entity.Item

/**
 * Show rarity particles above gun items laying on the ground.
 *
 * @author Hannah Schellekens
 */
open class ItemParticles(

    val plugin: MCBorderlandsPlugin,

    /**
     * Show particles every this amount of ticks.
     * This is to prevent visual overload.
     */
    val everyNTicks: Int = 7

) : Runnable {

    /**
     * How many ticks have passed since the start of this runnable.
     */
    private var tickCount: Int = 0

    override fun run() {
        if (tickCount++ % everyNTicks != 0) return

        plugin.server.worlds.forEach { world ->
            world.getEntitiesByClass(Item::class.java).forEach entities@ { item ->
                val gunProperties = item.gunProperties() ?: return@entities

                val particleCount = when (gunProperties.rarity) {
                    Rarity.COMMON, Rarity.UNCOMMON -> 3
                    Rarity.RARE, Rarity.EPIC -> 4
                    Rarity.LEGENDARY, Rarity.PEARLESCENT -> 5
                }

                repeat(particleCount) {
                    val loc = item.location.clone().add(0.0, 0.2 + 0.2 * it, 0.0)

                    plugin.scheduleTask(it.toLong()) {
                        loc.showElementalParticle(gunProperties.rarity.color, 1, size = 1f - 0.15f * it)
                    }
                }
            }
        }
    }
}