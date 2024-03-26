package maliwan.mcbl.weapons.gun

import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.Manufacturers
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.WeaponClasses
import maliwan.mcbl.weapons.gun.StatModifier.Property

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
        override val manufacturerStatModifiers: List<StatModifier> = emptyList()
    ) : WeaponPart {

        MALIWAN(
            Manufacturers.MALIWAN,
            Manufacturers.MALIWAN.displayName,
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

        VLADOF(
            Manufacturers.VLADOF,
            Manufacturers.VLADOF.displayName,
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

        BANDIT(
            Manufacturers.BANDIT,
            Manufacturers.BANDIT.displayName,
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
            Manufacturers.DAHL,
            Manufacturers.DAHL.displayName,
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
            Manufacturers.HYPERION,
            Manufacturers.HYPERION.displayName,
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
            Manufacturers.JAKOBS,
            Manufacturers.JAKOBS.displayName,
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

        TEDIORE(
            Manufacturers.TEDIORE,
            Manufacturers.TEDIORE.displayName,
            statModifierList {
                subtract(5, Property.PROJECTILE_SPEED)
            }
        ),

        TORGUE(
            Manufacturers.TORGUE,
            Manufacturers.TORGUE.displayName,
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
        ;

        override val weaponClass: WeaponClass = WeaponClasses.PISTOL
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

        MALIWAN(
            Manufacturers.MALIWAN,
            Manufacturers.MALIWAN.displayName,
            statModifierList {
                multiply(1.2, Property.ELEMENTAL_CHANCE)
                divide(1.15, Property.RELOAD_SPEED)
            },
            statModifierList {
                add(3, Property.MAGAZINE_SIZE)
                divide(1.217, Property.RELOAD_SPEED)
            }
        ),

        VLADOF(
            Manufacturers.VLADOF,
            Manufacturers.VLADOF.displayName,
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

        BANDIT(
            Manufacturers.BANDIT,
            Manufacturers.BANDIT.displayName,
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
            Manufacturers.DAHL,
            Manufacturers.DAHL.displayName,
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
            Manufacturers.HYPERION,
            Manufacturers.HYPERION.displayName,
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
            Manufacturers.JAKOBS,
            Manufacturers.JAKOBS.displayName,
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

        TEDIORE(
            Manufacturers.TEDIORE,
            Manufacturers.TEDIORE.displayName,
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
            Manufacturers.TORGUE,
            Manufacturers.TORGUE.displayName,
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
        ;

        override val weaponClass: WeaponClass = WeaponClasses.PISTOL
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
            Manufacturers.NONE,
            "Accuracy",
            statModifierList {
                add(0.015, Property.ACCURACY)
                multiply(1.1, Property.PROJECTILE_SPEED)
            }
        ),

        BAYONET(
            Manufacturers.NONE,
            "Bayonet",
            statModifierList {
                add(2.5, Property.MELEE)
            }
        ),

        DOUBLE(
            Manufacturers.NONE,
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
            Manufacturers.NONE,
            "Stock",
            statModifierList {
                add(0.018, Property.ACCURACY)
                add(0.002, Property.RECOIL)
            }
        ),

        MAGAZINE(
            Manufacturers.NONE,
            "Magazine",
            statModifierList {
                multiply(1.56, Property.MAGAZINE_SIZE)
                multiply(1.15, Property.RELOAD_SPEED)
            }
        ),

        DAMAGE(
            Manufacturers.NONE,
            "Damage",
            statModifierList {
                subtract(0.002, Property.RECOIL)
                multiply(1.18, Property.BASE_DAMAGE)
            }
        ),

        FIRE_RATE(
            Manufacturers.NONE,
            "Fire Rate",
            statModifierList {
                multiply(1.18, Property.FIRE_RATE)
                divide(1.03, Property.BASE_DAMAGE)
            }
        ),
        ;

        override val weaponClass: WeaponClass = WeaponClasses.PISTOL
    }
}