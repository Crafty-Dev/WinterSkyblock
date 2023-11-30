package de.crafty.winterskyblock.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public abstract class AbstractTransformRecipe implements Recipe<Container> {


    @Override
    public boolean isSpecial() {
        return true;
    }
}
