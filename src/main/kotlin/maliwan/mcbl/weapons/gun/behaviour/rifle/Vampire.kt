package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.util.spigot.scheduleTask
import maliwan.mcbl.weapons.WeaponDamageType
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostKillBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class Vampire : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, PostKillBehaviour {

    override val baseName = "Vampire"
    override var redText = "Blood is fuel."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Grip.VLADOF.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.extraInfoText += "Killing enemies increases fire rate"
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Cannot spawn with minigun barrel.
        val rifleAssembly = assembly as AssaultRifleAssembly
        return if (rifleAssembly.barrel == AssaultRifleParts.Barrel.VLADOF_MINIGUN) {
            assembly.replacePart(AssaultRifleParts.Barrel.VLADOF)
        }
        else assembly
    }

    override fun afterKill(
        handler: WeaponEventHandler,
        killer: Player,
        target: LivingEntity,
        gunExecution: GunExecution,
        damageType: WeaponDamageType
    ) {
        gunExecution.pelletCount = 2

        handler.plugin.scheduleTask(80) {
            gunExecution.pelletCount = 1
        }
    }

    companion object {

        val statModifiers = statModifierList {
            add(0.01, StatModifier.Property.TRANSFUSION)
            add(0.015, StatModifier.Property.ACCURACY)
            add(0.0015, StatModifier.Property.RECOIL)
            multiply(1.1, StatModifier.Property.BASE_DAMAGE)
            multiply(0.8, StatModifier.Property.FIRE_RATE)
            multiply(1.2, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}