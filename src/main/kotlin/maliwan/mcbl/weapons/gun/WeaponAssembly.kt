package maliwan.mcbl.weapons.gun

import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.behaviour.GrenadeOnReload
import maliwan.mcbl.weapons.gun.behaviour.GunBehaviour
import maliwan.mcbl.weapons.gun.behaviour.RocketLauncher
import maliwan.mcbl.weapons.gun.behaviour.forEachType
import maliwan.mcbl.weapons.gun.names.*
import maliwan.mcbl.weapons.gun.parts.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * An amalgamation of weapon parts into a single gun.
 *
 * @author Hannah Schellekens
 */
sealed class WeaponAssembly(

    /**
     * Type of weapon.
     */
    val weaponClass: WeaponClass,

    /**
     * The main manufacturer of the weapon: i.e. the body.
     */
    val manufacturer: Manufacturer,

    /**
     * All weapon parts of this gun.
     */
    @Transient
    open val parts: List<WeaponPart>?,

    /**
     * The elemental capacitor of the gun.
     */
    val capacitor: Capacitor? = null
) {

    /**
     * The weapon name.
     */
    abstract val gunName: String

    open val behaviours: List<GunBehaviour>
        get() {
            val mainParts = parts?.flatMap { it.behaviours } ?: emptyList()
            val capacitorBehaviour = capacitor?.behaviours ?: emptyList()
            return mainParts + capacitorBehaviour + manufacturerGimmickBehaviours()
        }

    constructor(weaponClass: WeaponClass, manufacturer: Manufacturer, capacitor: Capacitor?, vararg parts: WeaponPart?)
            : this(weaponClass, manufacturer, parts.filterNotNull(), capacitor)

    /**
     * Get all behaviours from the manufacturer's gimmkicks.
     */
    fun manufacturerGimmickBehaviours() = when (manufacturer) {
        Manufacturer.TEDIORE -> listOf(GrenadeOnReload())
        else -> emptyList()
    }

    /**
     * Applies the stat modifiers of all weapon parts of this gun to the given gun properties.
     */
    open fun applyModifiersToGun(properties: GunProperties): GunProperties {
        parts?.forEach {
            it.statModifiers.applyAll(properties)
            if (it.manufacturer == manufacturer) {
                it.manufacturerStatModifiers.applyAll(properties)
            }
            it.otherManufacturerStatModifiers[manufacturer]?.applyAll(properties)
        }
        return properties
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> partOfType(klass: KClass<*>): T? {
        if (klass == Capacitor::class) {
            return capacitor as T?
        }
        return parts?.find { it::class.isSubclassOf(klass) } as? T
    }
}

inline fun <reified T> WeaponAssembly.forEachBehaviour(action: (T) -> Unit) {
    behaviours.forEachType<T> { action(it) }
}

/**
 * @author Hannah Schellekens
 */
class PistolAssembly(
    val body: Manufacturer,
    val barrel: PistolParts.Barrel,
    val grip: PistolParts.Grip,
    val accessory: PistolParts.Accessory? = null,
    capacitor: Capacitor? = null
) : WeaponAssembly(WeaponClass.PISTOL, body, capacitor, barrel, grip, accessory) {

    override val parts: List<WeaponPart>
        get() = listOfNotNull(barrel, grip, accessory)

    override val gunName: String
        get() = PistolNames.nameOf(manufacturer, barrel, accessory, capacitor)

    override fun toString(): String {
        return "PistolAssembly(body=$body, barrel=$barrel, grip=$grip, accessory=$accessory)"
    }
}

/**
 * @author Hannah Schellekens
 */
class ShotgunAssembly(
    val body: Manufacturer,
    val barrel: ShotgunParts.Barrel,
    val grip: ShotgunParts.Grip,
    val stock: ShotgunParts.Stock,
    val accessory: ShotgunParts.Accessory? = null,
    capacitor: Capacitor? = null
) : WeaponAssembly(WeaponClass.SHOTGUN, body, capacitor, barrel, grip, stock, accessory) {

    override val parts: List<WeaponPart>
        get() = listOfNotNull(barrel, grip, stock, accessory)

    override val gunName: String
        get() = ShotgunNames.nameOf(manufacturer, barrel, accessory, capacitor)

    override fun toString(): String {
        return "ShotgunAssembly(body=$body, barrel=$barrel, grip=$grip, stock=$stock, accessory=$accessory)"
    }
}

/**
 * @author Hannah Schellekens
 */
class SniperAssembly(
    val body: Manufacturer,
    val barrel: SniperParts.Barrel,
    val grip: SniperParts.Grip,
    val stock: SniperParts.Stock,
    val accessory: SniperParts.Accessory? = null,
    capacitor: Capacitor? = null
) : WeaponAssembly(WeaponClass.SNIPER, body, capacitor, barrel, grip, stock, accessory) {

    override val parts: List<WeaponPart>
        get() = listOfNotNull(barrel, grip, stock, accessory)

    override val gunName: String
        get() = SniperNames.nameOf(manufacturer, barrel, accessory, capacitor)

    override fun toString(): String {
        return "SniperAssembly(body=$body, barrel=$barrel, grip=$grip, stock=$stock, accessory=$accessory)"
    }
}

/**
 * @author Hannah Schellekens
 */
class SmgAssembly(
    val body: Manufacturer,
    val barrel: SmgParts.Barrel,
    val grip: SmgParts.Grip,
    val stock: SmgParts.Stock,
    val accessory: SmgParts.Accessory? = null,
    capacitor: Capacitor? = null
) : WeaponAssembly(WeaponClass.SMG, body, capacitor, barrel, grip, stock, accessory) {

    override val parts: List<WeaponPart>
        get() = listOfNotNull(barrel, grip, stock, accessory)

    override val gunName: String
        get() = SmgNames.nameOf(manufacturer, barrel, accessory, capacitor)

    override fun toString(): String {
        return "SmgAssembly(body=$body, barrel=$barrel, grip=$grip, stock=$stock, accessory=$accessory)"
    }
}

/**
 * @author Hannah Schellekens
 */
class AssaultRifleAssembly(
    val body: Manufacturer,
    val barrel: AssaultRifleParts.Barrel,
    val grip: AssaultRifleParts.Grip,
    val stock: AssaultRifleParts.Stock,
    val accessory: AssaultRifleParts.Accessory? = null,
    capacitor: Capacitor? = null
) : WeaponAssembly(WeaponClass.ASSAULT_RIFLE, body, capacitor, barrel, grip, stock, accessory) {

    override val parts: List<WeaponPart>
        get() = listOfNotNull(barrel, grip, stock, accessory)

    override val gunName: String
        get() = AssaultRifleNames.nameOf(manufacturer, barrel, accessory, capacitor, grip.customBaseName())

    override fun toString(): String {
        return "AssaultRifleAssembly(body=$body, barrel=$barrel, grip=$grip, stock=$stock, accessory=$accessory)"
    }
}

/**
 * @author Hannah Schellekens
 */
class LauncherAssembly(
    val body: Manufacturer,
    val barrel: LauncherParts.Barrel,
    val grip: LauncherParts.Grip,
    val exhaust: LauncherParts.Exhaust,
    val accessory: LauncherParts.Accessory? = null,
    capacitor: Capacitor? = null
) : WeaponAssembly(WeaponClass.LAUNCHER, body, capacitor, barrel, grip, exhaust, accessory) {

    override val behaviours: List<GunBehaviour>
        get() = super.behaviours + listOf(RocketLauncher())

    override val parts: List<WeaponPart>
        get() = listOfNotNull(barrel, grip, exhaust, accessory)

    override val gunName: String
        get() = LauncherNames.nameOf(manufacturer, barrel, accessory, capacitor)

    override fun toString(): String {
        return "LauncherAssembly(body=$body, barrel=$barrel, grip=$grip, exhaust=$exhaust, accessory=$accessory)"
    }
}

/**
 * Replaces a part in this assembly by another weapon part.
 */
fun WeaponAssembly.replacePart(part: WeaponPart) = when (this) {
    is PistolAssembly -> PistolAssembly(
        manufacturer,
        if (part is PistolParts.Barrel) part else barrel,
        if (part is PistolParts.Grip) part else grip,
        if (part is PistolParts.Accessory) part else accessory,
        capacitor
    )
    is ShotgunAssembly -> ShotgunAssembly(
        manufacturer,
        if (part is ShotgunParts.Barrel) part else barrel,
        if (part is ShotgunParts.Grip) part else grip,
        if (part is ShotgunParts.Stock) part else stock,
        if (part is ShotgunParts.Accessory) part else accessory,
        capacitor
    )
    is SniperAssembly -> SniperAssembly(
        manufacturer,
        if (part is SniperParts.Barrel) part else barrel,
        if (part is SniperParts.Grip) part else grip,
        if (part is SniperParts.Stock) part else stock,
        if (part is SniperParts.Accessory) part else accessory,
        capacitor
    )
    is SmgAssembly -> SmgAssembly(
        manufacturer,
        if (part is SmgParts.Barrel) part else barrel,
        if (part is SmgParts.Grip) part else grip,
        if (part is SmgParts.Stock) part else stock,
        if (part is SmgParts.Accessory) part else accessory,
        capacitor
    )
    is AssaultRifleAssembly -> AssaultRifleAssembly(
        manufacturer,
        if (part is AssaultRifleParts.Barrel) part else barrel,
        if (part is AssaultRifleParts.Grip) part else grip,
        if (part is AssaultRifleParts.Stock) part else stock,
        if (part is AssaultRifleParts.Accessory) part else accessory,
        capacitor
    )
    is LauncherAssembly -> LauncherAssembly(
        manufacturer,
        if (part is LauncherParts.Barrel) part else barrel,
        if (part is LauncherParts.Grip) part else grip,
        if (part is LauncherParts.Exhaust) part else exhaust,
        if (part is LauncherParts.Accessory) part else accessory,
        capacitor
    )
}

/**
 * Replaces the capacitor in this assembly by another capacitor.
 */
fun WeaponAssembly.replaceCapacitor(newCapacitor: Capacitor) = when (this) {
    is PistolAssembly -> PistolAssembly(manufacturer, barrel, grip, accessory, newCapacitor)
    is ShotgunAssembly -> ShotgunAssembly(manufacturer, barrel, grip, stock, accessory, newCapacitor)
    is SniperAssembly -> SniperAssembly(manufacturer, barrel, grip, stock, accessory, newCapacitor)
    is SmgAssembly -> SmgAssembly(manufacturer, barrel, grip, stock, accessory, newCapacitor)
    is AssaultRifleAssembly -> AssaultRifleAssembly(manufacturer, barrel, grip, stock, accessory, newCapacitor)
    is LauncherAssembly -> LauncherAssembly(manufacturer, barrel, grip, exhaust, accessory, newCapacitor)
}