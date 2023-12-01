package de.crafty.winterskyblock.jei.category;

import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.jei.WinterSkyblockRecipeTypes;
import de.crafty.winterskyblock.jei.recipes.resource_sheeps.IJeiResourceSheepRecipe;
import de.crafty.winterskyblock.registry.ItemRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ResourceSheepRecipeCategory implements IRecipeCategory<IJeiResourceSheepRecipe> {

    private final IDrawable background;
    private final IDrawable icon;

    public ResourceSheepRecipeCategory(IGuiHelper guiHelper){

        this.background = guiHelper.createDrawable(WinterSkyblock.JEI_RECIPE_GUI, 133, 0, 66, 36);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ItemRegistry.COAL_ENRICHED_WHEAT.get()));
    }

    @Override
    public RecipeType<IJeiResourceSheepRecipe> getRecipeType() {
        return WinterSkyblockRecipeTypes.RESOURCE_SHEEP;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.jei.category.resource_sheep");
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
    public void setRecipe(IRecipeLayoutBuilder builder, IJeiResourceSheepRecipe recipe, IFocusGroup focuses) {

    }
}
