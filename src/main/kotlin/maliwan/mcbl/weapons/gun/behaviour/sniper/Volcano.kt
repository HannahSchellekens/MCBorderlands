package maliwan.mcbl.weapons.gun.behaviour.sniper

import maliwan.mcbl.util.showFlameParticle
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.ElementalStatusEffects
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostBulletLandBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.SniperParts
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * @author Hannah Schellekens
 */
open class Volcano : UniqueGun, PostGenerationBehaviour, PostBulletLandBehaviour {

    override val baseName = "Volcano"
    override val redText = "Pele humbly requests a sarifice, if\nit's not too much trouble."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Barrel.MALIWAN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        // 100% splash damage instead of 50% (used to be 80%).
        properties.splashDamage = properties.baseDamage
        properties.elementalPolicy = ElementalStatusEffects.ApplyPolicy.ADD
    }

    override fun afterBulletLands(
        handler: WeaponEventHandler,
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    ) {
        bullet.location.showFlameParticle()
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.03, StatModifier.Property.BASE_DAMAGE)
            multiply(3.5, StatModifier.Property.SPLASH_RADIUS)
            multiply(1.3, StatModifier.Property.ELEMENTAL_DAMAGE)
            multiply(1.5, StatModifier.Property.ELEMENTAL_DURATION)
        }
    }
}