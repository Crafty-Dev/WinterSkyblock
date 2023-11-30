package de.crafty.winterskyblock.jei.category;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.blockentities.MeltingCobblestoneBlockEntity;
import de.crafty.winterskyblock.jei.WinterSkyblockRecipeTypes;
import de.crafty.winterskyblock.jei.recipes.cobblestone_melting.IJeiCobblestoneMeltingRecipe;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CobblestoneMeltingRecipeCategory implements IRecipeCategory<IJeiCobblestoneMeltingRecipe> {

    private final LoadingCache<Integer, IDrawableAnimated> cachedFlames;

    private final IDrawable icon;
    private final IDrawable background;


    public CobblestoneMeltingRecipeCategory(IGuiHelper guiHelper){

        this.background = guiHelper.createDrawable(WinterSkyblock.JEI_RECIPE_GUI, 0, 72, 98, 54);

        this.icon = guiHelper.createDrawableItemStack(new ItemStack(Items.COBBLESTONE));

        this.cachedFlames = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer burnTime) {
                        return guiHelper.drawableBuilder(WinterSkyblock.JEI_RECIPE_GUI, 98, 72, 14, 14)
                                .buildAnimated(burnTime, IDrawableAnimated.StartDirection.BOTTOM, false);
                    }
                });
    }

    @Override
    public RecipeType<IJeiCobblestoneMeltingRecipe> getRecipeType() {
        return WinterSkyblockRecipeTypes.COBBLESTONE_MELTING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.jei.category.cobblestone_melting");
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
    public void setRecipe(IRecipeLayoutBuilder builder, IJeiCobblestoneMeltingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 9, 1).addItemStack(new ItemStack(Items.COBBLESTONE));


        if(recipe.getHeatSource() instanceof LiquidBlock liquidBlock){
            builder.addSlot(RecipeIngredientRole.INPUT, 9, 37).addIngredient(ForgeTypes.FLUID_STACK, new FluidStack(liquidBlock.getFluid(), 1000));
        }else
            builder.addSlot(RecipeIngredientRole.INPUT, 9, 37).addItemStack(recipe.getRepresentable().get());


        builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 19).addIngredient(ForgeTypes.FLUID_STACK, new FluidStack(Fluids.LAVA, 1000));
    }

    @Override
    public void draw(IJeiCobblestoneMeltingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {

        int meltingTime = (int) (MeltingCobblestoneBlockEntity.BASE_MELTING_TIME / (10 * recipe.getHeatEfficiency()));
        IDrawableAnimated flame = this.cachedFlames.getUnchecked(meltingTime);
        flame.draw(stack, 9, 20);

        //Draw Heat Efficiency
        Font font = Minecraft.getInstance().font;

        Component heatEfficiency = Component.literal(recipe.getHeatEfficiency() + "x");
        font.draw(stack, heatEfficiency, this.getWidth() - font.width(heatEfficiency), 45, 0xFF808080);

        Component heatSource = recipe.getHeatSource().getName();
        font.draw(stack, heatSource, this.getWidth() - font.width(heatSource), 1, 0xFF808080);
    }
}
