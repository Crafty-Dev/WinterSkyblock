package de.crafty.winterskyblock.fastdecay;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class FastDecay {


    public static final HashMap<ServerLevel, ArrayList<BlockPos>> LEAVES = new HashMap<>();


    @SubscribeEvent
    public void onDecay$1(TickEvent.LevelTickEvent event) {

        if (!(event.level instanceof ServerLevel level))
            return;


        ArrayList<BlockPos> positions = LEAVES.getOrDefault(level, new ArrayList<>());

        ArrayList<BlockPos> cleanups = new ArrayList<>();
        ArrayList<BlockPos> leavesToDecay = new ArrayList<>();

        positions.forEach(pos -> {

            BlockState state = level.getBlockState(pos);

            if (!(state.getBlock() instanceof LeavesBlock)) {
                cleanups.add(pos);
                return;
            }

            if (state.getValue(LeavesBlock.PERSISTENT) || state.getValue(LeavesBlock.DISTANCE) != 7) {
                cleanups.add(pos);
                return;
            }

            if (level.getRandom().nextFloat() < 0.1F)
                leavesToDecay.add(pos);

        });

        positions.removeAll(cleanups);
        positions.removeAll(leavesToDecay);

        leavesToDecay.forEach(pos -> {

            LeavesBlock.dropResources(level.getBlockState(pos), level, pos);
            level.removeBlock(pos, false);

        });

    }

}
