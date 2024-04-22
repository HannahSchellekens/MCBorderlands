package maliwan.mcbl.weapons.gun.parts

import maliwan.mcbl.loot.toUniformLootPool
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.StatModifier
import maliwan.mcbl.weapons.gun.StatModifier.Property
import maliwan.mcbl.weapons.gun.WeaponPart
import maliwan.mcbl.weapons.gun.behaviour.*
import maliwan.mcbl.weapons.gun.statModifierList

/**
 * @author Hannah Schellekens
 */
object SniperParts {

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

        DAHL(
            Manufacturer.DAHL,
            Manufacturer.DAHL.displayName,
            statModifierList {
                add(0.003, Property.ACCURACY)
                add(0.0025, Property.RECOIL)
                divide(1.09, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.003, Property.ACCURACY)
                multiply(1.1, Property.BASE_DAMAGE)
                add(1, Property.BURST_COUNT)
            }
        ),

        HYPERION(
            Manufacturer.HYPERION,
            Manufacturer.HYPERION.displayName,
            statModifierList {
                add(0.009, Property.ACCURACY)
                divide(1.1, Property.MAGAZINE_SIZE)
                divide(1.09, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.002, Property.ACCURACY)
                multiply(1.1, Property.BASE_DAMAGE)
            }
        ),

        JAKOBS(
            Manufacturer.JAKOBS,
            Manufacturer.JAKOBS.displayName,
            statModifierList {
                divide(1.18, Property.FIRE_RATE)
                subtract(0.005, Property.RECOIL)
                add(0.005, Property.ACCURACY)
                multiply(1.18, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.0015, Property.RECOIL)
                multiply(1.15, Property.BASE_DAMAGE)
            }
        ),

        MALIWAN(
            Manufacturer.MALIWAN,
            Manufacturer.MALIWAN.displayName,
            statModifierList {
                add(0.005, Property.ACCURACY)
                add(0.001, Property.RECOIL)
                multiply(0.7, Property.RECOIL_ANGLE)
                multiply(1.15, Property.ELEMENTAL_DAMAGE)
            },
            statModifierList {
                add(0.003, Property.ACCURACY)
                add(0.0005, Property.RECOIL)
                add(0.1, Property.ELEMENTAL_CHANCE)
                multiply(1.2, Property.BASE_DAMAGE)
            }
        ),

        VLADOF(
            Manufacturer.VLADOF,
            Manufacturer.VLADOF.displayName,
            statModifierList {
                multiply(1.3, Property.FIRE_RATE)
                subtract(0.002, Property.RECOIL)
                subtract(0.015, Property.ACCURACY)
                multiply(1.5, Property.MAGAZINE_SIZE)
            },
            statModifierList {
                add(0.005, Property.ACCURACY)
                multiply(1.13, Property.MAGAZINE_SIZE)
                multiply(1.1, Property.BASE_DAMAGE)
            }
        ),

        // Unique barrels.

        BUFFALO(Manufacturer.JAKOBS, "Buffalo", behaviours = listOf(Buffalo())),
        CHERE_AMIE(Manufacturer.HYPERION, "Chère-Amie", behaviours = listOf(ChereAmie())),
        COBRA(Manufacturer.JAKOBS, "Cobra", behaviours = listOf(Cobra())),
        SLOTH(Manufacturer.DAHL, "Sloth", behaviours = listOf(Sloth())),
        ;

        override val partTypeName: String = "Barrel"
        override val weaponClass: WeaponClass = WeaponClass.SNIPER

