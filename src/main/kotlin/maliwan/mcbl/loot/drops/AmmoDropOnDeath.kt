package maliwan.mcbl.loot.drops

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.loot.RarityTable
import maliwan.mcbl.loot.ammo.AmmoPack
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class AmmoDropOnDeath(val plugin: MCBorderlandsPlugin) : Listener {

    @EventHandler
    fun dropAmmoOnDeath(event: EntityDeathEvent) {
        val living = event.entity
        val dropChance = dropChance(living.type)
        val dropCount = dropAmount(living.type).random()
        val type = RarityTable.Ammo.regular.roll()

        repeat(dropCount) {
            if (Random.nextDouble() < dropChance) {
                val pack = AmmoPack(type)
                println(pack)
                event.drops.add(pack.toItemStack())
            }
        }
    }

    companion object {

        fun dropChance(entityType: EntityType) = when (entityType) {
            in lowAmmoDrops -> 0.2
            in highAmmoDrops -> 1.0
            else -> 0.7
        }

        fun dropAmount(entityType: EntityType) = when (entityType) {
            in lowAmmoDrops -> 0..1
            in highAmmoDrops -> 2..4
            else -> 1..2
        }

        val lowAmmoDrops = setOf(
            EntityType.DONKEY,
            EntityType.MULE,
            EntityType.SILVERFISH,
            EntityType.PIG,
            EntityType.SHEEP,
            EntityType.COW,
            EntityType.CHICKEN,
            EntityType.WOLF,
            EntityType.MUSHROOM_COW,
            EntityType.SNOWMAN,
            EntityType.OCELOT,
            EntityType.HORSE,
            EntityType.RABBIT,
            EntityType.LLAMA,
            EntityType.VILLAGER,
            EntityType.CAT,
            EntityType.PANDA,
            EntityType.TRADER_LLAMA,
            EntityType.WANDERING_TRADER,
            EntityType.FOX,
            EntityType.GOAT,
            EntityType.FROG,
            EntityType.CAMEL,
            EntityType.SNIFFER,
            EntityType.SQUID,
            EntityType.COD,
            EntityType.SALMON,
            EntityType.PUFFERFISH,
            EntityType.TROPICAL_FISH,
            EntityType.DOLPHIN,
            EntityType.AXOLOTL,
            EntityType.GLOW_SQUID,
            EntityType.TADPOLE,
            EntityType.TURTLE,
            EntityType.BAT,
            EntityType.PARROT,
            EntityType.BEE,
            EntityType.SLIME,
            EntityType.ENDERMITE,
            EntityType.ALLAY,
            EntityType.MAGMA_CUBE,
            EntityType.SKELETON_HORSE,
            EntityType.ZOMBIE_HORSE
        )

        val highAmmoDrops = setOf(
            EntityType.IRON_GOLEM,
            EntityType.ENDER_DRAGON,
            EntityType.WARDEN,
            EntityType.ELDER_GUARDIAN,
            EntityType.WITHER
        )
    }
}