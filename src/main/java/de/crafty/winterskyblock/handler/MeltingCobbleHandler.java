package de.crafty.winterskyblock.handler;

import de.crafty.winterskyblock.registry.BlockRegistry;
import de.crafty.winterskyblock.util.ValidHeatSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MeltingCobbleHandler {


    @SubscribeEvent
    public void onCampfireMelting(BlockEvent.NeighborNotifyEvent event){

        LevelAccessor level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        if(ValidHeatSource.getHeatEfficiency(level, state, pos) > 0.0F && event.getNotifiedSides().contains(Direction.UP)){
            if(level.getBlockState(pos.above()).getBlock() == Blocks.COBBLESTONE)
                level.setBlock(pos.above(), BlockRegistry.MELTING_COBBLESTONE.get().defaultBlockState(), 3);

            return;
        }

        if(state.getBlock() == Blocks.COBBLESTONE && event.getNotifiedSides().contains(Direction.DOWN) && ValidHeatSource.getHeatEfficiency(level, level.getBlockState(pos.below()), pos.below()) > 0.0F){
            level.setBlock(pos, BlockRegistry.MELTING_COBBLESTONE.get().defaultBlockState(), 3);
        }
    }
}
