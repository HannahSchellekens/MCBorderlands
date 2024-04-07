package maliwan.mcbl.weapons

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.entity.armorPoints
import maliwan.mcbl.entity.showHealthBar
import maliwan.mcbl.entity.temporarilyDisableKnockback
import maliwan.mcbl.util.Chance
import maliwan.mcbl.util.modifyRandom
import maliwan.mcbl.util.nearbyEntities
import maliwan.mcbl.util.showElementalParticle
import org.bukkit.Location
import org.bukkit.entity.LivingEntity

/**
 * Calculates whether splash damage must occur, and applies it.
 */
fun splashDamage(plugin: MCBorderlandsPlugin, location: Location, bulletMeta: BulletMeta) {
    val elementalStatusEffects = plugin.weaponEventHandler.elementalStatusEffects

    bulletMeta.elements.forEach { element ->
        if (element == Elemental.PHYSICAL) return@forEach

        // Explosive does boom, contrary to the DoT effects of the other elements.
        if (element == Elemental.EXPLOSIVE) {
            val chance = bulletMeta.elementalChance[element] ?: return@forEach
            if (chance.roll()) {
                val radius = bulletMeta.splashRadius
                location.world?.createExplosion(location, 0f)
                location.nearbyEntities(radius).forEach entities@{ target ->
                    if (target !is LivingEntity) return@entities
                    val slag = elementalStatusEffects.slagMultiplier(target)
                    target.temporarilyDisableKnockback(plugin)

                    // Effectiveness type.
                    val effectivenessType = EffectivenessType.typeOf(target.type)
                    val elementalModifier = bulletMeta.elements.fold(1.0) { acc, elemental ->
                        val elementDamageModifier = effectivenessType.damageMultiplier(elemental, target.armorPoints.toInt())
                        acc * elementDamageModifier
                    }

                    val totalDamage = bulletMeta.splashDamage.damage * slag * elementalModifier
                    target.damage(totalDamage, bulletMeta.shooter)
                    target.showHealthBar(plugin)

                    if (totalDamage >= 0.01) {
                        plugin.damageParticles.showDotDamageDisplay(
                            target.location.add(0.0, target.height, 0.0),
                            totalDamage,
                            element
                        )
                    }
                }
            }
        }
        // Other elements do not do boom, but they do hurt. Proc roll is incorporated in [rollElementalDot].
        else {
            val radius = bulletMeta.splashRadius

            // Show small splash animation at location.
            repeat((8 * radius).toInt()) {
                val splashLocation = location.clone().add(
                    0.0.modifyRandom(radius),
                    0.0.modifyRandom(radius),
                    0.0.modifyRandom(radius)
                )
                splashLocation.showElementalParticle(element.color, 1, size = 1.3f)
            }

            // Use slightly larger radius because locations are counted from the ground position of the entity
            // this compensates for larger entities.
            location.nearbyEntities(radius + 0.25).forEach entities@{ target ->
                if (target !is LivingEntity) return@entities
                /* Slag cannot enhance its own damage */
                val slag = if (element == Elemental.SLAG) 1.0 else elementalStatusEffects.slagMultiplier(target)
                target.temporarilyDisableKnockback(plugin)

                // Effectiveness type.
                val effectivenessType = EffectivenessType.typeOf(target.type)
                val elementalModifier = bulletMeta.elements.fold(1.0) { acc, elemental ->
                    val elementDamageModifier = effectivenessType.damageMultiplier(elemental, target.armorPoints.toInt())
                    acc * elementDamageModifier
                }

                val totalDamage = bulletMeta.splashDamage.damage * slag * elementalModifier
                target.damage(totalDamage, bulletMeta.shooter)
                rollElementalDot(plugin, target, bulletMeta)
                target.showHealthBar(plugin)

                if (totalDamage > 0.01) {
                    plugin.damageParticles.showDotDamageDisplay(
                        target.location.add(0.0, target.height, 0.0),
                        totalDamage,
                        element
                    )
                }
            }
        }
    }
}

/**
 * Rolls if an elemental (DoT) effect can proc.
 * The elemental effect will be applied to `target` and gets its info from `bulletMeta`.
 */
fun rollElementalDot(plugin: MCBorderlandsPlugin, target: LivingEntity, bulletMeta: BulletMeta) {
    val elementalStatusEffects = plugin.weaponEventHandler.elementalStatusEffects

    bulletMeta.elements.asSequence()
        .filter { it == Elemental.SLAG || (bulletMeta.elementalDamage[it]?.damage ?: 0.0) > 0.01 }
        .forEach {
            // Check if the effect will be procced.
            val chance = bulletMeta.elementalChance[it] ?: Chance.ZERO
            if (chance.roll().not()) return@forEach

            // Apply effect.
            val duration = bulletMeta.elementalDuration[it] ?: return@forEach
            val damage = bulletMeta.elementalDamage[it]!!
            val policy = bulletMeta.elementalPolicy

            val statusEffect = ElementalStatusEffect(it, duration, damage, inflictedBy = bulletMeta.shooter)
            elementalStatusEffects.applyEffect(target, statusEffect, policy)
        }
}