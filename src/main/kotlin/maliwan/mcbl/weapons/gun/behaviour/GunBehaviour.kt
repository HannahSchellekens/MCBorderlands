package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.util.plugin.Probability
import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.CustomGrenadeManager
import maliwan.mcbl.weapons.WeaponDamageType
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.GunExecution
import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.WeaponAssembly
import maliwan.mcbl.weapons.gun.pattern.BulletPatternProcessor
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.*

/**
 * Classes implementing this interface provide extra behaviour to guns outside the generic
 * property framework.
 */
interface GunBehaviour

/**
 * When a gun adds a specific kind of [GunExecution] implementation.
 */
interface CustomGunExecutionBehaviour<T : GunExecution> {

    /**
     * Creates a new gun execution based on the given properties.
     *
     * @param gunProperties The properties to base this execution on.
     */
    fun createGunExecution(gunProperties: GunProperties): T
}

/**
 * Adds behaviour for just before a gun is shot.
 */
interface PreGunShotBehaviour : GunBehaviour {

    /**
     * Gets called just before a gun is shot.
     *
     * @param execution
     *          From which execution the shot was.
     * @param player
     *          Who shot the gun.
     */
    fun beforeGunShot(execution: GunExecution, player: Player)
}

/**
 * Adds behaviour for just after a gun is shot.
 */
interface PostGunShotBehaviour : GunBehaviour {

    /**
     * Gets called just after a gun is shot.
     *
     * @param handler
     *          Weapon handler of the plugin that handled this shot.
     * @param execution
     *          From which execution the shot was.
     * @param bullets
     *          The bullet entities that were shot (one entity per pellet).
     * @param player
     *          Who shot the gun.
     */
    fun afterGunShot(handler: WeaponEventHandler, execution: GunExecution, bullets: List<Entity>, player: Player)
}

/**
 * Adds behaviour after a weapon kills an entity.
 */
interface PostKillBehaviour : GunBehaviour {

    /**
     * Gets called after an entity is killed or will be killed the next game tick.
     *
     * @param handler Weapon handler of the plugin that handled this shot
     * @param killer Who killed.
     * @param target Who got killed.
     * @param gunExecution The gun execution of the weapon that is used to kill.
     * @param damageType What kind of damage ended up killing the target.
     */
    fun afterKill(
        handler: WeaponEventHandler,
        killer: Player,
        target: LivingEntity,
        gunExecution: GunExecution,
        damageType: WeaponDamageType
    )
}

/**
 * Use a custom bullet pattern for the gun.
 *
 * See the [maliwan.mcbl.weapons.gun.pattern] package for available bullet patterns.
 */
interface BulletPatternProvider : GunBehaviour {

    /**
     * The bullet pattern to use for bullets fired from this gun.
     */
    val bulletPatternProcessor: BulletPatternProcessor
}

/**
 * Adds a preprocessor for bullets after they have just landed, but before damage calculations and effects.
 */
interface PreBulletLandBehaviour : GunBehaviour {

    /**
     * Gets called just after a bullet has landed, but before the damage calculations start..
     *
     * @param bullet The bullet entity that landed.
     * @param meta The meta information about the bullet.
     * @param hitLocation The location where the bullet hit something.
     * @param targetEntity The target entity that was hit (directly) by the bullet.
     * @param wouldBeCritical Whether the shot would be a critical hit via default processing.
     */
    fun beforeBulletLands(
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        wouldBeCritical: Boolean
    )
}

/**
 * Adds behaviour to bullets landing, after the regular processing.
 */
interface PostBulletLandBehaviour : GunBehaviour {

    /**
     * Gets called whenever a bullet lands.
     *
     * @param handler The weapon handler for the plugin.
     * @param bullet The bullet entity that landed.
     * @param meta The meta information about the bullet.
     * @param hitLocation The location where the bullet hit something.
     * @param targetEntity The target entity that was hit (directly) by the bullet.
     * @param isCriticalHit Whether the shot is a critical hit or not.
     */
    fun afterBulletLands(
        handler: WeaponEventHandler,
        bullet: Entity,
        meta: BulletMeta,
        hitLocation: Location?,
        targetEntity: LivingEntity?,
        isCriticalHit: Boolean
    )
}

/**
 * Adds effects to firing the bullet.
 *
 * Used in combination with [WeaponEventHandler.scheduleEffect] to schedule a delayed bullet effect.
 */
interface BulletEffectBehaviour : GunBehaviour {

    /**
     * Schedules a new bullet effect.
     *
     * @param handler The weapon handler for the plugin.
     * @param bullet The bullet that was shot.
     * @param bulletMeta Metadata for this bullet.
     */
    fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta)
}

/**
 * Add functionality for after a bullet bounces.
 */
interface PostBulletBounceBehaviour : GunBehaviour {

    /**
     * Gets called after a bullet has bounced off a surface.
     * The amount of bounces left has been decreased before this method call.
     *
     * @param handler
     *          The weapon handler.
     * @param bullet
     *          The bullet that bounced off a surface.
     * @param bulletMeta
     *          The bullet meta of the bullet that bounced off a surface.
     */
    fun afterBulletBounce(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta)
}

/**
 * Post processor for after the [GunExecution] has been initialized.
 */
interface GunExecutionInitializationBehaviour : GunBehaviour {

    /**
     * Gets called whenever the gun execution is done initializing.
     *
     * @param gunExecution
     *          The execution that was created.
     * @param player
     *          The owner of the execution.
     */
    fun onInitializedGunExecution(gunExecution: GunExecution, player: Player)
}

/**
 * Post processor for assemblies allowing classes to modify the final weapon assembly.
 */
interface UpdateAssemblyBehaviour : GunBehaviour {

