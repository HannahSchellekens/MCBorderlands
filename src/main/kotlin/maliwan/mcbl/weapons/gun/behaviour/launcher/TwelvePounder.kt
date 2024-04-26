package maliwan.mcbl.weapons.gun.behaviour.launcher

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.BulletTypeProvider
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.LauncherParts
import org.bukkit.entity.EntityType

/**
 * @author Hannah Schellekens
 */
open class TwelvePounder : UniqueGun, PostGenerationBehaviour, BulletTypeProvider {

    override val baseName = "12 Pounder"
    override val redText = "Nec Pluribus Impar, Bitches."
    override val bulletType = EntityType.SNOWBALL

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        LauncherParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.bounces = 1
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.35, StatModifier.Property.BASE_DAMAGE)
            divide(2, StatModifier.Property.MAGAZINE_SIZE)
            divide(2, StatModifier.Property.RELOAD_SPEED)
            multiply(0.9, StatModifier.Property.FIRE_RATE)
            multiply(2.0, StatModifier.Property.GRAVITY)
            add(0.015, StatModifier.Property.ACCURACY)
        }
    }
}