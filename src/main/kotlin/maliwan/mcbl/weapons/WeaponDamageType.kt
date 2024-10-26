package maliwan.mcbl.weapons

/**
 * @author Hannah Schellekens
 */
enum class WeaponDamageType {

    /**
     * Direct bullet hit.
     */
    DIRECT,

    /**
     * Splash damage by elemental type (not explosive).
     */
    SPLASH_ELEMENTAL,

    /**
     * Explosive splash damage (not elemental, but explosive).
     */
    SPLASH_EXPLOSIVE,

    /**
     * Damage over time effect.
     */
    OVER_TIME,

    /**
     * Melee damage.
     */
    MELEE
    ;

    /**
     * Whether this is splash damage (`true`) or not (`false`).
     */
    val isSplash: Boolean
        get() = this == SPLASH_EXPLOSIVE || this == SPLASH_ELEMENTAL
}