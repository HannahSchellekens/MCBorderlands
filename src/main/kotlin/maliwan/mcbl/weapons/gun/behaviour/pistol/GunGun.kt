package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.util.plugin.Ticks
import maliwan.mcbl.weapons.CustomGrenadeManager
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.behaviour.UpdateReloadGrenadeBehaviour
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class GunGun : UniqueGun, PostGenerationBehaviour, UpdateReloadGrenadeBehaviour {

    override val baseName = "Gun Gun"
    override val redText = "Gun Gunner gunned a gun of gunned\nguns."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.TEDIORE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.reloadSpeed = Ticks(5)
        properties.magazineSize = 1
    }

    override fun updateReloadGrenade(grenade: CustomGrenadeManager.CustomGrenade) {
        grenade.bulletMeta?.let {
            it.splashDamage *= 2.0
            it.splashRadius *= 1.5
        }
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.3, StatModifier.Property.BASE_DAMAGE)
        }
    }
}