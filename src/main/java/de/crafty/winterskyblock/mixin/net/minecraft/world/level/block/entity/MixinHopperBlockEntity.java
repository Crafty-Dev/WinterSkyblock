package de.crafty.winterskyblock.mixin.net.minecraft.world.level.block.entity;


import de.crafty.winterskyblock.handler.LavaDropHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LavaCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(HopperBlockEntity.class)
public abstract class MixinHopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper {

    @Shadow
    public static List<ItemEntity> getItemsAtAndAbove(Level p_155590_, Hopper p_155591_) {
        return null;
    }

    protected MixinHopperBlockEntity(BlockEntityType<?> p_155629_, BlockPos p_155630_, BlockState p_155631_) {
        super(p_155629_, p_155630_, p_155631_);
    }


    @Redirect(method = "suckInItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;getItemsAtAndAbove(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/entity/Hopper;)Ljava/util/List;"))
    private static List<ItemEntity> preventLavaDropBug(Level level, Hopper hopper){

        if(!(level.getBlockState(new BlockPos(hopper.getLevelX(), hopper.getLevelY() + 1, hopper.getLevelZ())).getBlock() instanceof LavaCauldronBlock))
            return getItemsAtAndAbove(level, hopper);


        List<ItemEntity> items = new ArrayList<>();
        for(ItemEntity current : getItemsAtAndAbove(level, hopper)){
            if(LavaDropHandler.getDropsFor(current.getItem().getItem()) == null)
                items.add(current);
        }


        return items;
    }

}
