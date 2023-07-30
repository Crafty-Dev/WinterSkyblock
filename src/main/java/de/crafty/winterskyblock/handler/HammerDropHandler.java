package de.crafty.winterskyblock.handler;

import de.crafty.winterskyblock.item.HammerItem;
import de.crafty.winterskyblock.registry.ParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HammerDropHandler {


    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {

        Player player = event.getPlayer();
        BlockState state = event.getState();
        BlockPos pos = event.getPos();

        ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (!(heldStack.getItem() instanceof HammerItem hammer))
            return;

        if (!(event.getLevel() instanceof ServerLevel level) || !(player instanceof ServerPlayer serverPlayer))
            return;

        if (!HammerItem.isHammerable(state.getBlock()))
            return;


        if (state.getMaterial().equals(Material.STONE))
            handleClientStone(serverPlayer, pos, level);

        for (ItemStack stack : HammerItem.getRandomDrop(state.getBlock(), level)) {
            if(this.isGoodHammer(hammer)){
                ItemEntity item = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.125D, pos.getZ() + 0.5D, stack);
                item.setDeltaMovement(0.0D, 0.0D, 0.0D);
                level.addFreshEntity(item);
                continue;
            }

            Block.popResource(level, pos, stack);
        }
    }


    private boolean isGoodHammer(HammerItem item){
        return !item.getTier().equals(Tiers.WOOD) && !item.getTier().equals(Tiers.STONE) && !item.getTier().equals(Tiers.IRON);
    }

    private static void handleClientStone(ServerPlayer serverPlayer, BlockPos pos, ServerLevel level) {

        serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.GRAVEL_BREAK, serverPlayer.getSoundSource(), pos.getX(), pos.getY(), pos.getZ(), 0.5F, 0.5F, level.random.nextLong()));

        int xParticles = 3;
        int yParticles = 3;
        int zParticles = 3;

        for (int x = 0; x < xParticles; x++) {
            for (int y = 0; y < yParticles; y++) {
                for (int z = 0; z < zParticles; z++) {
                    level.sendParticles(ParticleRegistry.HAMMER_STONE.get(), pos.getX() + (x * (1.0F / (xParticles - 1))), pos.getY() + (y * (1.0F / (yParticles - 1))), pos.getZ() + (z * (1.0F / (zParticles - 1))), 20, 0.0D, 0.0D, 0.0D, 1.0D);
                }
            }
        }

    }

}
