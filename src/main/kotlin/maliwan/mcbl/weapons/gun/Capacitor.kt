package maliwan.mcbl.weapons.gun

import maliwan.mcbl.weapons.Elemental

/**
 * @author Hannah Schellekens
 */
open class Capacitor(

    val element: Elemental
) {

    open val partName: String = element.displayName

    val nullIfPhysical: Capacitor?
        get() = if (element == Elemental.PHYSICAL) null else this
}