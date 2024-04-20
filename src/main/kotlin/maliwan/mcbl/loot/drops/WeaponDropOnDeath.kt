package maliwan.mcbl.loot.drops

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.entity.EnemyLevel
import maliwan.mcbl.entity.enemyLevel
import maliwan.mcbl.loot.gen.WeaponGenerator
import maliwan.mcbl.util.scheduleTask
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

            plugin.scheduleTask(1L) {
                detonate()
            }
        }
    }

    @EventHandler
    fun dropWeaponOnDeath(event: EntityDeathEvent) {
        val living = event.entity

        val baseLevel = living.enemyLevel() ?: EnemyLevel.REGULAR
        val level = when (living.type) {
            in shittyWeaponDrops -> baseLevel.previousLevel.previousLevel
            in lowWeaponDrops -> baseLevel.previousLevel
            in highWeaponDrops -> baseLevel.nextLevel
            else -> baseLevel
        }

        val dropChance = baseLevel.dropChance
        if (Random.nextDouble() > dropChance) return

        val lootPool = level.weaponTable

        val generator = WeaponGenerator(rarityTable = lootPool)
        val gun = generator.generate()
        val bow = ItemStack(Material.BOW, 1)
        gun.applyToItem(bow)
        event.drops.add(bow)
    }

    companion object {

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