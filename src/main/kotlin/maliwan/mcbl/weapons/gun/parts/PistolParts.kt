package maliwan.mcbl.weapons.gun.parts

import maliwan.mcbl.loot.lootPoolOf
import maliwan.mcbl.loot.toUniformLootPool
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.StatModifier
import maliwan.mcbl.weapons.gun.StatModifier.Property
import maliwan.mcbl.weapons.gun.WeaponPart
import maliwan.mcbl.weapons.gun.behaviour.GunBehaviour
import maliwan.mcbl.weapons.gun.behaviour.pistol.*
import maliwan.mcbl.weapons.gun.statModifierList

/**
 * @author Hannah Schellekens
 */
object PistolParts {

    /**
     * @author Hannah Schellekens
     */
    enum class Barrel(
        override val manufacturer: Manufacturer,
        override val partName: String,
        override val statModifiers: List<StatModifier> = emptyList(),
        override val manufacturerStatModifiers: List<StatModifier> = emptyList(),
        override val behaviours: List<GunBehaviour> = emptyList()
    ) : WeaponPart {

        ATLAS(
            Manufacturer.ATLAS,
            Manufacturer.ATLAS.displayName,
            statModifierList {
                add(0.014, Property.HOMING_STRENGTH)
                multiply(1.05, Property.BASE_DAMAGE)
                multiply(1.05, Property.FIRE_RATE)
                add(0.005, Property.ACCURACY)
            },
            statModifierList {
                add(0.007, Property.HOMING_STRENGTH)
                multiply(1.25, Property.BASE_DAMAGE)
                multiply(1.15, Property.FIRE_RATE)
                add(0.015, Property.ACCURACY)
            }
        ),

        BANDIT(
            Manufacturer.BANDIT,
            Manufacturer.BANDIT.displayName,
            statModifierList {
                subtract(0.008, Property.ACCURACY)
                multiply(1.06, Property.BASE_DAMAGE)
            },
            statModifierList {
                multiply(1.05, Property.BASE_DAMAGE)
                add(0.0015, Property.RECOIL)
            }
        ),

        DAHL(
            Manufacturer.DAHL,
            Manufacturer.DAHL.displayName,
            statModifierList {
                add(0.015, Property.ACCURACY)
                add(0.0025, Property.RECOIL)
                divide(1.09, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.008, Property.ACCURACY)
                multiply(1.1, Property.BASE_DAMAGE)
                add(1, Property.BURST_COUNT)
            }
        ),

        HYPERION(
            Manufacturer.HYPERION,
            Manufacturer.HYPERION.displayName,
            statModifierList {
                add(0.3, Property.BONUS_CRIT_MULTIPLIER)
                add(0.0015, Property.RECOIL)
                divide(1.14, Property.MAGAZINE_SIZE)
                add(0.018, Property.ACCURACY)
                divide(1.12, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.1, Property.BONUS_CRIT_MULTIPLIER)
                add(0.003, Property.ACCURACY)
                multiply(1.1, Property.BASE_DAMAGE)
            }
        ),

        JAKOBS(
            Manufacturer.JAKOBS,
            Manufacturer.JAKOBS.displayName,
            statModifierList {
                divide(1.36, Property.FIRE_RATE)
                subtract(0.004, Property.RECOIL)
                add(0.017, Property.ACCURACY)
                multiply(1.18, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.0004, Property.RECOIL)
                add(0.003, Property.ACCURACY)
                multiply(1.15, Property.BASE_DAMAGE)
            }
        ),

        MALIWAN(
            Manufacturer.MALIWAN,
            Manufacturer.MALIWAN.displayName,
            statModifierList {
                add(0.005, Property.ACCURACY)
                add(0.001, Property.RECOIL)
                multiply(1.15, Property.ELEMENTAL_DAMAGE)
            },
            statModifierList {
                add(0.003, Property.ACCURACY)
                multiply(1.095, Property.ELEMENTAL_DAMAGE)
                add(0.1, Property.ELEMENTAL_CHANCE)
                multiply(1.1, Property.BASE_DAMAGE)
            }
        ),

        TEDIORE(
            Manufacturer.TEDIORE,
            Manufacturer.TEDIORE.displayName,
            statModifierList {
                subtract(5, Property.PROJECTILE_SPEED)
            }
        ),

        TORGUE(
            Manufacturer.TORGUE,
            Manufacturer.TORGUE.displayName,
            statModifierList {
                divide(1.09, Property.FIRE_RATE)
                subtract(0.002, Property.RECOIL)
                subtract(0.019, Property.ACCURACY)
                multiply(1.25, Property.RELOAD_SPEED)
                multiply(1.24, Property.BASE_DAMAGE)
                subtract(0.006, Property.GRAVITY)
                subtract(5, Property.PROJECTILE_SPEED)
            },
            statModifierList {
                subtract(0.05, Property.FIRE_RATE)
                multiply(1.2, Property.BASE_DAMAGE)
                add(3, Property.MAGAZINE_SIZE)
            }
        ),

        VLADOF(
            Manufacturer.VLADOF,
            Manufacturer.VLADOF.displayName,
            statModifierList {
                multiply(1.3, Property.FIRE_RATE)
                add(-0.001, Property.RECOIL)
                multiply(1.28, Property.MAGAZINE_SIZE)
                add(-0.025, Property.ACCURACY)
            },
            statModifierList {
                multiply(1.2, Property.MAGAZINE_SIZE)
                multiply(1.1, Property.BASE_DAMAGE)
            }
        ),

        // Unique barrels.

        FIBBER_CRIT(Manufacturer.BANDIT, "Fibber", behaviours = listOf(FibberCrit())),
        FIBBER_SHOTGUN(Manufacturer.BANDIT, "Fibber", behaviours = listOf(FibberShotgun())),
        FIBBER_RICOCHET(Manufacturer.BANDIT, "Fibber", behaviours = listOf(FibberRicochet())),
        GREED(Manufacturer.JAKOBS, "Greed", behaviours = listOf(Greed())),
        GUB(Manufacturer.BANDIT, "Gub", behaviours = listOf(Gub())),
        GWENS_HEAD(Manufacturer.HYPERION, "Gwen's Head", behaviours = listOf(GwensHead())),
        HECTORS_PARADISE(Manufacturer.DAHL, "Hector's Paradise", behaviours = listOf(HectorsParadise())),
        HELLSHOCK(Manufacturer.MALIWAN, "Hellshock", behaviours = listOf(Hellshock())),
        HORNET(Manufacturer.DAHL, "Hornet", behaviours = listOf(Hornet())),
        INFINITY(Manufacturer.VLADOF, "Infinity", behaviours = listOf(Infinity())),
        JUDGE(Manufacturer.TEDIORE, "Judge", behaviours = listOf(Judge())),
        LADY_FINGER(Manufacturer.DAHL, "Lady Finger", behaviours = listOf(LadyFinger())),
        LADY_FIST(Manufacturer.DAHL, "Lady Fist", behaviours = listOf(LadyFist())),
        LAW(Manufacturer.DAHL, "Law", behaviours = listOf(Law())),
        LOGANS_GUN(Manufacturer.HYPERION, "Logan's Gun", behaviours = listOf(LogansGun())),
        POCKET_ROCKET(Manufacturer.TORGUE, "Pocket Rocket", behaviours = listOf(PocketRocket())),
        REX(Manufacturer.JAKOBS, "Rex", behaviours = listOf(Rex())),
        RUBI(Manufacturer.MALIWAN, "Rubi", behaviours = listOf(Rubi())),
        STINGER(Manufacturer.VLADOF, "Stinger", behaviours = listOf(Stinger())),
        TEAPOT(Manufacturer.TEDIORE, "Teapot", behaviours = listOf(Teapot())),
        THUNDERBALL_FISTS(Manufacturer.MALIWAN, "Thunderball Fists", behaviours = listOf(ThunderballFists())),
        UNKEMPT_HAROLD(Manufacturer.TORGUE, "Unkempt Harold", behaviours = listOf(UnkemptHarold())),
        WANDERLUST(Manufacturer.MALIWAN, "Wandelust", behaviours = listOf(Wanderlust())),
        ;

        override val partTypeName: String = "Barrel"
        override val weaponClass: WeaponClass = WeaponClass.PISTOL

        companion object {

            val commonBarrels = setOf(
                MALIWAN, VLADOF, BANDIT, DAHL, HYPERION, JAKOBS, TEDIORE, TORGUE, ATLAS
            )

            val commonLootPool = lootPoolOf(
                ATLAS to 8,
                BANDIT to 10,
                DAHL to 10,
                HYPERION to 10,
                JAKOBS to 10,
                MALIWAN to 10,
                TEDIORE to 10,
                TORGUE to 10,
                VLADOF to 10,
            )
        }
    }

