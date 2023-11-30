package de.crafty.winterskyblock.jei;

import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.jei.category.*;
import de.crafty.winterskyblock.jei.recipes.WinterSkyblockRecipeMaker;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JustEnoughItemsIntegration implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(WinterSkyblock.MODID, "jei");
    }


    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(WinterSkyblockRecipeTypes.LAVA_CONVERSION, WinterSkyblockRecipeMaker.getLavaConversionRecipes());
        registration.addRecipes(WinterSkyblockRecipeTypes.BLOCK_TRANSFORMATION, WinterSkyblockRecipeMaker.getBlockTransformationRecipes());
        registration.addRecipes(WinterSkyblockRecipeTypes.COBBLESTONE_MELTING, WinterSkyblockRecipeMaker.getCobblestoneMeltingRecipes());
        registration.addRecipes(WinterSkyblockRecipeTypes.HAMMERING, WinterSkyblockRecipeMaker.getHammeringRecipes());
        registration.addRecipes(WinterSkyblockRecipeTypes.LEAF_PRESS, WinterSkyblockRecipeMaker.getLeafPressRecipes());
    }


    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(new LavaConversionRecipeCategory(guiHelper));
        registration.addRecipeCategories(new BlockTransformationRecipeCategory(guiHelper));
        registration.addRecipeCategories(new CobblestoneMeltingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new HammeringRecipeCategory(guiHelper));
        registration.addRecipeCategories(new LeafPressRecipeCategory(guiHelper));
    }
}
