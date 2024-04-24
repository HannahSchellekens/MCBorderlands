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
import maliwan.mcbl.weapons.gun.*
import maliwan.mcbl.weapons.gun.behaviour.*
import maliwan.mcbl.weapons.gun.parts.Capacitor
import maliwan.mcbl.weapons.gun.parts.UniqueGunPart
import maliwan.mcbl.weapons.gun.parts.UniqueGunParts
import maliwan.mcbl.weapons.gun.stats.*
import java.util.*
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

        when (rarity) {
            Rarity.LEGENDARY -> return generateLegendary()
            Rarity.EPIC -> if (random.nextDouble() < 0.02) {
                return generateUnique(Rarity.EPIC)
            }
            Rarity.RARE -> if (random.nextDouble() < 0.0667) {
                return generateUnique(Rarity.RARE)
            }
            else -> Unit
        }

        val weaponClass = weaponClassTable.roll(random)
        val producers = Manufacturer.producersOf(weaponClass)
        val manufacturerPool = manufacturerTable.retainResults(producers)
        val manufacturer = manufacturerPool.roll(random)

        val properties = newBaseValueProperties(manufacturer, weaponClass)
        val assembly = WeaponAssemblyGenerator.forType(weaponClass, setOf(manufacturer), random).generate(rarity)

        return properties.applyAssembly(assembly, rarity)
    }

    /**
     * Generates a new legendary weapon.
     */
    fun generateLegendary(ofType: WeaponClass? = null): GunProperties {
        val eligibleParts = if (ofType != null) {
            UniqueGunParts.legendaryParts.filter { it.weaponClass == ofType }
        }
        else UniqueGunParts.legendaryParts

        val legendaryPart = eligibleParts.random()
        val manufacturer = legendaryPart.manufacturer
        val weaponClass = legendaryPart.weaponClass

        val properties = newBaseValueProperties(manufacturer, weaponClass)
        val baseAssembly = WeaponAssemblyGenerator.forType(weaponClass, setOf(manufacturer), random).generate(Rarity.LEGENDARY)
        val assembly = when (legendaryPart) {
            is UniqueGunPart.UniqueCapacitor -> baseAssembly.replaceCapacitor(legendaryPart.capacitor)
            is UniqueGunPart.UniqueWeaponPart -> baseAssembly.replacePart(legendaryPart.part)
        }

        return properties.applyAssembly(assembly, Rarity.LEGENDARY)
    }

    /**
     * Generates a new unique weapon that is not legendary+.
     */
    fun generateUnique(rarity: Rarity, ofType: WeaponClass? = null): GunProperties {
        val baseParts = when (rarity) {
            Rarity.EPIC -> UniqueGunParts.epicParts
            else -> UniqueGunParts.rareParts
        }

        val eligibleParts = if (ofType != null) {
            baseParts.filter { it.weaponClass == ofType }
        }
        else baseParts

        val uniquePart = eligibleParts.random()
        val manufacturer = uniquePart.manufacturer
        val weaponClass = uniquePart.weaponClass

        val properties = newBaseValueProperties(manufacturer, weaponClass)
        val baseAssembly = WeaponAssemblyGenerator.forType(weaponClass, setOf(manufacturer), random).generate(rarity)
        val assembly = when (uniquePart) {
            is UniqueGunPart.UniqueCapacitor -> baseAssembly.replaceCapacitor(uniquePart.capacitor)
            is UniqueGunPart.UniqueWeaponPart -> baseAssembly.replacePart(uniquePart.part)
        }

        var updatedAssembly = assembly
        assembly.behaviours.forEachType<UpdateAssemblyBehaviour> {
            updatedAssembly = it.updateAssembly(updatedAssembly)
        }

        return properties.applyAssembly(updatedAssembly, rarity)
    }

    /**
     * Applies all effects/stats of the gun assembly to the properties.
     */
    private fun GunProperties.applyAssembly(assembly: WeaponAssembly, rarity: Rarity): GunProperties {
        // First add elementals so their values can be modified by gun parts.
        addElementals(assembly)
        assembly.applyModifiersToGun(this)

        this.rarity = rarity
        weaponClass = assembly.weaponClass
        manufacturer = assembly.manufacturer
        name = assembly.gunName
        this.assembly = assembly

        applyGradeScaling()
        addManufacturerGimmick(assembly)

        assembly.applyCustomLore(this)

        assembly.forEachBehaviour<PostGenerationBehaviour> {
            it.onFinishGeneration(this, assembly)
        }

        return this
    }

    /**
     * Applies custom names and red/cyan text to gun.
     */
    private fun WeaponAssembly.applyCustomLore(properties: GunProperties) {
        behaviours.forEach {
            if (it is RedTextProvider) {
                properties.redText = it.redText
            }
            if (it is CyanTextProvider) {
                properties.cyanText = it.cyanText
            }
        }
    }

    /**
     * Adds all specific tweaks that are manufacturer specific.
     */
    private fun GunProperties.addManufacturerGimmick(assembly: WeaponAssembly) {
        when (assembly.manufacturer) {
            Manufacturer.DAHL -> {
                applyToEpicGuns {
                    burstCount++
                    burstDelay = Ticks(1)
                }
                if (weaponClass == WeaponClass.SMG) {
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
            Manufacturer.JAKOBS -> {
                applyToEpicGuns {
                    bonusCritMultiplier = (bonusCritMultiplier ?: 0.0) + 0.25
                }
                if (Elemental.EXPLOSIVE in elements) {
                    // Prevent Jakobs guns from totally enjoying explosive damage...
                    baseDamage = Damage(baseDamage.damage / 1.1)
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

        val baseValues = BaseValues.providerOf(assembly.weaponClass)
        val chance = baseValues.baseValue(assembly.manufacturer, Stat.elementalChance)
        val damage = baseValues.baseValue(assembly.manufacturer, Stat.elementalDamage)

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
        val mod = Modifiers.providerOf(weaponClass)
        val std = StandardDeviations.providerOf(weaponClass)

        // Base Damage
        val baseDamageBaseMult = mod.modifier(rarity, Modifier.baseDamage)
        val baseDamageStd = std.standardDeviation(rarity, StandardDeviation.baseDamage)
        val baseDamageMultiplier = random.nextGaussian() * baseDamageStd + baseDamageBaseMult
        baseDamage = Damage(baseDamage.damage * baseDamageMultiplier)

        // Magazine size
        val magSizeMult = mod.modifier(rarity, Modifier.magazineSize)
        val magSizeStd = std.standardDeviation(rarity, StandardDeviation.magazineSize)
        val magSizeMultiplier = random.nextGaussian() * magSizeStd + magSizeMult
        magazineSize = floor(magazineSize * magSizeMultiplier).toInt()

        // Reload time.
        val reloadBonus = mod.modifier(rarity, Modifier.reloadTime)
        reloadSpeed = Ticks(reloadSpeed.ticks + reloadBonus)

        // Fire rate
        val fireRateModifier = mod.modifier(rarity, Modifier.fireRate)
        fireRate *= fireRateModifier

        // Elemental chance and damage
        val eltChanceMultiplier = mod.modifier(rarity, Modifier.elementalChance)
        val eltDamageMult = mod.modifier(rarity, Modifier.elementalDamage)
        val eltDamageStd = std.standardDeviation(rarity, StandardDeviation.elementalDamage)
        val eltDamageMultiplier = random.nextGaussian() * eltDamageStd + eltDamageMult

        elements.forEach { element ->
            elementalChance[element] = Chance(min(0.995, elementalChance[element]!!.chance * eltChanceMultiplier))
            elementalDamage[element] = Damage(elementalDamage[element]!!.damage * eltDamageMultiplier)
        }

        // Accuracy
        val accuracyModifier = mod.modifier(rarity, Modifier.accuracy)
        accuracy = Chance(min(1.0, accuracy.chance + accuracyModifier))

        // Recoil
        val recoilModifier = mod.modifier(rarity, Modifier.recoil)
        recoil += recoilModifier
    }

    /**
     * Execute code on the gun property, but only if the rarity is EPIC or higher.
     */
    fun GunProperties.applyToEpicGuns(apply: GunProperties.() -> Unit) {
        when (rarity) {
            Rarity.EPIC, Rarity.LEGENDARY, Rarity.PEARLESCENT -> {
                this.apply()
            }
            else -> Unit
        }
    }
}