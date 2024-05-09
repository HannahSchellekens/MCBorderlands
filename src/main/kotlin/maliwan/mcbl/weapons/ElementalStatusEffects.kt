package maliwan.mcbl.weapons

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.entity.armorPoints
import maliwan.mcbl.entity.showHealthBar
import maliwan.mcbl.entity.temporarilyDisableIframes
import maliwan.mcbl.entity.temporarilyDisableKnockback
import maliwan.mcbl.gui.DamageParticles
import maliwan.mcbl.util.*
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
data class ElementalStatusEffect(

    /**
     * Type of the element.
     */
    val elemental: Elemental,

    /**
     * How long the effect lasts.
     */
    val duration: Ticks,

    /**
     * The amount of damage to deal each tick.
     */
    val damage: Damage,

    /**
     * Inflict damage every this many ticks.
     */
    val damageInterval: Ticks = Ticks(9),

    /**
     * Who inflicted this status effect, `null` for no entity that caused it.
     */
    val inflictedBy: LivingEntity? = null
)

/**
 * @author Hannah Schellekens
 */
open class ElementalStatusEffects(val plugin: MCBorderlandsPlugin) {

    /**
     * Counts how many ticks have passed.
     */
    private var tickCount: Int = 0

    /**
     * Maps each entity to a map containing all active status effects mapped to the unix time they expire.
     *
     * `Entity -> (Status Effect -> Expiry Time Millis)`
     */
    private val activeEffects = HashMap<LivingEntity, MutableList<Pair<ElementalStatusEffect, Long>>>()

    /**
     * `true` if the element has at least 1 elemental effect applied.
     */
    fun hasActiveEffects(entity: LivingEntity) = entity in activeEffects

    /**
     * Get the map of active effects of the given entity.
     *
     * @return Map that maps `Effect -> Expiry Time Millis`. Empty map if there are no current effects.
     */
    fun activeEffets(entity: LivingEntity): List<Pair<ElementalStatusEffect, Long>> {
        return activeEffects.getOrDefault(entity, emptyList())
    }

    /**
     * Checks if an entity has a certain elemental effect applied to them.
     */
    fun hasElementalEffect(entity: LivingEntity, effect: Elemental): Boolean {
        return activeEffects[entity]?.any { (it, _) -> it.elemental == effect } ?: false
    }

    /**
     * The damage multiplier for this target based on the current slagged status.
     */
    fun slagMultiplier(target: LivingEntity) = if (hasElementalEffect(target, Elemental.SLAG)) {
        2.0
    }
    else 1.0

    /**
     * Adds an elemental status effect to the given entity.
     * See [ApplyPolicy] for different ways of dealing with duplicate elemental types.
     *
     * @param entity
     *          The entity to apply the status effect to.
     * @param effect
     *          The effect to apply.
     * @param policy
     *          See PpplyPolicy for options how to deal with duplicate elemental types.
     */
    fun applyEffect(entity: LivingEntity, effect: ElementalStatusEffect, policy: ApplyPolicy = ApplyPolicy.DO_NOTHING) {
        var active = activeEffects.getOrDefault(entity, null)

        // Do not overwrite the effects if overwriting is disabled.
        if (policy == ApplyPolicy.DO_NOTHING && hasElementalEffect(entity, effect.elemental)) {
            return
        }

        // This is the first effect to add, so initialize the map.
        if (active == null) {
            active = ArrayList()
        }

        // Remove element effects of the same type if they need to be replaced.
        if (policy == ApplyPolicy.REPLACE) {
            active.removeIf { (it, _) -> it.elemental == effect.elemental }
        }

        val now = System.currentTimeMillis()
        val timestampExpired = now + effect.duration.millis
        active += (effect to timestampExpired)

        activeEffects[entity] = active
    }

    /**
     * Updates all status effects.
     */
    fun tick() {
        applyDamage()
        applyShockEffect()
        removeExpiredEffects()

        // Lower particle count.
        if (tickCount % 9 == 0) {
            showParticles()
        }

        tickCount++
    }

