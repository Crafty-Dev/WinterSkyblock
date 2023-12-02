package de.crafty.winterskyblock.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.jei.WinterSkyblockRecipeTypes;
import de.crafty.winterskyblock.jei.recipes.resource_sheeps.IJeiResourceSheepRecipe;
import de.crafty.winterskyblock.registry.ItemRegistry;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
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

    private final IDrawableStatic static_hearts;
    private final IDrawableAnimated animated_hearts;

    public ResourceSheepRecipeCategory(IGuiHelper guiHelper){

        this.background = guiHelper.drawableBuilder(WinterSkyblock.JEI_RECIPE_GUI, 0, 234, 132, 89).setTextureSize(512, 512).build();
        this.icon = guiHelper.drawableBuilder(WinterSkyblock.JEI_RECIPE_GUI, 14, 323, 16, 16).setTextureSize(512, 512).build();

        this.static_hearts = guiHelper.drawableBuilder(WinterSkyblock.JEI_RECIPE_GUI, 0, 323, 14, 12).setTextureSize(512, 512).build();
        this.animated_hearts = guiHelper.createAnimatedDrawable(this.static_hearts, 75, IDrawableAnimated.StartDirection.BOTTOM, false);
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

        builder.addSlot(RecipeIngredientRole.INPUT, 115, 17).addItemStacks(recipe.getWheat());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 115, 37).addItemStacks(recipe.getDrops());

    }

    @Override
    public void draw(IJeiResourceSheepRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {

        this.animated_hearts.draw(stack, 26, 19);
        this.animated_hearts.draw(stack, 26, 19 + 26);
        this.animated_hearts.draw(stack, 26, 19 + 26 * 2);

        Font font = Minecraft.getInstance().font;

        Component name = Component.translatable("entity.winterskyblock." + recipe.getSheepName().toLowerCase() + "_sheep");


        this.drawCenteredString(font, name, this.getWidth() / 2.0F, 0, 1.0F, stack, 0xFF808080);


        Component wheat = Component.translatable("gui.jei.category.resource_sheep.wheat").append(":");
        Component drop = Component.translatable("gui.jei.category.resource_sheep.drop").append(":");

        this.drawString(font, wheat, this.getWidth() - 18 - 4 - font.width(wheat), 21, 1.0F, stack, 0xFF808080);
        this.drawString(font, drop, this.getWidth() - 18 - 4 - font.width(drop), 41, 1.0F, stack, 0xFF808080);


        float chance = recipe.getChance();

        Component comp = Component.literal(((int)(chance * 100.0F)) + "%");
        Component comp1 = Component.literal(((int)(chance * 1.25F * 100.0F)) + "%");
        Component comp2 = Component.literal(((int)(1.0F * 100.0F)) + "%");


        this.drawCenteredString(font, comp, 32, 34, 0.5F, stack, 0xFF808080);
        this.drawCenteredString(font, comp1, 32, 34 + 26, 0.5F, stack, 0xFF808080);
        this.drawCenteredString(font, comp2, 32, 34 + 26 * 2, 0.5F, stack, 0xFF808080);

    }



    private void drawCenteredString(Font font, Component text, float x, float y, float scale, PoseStack stack, int color){
        this.drawString(font, text, x - font.width(text) / 2.0F * scale, y, scale, stack, color);
    }

    private void drawString(Font font, Component text, float x, float y, float scale, PoseStack stack, int color){

        stack.pushPose();
        stack.translate(x, y, 0.0F);
        stack.pushPose();
        stack.scale(scale, scale, 1.0F);
        font.draw(stack, text, 0, 0, color);
        stack.popPose();
        stack.popPose();
    }
}
