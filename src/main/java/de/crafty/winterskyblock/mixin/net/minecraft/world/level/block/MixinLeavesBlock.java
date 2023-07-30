package de.crafty.winterskyblock.mixin.net.minecraft.world.level.block;


import de.crafty.winterskyblock.fastdecay.FastDecay;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(LeavesBlock.class)
public abstract class MixinLeavesBlock extends Block implements SimpleWaterloggedBlock, net.minecraftforge.common.IForgeShearable {


    @Shadow
    @Final
    public static BooleanProperty PERSISTENT;

    @Shadow
    @Final
    public static IntegerProperty DISTANCE;

    public MixinLeavesBlock(Properties properties) {
        super(properties);
    }


    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void removeDecay(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource, CallbackInfo ci) {
        ci.cancel();
    }


    @Inject(method = "tick", at = @At("TAIL"))
    private void onStateChange(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource, CallbackInfo ci) {
        BlockState newState = level.getBlockState(pos);

        if (newState.getValue(PERSISTENT) || newState.getValue(DISTANCE) != 7)
            return;

        ArrayList<BlockPos> positions = FastDecay.LEAVES.getOrDefault(level, new ArrayList<>());
        if (positions.contains(pos))
            return;

        positions.add(pos);
        FastDecay.LEAVES.put(level, positions);

    }
}
