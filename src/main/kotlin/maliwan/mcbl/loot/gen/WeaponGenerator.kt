package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.LootPool
import maliwan.mcbl.loot.ManufacturerTable
import maliwan.mcbl.loot.RarityTable
import maliwan.mcbl.loot.WeaponClassTable
import maliwan.mcbl.util.Chance
import maliwan.mcbl.util.Damage
import maliwan.mcbl.util.Ticks
import maliwan.mcbl.weapons.Elemental
import maliwan.mcbl.weapons.Manufacturer
import maliwan.mcbl.weapons.Rarity
import maliwan.mcbl.weapons.WeaponClass
import maliwan.mcbl.weapons.gun.Capacitor
import maliwan.mcbl.weapons.gun.GunProperties
import maliwan.mcbl.weapons.gun.WeaponAssembly
import maliwan.mcbl.weapons.gun.stats.PistolBaseValues
import maliwan.mcbl.weapons.gun.stats.PistolGradeModifiers
import maliwan.mcbl.weapons.gun.stats.PistolGradeModifiers.StandardDeviation.Companion
import maliwan.mcbl.weapons.gun.stats.PistolGradeModifiers.modifier
import maliwan.mcbl.weapons.gun.stats.PistolGradeModifiers.standardDeviation
import maliwan.mcbl.weapons.gun.stats.newBaseValueProperties
import org.checkerframework.common.aliasing.qual.MaybeLeaked
import java.util.Random
import kotlin.math.floor
import kotlin.math.min

/**
 * @author Hannah Schellekens
 */
