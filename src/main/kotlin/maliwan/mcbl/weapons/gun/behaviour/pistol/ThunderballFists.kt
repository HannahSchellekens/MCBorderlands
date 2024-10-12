package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.MCBorderlandsPlugin
import maliwan.mcbl.util.scheduleTask
import maliwan.mcbl.util.showElementalParticle
import maliwan.mcbl.weapons.*
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.PistolParts
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class ThunderballFists : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, BulletEffectBehaviour,
    PostBulletLandBehaviour {

    override val baseName = "Thunderball Fists"
    override val redText = "I can have such a thing?"

    private lateinit var plugin: MCBorderlandsPlugin

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.elementalPolicy = ElementalStatusEffects.ApplyPolicy.ADD
        properties.splashDamage = properties.baseDamage
        properties.splashRadius *= 1.2
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always shock.
        return assembly.replaceCapacitor(Capacitor.SHOCK)
    }

    override fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta) {
        this.plugin = handler.plugin
    }

    override fun afterBulletLands(
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    ) {
        val baseLocation = hitLocation?.clone() ?: bullet.location
        val delay = Random.nextInt(0, 5)

        // Particles every other tick.
        repeat(9) {
            plugin.scheduleTask(delay + (it + 1) * 2L) {
                val percentage = it / 9.0
                val location = baseLocation.clone().add(0.0, 0.5 + 1.6 * percentage, 0.0)
                location.showElementalParticle(Elemental.SHOCK.color, 3 + it * 2, 1f, 0.7 * percentage)
            }
        }

        plugin.scheduleTask(delay + 19L) {
            val location = baseLocation.clone().add(0.0, 2.0, 0.0)
            location.world?.createExplosion(location, 0f)
            splashDamage(
                plugin,
                location,
                meta.copy(splashDamage = meta.splashDamage * 1.5, splashRadius = meta.splashRadius * 1.75)
            )
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.1, StatModifier.Property.BASE_DAMAGE)
            multiply(1.3, StatModifier.Property.MAGAZINE_SIZE)
            multiply(1.06, StatModifier.Property.ELEMENTAL_DAMAGE)
            multiply(1.5, StatModifier.Property.ELEMENTAL_DURATION)
            multiply(1.15, StatModifier.Property.ELEMENTAL_CHANCE)
        }
    }
}