    /**
     * @author Hannah Schellekens
     */
    enum class Grip(
        override val manufacturer: Manufacturer,
        override val partName: String,
        override val statModifiers: List<StatModifier> = emptyList(),
        override val manufacturerStatModifiers: List<StatModifier> = emptyList(),
    ) : WeaponPart {

        ATLAS(
            Manufacturer.ATLAS,
            Manufacturer.ATLAS.displayName,
            statModifierList {
                add(1, Property.MAGAZINE_SIZE)
                divide(1.1, Property.RELOAD_SPEED)
                add(0.0008, Property.RECOIL)
                multiply(1.05, Property.FIRE_RATE)
                add(0.005, Property.ACCURACY)
            },
            statModifierList {
                add(5, Property.MAGAZINE_SIZE)
                divide(1.35, Property.RELOAD_SPEED)
                add(0.002, Property.RECOIL)
                multiply(1.15, Property.FIRE_RATE)
                add(0.025, Property.ACCURACY)
                multiply(1.15, Property.BASE_DAMAGE)
                add(0.007, Property.HOMING_STRENGTH)
            }
        ),

        BANDIT(
            Manufacturer.BANDIT,
            Manufacturer.BANDIT.displayName,
            statModifierList {
                multiply(1.35, Property.MAGAZINE_SIZE)
                multiply(1.1, Property.RELOAD_SPEED)
                subtract(0.008, Property.ACCURACY)
            },
            statModifierList {
                add(4, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
            }
        ),

        DAHL(
            Manufacturer.DAHL,
            Manufacturer.DAHL.displayName,
            statModifierList {
                add(0.005, Property.ACCURACY)
                add(0.001, Property.RECOIL)
                divide(1.06, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(3, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
            }
        ),

        HYPERION(
            Manufacturer.HYPERION,
            Manufacturer.HYPERION.displayName,
            statModifierList {
                add(0.011, Property.ACCURACY)
                divide(1.09, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(3, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
            }
        ),

        JAKOBS(
            Manufacturer.JAKOBS,
            Manufacturer.JAKOBS.displayName,
            statModifierList {
                divide(1.09, Property.FIRE_RATE)
                subtract(0.001, Property.RECOIL)
                multiply(1.05, Property.RELOAD_SPEED)
                multiply(1.12, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.0005, Property.RECOIL)
                divide(1.3, Property.RELOAD_SPEED)
                add(1, Property.MAGAZINE_SIZE)
            }
        ),

        MALIWAN(
            Manufacturer.MALIWAN,
            Manufacturer.MALIWAN.displayName,
            statModifierList {
                multiply(1.2, Property.ELEMENTAL_CHANCE)
                divide(1.15, Property.RELOAD_SPEED)
            },
            statModifierList {
                add(3, Property.MAGAZINE_SIZE)
                divide(1.217, Property.RELOAD_SPEED)
            }
        ),

        TEDIORE(
            Manufacturer.TEDIORE,
            Manufacturer.TEDIORE.displayName,
            statModifierList {
                divide(1.12, Property.MAGAZINE_SIZE)
                divide(1.2, Property.RELOAD_SPEED)
                divide(1.06, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(2, Property.MAGAZINE_SIZE)
                subtract(-3, Property.RELOAD_SPEED)
            }
        ),

        TORGUE(
            Manufacturer.TORGUE,
            Manufacturer.TORGUE.displayName,
            statModifierList {
                subtract(0.001, Property.RECOIL)
                multiply(1.1, Property.RELOAD_SPEED)
                multiply(1.09, Property.BASE_DAMAGE)
                subtract(0.006, Property.ACCURACY)
            },
            statModifierList {
                divide(1.3, Property.RELOAD_SPEED)
                add(3, Property.MAGAZINE_SIZE)
            }
        ),

        VLADOF(
            Manufacturer.VLADOF,
            Manufacturer.VLADOF.displayName,
            statModifierList {
                multiply(1.12, Property.FIRE_RATE)
                subtract(0.018, Property.ACCURACY)
                divide(1.06, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(4, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
            }
        ),
        ;

        override val partTypeName: String = "Grip"
        override val weaponClass: WeaponClass = WeaponClass.PISTOL

        companion object {

            val commonLootPool = lootPoolOf(
                ATLAS to 8,
                BANDIT to 10,
                DAHL to 10,
                HYPERION to 10,
                JAKOBS to 10,
                MALIWAN to 10,
                TEDIORE to 10,
                TORGUE to 10,
                VLADOF to 10,
            )
        }
    }

    /**
     * @author Hannah Schellekens
     */
    enum class Accessory(
        override val manufacturer: Manufacturer,
        override val partName: String,
        override val statModifiers: List<StatModifier> = emptyList(),
        override val manufacturerStatModifiers: List<StatModifier> = emptyList()
    ) : WeaponPart {

        ACCURACY(
            Manufacturer.NONE,
            "Accuracy",
            statModifierList {
                add(0.015, Property.ACCURACY)
                multiply(1.1, Property.PROJECTILE_SPEED)
            }
        ),

        BAYONET(
            Manufacturer.NONE,
            "Bayonet",
            statModifierList {
                add(2.5, Property.MELEE)
            }
        ),

        DOUBLE(
            Manufacturer.NONE,
            "Double",
            statModifierList {
                divide(1.3, Property.FIRE_RATE)
                subtract(0.002, Property.RECOIL)
                multiply(1.28, Property.MAGAZINE_SIZE)
                add(2, Property.MAGAZINE_SIZE)
                subtract(0.012, Property.ACCURACY)
                multiply(2.0, Property.PELLET_COUNT)
                multiply(2.0, Property.AMMO_PER_SHOT)
                divide(1.15, Property.BASE_DAMAGE)
            }
        ),

        STOCK(
            Manufacturer.NONE,
            "Stock",
            statModifierList {
                add(0.018, Property.ACCURACY)
                add(0.002, Property.RECOIL)
            }
        ),

        MAGAZINE(
            Manufacturer.NONE,
            "Magazine",
            statModifierList {
                multiply(1.56, Property.MAGAZINE_SIZE)
                multiply(1.15, Property.RELOAD_SPEED)
            }
        ),

        DAMAGE(
            Manufacturer.NONE,
            "Damage",
            statModifierList {
                subtract(0.002, Property.RECOIL)
                multiply(1.18, Property.BASE_DAMAGE)
            }
        ),

        FIRE_RATE(
            Manufacturer.NONE,
            "Fire Rate",
            statModifierList {
                multiply(1.18, Property.FIRE_RATE)
                divide(1.03, Property.BASE_DAMAGE)
            }
        ),

        MASHER(
            Manufacturer.JAKOBS,
            "Masher",
            statModifierList {
                add(4, Property.PELLET_COUNT)
                subtract(0.04, Property.ACCURACY)
                divide(1.8, Property.BASE_DAMAGE)
                subtract(0.007, Property.RECOIL)
                divide(1.5, Property.MAGAZINE_SIZE)
                divide(1.1, Property.FIRE_RATE)
            }
        ),
        ;

        override val partTypeName: String = "Acc."
        override val weaponClass: WeaponClass = WeaponClass.PISTOL

        companion object {

            val commonAccessories = setOf(
                ACCURACY,
                BAYONET,
                DOUBLE,
                STOCK,
                MAGAZINE,
                DAMAGE,
                FIRE_RATE
            )

            val commonJakobsAccessories = commonAccessories + setOf(
                MASHER
            )

            val commonLootPool = commonAccessories.toUniformLootPool()
        }
    }
}