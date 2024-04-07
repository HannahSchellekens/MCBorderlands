package maliwan.mcbl.weapons.gun

import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.gun.parts.behaviour.GunBehaviour

/**
 * @author Hannah Schellekens
 */
open class Capacitor(

    val element: Elemental,
    val behaviours: List<GunBehaviour> = emptyList()
) {

    open val partName: String = element.displayName

    val nullIfPhysical: Capacitor?
        get() = if (element == Elemental.PHYSICAL) null else this
}