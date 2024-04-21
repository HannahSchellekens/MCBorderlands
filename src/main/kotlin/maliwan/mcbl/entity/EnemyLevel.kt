package maliwan.mcbl.entity

import maliwan.mcbl.Keys
import maliwan.mcbl.loot.LootPool
import maliwan.mcbl.loot.RarityTable
import maliwan.mcbl.loot.lootPoolOf
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.gun.GunProperties
import org.bukkit.entity.Entity
import org.bukkit.persistence.PersistentDataType

/**
 * @author Hannah Schellekens
 */
enum class EnemyLevel(
    val title: String?,
    val healthMultiplier: Double,
    val dropChance: Double,
    val weaponTable: LootPool<Rarity>
) : Comparable<EnemyLevel> {

    NOOB(null, 1.0, 0.01, RarityTable.WorldDrops.shitty),
    REGULAR(null, 1.0, 0.1, RarityTable.WorldDrops.regular),
    BADASS("Badass", 2.0, 0.4, RarityTable.WorldDrops.badass),
    SUPER_BADASS("Super Badass", 4.0, 0.8, RarityTable.WorldDrops.superBadass),
    ULTIMATE_BADASS("Ultimate Badass", 7.0, 1.0, RarityTable.WorldDrops.ultimateBadass),
    CHUBBY("Chubby", 11.0, 1.0, RarityTable.WorldDrops.chubby),
    ;

    val nextLevel: EnemyLevel
        get() = when (this) {
            NOOB -> REGULAR
            REGULAR -> BADASS
            BADASS -> SUPER_BADASS
            SUPER_BADASS -> ULTIMATE_BADASS
            ULTIMATE_BADASS -> CHUBBY
            CHUBBY -> CHUBBY
        }

    val previousLevel: EnemyLevel
        get() = when (this) {
            NOOB -> NOOB
            REGULAR -> NOOB
            BADASS -> REGULAR
            SUPER_BADASS -> SUPER_BADASS
            ULTIMATE_BADASS -> SUPER_BADASS
            CHUBBY -> ULTIMATE_BADASS
        }

    companion object {

        val enemyLevelPool = lootPoolOf(
            REGULAR to 4096,
            BADASS to 512,
            SUPER_BADASS to 64,
            ULTIMATE_BADASS to 8,
            CHUBBY to 1
        )
    }
}

/**
 * Get the enemy level of the given entity.
 * `null` if this item has no enemy level.
 */
fun Entity.enemyLevel(): EnemyLevel? {
    val dataStore = persistentDataContainer
    if (dataStore.has(Keys.enemyLevel, PersistentDataType.STRING).not()) return null

    val enemyLevelName = dataStore.get(Keys.enemyLevel, PersistentDataType.STRING) ?: return null
    return EnemyLevel.valueOf(enemyLevelName)
}