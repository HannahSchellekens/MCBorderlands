package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.util.music.melody.MELODY_WE_ARE_NUMBER_ONE
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateAssemblyBehaviour
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts
import maliwan.mcbl.weapons.gun.parts.Capacitor
import org.bukkit.Instrument
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class IceScream : UniqueGun, PostGenerationBehaviour, UpdateAssemblyBehaviour, PostGunShotBehaviour {

    override val baseName = "Ice Scream"
    override val redText = "Winter is numbing."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        AssaultRifleParts.Barrel.VLADOF_MINIGUN.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    override fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly {
        // Always cryo.
        return assembly.replaceCapacitor(Capacitor.CRYO)
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        val data = execution.executionData<ExecutionData>() ?: ExecutionData()

        val note = MELODY_WE_ARE_NUMBER_ONE.noteAt(data.shotsFired)
        player.playNote(player.location, Instrument.BELL, note)

        data.shotsFired++
        execution.setExecutionData(data)
    }

    data class ExecutionData(var shotsFired: Int = 0)

    companion object {

        val statModifiers = statModifierList {
            multiply(1.2, StatModifier.Property.ELEMENTAL_CHANCE)
            add(3, StatModifier.Property.MAGAZINE_SIZE)
        }
    }
}