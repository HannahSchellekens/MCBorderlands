package maliwan.mcbl.weapons.gun.parts

import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.gun.behaviour.*
import maliwan.mcbl.weapons.gun.behaviour.launcher.Roaster
import maliwan.mcbl.weapons.gun.behaviour.pistol.Defiler
import maliwan.mcbl.weapons.gun.behaviour.pistol.Tinderbox
import maliwan.mcbl.weapons.gun.behaviour.smg.HellFire

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

    // Unique capacitors.
    HELLFIRE(Elemental.INCENDIARY, "HellFire", HellFire()), /* SMG */
    DEFILER(Elemental.CORROSIVE, "Defiler", Defiler()), /* PISTOL */
    TINDERBOX(Elemental.INCENDIARY, "Tinderbox", Tinderbox()), /* PISTOL */
    ROASTER_SHOCK(Elemental.SHOCK, "Roaster", Roaster(Elemental.SHOCK)), /* LAUNCHER */
    ROASTER_INCENDIARY(Elemental.INCENDIARY, "Roaster", Roaster(Elemental.INCENDIARY)), /* LAUNCHER */
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