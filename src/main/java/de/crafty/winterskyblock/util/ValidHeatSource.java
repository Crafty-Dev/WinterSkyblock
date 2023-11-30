package de.crafty.winterskyblock.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ValidHeatSource {


    private static final Condition NO_CONDITION = (level, state, pos) -> true;

    public static final List<HeatSource> SOURCES = List.of(
            new HeatSource(Blocks.TORCH, 0.25F, NO_CONDITION),
            new HeatSource(Blocks.SOUL_TORCH, 0.5F, NO_CONDITION),
            new HeatSource(Blocks.LAVA, 2.5F, NO_CONDITION),
            new HeatSource(Blocks.CAMPFIRE, 1.0F, (level, state, pos) -> state.getValue(CampfireBlock.LIT)),
            new HeatSource(Blocks.SOUL_CAMPFIRE, 2.5F, (level, state, pos) -> state.getValue(CampfireBlock.LIT)),
            new HeatSource(Blocks.FIRE, 0.75F, NO_CONDITION, () -> new ItemStack(Items.FLINT_AND_STEEL)),
            new HeatSource(Blocks.SOUL_FIRE, 1.5F, NO_CONDITION, () -> new ItemStack(Items.FLINT_AND_STEEL))
    );


    public static float getHeatEfficiency(LevelAccessor level, BlockState state, BlockPos pos) {
        for (HeatSource source : SOURCES) {

            if (source.block() != state.getBlock())
                continue;

            if (source.condition().check(level, state, pos))
                return source.heatEfficiency();
        }

        return 0.0F;
    }


    public record HeatSource(Block block, float heatEfficiency, Condition condition, Supplier<ItemStack> representable) {

        private HeatSource(Block block, float heatEfficiency, Condition condition){
            this(block, heatEfficiency, condition, () -> new ItemStack(block));
        }

    }


    interface Condition {

        boolean check(LevelAccessor level, BlockState state, BlockPos pos);

    }

}
