package de.crafty.winterskyblock.jei.recipes.hammering;

import de.crafty.winterskyblock.handler.HammerDropHandler;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

public interface IJeiHammeringRecipe {


    List<HammerDropHandler.HammerDrop> getResults();

    List<Block> getBlocks();

}
