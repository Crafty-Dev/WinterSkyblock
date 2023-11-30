package de.crafty.winterskyblock.jei;

import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.jei.recipes.block_transformation.IJeiBlockTransformationRecipe;
import de.crafty.winterskyblock.jei.recipes.cobblestone_melting.IJeiCobblestoneMeltingRecipe;
import de.crafty.winterskyblock.jei.recipes.hammering.IJeiHammeringRecipe;
import de.crafty.winterskyblock.jei.recipes.lava_conversion.IJeiLavaConversionRecipe;
import de.crafty.winterskyblock.jei.recipes.leaf_press.IJeiLeafPressRecipe;
import mezz.jei.api.recipe.RecipeType;

public class WinterSkyblockRecipeTypes {

    public static final RecipeType<IJeiLavaConversionRecipe> LAVA_CONVERSION = RecipeType.create(WinterSkyblock.MODID, "lava_conversion", IJeiLavaConversionRecipe.class);
    public static final RecipeType<IJeiBlockTransformationRecipe> BLOCK_TRANSFORMATION = RecipeType.create(WinterSkyblock.MODID, "block_transformation", IJeiBlockTransformationRecipe.class);

    public static final RecipeType<IJeiCobblestoneMeltingRecipe> COBBLESTONE_MELTING = RecipeType.create(WinterSkyblock.MODID, "cobblestone_melting", IJeiCobblestoneMeltingRecipe.class);

    public static final RecipeType<IJeiHammeringRecipe> HAMMERING = RecipeType.create(WinterSkyblock.MODID, "hammering", IJeiHammeringRecipe.class);

    public static final RecipeType<IJeiLeafPressRecipe> LEAF_PRESS = RecipeType.create(WinterSkyblock.MODID, "leaf_press", IJeiLeafPressRecipe.class);
}
