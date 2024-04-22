package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.SmgParts
import org.bukkit.entity.Player
import kotlin.random.Random

/**
 * @author Hannah Schellekens
 */
open class Crit : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, ReloadBehaviour, DefaultPrefixProvider {

    override val baseName = "Crit"
    override val redText = "Slippery when wet."
    override val defaultPrefix = "Miss Moxxi's"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SmgParts.Barrel.HYPERION.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.transfusion = 0.025
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always shock.
        return assembly.replaceCapacitor(Capacitor.SHOCK)
    }

    override fun beforeReload(player: Player, gunExecution: GunExecution) {
        // Chance to drop on reload.
        if (Random.nextDouble() < 0.12) {
            gunExecution.clip = gunExecution.magazineSize
            player.dropItem(true)
        }
    }

    companion object {

        val statModifiers = statModifierList {
            add(1.5, StatModifier.Property.BONUS_CRIT_MULTIPLIER)
            multiply(1.3, StatModifier.Property.BASE_DAMAGE)
            multiply(1.15, StatModifier.Property.ELEMENTAL_DAMAGE)
            add(0.015, StatModifier.Property.ACCURACY)
            multiply(0.8, StatModifier.Property.RELOAD_SPEED)
            multiply(1.15, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}