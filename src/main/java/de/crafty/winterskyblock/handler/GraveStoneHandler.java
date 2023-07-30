package de.crafty.winterskyblock.handler;

import de.crafty.winterskyblock.blockentities.GraveStoneBlockEntity;
import de.crafty.winterskyblock.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.core.util.ExecutorServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;

public class GraveStoneHandler {



    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event){

        if(!(event.getEntity() instanceof ServerPlayer player))
            return;

        if(player.getInventory().isEmpty())
            return;

        ServerLevel level = player.getLevel();
        BlockPos pos = new BlockPos(player.getBlockX(), Math.max(player.getBlockY(), level.getMinBuildHeight() + 1), player.getBlockZ());

        level.setBlock(pos, BlockRegistry.GRAVE_STONE.get().defaultBlockState(), 3);
        if(level.getBlockState(pos.below()).isAir() || level.getBlockState(pos.below()).getMaterial().isLiquid())
            level.setBlock(pos.below(), Blocks.DIRT.defaultBlockState(), 3);

        if(!(level.getBlockEntity(pos) instanceof GraveStoneBlockEntity blockEntity))
            return;


        List<ItemStack> items = new ArrayList<>();
        player.getInventory().items.stream().filter(stack -> !stack.isEmpty()).forEach(items::add);
        player.getInventory().armor.stream().filter(stack -> !stack.isEmpty()).forEach(items::add);
        player.getInventory().offhand.stream().filter(stack -> !stack.isEmpty()).forEach(items::add);

        blockEntity.setContent(items);
        blockEntity.setPlayerProfile(player.getGameProfile());
        player.getInventory().clearContent();

        player.connection.send(new ClientboundSoundPacket(SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, pos.getX(), pos.getY(), pos.getZ(), 1.0F, 1.0F, level.random.nextLong()));


    }
}
