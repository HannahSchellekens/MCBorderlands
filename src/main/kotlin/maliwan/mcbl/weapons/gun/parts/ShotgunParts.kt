package maliwan.mcbl.weapons.gun.parts

import maliwan.mcbl.loot.toUniformLootPool
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.StatModifier
import maliwan.mcbl.weapons.gun.StatModifier.Property
import maliwan.mcbl.weapons.gun.WeaponPart
import maliwan.mcbl.weapons.gun.behaviour.GunBehaviour
import maliwan.mcbl.weapons.gun.behaviour.shotgun.*
import maliwan.mcbl.weapons.gun.statModifierList

/**
 * @author Hannah Schellekens
 */
object ShotgunParts {

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
                divide(1.23, Property.FIRE_RATE)
                subtract(0.0055, Property.RECOIL)
                add(1, Property.MAGAZINE_SIZE)
                add(8, Property.PELLET_COUNT)
                add(2, Property.AMMO_PER_SHOT)
                multiply(1.12, Property.BASE_DAMAGE)
                subtract(-0.1, Property.ACCURACY)
            },
            statModifierList {
                add(0.002, Property.RECOIL)
                multiply(1.05, Property.BASE_DAMAGE)
            }
        ),

        HYPERION(
            Manufacturer.HYPERION,
            Manufacturer.HYPERION.displayName,
            statModifierList {
                add(0.002, Property.RECOIL)
                divide(1.1, Property.MAGAZINE_SIZE)
                add(0.0075, Property.ACCURACY)
                divide(1.12, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.05, Property.ACCURACY)
                multiply(1.1, Property.BASE_DAMAGE)
            }
        ),

        JAKOBS(
            Manufacturer.JAKOBS,
            Manufacturer.JAKOBS.displayName,
            statModifierList {
                divide(1.15, Property.FIRE_RATE)
                subtract(0.006, Property.RECOIL)
                subtract(0.075, Property.ACCURACY)
                add(1, Property.AMMO_PER_SHOT)
                add(4, Property.PELLET_COUNT)
                multiply(1.06, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.0015, Property.RECOIL)
                add(0.02, Property.ACCURACY)
                add(2, Property.PELLET_COUNT)
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
                divide(1.36, Property.FIRE_RATE)
                subtract(0.008, Property.RECOIL)
                add(2, Property.MAGAZINE_SIZE)
                subtract(0.137, Property.ACCURACY)
                add(11, Property.PELLET_COUNT)
                add(3, Property.AMMO_PER_SHOT)
                multiply(1.18, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.05, Property.FIRE_RATE)
                multiply(1.2, Property.BASE_DAMAGE)
            }
        ),

        // Unique barrels.

        BLOCKHEAD(Manufacturer.TEDIORE, "Blockhead", behaviours = listOf(Blockhead())),
        DOG(Manufacturer.BANDIT, "Dog", behaviours = listOf(Dog())),
        FLAKKER(Manufacturer.TORGUE, "Flakker", behaviours = listOf(Flakker())),
        HEART_BREAKER(Manufacturer.HYPERION, "Heart Breaker", behaviours = listOf(HeartBreaker())),
        HYDRA(Manufacturer.JAKOBS, "Hydra", behaviours = listOf(Hydra())),
        ORPHAN_MAKER(Manufacturer.JAKOBS, "Orphan Maker", behaviours = listOf(OrphanMaker())),
        ROKSALT(Manufacturer.JAKOBS, "RokSalt", behaviours = listOf(RokSalt())),
        SLEDGES_SHOTGUN(Manufacturer.JAKOBS, "Sledge's", behaviours = listOf(SledgesShotgun())),
        ;

        override val partTypeName: String = "Barrel"
        override val weaponClass: WeaponClass = WeaponClass.SHOTGUN

        companion object {

            val commonBarrels = setOf(
                BANDIT,
                HYPERION,
                JAKOBS,
                TEDIORE,
                TORGUE
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
                divide(1.1, Property.RELOAD_SPEED)
                subtract(0.023, Property.ACCURACY)
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
                add(0.033, Property.ACCURACY)
                divide(1.09, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(1, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
            }
        ),

        JAKOBS(
            Manufacturer.JAKOBS,
            Manufacturer.JAKOBS.displayName,
            statModifierList {
                divide(1.09, Property.FIRE_RATE)
                subtract(0.002, Property.RECOIL)
                add(0.001, Property.RECOIL)
                multiply(1.05, Property.RELOAD_SPEED)
                multiply(1.12, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.001, Property.RECOIL)
                add(1, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
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
                add(2, Property.MAGAZINE_SIZE)
                subtract(3, Property.RELOAD_SPEED)
            }
        ),

        TORGUE(
            Manufacturer.TORGUE,
            Manufacturer.TORGUE.displayName,
            statModifierList {
                subtract(0.002, Property.RECOIL)
                multiply(1.1, Property.RELOAD_SPEED)
                multiply(1.09, Property.BASE_DAMAGE)
                subtract(0.035, Property.ACCURACY)
            },
            statModifierList {
                add(1, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
            }
        ),
        ;

        override val partTypeName: String = "Grip"
        override val weaponClass: WeaponClass = WeaponClass.SHOTGUN

        companion object {

            val commonGrips = setOf(
                BANDIT,
                HYPERION,
                JAKOBS,
                TEDIORE,
                TORGUE
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
            statModifierList {
            },
            statModifierList {
            }
        ),

        HYPERION(
            Manufacturer.HYPERION,
            Manufacturer.HYPERION.displayName,
            statModifierList {
                add(0.04, Property.ACCURACY)
            },
            statModifierList {
            }
        ),

        JAKOBS(
            Manufacturer.JAKOBS,
            Manufacturer.JAKOBS.displayName,
            statModifierList {
                subtract(0.002, Property.RECOIL)
                add(0.02, Property.ACCURACY)
                add(0.15, Property.BONUS_CRIT_MULTIPLIER)
            },
            statModifierList {
            }
        ),

        TEDIORE(
            Manufacturer.TEDIORE,
            Manufacturer.TEDIORE.displayName,
            statModifierList {
                add(0.002, Property.RECOIL)
                subtract(0.015, Property.ACCURACY)
            },
            statModifierList {
            }
        ),

        TORGUE(
            Manufacturer.TORGUE,
            Manufacturer.TORGUE.displayName,
            statModifierList {
                add(0.004, Property.RECOIL)
                subtract(0.007, Property.ACCURACY)
            },
            statModifierList {
            }
        ),
        ;

        override val partTypeName: String = "Stock"
        override val weaponClass: WeaponClass = WeaponClass.SHOTGUN

        companion object {

            val commonGrips = setOf(
                BANDIT,
                HYPERION,
                JAKOBS,
                TEDIORE,
                TORGUE
            )

            val commonLootPool = commonGrips.toUniformLootPool()
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

        CLIP(
            Manufacturer.NONE,
            "Clip",
            statModifierList {
                multiply(1.3, Property.MAGAZINE_SIZE)
                multiply(1.15, Property.RELOAD_SPEED)
            }
        ),

        SHELL(
            Manufacturer.NONE,
            "Shell",
            statModifierList {
                multiply(1.03, Property.FIRE_RATE)
                add(1, Property.MAGAZINE_SIZE)
                divide(1.05, Property.RELOAD_SPEED)
                multiply(1.03, Property.BASE_DAMAGE)
            }
        ),

        ACCURACY(
            Manufacturer.NONE,
            "Accuracy",
            statModifierList {
                multiply(1.12, Property.FIRE_RATE)
                divide(1.15, Property.BASE_DAMAGE)
                add(0.075, Property.ACCURACY)
            }
        ),

        CRIT(
            Manufacturer.NONE,
            "Crit",
            statModifierList {
                add(0.5, Property.BONUS_CRIT_MULTIPLIER)
                add(0.025, Property.ACCURACY)
            }
        ),

        RELOAD(
            Manufacturer.NONE,
            "Reload",
            statModifierList {
                multiply(1.06, Property.FIRE_RATE)
                multiply(1.1, Property.MAGAZINE_SIZE)
                divide(1.25, Property.RELOAD_SPEED)
                divide(1.03, Property.BASE_DAMAGE)
            }
        ),

        VERTICAL_GRIP(
            Manufacturer.NONE,
            "VertGrip",
            statModifierList {
                multiply(1.21, Property.FIRE_RATE)
                subtract(0.03, Property.ACCURACY)
                add(2, Property.PELLET_COUNT)
            }
        ),
        ;

        override val partTypeName: String = "Acc."
        override val weaponClass: WeaponClass = WeaponClass.SHOTGUN

        companion object {

            val commonAccessories = setOf(
                BAYONET,
                CLIP,
                SHELL,
                ACCURACY,
                CRIT,
                RELOAD,
                VERTICAL_GRIP
            )

            val commonLootPool = commonAccessories.toUniformLootPool()
        }
    }
}