package maliwan.mcbl.inventory

import maliwan.mcbl.Keys
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe

/**
 * Adds all recipes.
 */
fun registerRecipes() {
    registerSduRecipe(Keys.recipeSduRare, SDU.uncommon.item, Material.GOLD_BLOCK, SDU.rare.item)
    registerSduRecipe(Keys.recipeSduEpic, SDU.rare.item, Material.EMERALD_BLOCK, SDU.epic.item)
    registerSduRecipe(Keys.recipeSduLegendary, SDU.epic.item, Material.DIAMOND_BLOCK, SDU.legendary.item)
    registerSduRecipe(Keys.recipeSduPearlescent, SDU.legendary.item, Material.NETHERITE_BLOCK, SDU.pearlescent.item)
}

private fun registerSduRecipe(
    key: NamespacedKey,
    base: ItemStack,
    ingredient: Material,
    result: ItemStack
) {
    val recipe = ShapedRecipe(key, result)
    recipe.shape("BBB", "BSB", "BBB")
    recipe.setIngredient('B', ingredient)
    recipe.setIngredient('S', RecipeChoice.ExactChoice(base))
    Bukkit.addRecipe(recipe)
}