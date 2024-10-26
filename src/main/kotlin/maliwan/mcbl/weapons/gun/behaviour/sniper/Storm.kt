package maliwan.mcbl.weapons.gun.behaviour.sniper

import maliwan.mcbl.util.spigot.scheduleTask
import maliwan.mcbl.util.spigot.showElementalParticle
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.ElementalStatusEffects
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostBulletLandBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.SniperParts
import maliwan.mcbl.weapons.splashDamage
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Storm : UniqueGun, PostGenerationBehaviour, PostBulletLandBehaviour, UpdateAssemblyBehaviour {

    override val baseName = "Storm"
    override val redText = "Tut, Tut, it looks like rain."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.splashDamage = properties.baseDamage * 0.5
        properties.elementalPolicy = ElementalStatusEffects.ApplyPolicy.ADD
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always shock.
        return assembly.replaceCapacitor(Capacitor.SHOCK)
    }

    override fun afterBulletLands(
        handler: WeaponEventHandler,
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    ) {
        val baseLocation = (hitLocation ?: bullet.location)

        baseLocation.world?.let {
            it.strikeLightningEffect(baseLocation)
            it.playSound(baseLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 16f, 1f)
        }

        // All orbs proc 4 times
        val splashDamage = meta.copy(splashDamage = meta.splashDamage * 0.25, splashRadius = 1.4)

        repeat(4) { orbIndex ->
            val angle = Random.nextDouble() * Math.PI * 2
            val orbLocation = Location(
                baseLocation.world,
                baseLocation.x + (3.2 + Random.nextDouble()) * cos(angle),
                baseLocation.y + 0.5 + Random.nextDouble() * 1.5 - 0.75,
                baseLocation.z + (3.2 + Random.nextDouble()) * sin(angle)
            )

            val offset = Random.nextInt(0, 12)
            repeat(4) { splashIndex ->
                if (orbIndex == splashIndex) {
                    handler.plugin.scheduleTask((splashIndex + 1) * 8L + offset) {
                        orbLocation.world?.strikeLightningEffect(orbLocation)
                        orbLocation.world?.playSound(orbLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1.2f)
                    }
                }

                handler.plugin.scheduleTask((splashIndex + 1) * 8L + offset) {
                    splashDamage(handler.plugin, orbLocation, splashDamage)
                }
                repeat(8) { particleIndex ->
                    handler.plugin.scheduleTask((splashIndex + 1) * 8L + particleIndex + offset) {
                        orbLocation.showElementalParticle(Color.fromRGB(230,245,255), 12, spread = 0.3)
                    }
                }
            }
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.06, StatModifier.Property.BASE_DAMAGE)
            multiply(3.5, StatModifier.Property.SPLASH_RADIUS)
            multiply(1.3, StatModifier.Property.ELEMENTAL_DAMAGE)
        }
    }
}