package de.crafty.winterskyblock.jei.recipes.block_transformation;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public interface IJeiBlockTransformationRecipe {


    Block getBase();

    ItemStack getConverter();

    Block getResult();

}
