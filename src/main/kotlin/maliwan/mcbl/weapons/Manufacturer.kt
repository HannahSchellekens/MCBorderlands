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
}