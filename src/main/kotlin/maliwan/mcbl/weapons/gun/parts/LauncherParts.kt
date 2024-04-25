package maliwan.mcbl.weapons.gun.parts

import maliwan.mcbl.loot.toUniformLootPool
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.StatModifier
import maliwan.mcbl.weapons.gun.StatModifier.Property
import maliwan.mcbl.weapons.gun.WeaponPart
import maliwan.mcbl.weapons.gun.behaviour.Badaboom
import maliwan.mcbl.weapons.gun.behaviour.Creamer
import maliwan.mcbl.weapons.gun.behaviour.GunBehaviour
import maliwan.mcbl.weapons.gun.behaviour.TwelvePounder
import maliwan.mcbl.weapons.gun.statModifierList

/**
 * @author Hannah Schellekens
 */
object LauncherParts {

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

        BANDIT(
            Manufacturer.BANDIT,
            Manufacturer.BANDIT.displayName,
            statModifierList {
                multiply(1.06, Property.BASE_DAMAGE)
                subtract(0.015, Property.ACCURACY)
            },
            statModifierList {
                multiply(1.08, Property.BASE_DAMAGE)
            }
        ),

        MALIWAN(
            Manufacturer.MALIWAN,
            Manufacturer.MALIWAN.displayName,
            statModifierList {
                add(0.015, Property.ACCURACY)
                add(10, Property.PROJECTILE_SPEED)
                multiply(1.1, Property.ELEMENTAL_DAMAGE)
            },
            statModifierList {
                add(0.005, Property.ACCURACY)
                multiply(1.08, Property.BASE_DAMAGE)
            }
        ),

        TEDIORE(
            Manufacturer.TEDIORE,
            Manufacturer.TEDIORE.displayName,
            statModifierList {
                subtract(5, Property.PROJECTILE_SPEED)
            },
            statModifierList {
                multiply(1.08, Property.BASE_DAMAGE)
            }
        ),

        TORGUE(
            Manufacturer.TORGUE,
            Manufacturer.TORGUE.displayName,
            statModifierList {
                multiply(1.15, Property.BASE_DAMAGE)
                subtract(0.025, Property.ACCURACY)
                subtract(0.008, Property.RECOIL)
                divide(1.25, Property.RELOAD_SPEED)
                subtract(15, Property.PROJECTILE_SPEED)
            },
            statModifierList {
                multiply(1.08, Property.BASE_DAMAGE)
            }
        ),

        VLADOF(
            Manufacturer.VLADOF,
            Manufacturer.VLADOF.displayName,
            statModifierList {
                multiply(1.3, Property.FIRE_RATE)
                add(5, Property.PROJECTILE_SPEED)
            },
            statModifierList {
                multiply(1.08, Property.BASE_DAMAGE)
            }
        ),

        // Unique barrels.

        BADABOOM(Manufacturer.BANDIT, "Badaboom", behaviours = listOf(Badaboom())),
        CREAMER(Manufacturer.MALIWAN, "Greed", behaviours = listOf(Creamer())),
        TWELVE_POUNDER(Manufacturer.TORGUE, "12Pwndr", behaviours = listOf(TwelvePounder())),
        ;

        override val partTypeName: String = "Barrel"
        override val weaponClass: WeaponClass = WeaponClass.SNIPER

