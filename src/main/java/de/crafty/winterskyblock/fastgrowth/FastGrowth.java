package de.crafty.winterskyblock.fastgrowth;

import de.crafty.winterskyblock.network.SkyblockNetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public class FastGrowth {


    private final HashMap<Player, Boolean> prevCrouch = new HashMap<>();
    private final HashMap<Player, BlockPos> prevPositions = new HashMap<>();

    @SubscribeEvent
    public void onTweark(TickEvent.PlayerTickEvent event) {

        Player player = event.player;
        Level level = player.getLevel();

        if (player.isCrouching() != this.prevCrouch.getOrDefault(player, false) && player.isCrouching() && level instanceof ServerLevel serverLevel)
            this.applyFastGrowth(serverLevel, player.blockPosition(), 0.075F);

        this.prevCrouch.put(player, player.isCrouching());
    }

    @SubscribeEvent
    public void onMove(TickEvent.PlayerTickEvent event){

        Player player = event.player;
        Level level = player.getLevel();

        if(!(level instanceof ServerLevel serverLevel))
            return;

        int x = player.blockPosition().getX();
        int y = player.blockPosition().getY();
        int z = player.blockPosition().getZ();

        BlockPos pos = this.prevPositions.getOrDefault(player, player.blockPosition());
        int prevX = pos.getX();
        int prevY = pos.getY();
        int prevZ = pos.getZ();

        if(prevX != x || prevY != y || prevZ != z){
            this.applyFastGrowth(serverLevel, player.blockPosition(), 0.05F);
            this.prevPositions.put(player, player.blockPosition());
        }

        if(!this.prevPositions.containsKey(player))
            this.prevPositions.put(player, player.blockPosition());
    }


    private void applyFastGrowth(ServerLevel level, BlockPos src, float f) {

        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {

                BlockPos pos = new BlockPos(src.getX() + x, src.getY(), src.getZ() + z);

                BlockState state = level.getBlockState(pos);

                if (!(state.getBlock() instanceof SaplingBlock sapling))
                    continue;


                if (level.getRandom().nextFloat() < f) {
                    sapling.performBonemeal(level, level.getRandom(), pos, state);
                    SkyblockNetworkManager.sendBonemealEffectPacket(pos, level.getChunkAt(pos));
                }
            }


        }
    }

}

