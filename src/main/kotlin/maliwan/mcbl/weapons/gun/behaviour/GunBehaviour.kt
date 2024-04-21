package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.BulletMeta
import maliwan.mcbl.weapons.WeaponEventHandler
import maliwan.mcbl.weapons.gun.GunExecution
import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.WeaponAssembly
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile

/**
 * Classes implementing this interface provide extra behaviour to guns outside the generic
 * property framework.
 */
interface GunBehaviour

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
     * @param execution
     *          From which execution the shot was.
     * @param player
     *          Who shot the gun.
     */
    fun afterGunShot(execution: GunExecution, player: Player)
}

/**
 * Adds behaviour to bullets landing, after the regular processing.
 */
interface PostBulletLandBehaviour : GunBehaviour {

    /**
     * Gets called whenever a bullet lands.
     *
     * @param bullet
     *          The bullet entity that landed.
     * @param meta
     *          The meta information about the bullet.
     */
    fun afterBulletLands(bullet: Entity, meta: BulletMeta)
}

/**
 * Adds effects to firing the bullet.
 */
interface BulletEffectBehaviour : GunBehaviour {

    /**
     * Schedules a new bullet effect.
     *
     * @param handler
     *          The weapon handler for the plugin.
     * @param bullet
     *          The bullet that was shot.
     * @param bulletMeta
     *          Metadata for this bullet.
     */
    fun scheduleEffects(handler: WeaponEventHandler, bullet: Entity, bulletMeta: BulletMeta)
}

/**
 * Post processor for after the gun execution has been initialized.
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
 * Gets called after the gun has been generated.
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
 * Determines which type of projectile the gun shoots.
 */
interface BulletTypeProvider: GunBehaviour {

    /**
     * The type of bullet that gets shot.
     * Must be a [Projectile].
     */
    val bulletType: EntityType
}

/**
 * Gun has a custom base name.
 */
interface CustomBaseNameProvider : GunBehaviour {

    /**
     * The custom base name of this gun.
     */
    val baseName: String

    fun applyToGun(properties: GunProperties) {
        properties.name = baseName
    }
}

/**
 * Gun has custom red text.
 */
interface RedTextProvider : GunBehaviour {

    /**
     * The red text of the gun.
     */
    val redText: String
}

/**
 * Gun has custom cyan text.
 */
interface CyanTextProvider : GunBehaviour {

    /**
     * The cyan text of the gun.
     */
    val cyanText: String
}

/**
 * Gun has unique properties.
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
 * Behaviour before a player reloaded.
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