        companion object {

            val commonBarrels = setOf(
                BANDIT,
                MALIWAN,
                TEDIORE,
                TORGUE,
                VLADOF
            )

            val commonLootPool = commonBarrels.toUniformLootPool()
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

        BANDIT(
            Manufacturer.BANDIT,
            Manufacturer.BANDIT.displayName,
            statModifierList {
                add(2.5, Property.MELEE)
                add(1, Property.MAGAZINE_SIZE)
                multiply(1.1, Property.RELOAD_SPEED)
                subtract(0.012, Property.ACCURACY)
            },
            statModifierList {
                add(1, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
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
                add(1, Property.MAGAZINE_SIZE)
                divide(1.217, Property.RELOAD_SPEED)
            }
        ),

        TEDIORE(
            Manufacturer.TEDIORE,
            Manufacturer.TEDIORE.displayName,
            statModifierList {
                divide(1.2, Property.RELOAD_SPEED)
                divide(1.06, Property.BASE_DAMAGE)
                subtract(1, Property.MAGAZINE_SIZE)
            },
            statModifierList {
                divide(1.3, Property.RELOAD_SPEED)
                add(2, Property.MAGAZINE_SIZE)
            }
        ),

        TORGUE(
            Manufacturer.TORGUE,
            Manufacturer.TORGUE.displayName,
            statModifierList {
                multiply(1.08, Property.BASE_DAMAGE)
                subtract(0.02, Property.ACCURACY)
                multiply(1.1, Property.RELOAD_SPEED)
                subtract(0.004, Property.RECOIL)
            },
            statModifierList {
                divide(1.3, Property.RELOAD_SPEED)
                add(1, Property.MAGAZINE_SIZE)
            }
        ),

        VLADOF(
            Manufacturer.VLADOF,
            Manufacturer.VLADOF.displayName,
            statModifierList {
                multiply(1.12, Property.FIRE_RATE)
                subtract(0.004, Property.RECOIL)
            },
            statModifierList {
                add(2, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
            }
        ),
        ;

        override val partTypeName: String = "Grip"
        override val weaponClass: WeaponClass = WeaponClass.LAUNCHER

        companion object {

            val commonGrips = setOf(
                BANDIT,
                MALIWAN,
                TEDIORE,
                TORGUE,
                VLADOF
            )

            val commonLootPool = commonGrips.toUniformLootPool()
        }
    }

    /**
     * @author Hannah Schellekens
     */
    enum class Exhaust(
        override val manufacturer: Manufacturer,
        override val partName: String,
        override val statModifiers: List<StatModifier> = emptyList(),
        override val manufacturerStatModifiers: List<StatModifier> = emptyList(),
    ) : WeaponPart {

        BANDIT(
            Manufacturer.BANDIT,
            Manufacturer.BANDIT.displayName,
            statModifierList {
                add(1, Property.MAGAZINE_SIZE)
                subtract(0.01, Property.ACCURACY)
            },
            statModifierList {
            }
        ),

        MALIWAN(
            Manufacturer.MALIWAN,
            Manufacturer.MALIWAN.displayName,
            statModifierList {
                add(10, Property.PROJECTILE_SPEED)
                add(0.8, Property.RECOIL_ANGLE)
            },
        ),

        TEDIORE(
            Manufacturer.TEDIORE,
            Manufacturer.TEDIORE.displayName,
            statModifierList {
            },
        ),

        TORGUE(
            Manufacturer.TORGUE,
            Manufacturer.TORGUE.displayName,
            statModifierList {
                multiply(1.1, Property.BASE_DAMAGE)
                subtract(5, Property.PROJECTILE_SPEED)
                divide(1.05, Property.FIRE_RATE)
                subtract(1, Property.RECOIL_ANGLE)
            },
        ),

        VLADOF(
            Manufacturer.VLADOF,
            Manufacturer.VLADOF.displayName,
            statModifierList {
                multiply(1.12, Property.FIRE_RATE)
                subtract(0.05, Property.RECOIL_ANGLE)
            },
            statModifierList {
            }
        ),
        ;

        override val partTypeName: String = "Exhaust"
        override val weaponClass: WeaponClass = WeaponClass.LAUNCHER

        companion object {

            val commonStocks = setOf(
                BANDIT,
                MALIWAN,
                TEDIORE,
                TORGUE,
                VLADOF
            )

            val commonLootPool = commonStocks.toUniformLootPool()
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

        MAGAZINE(
            Manufacturer.NONE,
            "Magazine",
            statModifierList {
                add(2, Property.MAGAZINE_SIZE)
                multiply(1.2, Property.RELOAD_SPEED)
            }
        ),

        ACCURACY(
            Manufacturer.NONE,
            "Accuracy",
            statModifierList {
                add(0.032, Property.ACCURACY)
                add(5, Property.PROJECTILE_SPEED)
            }
        ),

        RELOAD(
            Manufacturer.NONE,
            "Reload",
            statModifierList {
                divide(1.25, Property.RELOAD_SPEED)
            }
        ),

        SPEED(
            Manufacturer.NONE,
            "Speed",
            statModifierList {
                add(10, Property.PROJECTILE_SPEED)
            }
        ),

        FIRE_RATE(
            Manufacturer.NONE,
            "Fire Rate",
            statModifierList {
                multiply(1.2, Property.FIRE_RATE)
                divide(1.06, Property.BASE_DAMAGE)
            }
        ),

        DAMAGE(
            Manufacturer.NONE,
            "Damage",
            statModifierList {
                multiply(1.1, Property.BASE_DAMAGE)
                divide(1.06, Property.FIRE_RATE)
                subtract(0.0035, Property.RECOIL)
            }
        ),
        ;

        override val partTypeName: String = "Acc."
        override val weaponClass: WeaponClass = WeaponClass.LAUNCHER

        companion object {

            val commonAccessories = setOf(
                MAGAZINE,
                ACCURACY,
                RELOAD,
                SPEED,
                FIRE_RATE,
                DAMAGE
            )

            val commonLootPool = commonAccessories.toUniformLootPool()
        }
    }
}