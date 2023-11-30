package de.crafty.winterskyblock.jei.recipes.cobblestone_melting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface IJeiCobblestoneMeltingRecipe {


    Block getHeatSource();

    float getHeatEfficiency();

    Supplier<ItemStack> getRepresentable();

}
