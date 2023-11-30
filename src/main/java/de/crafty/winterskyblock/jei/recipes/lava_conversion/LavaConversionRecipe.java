package de.crafty.winterskyblock.jei.recipes.lava_conversion;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class LavaConversionRecipe implements IJeiLavaConversionRecipe {

    private final Item ingredient;
    private final List<ItemStack> results;

    public LavaConversionRecipe(Item ingredient, List<ItemStack> results){
        this.ingredient = ingredient;
        this.results = results;
    }

    @Override
    public Item getIngredient() {
        return this.ingredient;
    }

    @Override
    public List<ItemStack> getResults() {
        return this.results;
    }
}
