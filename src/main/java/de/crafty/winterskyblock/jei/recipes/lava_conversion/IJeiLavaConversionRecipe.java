package de.crafty.winterskyblock.jei.recipes.lava_conversion;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;

public interface IJeiLavaConversionRecipe {


    Item getIngredient();

    List<ItemStack> getResults();

}
