package de.crafty.winterskyblock.jei.recipes.resource_sheeps;

import de.crafty.winterskyblock.entity.ResourceSheep;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IJeiResourceSheepRecipe {


    float getStrength();

    float getChance();

    List<ItemStack> getDrops();

    List<ItemStack> getWheat();

    String getSheepName();


}
