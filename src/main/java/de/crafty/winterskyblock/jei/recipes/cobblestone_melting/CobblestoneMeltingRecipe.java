package de.crafty.winterskyblock.jei.recipes.cobblestone_melting;

import de.crafty.winterskyblock.util.ValidHeatSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class CobblestoneMeltingRecipe implements IJeiCobblestoneMeltingRecipe {

    private final ValidHeatSource.HeatSource heatSource;

    public CobblestoneMeltingRecipe(ValidHeatSource.HeatSource heatSource){
        this.heatSource = heatSource;
    }

    @Override
    public Block getHeatSource() {
        return heatSource.block();
    }

    @Override
    public float getHeatEfficiency() {
        return heatSource.heatEfficiency();
    }

    @Override
    public Supplier<ItemStack> getRepresentable() {
        return this.heatSource.representable();
    }
}
