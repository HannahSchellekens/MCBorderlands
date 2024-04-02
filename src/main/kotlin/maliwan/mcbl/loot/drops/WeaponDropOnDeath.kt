package maliwan.mcbl.loot.drops

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.entity.genericMaxHealth
import maliwan.mcbl.loot.RarityTable
import maliwan.mcbl.loot.gen.WeaponGenerator
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.gun.gunProperties
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class WeaponDropOnDeath(val plugin: MCBorderlandsPlugin) : Listener {

    @EventHandler
    fun legendarySparkles(event: ItemSpawnEvent) {
        val item = event.entity.itemStack.gunProperties() ?: return
        val rarity = item.rarity
        when (rarity) {
            Rarity.LEGENDARY, Rarity.PEARLESCENT -> Unit
            else -> return
        }

        val location = event.location
        val firework = location.world?.spawnEntity(location, EntityType.FIREWORK) as? Firework ?: return
        firework.apply {
            fireworkMeta = fireworkMeta.apply {
                power = 0
                val builder = FireworkEffect.builder()
                    .with(FireworkEffect.Type.STAR)
                    .withColor(rarity.color)
                addEffect(builder.build())
            }

            plugin.server.scheduler.scheduleSyncDelayedTask(plugin, {
                detonate()
            }, 1L)
        }
    }

    @EventHandler
    fun dropWeaponOnDeath(event: EntityDeathEvent) {
        val living = event.entity

        // Determine if there will be a drop.
        val dropChance = if (living.genericMaxHealth >= 50.0) {
            BADASS_DROP_CHANCE
        }
        else dropChance(living.type)

        if (Random.nextDouble() > dropChance) return

        // Determine rarity table.
        val lootPool = if (living.genericMaxHealth >= 50.0) {
            RarityTable.WorldDrops.badass
        }
        else lootTable(living.type)

        val generator = WeaponGenerator(rarityTable = lootPool)
        val gun = generator.generate()
        val bow = ItemStack(Material.BOW, 1)
        gun.applyToItem(bow)
        event.drops.add(bow)
    }

    companion object {

        const val BADASS_DROP_CHANCE: Double = 0.25

        fun dropChance(entityType: EntityType) = when (entityType) {
            in shittyWeaponDrops, in lowWeaponDrops -> 0.01
            in highWeaponDrops -> 1.0
            else -> 0.075
        }

        fun lootTable(entityType: EntityType) = when (entityType) {
            in shittyWeaponDrops -> RarityTable.WorldDrops.shitty
            in highWeaponDrops -> RarityTable.WorldDrops.superBadass
            else -> RarityTable.WorldDrops.regular
        }

        val shittyWeaponDrops = setOf(
            EntityType.SILVERFISH,
            EntityType.CHICKEN,
            EntityType.SNOWMAN,
            EntityType.RABBIT,
            EntityType.FROG,
            EntityType.SQUID,
            EntityType.COD,
            EntityType.SALMON,
            EntityType.PUFFERFISH,
            EntityType.TROPICAL_FISH,
            EntityType.TADPOLE,
            EntityType.BAT,
            EntityType.BEE,
            EntityType.ENDERMITE,
        )

        val lowWeaponDrops = setOf(
            EntityType.DONKEY,
            EntityType.MULE,
            EntityType.PIG,
            EntityType.SHEEP,
            EntityType.COW,
            EntityType.WOLF,
            EntityType.MUSHROOM_COW,
            EntityType.OCELOT,
            EntityType.HORSE,
            EntityType.LLAMA,
            EntityType.VILLAGER,
            EntityType.CAT,
            EntityType.PANDA,
            EntityType.TRADER_LLAMA,
            EntityType.WANDERING_TRADER,
            EntityType.FOX,
            EntityType.GOAT,
            EntityType.CAMEL,
            EntityType.SNIFFER,
            EntityType.DOLPHIN,
            EntityType.AXOLOTL,
            EntityType.GLOW_SQUID,
            EntityType.TURTLE,
            EntityType.PARROT,
            EntityType.SLIME,
            EntityType.ALLAY,
            EntityType.MAGMA_CUBE,
            EntityType.SKELETON_HORSE,
            EntityType.ZOMBIE_HORSE
        )

        val highWeaponDrops = setOf(
            EntityType.ENDER_DRAGON,
            EntityType.WARDEN,
            EntityType.WITHER
        )
    }
}