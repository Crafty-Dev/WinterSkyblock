package de.crafty.winterskyblock.jei.recipes.resource_sheeps;

import de.crafty.winterskyblock.entity.ResourceSheep;
import de.crafty.winterskyblock.item.ResourceWheatItem;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ResourceSheepRecipe implements IJeiResourceSheepRecipe {

    private final ResourceSheep.Type sheepType;


    public ResourceSheepRecipe(ResourceSheep.Type sheepType){
        this.sheepType = sheepType;
    }

    @Override
    public float getStrength() {
        return this.sheepType.getStrength();
    }

    @Override
    public float getChance() {
        return ((ResourceWheatItem)this.sheepType.getBait()).getSpawnChance();
    }

    @Override
    public List<ItemStack> getDrops() {
        return List.of(new ItemStack(this.sheepType.getResource()));
    }

    @Override
    public List<ItemStack> getWheat() {
        return List.of(new ItemStack(this.sheepType.getBait()));
    }
}
