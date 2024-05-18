package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.BulletPatternProvider
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts
import maliwan.mcbl.weapons.gun.pattern.SequentialBulletPattern

/**
 * @author Hannah Schellekens
 */
open class Veruc : UniqueGun, PostGenerationBehaviour, BulletPatternProvider {

    override val baseName = "Veruc"
    override var redText = "I want that rifle, Daddy!"

    override val bulletPatternProcessor = SequentialBulletPattern.of(
        (0.0 to -0.16), (0.0 to 0.0), (0.0 to 0.16),
        (0.0 to -0.12), (0.0 to 0.0), (0.0 to 0.12),
        (0.0 to -0.08), (0.0 to 0.0), (0.0 to 0.08),
        (0.0 to -0.04), (0.0 to 0.0), (0.0 to 0.04),
        (0.0 to -0.0), (0.0 to 0.0), (0.0 to 0.0),
    )

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Barrel.DAHL.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.pelletCount = 3
        properties.ammoPerShot = 2
        properties.burstCount = 5
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.25, StatModifier.Property.BASE_DAMAGE)
            add(0.024, StatModifier.Property.ACCURACY)
            multiply(1.35, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}