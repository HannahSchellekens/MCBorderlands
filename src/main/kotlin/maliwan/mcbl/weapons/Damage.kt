package maliwan.mcbl.weapons

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.entity.*
import maliwan.mcbl.gui.DamageParticles
import maliwan.mcbl.util.modifyRandom
import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.util.spigot.nearbyEntities
import maliwan.mcbl.util.spigot.showElementalParticle
import maliwan.mcbl.weapons.gun.behaviour.PostKillBehaviour
import maliwan.mcbl.weapons.gun.forEachBehaviour
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * Calculates whether splash damage must occur, and applies it.
 */
fun splashDamage(plugin: MCBorderlandsPlugin, location: Location, bulletMeta: BulletMeta) {
    val elementalStatusEffects = plugin.weaponEventHandler.elementalStatusEffects

    bulletMeta.elements.forEach { element ->
        if (element == Elemental.PHYSICAL) return@forEach

        // Explosive does boom, contrary to the DoT effects of the other elements.
        if (element == Elemental.EXPLOSIVE) {
            splashDamageExplosive(plugin, location, bulletMeta)
            return
        }

        // Other elements do not do boom, but they do hurt. Proc roll is incorporated in [rollElementalDot].
        val radius = bulletMeta.splashRadius
        location.showSplashParticles(radius, element.color)

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

            // Call post kill hook
            if (target.isDead) {
                (bulletMeta.shooter as? Player)?.let { player ->
                    val execution = plugin.weaponEventHandler.obtainGunExecutionFromInventory(player)
                    execution?.forEachBehaviour<PostKillBehaviour> {
                        it.afterKill(plugin.weaponEventHandler, player, target, execution, WeaponDamageType.SPLASH_ELEMENTAL)
                    }
                }
            }

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

/**
 * Shows particles for splash damage at this location.
 */
fun Location.showSplashParticles(radius: Double, color: Color) {
    repeat((8 * radius).toInt()) {
        val splashLocation = clone().add(
            0.0.modifyRandom(radius),
            0.0.modifyRandom(radius),
            0.0.modifyRandom(radius)
        )
        splashLocation.showElementalParticle(color, 1, size = 1.3f)
    }
}

/**
 * Applies explosive splash damage to the given location.
 */
fun splashDamageExplosive(plugin: MCBorderlandsPlugin, location: Location, bulletMeta: BulletMeta) {
    val element = Elemental.EXPLOSIVE
    val elementalStatusEffects = plugin.weaponEventHandler.elementalStatusEffects

    val chance = bulletMeta.elementalProbability[element] ?: return
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

            // Cryo
            val cryoMultiplier = if (Elemental.EXPLOSIVE in bulletMeta.elements) {
                elementalStatusEffects.cryoDamageMultiplier(target)
            }
            else 1.0

            val totalDamage = bulletMeta.splashDamage.damage * slag * elementalModifier * cryoMultiplier
            target.temporarilyDisableIframes(plugin)
            target.damage(totalDamage, bulletMeta.shooter)
            target.showHealthBar(plugin)

            if (target.isDead) {
                (bulletMeta.shooter as? Player)?.let { player ->
                    val execution = plugin.weaponEventHandler.obtainGunExecutionFromInventory(player)
                    execution?.forEachBehaviour<PostKillBehaviour> {
                        it.afterKill(plugin.weaponEventHandler, player, target, execution, WeaponDamageType.SPLASH_EXPLOSIVE)
                    }
                }
            }

            // Heal when transfusion effect is active. Does not account for armour.
            // Might not even be that big of a deal.
            val healAmount = totalDamage * bulletMeta.transfusion
            bulletMeta.shooter.heal(healAmount)

            if (totalDamage >= 0.01) {
                plugin.damageParticles.scheduleDisplay(DamageParticles.DamageParticleEntry(
                    target,
                    target.location.add(0.0, target.height, 0.0),
                    element,
                    totalDamage
                ))
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
        .filter { it == Elemental.SLAG || it == Elemental.CRYO || (bulletMeta.elementalDamage[it]?.damage ?: 0.0) > 0.01 }
        .forEach {
            // Check if the effect will be procced.
            val probability = bulletMeta.elementalProbability[it] ?: Probability.ZERO
            if (probability.roll().not()) return@forEach

            // Apply effect.
            val duration = bulletMeta.elementalDuration[it] ?: return@forEach
            val damage = bulletMeta.elementalDamage[it]!!
            val policy = bulletMeta.elementalPolicy

            val statusEffect = ElementalStatusEffect(it, duration, damage, inflictedBy = bulletMeta.shooter)
            elementalStatusEffects.applyEffect(target, statusEffect, policy)
        }
}