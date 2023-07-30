package de.crafty.winterskyblock.mixin.net.minecraft.world.level.material;

import de.crafty.winterskyblock.network.SkyblockNetworkManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(FlowingFluid.class)
public abstract class MixinFlowingFluid extends Fluid {


    @Inject(method = "canHoldFluid", at = @At("HEAD"), cancellable = true)
    private void modifyPortal(BlockGetter getter, BlockPos pos, BlockState state, Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof EndPortalBlock && getter instanceof Level level && level.dimension() != Level.END)
            cir.setReturnValue(true);

    }

    @Inject(method = "spreadTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", shift = At.Shift.BEFORE))
    private void modifyP(LevelAccessor level, BlockPos pos, BlockState state, Direction direction, FluidState fluidState, CallbackInfo ci){
        if(state.getBlock() instanceof EndPortalBlock){
            if(level instanceof ServerLevel serverLevel){
                SkyblockNetworkManager.sendDestroyBlockEffectPacket(pos, ((ServerLevel) level).getChunkAt(pos));
            }
        }
    }

}
