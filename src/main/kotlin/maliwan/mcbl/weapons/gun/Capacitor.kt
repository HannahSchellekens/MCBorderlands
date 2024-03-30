package maliwan.mcbl.weapons.gun

import maliwan.mcbl.weapons.Elemental

/**
 * @author Hannah Schellekens
 */
class Capacitor(

    val element: Elemental
) {

    val nullIfPhysical: Capacitor?
        get() = if (element == Elemental.PHYSICAL) null else this
}