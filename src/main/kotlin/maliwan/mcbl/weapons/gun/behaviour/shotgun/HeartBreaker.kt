package maliwan.mcbl.weapons.gun.behaviour.shotgun

import maliwan.mcbl.util.Probability
import maliwan.mcbl.util.modifyRandom
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import maliwan.mcbl.weapons.gun.pattern.RandomBulletPattern

/**
 * @author Hannah Schellekens
 */
open class HeartBreaker(scale: Double = 0.02) : UniqueGun, PostGenerationBehaviour, DefaultPrefixProvider,
    UpdateAssemblyBehaviour, BulletPatternProvider {

    override val baseName = "Heart Breaker"
    override val redText = "I don't want to set the world on\nfire..."
    override val defaultPrefix = "Miss Moxxi's"
    override val bulletPatternProcessor = RandomBulletPattern.of(
        scale to 0.0,
        2.0 * scale to 1.0 * scale,
        2.0 * scale to 2.0 * scale,
        scale to 3.0 * scale,
        0.0 to 3.0 * scale,
        -scale to 2.0 * scale,
        -2.0 * scale to scale,
        -3.0 * scale to 0.0,
        -2.0 * scale to -scale,
        -scale to -2.0 * scale,
        0.0 to -3.0 * scale,
        scale to -3.0 * scale,
        2.0 * scale to -2.0 * scale,
        2.0 * scale to -scale
    )

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.HYPERION.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.pelletCount = 10
        properties.accuracy = Probability(0.75.modifyRandom(0.04))
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always incendiary.
        return assembly.replaceCapacitor(Capacitor.INCENDIARY)
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.02, StatModifier.Property.TRANSFUSION)
            add(0.5, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            multiply(0.92, StatModifier.Property.BASE_DAMAGE)
        }
    }
}