package de.crafty.winterskyblock.jei.category;

import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.jei.WinterSkyblockRecipeTypes;
import de.crafty.winterskyblock.jei.recipes.lava_conversion.IJeiLavaConversionRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

;

public class LavaConversionRecipeCategory implements IRecipeCategory<IJeiLavaConversionRecipe> {


    private final IDrawable background;
    private final IDrawable icon;


    public LavaConversionRecipeCategory(IGuiHelper guiHelper){
        this.background = guiHelper.createDrawable(WinterSkyblock.JEI_RECIPE_GUI, 0, 0, 133, 36);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(Items.LAVA_BUCKET));
    }

    @Override
    public RecipeType<IJeiLavaConversionRecipe> getRecipeType() {
        return WinterSkyblockRecipeTypes.LAVA_CONVERSION;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.jei.category.lava_conversion");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IJeiLavaConversionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 79, 1).addItemStack(new ItemStack(recipe.getIngredient()));

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 18 + 1).addItemStack(new ItemStack(Items.LAVA_BUCKET));
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 18 + 1).addItemStack(new ItemStack(Items.CAULDRON));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 18 + 1).addItemStacks(recipe.getResults());

    }

}
