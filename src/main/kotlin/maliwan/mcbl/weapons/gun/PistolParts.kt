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
        override val weaponClass: WeaponClass,
        override val manufacturer: Manufacturer,
        override val partName: String,
        override val statModifiers: List<StatModifier> = emptyList(),
        override val manufacturerStatModifiers: List<StatModifier> = emptyList()
    ) : WeaponPart {

        MALIWAN(
            WeaponClasses.PISTOL,
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
            WeaponClasses.PISTOL,
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
    }

    /**
     * @author Hannah Schellekens
     */
    enum class Grip(
        override val weaponClass: WeaponClass,
        override val manufacturer: Manufacturer,
        override val partName: String,
        override val statModifiers: List<StatModifier> = emptyList(),
        override val manufacturerStatModifiers: List<StatModifier> = emptyList()
    ) : WeaponPart {

        MALIWAN(
            WeaponClasses.PISTOL,
            Manufacturers.MALIWAN,
            Manufacturers.MALIWAN.displayName,
            statModifierList {
                multiply(1.2, Property.ELEMENTAL_CHANCE)
                divide(1.15, Property.RELOAD_SPEED)
            },
            statModifierList {
                add(3.0, Property.MAGAZINE_SIZE)
                divide(1.217, Property.RELOAD_SPEED)
            }
        ),

        VLADOF(
            WeaponClasses.PISTOL,
            Manufacturers.VLADOF,
            Manufacturers.VLADOF.displayName,
            statModifierList {
                multiply(1.12, Property.FIRE_RATE)
                subtract(0.018, Property.ACCURACY)
                divide(1.06, Property.BASE_DAMAGE)
            },
            statModifierList {
                add(4.0, Property.MAGAZINE_SIZE)
                divide(1.3, Property.RELOAD_SPEED)
            }
        ),
    }
}