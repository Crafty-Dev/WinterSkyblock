package de.crafty.winterskyblock.creativetab;

import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class WinterSkyblockCreativeTab extends CreativeModeTab {


    public WinterSkyblockCreativeTab() {
        super("winter_skyblock");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemRegistry.LEAF_PRESS.get());
    }
}
