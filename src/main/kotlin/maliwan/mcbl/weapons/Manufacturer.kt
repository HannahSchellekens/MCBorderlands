package maliwan.mcbl.weapons

/**
 * @author Hannah Schellekens
 */
enum class Manufacturer(
    val displayName: String
) {

    MALIWAN("Maliwan"),
    S_AND_S_MUNITIONS("S&S Munitions"),
    ATLAS("Atlas"),
    HYPERION("Hyperion"),
    VLADOF("Vladof"),
    TEDIORE("Tediore"),
    BANDIT("Bandit"),
    DAHL("Dahl"),
    TORGUE("TORGUE"),
    ERIDIAN("Eridian"),
    JAKOBS("Jakobs"),
    ANSHIN("Anshin"),
    PANGOLIN("Pangolin"),
    NONE(""),
    ;

    val id: String
        get() = name

    companion object {

        val pistolProducers = setOf(MALIWAN, HYPERION, VLADOF, TEDIORE, BANDIT, DAHL, TORGUE, JAKOBS)

        /**
         * Set of manufacturers that make weapons of class `weaponClass`.
         */
        fun producersOf(weaponClass: WeaponClass) = when (weaponClass) {
            WeaponClass.PISTOL -> pistolProducers
            else -> emptySet()
        }
    }
}