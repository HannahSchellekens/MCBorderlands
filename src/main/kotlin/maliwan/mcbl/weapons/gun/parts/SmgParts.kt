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
object SmgParts {

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
                subtract(0.003, Property.ACCURACY)
                add(0.002, Property.RECOIL)
            },
            statModifierList {
                add(0.007, Property.ACCURACY)
                multiply(1.1, Property.BASE_DAMAGE)
                add(1, Property.BURST_COUNT)
            }
        ),

        HYPERION(
            Manufacturer.HYPERION,
            Manufacturer.HYPERION.displayName,
            statModifierList {
                add(0.002, Property.RECOIL)
                add(0.015, Property.ACCURACY)
                divide(1.09, Property.BASE_DAMAGE)
                add(0.1, Property.BONUS_CRIT_MULTIPLIER)
            },
            statModifierList {
                add(0.01, Property.ACCURACY)
                multiply(1.1, Property.BASE_DAMAGE)
                add(0.05, Property.BONUS_CRIT_MULTIPLIER)
            }
        ),

        MALIWAN(
            Manufacturer.MALIWAN,
            Manufacturer.MALIWAN.displayName,
            statModifierList {
                add(0.005, Property.ACCURACY)
                add(0.001, Property.RECOIL)
                multiply(1.15, Property.ELEMENTAL_DAMAGE)
                multiply(1.15, Property.ELEMENTAL_CHANCE)
            },
            statModifierList {
                multiply(1.1, Property.BASE_DAMAGE)
            }
        ),

        TEDIORE(
            Manufacturer.TEDIORE,
            Manufacturer.TEDIORE.displayName,
            statModifierList {
                divide(1.15, Property.RELOAD_SPEED)
                divide(1.06, Property.BASE_DAMAGE)
                subtract(5, Property.PROJECTILE_SPEED)
            },
            statModifierList {
                divide(1.25, Property.RELOAD_SPEED)
                multiply(1.1, Property.BASE_DAMAGE)
            }
        ),

        ERIDIAN(
            Manufacturer.ERIDIAN,
            Manufacturer.ERIDIAN.displayName,
            statModifierList {
                subtract(0.002, Property.RECOIL)
                multiply(1.8, Property.MAGAZINE_SIZE)
                add(1, Property.AMMO_PER_SHOT)
                multiply(1.2, Property.ELEMENTAL_CHANCE)
                multiply(1.8, Property.BASE_DAMAGE)
                multiply(1.4, Property.ELEMENTAL_DAMAGE)
            },
            behaviours = listOf(PlasmaCaster())
        ),

        // Unique barrels.

        ANARCHY(Manufacturer.TEDIORE, "Anarchy", behaviours = listOf(Anarchy())),
        BAD_TOUCH(Manufacturer.MALIWAN, "Bad Touch", behaviours = listOf(BadTouch())),
        BANE(Manufacturer.DAHL, "Bane", behaviours = listOf(Bane())),
        BONE_SHREDDER(Manufacturer.BANDIT, "Bone Shredder", behaviours = listOf(BoneShredder())),
        CHULAINN(Manufacturer.TEDIORE, "Chulainn", behaviours = listOf(Chulainn())),
        COMMERCE(Manufacturer.TEDIORE, "Commerce", behaviours = listOf(Commerce())),
        CRIT(Manufacturer.HYPERION, "Crit", behaviours = listOf(Crit())),
        DOUBLE_ANARCHY(Manufacturer.TEDIORE, "Double Anarchy", behaviours = listOf(DoubleAnarchy())),
        GOOD_TOUCH(Manufacturer.MALIWAN, "Good Touch", behaviours = listOf(GoodTouch())),
        SLAGGA(Manufacturer.BANDIT, "Slagga", behaviours = listOf(Slagga())),
        ;

        override val partTypeName: String = "Barrel"
        override val weaponClass: WeaponClass = WeaponClass.SMG

        companion object {

            val commonBarrels = setOf(
                BANDIT,
                DAHL,
                HYPERION,
                MALIWAN,
                TEDIORE
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
                multiply(1.25, Property.MAGAZINE_SIZE)
                multiply(1.1, Property.RELOAD_SPEED)
                subtract(0.008, Property.ACCURACY)
            },
            statModifierList {
                add(6, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
            }
        ),

        DAHL(
            Manufacturer.DAHL,
            Manufacturer.DAHL.displayName,
            statModifierList {
                add(0.0015, Property.RECOIL)
                divide(1.06, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(4, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
            }
        ),

        HYPERION(
            Manufacturer.HYPERION,
            Manufacturer.HYPERION.displayName,
            statModifierList {
                add(0.009, Property.ACCURACY)
                add(0.001, Property.RECOIL)
                divide(1.09, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(4, Property.MAGAZINE_SIZE)
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
                add(4, Property.MAGAZINE_SIZE)
                divide(1.4, Property.RELOAD_SPEED)
            }
        ),

        TEDIORE(
            Manufacturer.TEDIORE,
            Manufacturer.TEDIORE.displayName,
            statModifierList {
                divide(1.1, Property.MAGAZINE_SIZE)
                divide(1.2, Property.RELOAD_SPEED)
                divide(1.06, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(3, Property.MAGAZINE_SIZE)
                subtract(3, Property.RELOAD_SPEED)
            }
        ),
        ;

        override val partTypeName: String = "Grip"
        override val weaponClass: WeaponClass = WeaponClass.SMG

        companion object {

            val commonGrips = setOf(
                BANDIT,
                DAHL,
                HYPERION,
                MALIWAN,
                TEDIORE
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

        BANDIT(
            Manufacturer.BANDIT,
            Manufacturer.BANDIT.displayName,
            emptyList(),
            emptyList()
        ),

        DAHL(
            Manufacturer.DAHL,
            Manufacturer.DAHL.displayName,
            statModifierList {
                add(0.0015, Property.RECOIL)
            },
            statModifierList {
                add(1, Property.BURST_COUNT)
            }
        ),

        HYPERION(
            Manufacturer.HYPERION,
            Manufacturer.HYPERION.displayName,
            statModifierList {
                add(0.005, Property.ACCURACY)
                add(0.001, Property.RECOIL)
            },
            statModifierList {
            }
        ),

        MALIWAN(
            Manufacturer.MALIWAN,
            Manufacturer.MALIWAN.displayName,
            statModifierList {
                add(0.008, Property.ACCURACY)
                add(0.001, Property.RECOIL)
            },
            statModifierList {
            }
        ),

        TEDIORE(
            Manufacturer.TEDIORE,
            Manufacturer.TEDIORE.displayName,
            statModifierList {
                add(0.001, Property.RECOIL)
                subtract(0.005, Property.ACCURACY)
            },
            statModifierList {
            }
        ),
        ;

        override val partTypeName: String = "Stock"
        override val weaponClass: WeaponClass = WeaponClass.SMG

        companion object {

            val commonStocks = setOf(
                BANDIT,
                DAHL,
                HYPERION,
                MALIWAN,
                TEDIORE
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
                add(0.011, Property.ACCURACY)
            }
        ),

        DAMAGE(
            Manufacturer.NONE,
            "Damage",
            statModifierList {
                subtract(0.0015, Property.RECOIL)
                multiply(1.18, Property.BASE_DAMAGE)
            }
        ),

        SPEED(
            Manufacturer.NONE,
            "Speed",
            statModifierList {
                multiply(1.12, Property.FIRE_RATE)
                add(20, Property.PROJECTILE_SPEED)
            }
        ),

        STABILITY(
            Manufacturer.NONE,
            "Stability",
            statModifierList {
                add(0.0025, Property.RECOIL)
                add(0.006, Property.ACCURACY)
            }
        ),

        RELOAD(
            Manufacturer.NONE,
            "Reload",
            statModifierList {
                multiply(1.1, Property.MAGAZINE_SIZE)
                divide(1.25, Property.RELOAD_SPEED)
            }
        ),
        ;

        override val partTypeName: String = "Acc."
        override val weaponClass: WeaponClass = WeaponClass.SMG

        companion object {

            val commonAccessories = setOf(
                BAYONET,
                ACCURACY,
                DAMAGE,
                SPEED,
                STABILITY,
                RELOAD
            )

            val commonLootPool = commonAccessories.toUniformLootPool()
        }
    }
}