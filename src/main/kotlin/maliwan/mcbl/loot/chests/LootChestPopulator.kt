package maliwan.mcbl.loot.chests

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.inventory.SDU
import maliwan.mcbl.loot.RarityTable
import maliwan.mcbl.loot.ammo.AmmoPack
import maliwan.mcbl.loot.gen.WeaponGenerator
import maliwan.mcbl.util.spigot.isInDungeon
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.LootGenerateEvent
import org.bukkit.generator.structure.Structure
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class LootChestPopulator(val plugin: MCBorderlandsPlugin) : Listener {

    @EventHandler
    fun generateLoot(event: LootGenerateEvent) {
        val loc = event.lootContext.location

        val dungeon = loc.isInDungeon()
        val structure = loc.findStructure()

        val (lootPool, amount) = when {
            dungeon -> Pair(RarityTable.Treasure.regular, 0..2)
            else -> when (structure) {
                null -> null to null
                in shittyChest -> Pair(RarityTable.Treasure.shitty, 0..2)
                in whiteChest -> RarityTable.Treasure.regular to 1..3
                in redChest -> RarityTable.Treasure.highQuality to 2..4
                in stellarChest -> RarityTable.Treasure.stellar to 3..4
                else -> null to null
            }
        }

        // Weapon drops.
        val pool = lootPool ?: return
        val count = amount?.random() ?: 0
        val generator = WeaponGenerator(rarityTable = pool)
        repeat(count) {
            val gun = generator.generate()
            val bow = ItemStack(Material.BOW, 1)
            gun.applyToItem(bow)
            event.loot.add(bow)
        }

        // Ammo drops.
        repeat(Random.nextInt(1, 4)) {
            val type = RarityTable.Ammo.regular.roll()
            val pack = AmmoPack(type)
            event.loot.add(pack.toItemStack())
        }

        // SDU drops
        if (structure in whiteChest && Random.nextDouble() < 0.01) {
            event.loot.add(SDU.uncommon.item)
        }
        else if (structure in redChest && Random.nextDouble() < 0.1) {
            event.loot.add(SDU.uncommon.item)
        }
    }

    /**
     * Find the most probable type of structure the chest is in, excluding dungeons (which are marked as
     * features).
     */
    private fun Location.findStructure(): Structure? {
        val structures = world?.getStructures(chunk.x, chunk.z) ?: return null
        return if (structures.isEmpty()) {
            null
        }
        else if (structures.size == 1) {
            structures.first().structure
        }
        else structures.minBy {
            it.boundingBox.center.distance(this@findStructure.toVector())
        }.structure
    }

    companion object {

        val shittyChest: Set<Structure> = setOf(
            Structure.IGLOO,
            Structure.SWAMP_HUT,
            Structure.RUINED_PORTAL,
            Structure.RUINED_PORTAL_DESERT,
            Structure.RUINED_PORTAL_JUNGLE,
            Structure.RUINED_PORTAL_SWAMP,
            Structure.RUINED_PORTAL_MOUNTAIN,
            Structure.RUINED_PORTAL_OCEAN,
            Structure.VILLAGE_SNOWY,
            Structure.VILLAGE_TAIGA,
            Structure.VILLAGE_DESERT,
            Structure.VILLAGE_PLAINS,
            Structure.VILLAGE_SAVANNA
        )

        val whiteChest: Set<Structure> = setOf(
            Structure.PILLAGER_OUTPOST,
            Structure.MINESHAFT,
            Structure.MINESHAFT_MESA,
            Structure.JUNGLE_PYRAMID,
            Structure.DESERT_PYRAMID,
            Structure.SHIPWRECK,
            Structure.SHIPWRECK_BEACHED,
            Structure.OCEAN_RUIN_COLD,
            Structure.OCEAN_RUIN_WARM,
            Structure.FORTRESS,
            Structure.BURIED_TREASURE,
            Structure.RUINED_PORTAL_NETHER
        )

        val redChest: Set<Structure> = setOf(
            Structure.MANSION,
            Structure.STRONGHOLD,
            Structure.MONUMENT,
            Structure.FORTRESS,
            Structure.END_CITY,
            Structure.BURIED_TREASURE,
            Structure.BASTION_REMNANT,
            Structure.ANCIENT_CITY
        )

        val stellarChest: Set<Structure> = setOf(
        )
    }
}