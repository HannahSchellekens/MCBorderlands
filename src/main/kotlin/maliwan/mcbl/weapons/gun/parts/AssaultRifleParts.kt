package maliwan.mcbl.weapons.gun.parts

import maliwan.mcbl.loot.toUniformLootPool
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.StatModifier
import maliwan.mcbl.weapons.gun.StatModifier.Property
import maliwan.mcbl.weapons.gun.WeaponPart
import maliwan.mcbl.weapons.gun.behaviour.GunBehaviour
import maliwan.mcbl.weapons.gun.behaviour.rifle.*
import maliwan.mcbl.weapons.gun.statModifierList

/**
 * @author Hannah Schellekens
 */
object AssaultRifleParts {

    /**
     * @author Hannah Schellekens
     */
    enum class Barrel(
        override val manufacturer: Manufacturer,
        override val partName: String,
        override val statModifiers: List<StatModifier> = emptyList(),
        override val manufacturerStatModifiers: List<StatModifier> = emptyList(),
        override val otherManufacturerStatModifiers: Map<Manufacturer, List<StatModifier>> = emptyMap(),
        override val behaviours: List<GunBehaviour> = emptyList()
    ) : WeaponPart {

        ATLAS(
            Manufacturer.ATLAS,
            Manufacturer.ATLAS.displayName,
            statModifierList {
                add(0.005, Property.ACCURACY)
                multiply(1.06, Property.BASE_DAMAGE)
                add(0.014, Property.HOMING_STRENGTH)
            },
            statModifierList {
                add(0.015, Property.ACCURACY)
                multiply(1.06, Property.BASE_DAMAGE)
                multiply(1.2, Property.FIRE_RATE)
                add(0.014, Property.HOMING_STRENGTH)
            }
        ),

        BANDIT(
            Manufacturer.BANDIT,
            Manufacturer.BANDIT.displayName,
            statModifierList {
                subtract(0.015, Property.ACCURACY)
                multiply(1.06, Property.BASE_DAMAGE)
            },
            statModifierList {
                multiply(1.05, Property.BASE_DAMAGE)
                add(0.0008, Property.RECOIL)
            }
        ),

        DAHL(
            Manufacturer.DAHL,
            Manufacturer.DAHL.displayName,
            statModifierList {
                add(0.002, Property.RECOIL)
                subtract(0.003, Property.ACCURACY)
                divide(1.09, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.006, Property.ACCURACY)
                multiply(1.1, Property.BASE_DAMAGE)
                add(1, Property.BURST_COUNT)
            }
        ),

        JAKOBS(
            Manufacturer.JAKOBS,
            Manufacturer.JAKOBS.displayName,
            statModifierList {
                subtract(0.004, Property.RECOIL)
                add(0.021, Property.ACCURACY)
                multiply(1.18, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(0.0007, Property.RECOIL)
                add(0.003, Property.ACCURACY)
                multiply(1.15, Property.BASE_DAMAGE)
            }
        ),

        TORGUE(
            Manufacturer.TORGUE,
            Manufacturer.TORGUE.displayName,
            statModifierList {
            },
            statModifierList {
                /* TORGUE */
                divide(1.9, Property.FIRE_RATE)
                subtract(0.003, Property.RECOIL)
                add(5, Property.MAGAZINE_SIZE)
                multiply(1.25, Property.RELOAD_SPEED)
                add(3, Property.AMMO_PER_SHOT)
                multiply(3.6, Property.BASE_DAMAGE)
            },
            otherManufacturerStatModifiers = mapOf(
                Manufacturer.DAHL to statModifierList {
                    divide(1.75, Property.FIRE_RATE)
                    add(4, Property.MAGAZINE_SIZE)
                    multiply(1.25, Property.RELOAD_SPEED)
                    add(2, Property.AMMO_PER_SHOT)
                    multiply(2.6, Property.BASE_DAMAGE)
                },
                Manufacturer.JAKOBS to statModifierList {
                    divide(1.9, Property.FIRE_RATE)
                    subtract(0.003, Property.RECOIL)
                    add(4, Property.MAGAZINE_SIZE)
                    multiply(1.25, Property.RELOAD_SPEED)
                    add(1, Property.AMMO_PER_SHOT)
                    multiply(2.5, Property.BASE_DAMAGE)
                },
                Manufacturer.VLADOF to statModifierList {
                    divide(1.6, Property.FIRE_RATE)
                    subtract(0.003, Property.RECOIL)
                    add(5, Property.MAGAZINE_SIZE)
                    multiply(1.25, Property.RELOAD_SPEED)
                    add(1, Property.AMMO_PER_SHOT)
                    multiply(1.7, Property.BASE_DAMAGE)
                },
                Manufacturer.BANDIT to statModifierList {
                    divide(1.9, Property.FIRE_RATE)
                    subtract(0.003, Property.RECOIL)
                    add(8, Property.MAGAZINE_SIZE)
                    multiply(1.25, Property.RELOAD_SPEED)
                    add(2, Property.AMMO_PER_SHOT)
                    multiply(2.5, Property.BASE_DAMAGE)
                }
            ),
            behaviours = listOf(TorgueBarrelAssaultRifle(), Grenadier())
        ),

        VLADOF(
            Manufacturer.VLADOF,
            Manufacturer.VLADOF.displayName,
            statModifierList {
                multiply(1.15, Property.FIRE_RATE)
                subtract(0.001, Property.RECOIL)
                multiply(1.05, Property.MAGAZINE_SIZE)
                subtract(0.021, Property.ACCURACY)
            },
            statModifierList {
                multiply(1.05, Property.MAGAZINE_SIZE)
                multiply(1.1, Property.BASE_DAMAGE)
            }
        ),

        VLADOF_MINIGUN(
            Manufacturer.VLADOF,
            Manufacturer.VLADOF.displayName + " Minigun",
            statModifierList {
                multiply(1.35, Property.FIRE_RATE)
                add(0.004, Property.RECOIL)
                multiply(1.15, Property.MAGAZINE_SIZE)
                subtract(0.011, Property.ACCURACY)
            },
            statModifierList {
                /* VLADOF */
                add(6, Property.MAGAZINE_SIZE)
                multiply(1.1, Property.BASE_DAMAGE)
            },
            otherManufacturerStatModifiers = mapOf(
                Manufacturer.DAHL to statModifierList {
                    add(0.003, Property.FIRE_RATE)
                    add(2, Property.BURST_COUNT)
                    add(2, Property.AMMO_PER_SHOT)
                },
                Manufacturer.JAKOBS to statModifierList {
                    subtract(0.002, Property.RECOIL)
                    subtract(0.01, Property.ACCURACY)
                    add(2, Property.AMMO_PER_SHOT)
                    add(2, Property.PELLET_COUNT)
                },
                Manufacturer.TORGUE to statModifierList {
                    add(4, Property.MAGAZINE_SIZE)
                    add(0.25, Property.EXTRA_SHOT_CHANCE)
                },
            ),
            behaviours = listOf(Spinigun())
        ),

        // Unique barrels.

        BOOM_PUPPY(Manufacturer.TORGUE, "Boom Puppy", behaviours = listOf(BoomPuppy())),
        DAMNED_COWBOY(Manufacturer.BANDIT, "Damned Cowboy", behaviours = listOf(DamnedCowboy())),
        HAIL(Manufacturer.DAHL, "Hail", behaviours = listOf(Hail())),
        HAMMER_BUSTER(Manufacturer.JAKOBS, "Hammer Buster", behaviours = listOf(HammerBuster())),
        ICE_SCREAM(Manufacturer.VLADOF, "Ice Scream", behaviours = listOf(IceScream(), Spinigun())),
        SAWBAR(Manufacturer.BANDIT, "Sawbar", behaviours = listOf(Sawbar())),
        SCORPIO(Manufacturer.JAKOBS, "Scorpio", behaviours = listOf(Scorpio())),
        SHREDIFIER(Manufacturer.VLADOF, "Shredifier", behaviours = listOf(Shredifier(), Spinigun())),
        STINKPOT(Manufacturer.TORGUE, "Stinkpot", behaviours = listOf(Stinkpot())),
        STOMPER(Manufacturer.JAKOBS, "Stomper", behaviours = listOf(Stomper())),
        THE_MONARCH(Manufacturer.VLADOF, "The Monarch", behaviours = listOf(TheMonarch())),
        VERUC(Manufacturer.DAHL, "Veruc", behaviours = listOf(Veruc())),
        ;

        override val partTypeName: String = "Barrel"
        override val weaponClass: WeaponClass = WeaponClass.ASSAULT_RIFLE

        companion object {

            val commonBarrels = setOf(
                ATLAS,
                BANDIT,
                DAHL,
                JAKOBS,
                TORGUE,
                VLADOF,
                VLADOF_MINIGUN
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
        override val behaviours: List<GunBehaviour> = emptyList()
    ) : WeaponPart {

        ATLAS(
            Manufacturer.ATLAS,
            Manufacturer.ATLAS.displayName,
            statModifierList {
                add(1, Property.MAGAZINE_SIZE)
                divide(1.1, Property.RELOAD_SPEED)
                multiply(1.03, Property.BASE_DAMAGE)
                add(0.0008, Property.RECOIL)
                multiply(1.03, Property.FIRE_RATE)
                add(0.004, Property.HOMING_STRENGTH)
            },
            statModifierList {
                add(3, Property.MAGAZINE_SIZE)
                divide(1.35, Property.RELOAD_SPEED)
                multiply(1.1, Property.BASE_DAMAGE)
                add(0.002, Property.RECOIL)
                multiply(1.1, Property.FIRE_RATE)
                add(0.01, Property.HOMING_STRENGTH)
            }
        ),

        BANDIT(
            Manufacturer.BANDIT,
            Manufacturer.BANDIT.displayName,
            statModifierList {
                multiply(1.25, Property.MAGAZINE_SIZE)
                multiply(1.1, Property.RELOAD_SPEED)
                subtract(0.015, Property.ACCURACY)
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
                subtract(0.003, Property.ACCURACY)
                divide(1.06, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(4, Property.MAGAZINE_SIZE)
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
                add(0.0004, Property.RECOIL)
                divide(1.3, Property.RELOAD_SPEED)
                add(2, Property.MAGAZINE_SIZE)
            }
        ),

        TORGUE(
            Manufacturer.TORGUE,
            Manufacturer.TORGUE.displayName,
            statModifierList {
                subtract(0.0015, Property.RECOIL)
                multiply(1.1, Property.RELOAD_SPEED)
                multiply(1.09, Property.BASE_DAMAGE)
                subtract(0.009, Property.ACCURACY)
            },
            statModifierList {
                divide(1.3, Property.RELOAD_SPEED)
                add(4, Property.MAGAZINE_SIZE)
            }
        ),

        VLADOF(
            Manufacturer.VLADOF,
            Manufacturer.VLADOF.displayName,
            statModifierList {
                multiply(1.12, Property.FIRE_RATE)
                divide(1.06, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(5, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
            }
        ),

        // Unique Grips.

        CHOPPER(Manufacturer.VLADOF, "CHOPPER", behaviours = listOf(Chopper())),
        VAMPIRE(Manufacturer.VLADOF, "Vampire", behaviours = listOf(Vampire())),
        ;

        override val partTypeName: String = "Grip"
        override val weaponClass: WeaponClass = WeaponClass.ASSAULT_RIFLE

        companion object {

            val commonGrips = setOf(
                ATLAS,
                BANDIT,
                DAHL,
                JAKOBS,
                TORGUE,
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

        ATLAS(
            Manufacturer.ATLAS,
            Manufacturer.ATLAS.displayName,
            statModifierList {
                add(0.01, Property.ACCURACY)
                add(0.0015, Property.RECOIL)
            },
            statModifierList {
                add(0.002, Property.HOMING_STRENGTH)
            }
        ),

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
                add(0.0012, Property.RECOIL)
                subtract(0.002, Property.ACCURACY)
            },
            statModifierList {
            }
        ),

        JAKOBS(
            Manufacturer.JAKOBS,
            Manufacturer.JAKOBS.displayName,
            statModifierList {
                add(0.01, Property.ACCURACY)
                subtract(0.0015, Property.RECOIL)
            },
            statModifierList {
            }
        ),

        TORGUE(
            Manufacturer.TORGUE,
            Manufacturer.TORGUE.displayName,
            statModifierList {
                add(0.0015, Property.RECOIL)
            },
            statModifierList {
            }
        ),

        VLADOF(
            Manufacturer.VLADOF,
            Manufacturer.VLADOF.displayName,
            statModifierList {
                subtract(0.001, Property.RECOIL)
                add(0.005, Property.ACCURACY)
            },
            statModifierList {
            }
        ),
        ;

        override val partTypeName: String = "Stock"
        override val weaponClass: WeaponClass = WeaponClass.ASSAULT_RIFLE

        companion object {

            val commonStocks = setOf(
                ATLAS,
                BANDIT,
                DAHL,
                JAKOBS,
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

        BAYONET(
            Manufacturer.NONE,
            "Bayonet",
            statModifierList {
                add(3.5, Property.MELEE)
            }
        ),

        SPEED(
            Manufacturer.NONE,
            "Speed",
            statModifierList {
                multiply(1.15, Property.FIRE_RATE)
                add(15, Property.PROJECTILE_SPEED)
            }
        ),

        DAMAGE(
            Manufacturer.NONE,
            "Damage",
            statModifierList {
                divide(1.12, Property.FIRE_RATE)
                subtract(0.003, Property.RECOIL)
                multiply(1.21, Property.BASE_DAMAGE)
            }
        ),

        FOREGRIP(
            Manufacturer.NONE,
            "Foregrip",
            statModifierList {
                add(0.0025, Property.RECOIL)
                add(0.004, Property.ACCURACY)
            }
        ),

        MAGAZINE(
            Manufacturer.NONE,
            "Magazine",
            statModifierList {
                multiply(1.35, Property.MAGAZINE_SIZE)
                add(3, Property.MAGAZINE_SIZE)
            }
        ),

        ACCURACY(
            Manufacturer.NONE,
            "Accuracy",
            statModifierList {
                add(0.17, Property.ACCURACY)
            }
        ),

        WILD(
            Manufacturer.NONE,
            "Wild",
            statModifierList {
                multiply(1.12, Property.FIRE_RATE)
                multiply(1.21, Property.BASE_DAMAGE)
                subtract(0.03, Property.ACCURACY)
            }
        )
        ;

        override val partTypeName: String = "Acc."
        override val weaponClass: WeaponClass = WeaponClass.ASSAULT_RIFLE

        companion object {

            val commonAccessories = setOf(
                BAYONET,
                SPEED,
                DAMAGE,
                FOREGRIP,
                MAGAZINE,
                ACCURACY,
                WILD
            )

            val commonLootPool = commonAccessories.toUniformLootPool()
        }
    }
}