        companion object {

            val commonBarrels = setOf(
                DAHL,
                HYPERION,
                JAKOBS,
                MALIWAN,
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

        DAHL(
            Manufacturer.DAHL,
            Manufacturer.DAHL.displayName,
            statModifierList {
                divide(1.06, Property.BASE_DAMAGE)
                add(0.011, Property.ACCURACY)
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
                divide(1.09, Property.BASE_DAMAGE)
                add(0.005, Property.ACCURACY)
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
                subtract(0.002, Property.RECOIL)
                multiply(1.05, Property.RELOAD_SPEED)
                multiply(1.12, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.0007, Property.RECOIL)
                add(3, Property.MAGAZINE_SIZE)
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
                add(3, Property.MAGAZINE_SIZE)
                divide(1.217, Property.RELOAD_SPEED)
            }
        ),

        VLADOF(
            Manufacturer.VLADOF,
            Manufacturer.VLADOF.displayName,
            statModifierList {
                multiply(1.12, Property.FIRE_RATE)
                subtract(0.007, Property.ACCURACY)
                divide(1.06, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.002, Property.ACCURACY)
                add(3, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
            }
        ),
        ;

        override val partTypeName: String = "Grip"
        override val weaponClass: WeaponClass = WeaponClass.SNIPER

        companion object {

            val commonGrips = setOf(
                DAHL,
                HYPERION,
                JAKOBS,
                MALIWAN,
                VLADOF
            )

            val commonLootPool = commonGrips.toUniformLootPool()
        }
    }

    /**
     * @author Hannah Schellekens
     */
    enum class Stock(
        override val manufacturer: Manufacturer,
        override val partName: String,
        override val statModifiers: List<StatModifier> = emptyList(),
        override val manufacturerStatModifiers: List<StatModifier> = emptyList(),
    ) : WeaponPart {

        DAHL(
            Manufacturer.DAHL,
            Manufacturer.DAHL.displayName,
            statModifierList {
                add(0.0015, Property.RECOIL)
                subtract(0.002, Property.ACCURACY)
                add(1.5, Property.RECOIL_ANGLE)
            },
            statModifierList {
                add(1, Property.BURST_COUNT)
            }
        ),

        HYPERION(
            Manufacturer.HYPERION,
            Manufacturer.HYPERION.displayName,
            statModifierList {
                add(0.002, Property.RECOIL)
                add(0.8, Property.RECOIL_ANGLE)
            },
        ),

        JAKOBS(
            Manufacturer.JAKOBS,
            Manufacturer.JAKOBS.displayName,
            statModifierList {
                add(0.005, Property.ACCURACY)
                subtract(0.003, Property.RECOIL)
                subtract(1.8, Property.RECOIL_ANGLE)
                add(0.15, Property.BONUS_CRIT_MULTIPLIER)
            },
        ),

        MALIWAN(
            Manufacturer.MALIWAN,
            Manufacturer.MALIWAN.displayName,
            statModifierList {
                add(0.005, Property.ACCURACY)
                add(0.001, Property.RECOIL)
                add(0.3, Property.RECOIL_ANGLE)
            },
        ),

        VLADOF(
            Manufacturer.VLADOF,
            Manufacturer.VLADOF.displayName,
            statModifierList {
                subtract(0.001, Property.RECOIL)
                add(0.001, Property.ACCURACY)
                add(0.3, Property.RECOIL_ANGLE)
            },
        ),
        ;

        override val partTypeName: String = "Stock"
        override val weaponClass: WeaponClass = WeaponClass.SNIPER

        companion object {

            val commonStocks = setOf(
                DAHL,
                HYPERION,
                JAKOBS,
                MALIWAN,
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

        BAYONET(
            Manufacturer.NONE,
            "Bayonet",
            statModifierList {
                add(2.5, Property.MELEE)
            }
        ),

        ACCURACY(
            Manufacturer.NONE,
            "Accuracy",
            statModifierList {
                add(0.005, Property.ACCURACY)
                subtract(0.001, Property.RECOIL)
            }
        ),

        CRIT(
            Manufacturer.NONE,
            "Crit",
            statModifierList {
                add(0.25, Property.BONUS_CRIT_MULTIPLIER)
                add(0.002, Property.ACCURACY)
                subtract(0.0008, Property.RECOIL)
            }
        ),

        FOREGRIP(
            Manufacturer.NONE,
            "Foregrip",
            statModifierList {
                add(0.002, Property.RECOIL)
                subtract(0.005, Property.ACCURACY)
            }
        ),

        MAGAZINE(
            Manufacturer.NONE,
            "Magazine",
            statModifierList {
                multiply(1.35, Property.MAGAZINE_SIZE)
                divide(1.15, Property.RELOAD_SPEED)
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

        DAMAGE(
            Manufacturer.NONE,
            "Damage",
            statModifierList {
                subtract(0.0025, Property.RECOIL)
                multiply(1.18, Property.BASE_DAMAGE)
            }
        ),
        ;

        override val partTypeName: String = "Acc."
        override val weaponClass: WeaponClass = WeaponClass.SNIPER

        companion object {

            val commonAccessories = setOf(
                BAYONET,
                ACCURACY,
                CRIT,
                FOREGRIP,
                MAGAZINE,
                FIRE_RATE,
                DAMAGE
            )

            val commonLootPool = commonAccessories.toUniformLootPool()
        }
    }
}