    /**
     * Gets called just after a weapon assembly is generated.
     *
     * @param assembly
     *          The generated assembly.
     * @return the modified assembly.
     */
    fun updateAssembly(assembly: WeaponAssembly): WeaponAssembly
}

/**
 * Adds behaviour for after the gun has been generated.
 * This behaviour is used by almost all gun behaviour to modify the base stats of the gun.
 */
interface PostGenerationBehaviour : GunBehaviour {

    /**
     * Gets called whenever the gun generator has finished generating this gun.
     *
     * @param properties
     *          The generated gun properties.
     * @param assembly
     *          The weapon assembly of the generated gun.
     */
    fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly)
}

/**
 * Determine which type of projectile the gun shoots.
 */
interface BulletTypeProvider : GunBehaviour {

    /**
     * The type of bullet that gets shot.
     * Must be a [Projectile].
     */
    val bulletType: EntityType
}

/**
 * Determine which sound to play when the gun fires.
 */
interface BulletSoundProvider : GunBehaviour {

    /**
     * The sound to play when the gun gets fired.
     */
    val shootSound: Sound
}

/**
 * Overrides the default gun name by this name.
 * Multiple gun parts with a custom name (should..) join all custom names together.
 */
interface CustomBaseNameProvider : GunBehaviour {

    /**
     * The custom base name of this gun.
     */
    val baseName: String
}

/**
 * Provides cyan flavour text for the weapon card.
 */
interface RedTextProvider : GunBehaviour {

    /**
     * The red text of the gun.
     */
    val redText: String
}

/**
 * Provides cyan flavour text for the weapon card.
 */
interface CyanTextProvider : GunBehaviour {

    /**
     * The cyan text of the gun.
     */
    val cyanText: String
}

/**
 * A unique gun has a custom name and rex text: a combination of [CustomBaseNameProvider] and [RedTextProvider].
 */
interface UniqueGun : CustomBaseNameProvider, RedTextProvider

/**
 * Gun has a default prefix for gun names that should be used
 * when no accessory is present.
 */
interface DefaultPrefixProvider : GunBehaviour {

    /**
     * The default prefix for the gun when no accessory is present.
     */
    val defaultPrefix: String
}

/**
 * Adds behaviour before and/or after a gun gets reloaded.
 */
interface ReloadBehaviour : GunBehaviour {

    /**
     * Gets called before a player reloads their gun.
     * Does not get called when the player cannot reload their gun.
     *
     * @param player
     *          The player who reloaded their gun.
     * @param gunExecution
     *          The gun execution of the reloaded gun.
     */
    fun beforeReload(player: Player, gunExecution: GunExecution) = Unit

    /**
     * Gets called after a player reloaded their gun.
     *
     * @param player
     *          The player who reloaded their gun.
     * @param gunExecution
     *          The gun execution of the reloaded gun.
     */
    fun afterReload(player: Player, gunExecution: GunExecution) = Unit
}

/**
 * Provides a modified [CustomGrenadeManager.CustomGrenade] that is being thrown on a Tediore-reload
 * (see[GrenadeOnReload]).
 */
interface UpdateReloadGrenadeBehaviour : GunBehaviour {

    /**
     * Updates the custom grenade that is being thrown on a Tediore-reload.
     * Called just before the grenade is thrown.
     */
    fun updateReloadGrenade(grenade: CustomGrenadeManager.CustomGrenade)
}

/**
 * Makes the gun talk (send chag messages) when firing.
 */
interface TalkWhenFiring : GunBehaviour {

    /**
     * For every shot the chance to talk to the user.
     */
    val talkChanceFiring: Probability

    /**
     * The messages to pick from.
     */
    val talkMessagesFiring: List<String>

    /**
     * The message to send
     */
    fun messageFiring(): String? = if (talkChanceFiring.roll()) {
        talkMessagesFiring.random()
    }
    else null
}

/**
 * Makes the gun talk (send chag messages) when reloading.
 */
interface TalkWhenReloading : GunBehaviour {

    /**
     * For every reload the chance to talk to the user.
     */
    val talkChanceReloading: Probability

    /**
     * The messages to pick from.
     */
    val talkMessagesReloading: List<String>

    /**
     * The message to send
     */
    fun messageReloading(): String? = if (talkChanceReloading.roll()) {
        talkMessagesReloading.random()
    }
    else null
}

/**
 * Makes the visuals on the weapon card show false information.
 */
interface FibWeaponCard : GunBehaviour {

    /**
     * With what to multiply the original values with.
     */
    val fibMultiplierBase: Double

    /**
     * The maximum deviation from the [fibMultiplierBase].
     */
    val fibMultiplierFuzz: Double

    /**
     * Whether to show gun effect hints that are generated based on the gun stats.
     * For example: manufacturer gimmicks, transfusion percentages, bonus elemental damage...
     */
    val showGeneratedInfo: Boolean
}

/**
 * Provides a differnt name for the manufacturer shown on the weapon card.
 * This is purely visual. The actual manufacturer does not change.
 */
interface OverrideManufacturerOnWeaponCard : GunBehaviour {

    /**
     * Get the manufacturer name that must be shown on the weapon card.
     *
     * @param oldName The original manufacturer name.
     * @return The new manufacturer name on the weapon card.
     */
    fun newManufacturerName(oldName: String): String
}

inline fun <reified T> List<GunBehaviour>.forEachType(action: (T) -> Unit) {
    forEach { behaviour ->
        if (behaviour is T) {
            action(behaviour)
        }
    }
}

inline fun <reified T : GunBehaviour, U> List<GunBehaviour>.first(producer: (T) -> U?): U? {
    forEach { behaviour ->
        if (behaviour is T) {
            val item = producer(behaviour)
            if (item != null) {
                return item
            }
        }
    }
    return null
}