    /**
     * Applies all status effect damage to entities with status effects.
     */
    @Suppress("UnstableApiUsage" /* Damage API is experimental */)
    fun applyDamage() = activeEffects.forEach { (entity, effects) ->
        effects.asSequence()
            .filter { (effect, _) -> effect.damage.damage > 0.01 && tickCount % effect.damageInterval.ticks == 0 }
            .forEach { (effect, _) ->
                val damage = effect.damage
                val element = effect.elemental
                val weaknessType = EffectivenessType.typeOf(entity.type)
                val multiplier = weaknessType.damageMultiplier(element, entity.armorPoints.toInt())
                val slag = if (effect.elemental == Elemental.SLAG) 1.0 else slagMultiplier(entity)
                val totalDamage = damage.damage * multiplier * slag

                // Prevent elemental damage to increase damage output.
                val cause = when (effect.elemental) {
                    Elemental.INCENDIARY -> DamageSource.builder(DamageType.ON_FIRE).build()
                    Elemental.CORROSIVE -> DamageSource.builder(DamageType.WITHER).build()
                    Elemental.SHOCK -> DamageSource.builder(DamageType.LIGHTNING_BOLT).build()
                    else -> DamageSource.builder(DamageType.MAGIC).build()
                }

                entity.temporarilyDisableKnockback(plugin)
                entity.temporarilyDisableIframes(plugin)
                entity.damage(totalDamage, cause)

                plugin.damageParticles.scheduleDisplay(DamageParticles.DamageParticleEntry(
                    entity,
                    entity.location.add(0.0, entity.height, 0.0),
                    element,
                    totalDamage
                ))
            }
    }

    /**
     * Executes all special effects when the player has a shock DoT.
     * This method checks for this DoT effect.
     */
    fun applyShockEffect() = activeEffects.forEach { (entity, effects) ->
        effects.asSequence()
            .filter { (effect, _) -> effect.elemental == Elemental.SHOCK }
            .filter { (effect, _) -> tickCount % effect.damageInterval.ticks == 0 }
            .forEach { (_, _) ->
                val location = entity.location
                location.pitch += Random.nextDouble(-3.0, 3.0).toFloat()
                location.yaw += Random.nextDouble(-3.0, 3.0).toFloat()
                entity.teleport(location)
            }
    }

    /**
     * Shows particles for all affected entities.
     */
    fun showParticles() = activeEffects.forEach { (entity, effects) ->
        effects.asSequence()
            .distinctBy { (effect, _) -> effect.elemental }
            .forEach { (effect, _) -> showStatusParticle(entity, effect.elemental) }
    }

    /**
     * Shows particles of the status effect around the entity.
     */
    fun showStatusParticle(entity: LivingEntity, elemental: Elemental) {
        when (elemental) {
            Elemental.INCENDIARY -> {
                entity.location.showFlameParticle()
            }
            Elemental.CORROSIVE,
            Elemental.SHOCK,
            Elemental.SLAG -> {
                repeat(8) {
                    val spread = entity.width / 2.0
                    val loc = entity.location.clone().add(0.5.modifyRandom(spread), 1.0.modifyRandom(spread), 0.5.modifyRandom(spread))
                    loc.showElementalParticle(elemental.color, 1, size = 1.4f)
                }
            }
            else -> Unit
        }
    }

    /**
     * Removes all status effects that have expired.
     */
    fun removeExpiredEffects() {
        val now = System.currentTimeMillis()
        activeEffects.forEach { (entity, effectList) ->
            effectList.removeIf { (toRemove, endTimestamp) ->
                if (now > endTimestamp) {
                    val effects = activeEffets(entity)
                        .filter { (effect, _) -> effect != toRemove }
                        .map { (effect, _) -> effect.elemental }

                    entity.showHealthBar(statusEffects = effects)
                    true
                }
                else false
            }
        }
        activeEffects.entries.removeIf { (_, effects) -> effects.isEmpty() }
    }

    /**
     * Cleans up all memory for all players.
     */
    fun cleanup() {
        activeEffects.clear()
    }

    /**
     * Cleans up all memory for a specific player.
     */
    fun cleanup(entity: LivingEntity) {
        activeEffects.remove(entity)
    }

    /**
     * Different policies for dealing with duplicate elemental type effects.
     *
     * @author Hannah Schellekens
     */
    enum class ApplyPolicy {

        /**
         * Do not add a new effect if an effect of the same type already exists.
         */
        DO_NOTHING,

        /**
         * Replace the existing effect of the same elemental type with a new effect.
         */
        REPLACE,

        /**
         * Add another status effect of the same elemental type so damage stacks.
         * Hehehellfire.
         */
        ADD
        ;
    }
}