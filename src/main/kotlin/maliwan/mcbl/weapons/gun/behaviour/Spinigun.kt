package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.GunExecution
import org.bukkit.entity.Player
import kotlin.math.min

/**
 * @author Hannah Schellekens
 */
open class Spinigun(

    /**
     * How long it takes in seconds to wind up to full fire rate
     */
    val windupSeconds: Double = 1.0,

    /**
     * What the starting fire rate is
     */
    val startFireRate: Double = 2.0,

    /**
     * After how many seconds of not shooting the fire rate should wind down.
     */
    val cooldownInterval: Double = 1.0

) : PreGunShotBehaviour, GunExecutionInitializationBehaviour {

    private var lastShot = 0L

    override fun beforeGunShot(execution: GunExecution, player: Player) {
        val timeSinceLastShot = System.currentTimeMillis() - lastShot
        val stepPerSecond = (execution.originalFireRate - startFireRate) / windupSeconds
        val step = stepPerSecond / execution.originalFireRate

        // Wind down when last shot has been too long ago.
        if (timeSinceLastShot >= cooldownInterval * 1000L) {
            execution.fireRate = startFireRate
        }
        // Wind up on consecutive shots.
        else {
            execution.fireRate = min(execution.originalFireRate, execution.fireRate + step)
        }

        lastShot = System.currentTimeMillis()
    }

    override fun onInitializedGunExecution(gunExecution: GunExecution, player: Player) {
        gunExecution.fireRate = startFireRate
    }
}