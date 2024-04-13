package maliwan.mcbl.weapons.gun.parts

import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.gun.behaviour.Defiler
import maliwan.mcbl.weapons.gun.behaviour.GunBehaviour
import maliwan.mcbl.weapons.gun.behaviour.Hellfire
import maliwan.mcbl.weapons.gun.behaviour.Tinderbox

/**
 * @author Hannah Schellekens
 */
// Needs to be a separate enum class for serialization.
// This way behaviour objects do not need to be deserialized.
enum class Capacitor(elemental: Elemental, val partName: String, vararg behaviours: GunBehaviour) {

    NONE(Elemental.PHYSICAL, "None"),
    INCENDIARY(Elemental.INCENDIARY, "Incendiary"),
    SHOCK(Elemental.SHOCK, "Shock"),
    CORROSIVE(Elemental.CORROSIVE, "Corrisove"),
    SLAG(Elemental.SLAG, "Slag"),
    EXPLOSIVE(Elemental.EXPLOSIVE, "Explosive"),

    /**
     * MALIWAN HELLFIRE SMG.
     */
    HELLFIRE(Elemental.INCENDIARY, "HellFire", Hellfire()),

    /**
     * MALIWAN DEFILER PISTOL.
     */
    DEFILER(Elemental.CORROSIVE, "Defiler", Defiler()),

    /**
     * BANDIT TINDERBOX PISTOL.
     */
    TINDERBOX(Elemental.INCENDIARY, "Tinderbox", Tinderbox()),
    ;

    val element: Elemental = elemental

    val behaviours: List<GunBehaviour> = behaviours.toList()

    val nullIfPhysical: Capacitor?
        get() = if (element == Elemental.PHYSICAL) null else this

    companion object {

        val commonCapacitors = setOf(
            INCENDIARY,
            SHOCK,
            CORROSIVE,
            SLAG,
            EXPLOSIVE
        )

        val legendaryCapacitors = setOf(
            HELLFIRE,
            DEFILER
        )
    }
}