package maliwan.mcbl.weapons

import org.bukkit.entity.Entity

/**
 * @author Hannah Schellekens
 */
open class BulletEffects(val handler: WeaponEventHandler) : Runnable {

    /**
     * The amount of ticks since the start of this bullet effects.
     */
    var tick: Long = 0
        private set

    /**
     * All effects that need to be processed:
     * `(effect definition, the actual effect, tick every N ticks)`
     */
    private val effects = ArrayList<Triple<BulletEffectDefinition, BulletEffects.BulletEffectDefinition.(Long) -> Unit, Long>>()

    /**
     * Adds the given effect to the
     */
    fun addEffect(
        definition: BulletEffectDefinition,
        everyNTicks: Long,
        effect: BulletEffects.BulletEffectDefinition.(Long) -> Unit
    ) {
        effects.add(Triple(definition, effect, everyNTicks))
    }

    override fun run() {
        effects.forEach { (definition, effect, tickNumber) ->
            if (tick % tickNumber != 0L) return@forEach
            definition.effect(tickNumber)
        }
        effects.removeIf { (definition, _, _) -> definition.toRemove }
        tick++
    }

    /**
     * @author Hannah Schellekens
     */
    inner class BulletEffectDefinition(
        val bullet: Entity,
        val meta: BulletMeta
    ) {

        var toRemove: Boolean = false
            private set

        fun stopEffect(killEntity: Boolean = true) {
            toRemove = true
            handler.unregisterBullet(bullet)
            if (killEntity) {
                bullet.remove()
            }
        }
    }
}