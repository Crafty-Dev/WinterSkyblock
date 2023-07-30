package de.crafty.winterskyblock.handler;

import de.crafty.winterskyblock.block.EndPortalFrameBlock;
import de.crafty.winterskyblock.blockentities.EndPortalCoreBlockEntity;
import de.crafty.winterskyblock.registry.BlockRegistry;
import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockBreakHandler {


    @SubscribeEvent
    public void onSpawnerBreak(BlockEvent.BreakEvent event){

        if(event.getState().is(Blocks.SPAWNER))
            Block.popResource(event.getPlayer().getLevel(), event.getPos(), new ItemStack(ItemRegistry.SPAWNER_SHARD.get(), 1 + event.getLevel().getRandom().nextInt(4)));

    }


    @SubscribeEvent
    public void onPortalBlockBreak(BlockEvent.BreakEvent event){

        if(event.getPlayer().isCreative())
            return;

        BlockState state = event.getState();
        if(state.is(BlockRegistry.END_PORTAL_FRAME.get()) && state.getValue(EndPortalFrameBlock.TRANSFORMING))
            event.setCanceled(true);

        if(event.getLevel().getBlockEntity(event.getPos()) instanceof EndPortalCoreBlockEntity blockEntity && blockEntity.hasAnimationStarted())
            event.setCanceled(true);

    }

}
