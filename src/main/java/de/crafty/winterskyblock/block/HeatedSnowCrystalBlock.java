package de.crafty.winterskyblock.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HeatedSnowCrystalBlock extends Block {

    public HeatedSnowCrystalBlock(Properties properties) {
        super(properties);
    }


    @Override
    public void fallOn(Level level, BlockState state, BlockPos blockPos, Entity entity, float damage) {
        super.fallOn(level, state, blockPos, entity, damage);

        if(entity instanceof ItemEntity item && !item.fireImmune()){
            item.setRemainingFireTicks(item.getRemainingFireTicks() + 1);
            if (item.getRemainingFireTicks() == 0) {
                item.setSecondsOnFire(8);
            }
        }

    }
}
