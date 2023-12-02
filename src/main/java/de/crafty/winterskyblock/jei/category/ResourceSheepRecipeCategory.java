package de.crafty.winterskyblock.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.jei.WinterSkyblockRecipeTypes;
import de.crafty.winterskyblock.jei.recipes.resource_sheeps.IJeiResourceSheepRecipe;
import de.crafty.winterskyblock.registry.ItemRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ResourceSheepRecipeCategory implements IRecipeCategory<IJeiResourceSheepRecipe> {

    private final IDrawable background;
    private final IDrawable icon;

    public ResourceSheepRecipeCategory(IGuiHelper guiHelper){

        this.background = guiHelper.drawableBuilder(WinterSkyblock.JEI_RECIPE_GUI, 174, 0, 66, 130).setTextureSize(512, 512).build();
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

        builder.addSlot(RecipeIngredientRole.INPUT, 49, 12).addItemStacks(recipe.getWheat());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 49, 32).addItemStacks(recipe.getDrops());

    }

    @Override
    public void draw(IJeiResourceSheepRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {


        Font font = Minecraft.getInstance().font;

        Component name = Component.translatable("entity.winterskyblock." + recipe.getSheepName().toLowerCase() + "_sheep");

        font.draw(stack, name, this.getWidth() / 2.0F - font.width(name) / 2.0F, 0, 0xFF808080);

        Component wheat = Component.translatable("gui.jei.category.resource_sheep.wheat").append(":");
        Component drop = Component.translatable("gui.jei.category.resource_sheep.drop").append(":");

        font.draw(stack, wheat, 4, 11 + 9 - font.lineHeight / 2.0F, 0xFF808080);
        font.draw(stack, drop, 4, 11 + 29 - font.lineHeight / 2.0F, 0xFF808080);
    }
}
