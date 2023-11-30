package de.crafty.winterskyblock.jei.recipes.hammering;

import de.crafty.winterskyblock.handler.HammerDropHandler;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

public class HammeringRecipe implements IJeiHammeringRecipe {

    private final List<Block> blocks;
    private final List<HammerDropHandler.HammerDrop> drops;

    public HammeringRecipe(List<Block> blocks, List<HammerDropHandler.HammerDrop> drops){
        this.blocks = blocks;
        this.drops = drops;
    }

    @Override
    public List<HammerDropHandler.HammerDrop> getResults() {
        return this.drops;
    }

    @Override
    public List<Block> getBlocks() {
        return this.blocks;
    }
}
