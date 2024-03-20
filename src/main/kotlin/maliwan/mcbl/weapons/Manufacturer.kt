package maliwan.mcbl.weapons

/**
 * @author Hannah Schellekens
 */
interface Manufacturer {

    val displayName: String
}

/**
 * @author Hannah Schellekens
 */
enum class Manufacturers(
    override val displayName: String
) : Manufacturer {

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
    PANGOLIN("Pangolin")
    ;

    override fun toString(): String {
        return displayName
    }
}