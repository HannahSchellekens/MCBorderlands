package maliwan.mcbl.weapons.gun.parts

import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.gun.behaviour.GunBehaviour
import maliwan.mcbl.weapons.gun.behaviour.launcher.Blizzard
import maliwan.mcbl.weapons.gun.behaviour.launcher.Roaster
import maliwan.mcbl.weapons.gun.behaviour.pistol.Defiler
import maliwan.mcbl.weapons.gun.behaviour.pistol.Tinderbox
import maliwan.mcbl.weapons.gun.behaviour.smg.HellFire
import maliwan.mcbl.weapons.gun.behaviour.sniper.Volcano

/**
 * @author Hannah Schellekens
 */
// Needs to be a separate enum class for serialization.
// This way behaviour objects do not need to be deserialized.
enum class Capacitor(elemental: Elemental, val partName: String, vararg behaviours: GunBehaviour) {

    NONE(Elemental.PHYSICAL, "None"),
    INCENDIARY(Elemental.INCENDIARY, "Incendiary"),
    SHOCK(Elemental.SHOCK, "Shock"),
    CORROSIVE(Elemental.CORROSIVE, "Corrosive"),
    SLAG(Elemental.SLAG, "Slag"),
    EXPLOSIVE(Elemental.EXPLOSIVE, "Explosive"),
    CRYO(Elemental.CRYO, "Cryo"),

    // Unique capacitors.
    BLIZZARD(Elemental.CRYO, "Blizzard", Blizzard()), /* LAUNCHER */
    DEFILER(Elemental.CORROSIVE, "Defiler", Defiler()), /* PISTOL */
    HELLFIRE(Elemental.INCENDIARY, "HellFire", HellFire()), /* SMG */
    TINDERBOX(Elemental.INCENDIARY, "Tinderbox", Tinderbox()), /* PISTOL */
    ROASTER_SHOCK(Elemental.SHOCK, "Roaster", Roaster(Elemental.SHOCK)), /* LAUNCHER */
    ROASTER_INCENDIARY(Elemental.INCENDIARY, "Roaster", Roaster(Elemental.INCENDIARY)), /* LAUNCHER */
    VOLCANO(Elemental.INCENDIARY, "Volcano", Volcano()), /* SNIPER */
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
            EXPLOSIVE,
            CRYO
        )

        val legendaryCapacitors = setOf(
            HELLFIRE,
            DEFILER,
            VOLCANO
        )
    }
}