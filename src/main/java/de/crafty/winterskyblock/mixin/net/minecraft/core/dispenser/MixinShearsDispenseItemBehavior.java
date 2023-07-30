package de.crafty.winterskyblock.mixin.net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.IForgeShearable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ShearsDispenseItemBehavior.class)
public abstract class MixinShearsDispenseItemBehavior extends OptionalDispenseItemBehavior {


    @Shadow
    private static boolean tryShearBeehive(ServerLevel p_123577_, BlockPos p_123578_) {
        return false;
    }

    @Inject(method = "execute", at = @At("HEAD"), cancellable = true)
    private void addForgeSupport(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir){
        ServerLevel level = source.getLevel();
        if (!level.isClientSide()) {
            BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
            this.setSuccess(tryShearBeehive(level, blockpos) || tryShearLivingEntity(level, blockpos, stack));
            if (this.isSuccess() && stack.hurt(1, level.getRandom(), null)) {
                stack.setCount(0);
            }
        }

        cir.setReturnValue(stack);
    }


    private static boolean tryShearLivingEntity(ServerLevel level, BlockPos pos, ItemStack stack) {
        for(LivingEntity livingentity : level.getEntitiesOfClass(LivingEntity.class, new AABB(pos), EntitySelector.NO_SPECTATORS)) {
            if (livingentity instanceof Shearable shearable) {
                if (shearable.readyForShearing()) {
                    shearable.shear(SoundSource.BLOCKS);
                    level.gameEvent(null, GameEvent.SHEAR, pos);
                    return true;
                }
            }

            if(livingentity instanceof IForgeShearable shearable){
                if(shearable.isShearable(stack, level, pos)){
                    List<ItemStack> drops = shearable.onSheared(null, stack, level, pos, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack));
                    drops.forEach(d -> {
                        ItemEntity ent = livingentity.spawnAtLocation(d, 1.0F);
                        ent.setDeltaMovement(ent.getDeltaMovement().add((level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.1F, level.getRandom().nextFloat() * 0.05F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.1F));
                    });
                    level.gameEvent(null, GameEvent.SHEAR, pos);
                    return true;
                }
            }
        }

        return false;
    }
}
