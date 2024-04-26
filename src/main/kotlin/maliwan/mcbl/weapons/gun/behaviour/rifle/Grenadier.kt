package maliwan.mcbl.weapons.gun.behaviour.rifle

import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.AssaultRifleAssembly
import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.WeaponAssembly
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.parts.AssaultRifleParts

/**
 * @author Hannah Schellekens
 */
open class Grenadier : PostGenerationBehaviour {

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        if (properties.weaponClass != WeaponClass.ASSAULT_RIFLE) return
        if (properties.manufacturer != Manufacturer.DAHL) return
        val rifle = assembly as? AssaultRifleAssembly ?: return
        if (rifle.barrel != AssaultRifleParts.Barrel.TORGUE) return

        properties.gravity = 0.03
        properties.bulletSpeed /= 1.5
        properties.bounces = 3
    }
}