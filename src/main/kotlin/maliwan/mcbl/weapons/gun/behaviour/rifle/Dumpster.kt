package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.GunExecution
import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.WeaponAssembly
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import kotlin.math.ceil

/**
 * @author Hannah Schellekens
 */
open class Dumpster : UniqueGun, PostGenerationBehaviour, PostGunShotBehaviour {

    override val baseName = "Dumpster"
    override val redText = "I Love Trash"

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        val ammoLeft = properties.magazineSize
        val perShot = properties.ammoPerShot
        val shotsPerClip = ceil(ammoLeft.toDouble() / perShot.toDouble()).toInt()
        val fireRate = properties.fireRate

        val timePerClip = shotsPerClip.toDouble() / fireRate * 20
        properties.reloadSpeed = Ticks(timePerClip.toInt())
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        val ammoLeft = execution.clip
        val perShot = execution.ammoPerShot
        val shotsLeft = ceil(ammoLeft.toDouble() / perShot.toDouble()).toInt()

        repeat(shotsLeft) {
            handler.shootGun(player, execution, triggerAfterGunShot = false)
        }
    }
}