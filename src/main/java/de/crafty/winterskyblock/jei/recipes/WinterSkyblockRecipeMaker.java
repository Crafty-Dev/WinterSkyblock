package de.crafty.winterskyblock.jei.recipes;

import de.crafty.winterskyblock.block.LeafPressBlock;
import de.crafty.winterskyblock.entity.ResourceSheep;
import de.crafty.winterskyblock.handler.BlockTransformationHandler;
import de.crafty.winterskyblock.handler.HammerDropHandler;
import de.crafty.winterskyblock.handler.LavaDropHandler;
import de.crafty.winterskyblock.jei.recipes.block_transformation.BlockTransformationRecipe;
import de.crafty.winterskyblock.jei.recipes.block_transformation.IJeiBlockTransformationRecipe;
import de.crafty.winterskyblock.jei.recipes.cobblestone_melting.CobblestoneMeltingRecipe;
import de.crafty.winterskyblock.jei.recipes.cobblestone_melting.IJeiCobblestoneMeltingRecipe;
import de.crafty.winterskyblock.jei.recipes.hammering.HammeringRecipe;
import de.crafty.winterskyblock.jei.recipes.hammering.IJeiHammeringRecipe;
import de.crafty.winterskyblock.jei.recipes.lava_conversion.IJeiLavaConversionRecipe;
import de.crafty.winterskyblock.jei.recipes.lava_conversion.LavaConversionRecipe;
import de.crafty.winterskyblock.jei.recipes.leaf_press.IJeiLeafPressRecipe;
import de.crafty.winterskyblock.jei.recipes.leaf_press.LeafPressRecipe;
import de.crafty.winterskyblock.jei.recipes.resource_sheeps.IJeiResourceSheepRecipe;
import de.crafty.winterskyblock.jei.recipes.resource_sheeps.ResourceSheepRecipe;
import de.crafty.winterskyblock.registry.EntityRegistry;
import de.crafty.winterskyblock.registry.ItemRegistry;
import de.crafty.winterskyblock.util.ValidHeatSource;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WinterSkyblockRecipeMaker {


    public static List<IJeiBlockTransformationRecipe> getBlockTransformationRecipes(){

        List<IJeiBlockTransformationRecipe> list = new ArrayList<>();

        BlockTransformationHandler.TRANSFORMATIONS.forEach(blockTransformation -> {

            list.add(new BlockTransformationRecipe(blockTransformation.block().get(), blockTransformation.representable().get(), blockTransformation.result().get().getBlock()));
        });


        return list;
    }

    public static List<IJeiLavaConversionRecipe> getLavaConversionRecipes(){

        List<IJeiLavaConversionRecipe> list = new ArrayList<>();

        LavaDropHandler.LAVA_DROPS.forEach((itemSupplier, lavaDrops) -> {

            List<ItemStack> items = new ArrayList<>();

            lavaDrops.forEach(lavaDrop -> {
                for(int i = lavaDrop.min(); i <= lavaDrop.max(); i++){
                    items.add(new ItemStack(lavaDrop.itemSupplier().get(), i));
                }
            });

            list.add(new LavaConversionRecipe(itemSupplier.get(), items));

        });

        return list;
    }

    public static List<IJeiCobblestoneMeltingRecipe> getCobblestoneMeltingRecipes(){

        List<IJeiCobblestoneMeltingRecipe> list = new ArrayList<>();

        ValidHeatSource.SOURCES.forEach(heatSource -> list.add(new CobblestoneMeltingRecipe(heatSource)));

        return list;
    }

    public static List<IJeiHammeringRecipe> getHammeringRecipes(){

        List<IJeiHammeringRecipe> list = new ArrayList<>();

        HammerDropHandler.BLOCK_GROUPS.forEach(blocks -> {
            if(!blocks.isEmpty())
                list.add(new HammeringRecipe(blocks, HammerDropHandler.HAMMER_DROPS.get(blocks.get(0))));
        });

        return list;
    }

    //TODO: Create rework of Leaf Press (Fluid Capability, Recipes, etc...)
    public static List<IJeiLeafPressRecipe> getLeafPressRecipes(){

        List<IJeiLeafPressRecipe> list = new ArrayList<>();

        list.add(new LeafPressRecipe(LeafPressBlock.VALID_LEAVES, 0.25f, new ItemStack(ItemRegistry.DRIED_LEAVES.get())));

        return list;
    }

    public static List<IJeiResourceSheepRecipe> getResourceSheepRecipes(){

        List<IJeiResourceSheepRecipe> list = new ArrayList<>();

        for(ResourceSheep.Type type : ResourceSheep.Type.values()){
            list.add(new ResourceSheepRecipe(type));
        }

        return list;
    }
}
