package maliwan.mcbl.weapons.gun

import maliwan.mcbl.util.Damage
import maliwan.mcbl.util.Ticks
import maliwan.mcbl.util.asList
import maliwan.mcbl.util.toChance
import kotlin.math.ceil
import kotlin.math.floor

/**
 * @author Hannah Schellekens
 */
open class StatModifier(

    /**
     * The number to add/multiply.
     */
    val value: Double,

    /**
     * How to apply this modifier to a base value.
     */
    val type: StatModifier.Type,

    /**
     * Which property to edit.
     */
    val property: Property
) {

    /**
     * Updates the given gun properties to reflect the stat changes defined in this stat modifier.
     *
     * Does change the state of the given gun properties.
     */
    fun applyStatModifier(stats: GunProperties): GunProperties {
        val resultingValues = property.getProperty(stats).toMutableList()

        for (i in resultingValues.indices) {
            resultingValues[i] = when (type) {
                Type.ADD -> resultingValues[i] + value
                Type.MULTIPLY -> resultingValues[i] * value
            }
        }

        property.setProperty(stats, resultingValues)

        return stats
    }

    /**
     * @author Hannah Schellekens
     */
    enum class Property(

        /**
         * How to get the property that must be changed.
         */
        val getProperty: (stats: GunProperties) -> Iterable<Double>,

        /**
         * How to aplpy the new value to the gun properties.
         */
        val setProperty: (stats: GunProperties, value: List<Double>) -> Unit
    ) {

        BASE_DAMAGE(
            { it.baseDamage.damage.asList() },
            { stats, it -> stats.baseDamage = Damage(it.first()) }
        ),
        ACCURACY(
            { it.accuracy.chance.asList() },
            { stats, it -> stats.accuracy = it.first().toChance() }
        ),
        RECOIL(
            { it.recoil.asList() },
            { stats, it -> stats.recoil = it.first() }
        ),
        FIRE_RATE(
            { it.fireRate.asList() },
            { stats, it -> stats.fireRate = it.first() }
        ),
        MAGAZINE_SIZE(
            { it.magazineSize.toDouble().asList() },
            { stats, it -> stats.magazineSize = floor(it.first()).toInt() }
        ),
        RELOAD_SPEED(
            { it.reloadSpeed.ticks.toDouble().asList() },
            { stats, it -> stats.reloadSpeed = Ticks(ceil(it.first()).toInt()) }
        ),
        ELEMENTAL_DAMAGE(
            { it.elementalDamage.values.map { dmg -> dmg.damage } },
            { stats, list ->
                // Because of linked has maps, all elements should be nicely sorted and all elementals should match up...
                stats.elements.forEachIndexed { index, elemental ->
                    stats.elementalDamage[elemental] = Damage(list[index])
                }
            }
        ),
        ELEMENTAL_CHANCE(
            { it.elementalChance.values.map { prob -> prob.chance } },
            { stats, list ->
                // Because of linked has maps, all elements should be nicely sorted and all elementals should match up...
                stats.elements.forEachIndexed { index, elemental ->
                    stats.elementalChance[elemental] = list[index].toChance()
                }
            }
        ),
        ;
    }

    /**
     * @author Hannah Schellekens
     */
    enum class Type {

        ADD,
        MULTIPLY,
        ;
    }
}

/**
 * Get only the stat modifiers of a certain type.
 */
fun List<StatModifier>.ofType(type: StatModifier.Type) = filter { it.type == type }

/**
 * Applies these stat modifiers in order, and with a certain operator precedence defined in `order`.
 *
 * @param properties
 *          The gun properties to apply the stat modifiers to.
 * @param order
 *          In which operator precedence, see [StatModifier.Type].
 */
fun List<StatModifier>.applyAll(
    properties: GunProperties,
    order: List<StatModifier.Type> = listOf(StatModifier.Type.MULTIPLY, StatModifier.Type.ADD)
) {
    order.forEach { type ->
        ofType(type).forEach { it.applyStatModifier(properties) }
    }
}

/**
 * Utility builder to support a concise DSL to define stat modifiers.
 * A lot of modifiers need to be defined, so lets make it easier :)
 *
 * @author Hannah Schellekens
 */
class StatModifierBuilder {

    private val resultList = ArrayList<StatModifier>()

    fun add(value: Double, property: StatModifier.Property): StatModifierBuilder {
        resultList += StatModifier(value, StatModifier.Type.ADD, property)
        return this
    }

    fun subtract(value: Double, property: StatModifier.Property): StatModifierBuilder {
        return add(-value, property)
    }

    fun multiply(value: Double, property: StatModifier.Property): StatModifierBuilder {
        resultList += StatModifier(value, StatModifier.Type.MULTIPLY, property)
        return this
    }

    fun divide(value: Double, property: StatModifier.Property): StatModifierBuilder {
        return multiply(1.0 / value, property)
    }

    fun build(): List<StatModifier> = resultList
}

/**
 * Creates a builder to quickly create a list of stat modifiers.
 * Use add(value, property) and multiply(value, property) to add additive and multiplicative values.
 */
fun statModifierList(builder: StatModifierBuilder.() -> Unit): List<StatModifier> {
    val listBuilder = StatModifierBuilder()
    listBuilder.builder()
    return listBuilder.build()
}