package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.ShotgunParts
import org.bukkit.entity.Entity

/**
 * @author Hannah Schellekens
 */
open class OrphanMaker : UniqueGun, CyanTextProvider, PostGenerationBehaviour, DefaultPrefixProvider,
    PostBulletLandBehaviour {

    override val baseName = "Orphan Maker"
    override val redText = "Makes Orphans. Often."
    override val cyanText = "Curse of the Nefarious Backlash!"
    override val defaultPrefix = "Captain Blade's"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.pelletCount = 2
    }

    override fun afterBulletLands(bullet: Entity, meta: BulletMeta) {

    }

    companion object {

        val statModifiers = statModifierList {
            subtract(0.05, StatModifier.Property.TRANSFUSION) /* 5% self-damage is -5% transfusion */
            multiply(5.8, StatModifier.Property.BASE_DAMAGE)
            multiply(0.8, StatModifier.Property.RELOAD_SPEED)
            multiply(1.5, StatModifier.Property.ACCURACY)
        }
    }
}