package maliwan.mcbl.loot.gen

import maliwan.mcbl.loot.LootPool
import maliwan.mcbl.loot.ManufacturerTable
import maliwan.mcbl.loot.RarityTable
import maliwan.mcbl.loot.WeaponClassTable
import maliwan.mcbl.util.Damage
import maliwan.mcbl.util.Probability
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
import org.bukkit.ChatColor
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
            Rarity.PEARLESCENT -> return generatePearlescent()
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
    fun generatePearlescent(ofType: WeaponClass? = null, ofManufacturer: Manufacturer? = null): GunProperties {
        val eligibleParts = UniqueGunParts.partsFor(Rarity.PEARLESCENT, ofType, ofManufacturer)

        val pearlescentPart = eligibleParts.random()
        val manufacturer = pearlescentPart.manufacturer
        val weaponClass = pearlescentPart.weaponClass

        val properties = newBaseValueProperties(manufacturer, weaponClass)
        val baseAssembly = WeaponAssemblyGenerator.forType(weaponClass, setOf(manufacturer), random).generate(Rarity.PEARLESCENT)
        val assembly = when (pearlescentPart) {
            is UniqueGunPart.UniqueCapacitor -> baseAssembly.replaceCapacitor(pearlescentPart.capacitor)
            is UniqueGunPart.UniqueWeaponPart -> baseAssembly.replacePart(pearlescentPart.part)
        }

        val updatedAssembly = assembly.update()

        // Small chance for it to generate as a hybrid weapon.
        val hybridAssembly = if (kotlin.random.Random.nextDouble() < 0.05) {
            val allEligibleParts = UniqueGunParts.partsFor(Rarity.PEARLESCENT, ofType, allowLowerRarity = true) +
                    UniqueGunParts.partsFor(Rarity.LEGENDARY, ofType, ofManufacturer) +
                    UniqueGunParts.partsFor(Rarity.EPIC, ofType, ofManufacturer) +
                    UniqueGunParts.partsFor(Rarity.RARE, ofType, ofManufacturer)
            forgeHybrid(updatedAssembly, pearlescentPart, allEligibleParts)
        }
        else updatedAssembly

        return properties.applyAssembly(hybridAssembly, Rarity.PEARLESCENT)
    }

    /**
     * Generates a new legendary weapon.
     */
    fun generateLegendary(ofType: WeaponClass? = null, ofManufacturer: Manufacturer? = null): GunProperties {
        val eligibleParts = UniqueGunParts.partsFor(Rarity.LEGENDARY, ofType, ofManufacturer)

        val legendaryPart = eligibleParts.random()
        val manufacturer = legendaryPart.manufacturer
        val weaponClass = legendaryPart.weaponClass

        val properties = newBaseValueProperties(manufacturer, weaponClass)
        val baseAssembly = WeaponAssemblyGenerator.forType(weaponClass, setOf(manufacturer), random).generate(Rarity.LEGENDARY)
        val assembly = when (legendaryPart) {
            is UniqueGunPart.UniqueCapacitor -> baseAssembly.replaceCapacitor(legendaryPart.capacitor)
            is UniqueGunPart.UniqueWeaponPart -> baseAssembly.replacePart(legendaryPart.part)
        }

        val updatedAssembly = assembly.update()

        // Small chance for it to generate as a hybrid weapon.
        val hybridAssembly = if (kotlin.random.Random.nextDouble() < 0.05) {
            val allEligibleParts = UniqueGunParts.partsFor(Rarity.LEGENDARY, ofType, allowLowerRarity = true) +
                    UniqueGunParts.partsFor(Rarity.EPIC, ofType, ofManufacturer) +
                    UniqueGunParts.partsFor(Rarity.RARE, ofType, ofManufacturer)
            forgeHybrid(updatedAssembly, legendaryPart, allEligibleParts)
        }
        else updatedAssembly

        return properties.applyAssembly(hybridAssembly, Rarity.LEGENDARY)
    }

    /**
     * Generates a new unique weapon that is not legendary+.
     */
    fun generateUnique(rarity: Rarity, ofType: WeaponClass? = null, ofManufacturer: Manufacturer? = null): GunProperties {
        val baseParts = when (rarity) {
            Rarity.EPIC -> UniqueGunParts.epicParts
            else -> UniqueGunParts.rareParts
        }

        val eligibleParts = if (ofType != null || ofManufacturer != null) {
            baseParts.filter {
                (ofType != null && it.weaponClass == ofType) &&
                        (ofManufacturer != null && it.manufacturer == ofManufacturer)
            }
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

        val updatedAssembly = assembly.update()
        return properties.applyAssembly(updatedAssembly, rarity)
    }

    /**
     * Turns a regular legendary weapon into a hybrid weapon: where there are 2 unique parts in one weapon.
     *
     * @param base The base assembly to modify.
     * @param basePart The first unique part added to the gun.
     * @param eligibleParts All possible parts.
     */
    fun forgeHybrid(
        base: WeaponAssembly,
        basePart: UniqueGunPart,
        eligibleParts: List<UniqueGunPart>
    ): WeaponAssembly {
        // Regular part to add: EZ as there is only 1 kind of capacitor.
        if (basePart is UniqueGunPart.UniqueCapacitor) {
            val part = eligibleParts
                .filterIsInstance<UniqueGunPart.UniqueWeaponPart>()
                .randomOrNull()
                ?: return base
            return base.replacePart(part.part)
        }

        // Not capacitor, harder as we need to figure out which gun part it is exactly.
        val baseWeaponPart = basePart as UniqueGunPart.UniqueWeaponPart
        val candidateHybridParts = eligibleParts.filter {
            when (it) {
                is UniqueGunPart.UniqueCapacitor -> true
                is UniqueGunPart.UniqueWeaponPart -> {
                    val partType = it.part::class
                    partType != baseWeaponPart.part::class
                }
            }
        }
        val hybridPart = candidateHybridParts.randomOrNull() ?: kotlin.run {
            return base
        }

        return when (hybridPart) {
            is UniqueGunPart.UniqueWeaponPart -> base.replacePart(hybridPart.part)
            is UniqueGunPart.UniqueCapacitor -> base.replaceCapacitor(hybridPart.capacitor)
        }
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
        name = assembly.calculateGunName()
        this.assembly = assembly

        applyGradeScaling()
        addManufacturerGimmick(assembly)
        updateElementalChances()

        assembly.applyCustomLore(this)

        assembly.forEachBehaviour<PostGenerationBehaviour> {
            it.onFinishGeneration(this, assembly)
        }

        return this
    }

    /**
     * Post processes the elemental chance.
     */
    private fun GunProperties.updateElementalChances() {
        val baseChance = elementalProbability[Elemental.CRYO] ?: return
        val multiplier = when (weaponClass) {
            WeaponClass.PISTOL -> 1.3
            WeaponClass.SHOTGUN -> 1.0
            WeaponClass.ASSAULT_RIFLE -> 1.0
            WeaponClass.SNIPER -> 1.5
            WeaponClass.SMG -> 0.5
            WeaponClass.LAUNCHER -> 1.0
        }
        elementalProbability[Elemental.CRYO] = Probability(baseChance.chance * multiplier)
    }

    /**
     * Applies weapon specific behaviours to this assembly, returns the new and updated assembly.
     */
    fun WeaponAssembly.update(): WeaponAssembly {
        var updatedAssembly = this
        behaviours.forEachType<UpdateAssemblyBehaviour> {
            updatedAssembly = it.updateAssembly(updatedAssembly)
        }
        return updatedAssembly
    }

    /**
     * Applies custom names and red/cyan text to gun.
     */
    private fun WeaponAssembly.applyCustomLore(properties: GunProperties) {
        val redTextLines = ArrayList<String>()
        val cyanTextLines = ArrayList<String>()

        behaviours.forEach {
            if (it is RedTextProvider) {
                redTextLines += it.redText
            }
            if (it is CyanTextProvider) {
                cyanTextLines += it.cyanText
            }
        }

        redTextLines.forEachIndexed { i, redText ->
            // Val evens: regular red.
            if (i % 2 == 0) {
                properties.redText = (properties.redText ?: "") + redText + "\n"
            }
            // Odds: darker red, to differentiate hybrid red text.
            else {
                properties.redText +=
                    "${ChatColor.DARK_RED}" + redText.replace("\n", "\n${ChatColor.DARK_RED}")
            }
        }
        properties.redText = properties.redText?.trim()

        properties.cyanText = cyanTextLines.joinToString("\n")
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
                    elementalProbability[Elemental.EXPLOSIVE] = Probability.ONE
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
        val chance = baseValues.baseValue(assembly.manufacturer, Stat.elementalProbability)
        val damage = baseValues.baseValue(assembly.manufacturer, Stat.elementalDamage)

        // Update properties to include element.
        elements += element
        elementalDamage[element] = damage * element.noDotMultiplier
        elementalProbability[element] = if (element == Elemental.EXPLOSIVE) Probability.ONE else chance
        elementalDuration[element] = element.baseDotDuration

        // Cut base damage for DoT elements.
        val damageMultiplier = when (element) {
            Elemental.INCENDIARY,
            Elemental.CORROSIVE,
            Elemental.SHOCK,
            Elemental.SLAG -> 0.833
            Elemental.CRYO -> 0.95
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
            elementalProbability[element] = Probability(min(0.995, elementalProbability[element]!!.chance * eltChanceMultiplier))
            elementalDamage[element] = Damage(elementalDamage[element]!!.damage * eltDamageMultiplier)
        }

        // Accuracy
        val accuracyModifier = mod.modifier(rarity, Modifier.accuracy)
        accuracy = Probability(min(1.0, accuracy.chance + accuracyModifier))

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