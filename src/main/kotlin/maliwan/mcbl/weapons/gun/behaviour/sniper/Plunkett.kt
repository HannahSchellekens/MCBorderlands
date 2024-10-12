package maliwan.mcbl.weapons.gun.behaviour.sniper

import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostBulletLandBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.PostGunShotBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.SniperParts
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

/**
 * @author Hannah Schellekens
 */
open class Plunkett : UniqueGun, PostGenerationBehaviour, PostBulletLandBehaviour, PostGunShotBehaviour {

    override val baseName = "Plunkett"
    override val redText = "Make sure they know the first one\nwasn't an accident."

    private var handler: WeaponEventHandler? = null

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        SniperParts.Grip.JAKOBS.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.extraInfoText +=
            "Critical hits remove bullets directly\nfrom your backpack instead of the\nweapon's magazine."
    }

    override fun afterGunShot(
        handler: WeaponEventHandler,
        execution: GunExecution,
        bullets: List<Entity>,
        player: Player
    ) {
        this.handler = handler
    }

    override fun afterBulletLands(
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    ) {
        if (isCriticalHit.not()) return

        val player = meta.shooter as? Player ?: return
        val execution = handler?.obtainGunExecutionFromInventory(player) ?: return
        execution.clip += 1
        handler?.plugin?.inventoryManager?.let {
            val inventory = it[player]
            inventory.removeAmmo(execution.weaponClass, 1)
        }
    }

    companion object {

        val statModifiers = statModifierList {
        }
    }
}