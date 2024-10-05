package maliwan.mcbl.weapons.gun.behaviour.pistol

import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.PistolParts

/**
 * @author Hannah Schellekens
 */
open class GunGun : UniqueGun, PostGenerationBehaviour {

    override val baseName = "Gun Gun"
    override val redText = "Gun Gunner gunned a gun of gunned\nguns."

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        PistolParts.Barrel.TEDIORE.applyStatModifiers(properties)
        statModifiers.applyAll(properties)

        properties.reloadSpeed = Ticks(5)
        properties.magazineSize = 1
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.3, StatModifier.Property.BASE_DAMAGE)
        }
    }
}