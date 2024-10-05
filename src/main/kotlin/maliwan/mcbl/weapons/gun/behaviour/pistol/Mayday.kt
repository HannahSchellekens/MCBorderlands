package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class Mayday : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour {

    override val baseName = "Mayday"
    override val redText = "Help me, is anybody there?"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.TORGUE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.burstDelay = Ticks(3)
        properties.burstCount = 3
        properties.magazineSize = 9
        properties.fireRate = 2.0
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        val data = execution.executionData<ExecutionData>() ?: ExecutionData()

        if (data.shotIndex in 3..5) {
            execution.burstDelay = Ticks(5)
        }
        else {
            execution.burstDelay = Ticks(2)
        }

        data.shotIndex++
        data.shotIndex %= 9
        execution.setExecutionData(data)
    }

    data class ExecutionData(var shotIndex: Int = 0)

    companion object {

        val statModifiers = statModifierList {
            multiply(1.3, StatModifier.Property.BASE_DAMAGE)
            divide(1.65, StatModifier.Property.RELOAD_SPEED)
        }
    }
}