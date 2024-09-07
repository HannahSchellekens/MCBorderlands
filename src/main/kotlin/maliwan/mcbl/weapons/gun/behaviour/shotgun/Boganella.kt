package maliwan.mcbl.weapons.gun.behaviour.shotgun

import maliwan.mcbl.util.Probability
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.PostGenerationBehaviour
import maliwan.mcbl.weapons.gun.behaviour.TalkWhenFiring
import maliwan.mcbl.weapons.gun.behaviour.TalkWhenReloading
import maliwan.mcbl.weapons.gun.behaviour.UniqueGun
import maliwan.mcbl.weapons.gun.parts.ShotgunParts

/**
 * @author Hannah Schellekens
 */
open class Boganella : UniqueGun, PostGenerationBehaviour, TalkWhenFiring, TalkWhenReloading {

    override val baseName = "Boganella"
    override val redText = "Rack off ya dag!"

    override val talkChanceFiring = Probability(0.5)
    override val talkMessagesFiring = listOf(
        "Smash the f***!!",
        "F***ing take it!!",
        "Yeah! I'm getting a f***ng lady boner!",
        "I'm bloody firing!",
        "Get in theeeeere",
        "Yeah that's not a gun-- I'M A GUN!",
        "I bloody love it!",
        "I'm a f***ing GUN!!",
        "C'mon, F*** YEAH!!",
        "Get a piece of me!",
        "Get 'em!",
        "Yeh, get into 'em!",
        "Cop that!",
        "Have a go, ya mug!",
        "Get that F***ing up ya!",
        "F**kin' FERAL!",
        "Get  into 'em proper!",
        "Wear that on yer head!",
        "F**k you in the c**t and the *** and the f**kin's s**m p***e!",
    )

    override val talkChanceReloading = Probability(0.8)
    override val talkMessagesReloading = listOf(
        "Yeah, that's right!",
        "Yeaaaahhhh!! Get me ready!",
        "Am I too much f***ing gun for ya?!",
        "Pump more in me!",
        "I need another!",
        "Put another in me!",
        "Haw, do me!",
        "Jam another in me!",
        "Fill me up!",
        "F**kin' stick it in!",
    )

    override fun onFinishGeneration(properties: GunProperties, assembly: WeaponAssembly) {
        ShotgunParts.Barrel.BANDIT.applyStatModifiers(properties)
        statModifiers.applyAll(properties)
    }

    companion object {

        val statModifiers = statModifierList {
            multiply(1.1, StatModifier.Property.BASE_DAMAGE)
            add(2, StatModifier.Property.MAGAZINE_SIZE)
            multiply(1.1, StatModifier.Property.FIRE_RATE)
            multiply(1.1, StatModifier.Property.ACCURACY)
            add(2, StatModifier.Property.PELLET_COUNT)
        }
    }
}