open class WeaponGenerator(
    val rarityTable: LootPool<Rarity> = RarityTable.Treasure.regular,
    val weaponClassTable: LootPool<WeaponClass> = WeaponClassTable.generation,
    val manufacturerTable: LootPool<Manufacturer> = ManufacturerTable.Weapons.generation,
    val random: Random = Random()
) {

    /**
     * Generates a new weapon.
     */
    fun generate(): GunProperties {
        val rarity = rarityTable.roll(random)
        val weaponClass = weaponClassTable.roll(random)

        val producers = Manufacturer.producersOf(weaponClass)
        val manufacturerPool = manufacturerTable.retainResults(producers)
        val manufacturer = manufacturerPool.roll(random)

        val properties = newBaseValueProperties(manufacturer, weaponClass)
        val assembly = WeaponAssemblyGenerator.forType(weaponClass, setOf(manufacturer), random).generate(rarity)

        // First add elementals so their values can be modified by gun parts.
        properties.addElementals(assembly)
        assembly.applyToGun(properties)

        properties.rarity = rarity
        properties.weaponClass = weaponClass
        properties.manufacturer = manufacturer
        properties.name = assembly.gunName
        properties.assembly = assembly

        properties.applyGradeScaling()
        properties.addManufacturerGimmkcisk(assembly)

        return properties
    }

    /**
     * Adds all specific tweaks that are manufacturer specific.
     */
    private fun GunProperties.addManufacturerGimmkcisk(assembly: WeaponAssembly) {
        when (assembly.manufacturer) {
            Manufacturer.DAHL -> {
                if (rarity == Rarity.EPIC) {
                    burstCount++
                    burstDelay = Ticks(1)
                }
            }
            Manufacturer.TORGUE -> {
                if (Elemental.EXPLOSIVE !in elements) {
                    elements += Elemental.EXPLOSIVE
                    elementalChance[Elemental.EXPLOSIVE] = Chance.ONE
                    elementalDuration[Elemental.EXPLOSIVE] = Ticks(0)
                    elementalDamage[Elemental.EXPLOSIVE] = baseDamage * 0.5
                    splashDamage = baseDamage * 0.5
                }
            }
            else -> Unit
        }
    }

    /**
     * Add elemental effects to the gun.
     */
    private fun GunProperties.addElementals(assembly: WeaponAssembly) {
        val capacitor = assembly.partOfType<Capacitor>(Capacitor::class) ?: return /* no capacitor -> no element */

        // Base values.
        val element = capacitor.element
        if (element == Elemental.PHYSICAL) return /* Physical => no elemental damage */

        val chance = PistolBaseValues.baseValue(assembly.manufacturer, PistolBaseValues.Stat.elementalChance)
        val damage = PistolBaseValues.baseValue(assembly.manufacturer, PistolBaseValues.Stat.elementalDamage)

        // Update properties to include element.
        elements += element
        elementalDamage[element] = damage * element.noDotMultiplier
        elementalChance[element] = if (element == Elemental.EXPLOSIVE) Chance.ONE else chance
        elementalDuration[element] = element.baseDotDuration

        // Cut base damage for DoT elements.
        val damageMultiplier = when (element) {
            Elemental.INCENDIARY,
            Elemental.CORROSIVE,
            Elemental.SHOCK,
            Elemental.SLAG -> 0.833
            else -> 1.0
        }
        baseDamage *= damageMultiplier

        // Apply splash damage.
        val splashPercentage = assembly.splashDamage(element)
        splashDamage = baseDamage * splashPercentage
    }

    /**
     * Calculates the percentage of the base damage to deal as splash damage.
     */
    private fun WeaponAssembly.splashDamage(elemental: Elemental) = if (elemental == Elemental.EXPLOSIVE) {
        when (manufacturer) {
            Manufacturer.MALIWAN -> 0.8
            Manufacturer.TORGUE -> 0.5
            else -> 0.2
        }
    }
    else if (manufacturer == Manufacturer.MALIWAN) {
        when (weaponClass) {
            WeaponClass.PISTOL -> 0.8
            WeaponClass.SNIPER -> 0.5
            WeaponClass.LAUNCHER -> 0.75
            else -> 0.0
        }
    }
    else 0.0

    /**
     * Scales the weapons base stats based on the rarity of the gun.
     */
    private fun GunProperties.applyGradeScaling() {
        // Base Damage
        val baseDamageBaseMult = modifier(rarity, PistolGradeModifiers.Modifier.baseDamage)
        val baseDamageStd = standardDeviation(rarity, PistolGradeModifiers.StandardDeviation.baseDamage)
        val baseDamageMultiplier = random.nextGaussian() * baseDamageStd + baseDamageBaseMult
        baseDamage = Damage(baseDamage.damage * baseDamageMultiplier)

        // Magazine size
        val magSizeMult = modifier(rarity, PistolGradeModifiers.Modifier.magazineSize)
        val magSizeStd = standardDeviation(rarity, PistolGradeModifiers.StandardDeviation.magazineSize)
        val magSizeMultiplier = random.nextGaussian() * magSizeStd + magSizeMult
        magazineSize = floor(magazineSize * magSizeMultiplier).toInt()

        // Reload time.
        val reloadBonus = modifier(rarity, PistolGradeModifiers.Modifier.reloadTime)
        reloadSpeed = Ticks(reloadSpeed.ticks + reloadBonus)

        // Fire rate
        val fireRateModifier = modifier(rarity, PistolGradeModifiers.Modifier.fireRate)
        fireRate *= fireRateModifier

        // Elemental chance and damage
        val eltChanceMultiplier = modifier(rarity, PistolGradeModifiers.Modifier.elementalChance)
        val eltDamageMult = modifier(rarity, PistolGradeModifiers.Modifier.elementalDamage)
        val eltDamageStd = standardDeviation(rarity, PistolGradeModifiers.StandardDeviation.elementalDamage)
        val eltDamageMultiplier = random.nextGaussian() * eltDamageStd + eltDamageMult

        elements.forEach { element ->
            elementalChance[element] = Chance(min(0.995, elementalChance[element]!!.chance * eltChanceMultiplier))
            elementalDamage[element] = Damage(elementalDamage[element]!!.damage * eltDamageMultiplier)
        }

        // Accuracy
        val accuracyModifier = modifier(rarity, PistolGradeModifiers.Modifier.accuracy)
        accuracy = Chance(accuracy.chance + accuracyModifier)

        // Recoil
        val recoilModifier = modifier(rarity, PistolGradeModifiers.Modifier.recoil)
        recoil += recoilModifier
    }
}