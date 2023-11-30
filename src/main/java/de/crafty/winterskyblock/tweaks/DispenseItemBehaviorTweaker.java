package de.crafty.winterskyblock.tweaks;

import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class DispenseItemBehaviorTweaker {


    public static void bootstrap() {

        DispenseItemBehavior dispenseitembehavior1 = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            public ItemStack execute(BlockSource p_123561_, ItemStack p_123562_) {
                DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem) p_123562_.getItem();
                BlockPos blockpos = p_123561_.getPos().relative(p_123561_.getBlockState().getValue(DispenserBlock.FACING));
                Level level = p_123561_.getLevel();
                if (dispensiblecontaineritem.emptyContents((Player) null, level, blockpos, (BlockHitResult) null)) {
                    dispensiblecontaineritem.checkExtraContent((Player) null, level, p_123562_, blockpos);
                    return new ItemStack(ItemRegistry.WOODEN_BUCKET.get());
                } else {
                    return this.defaultDispenseItemBehavior.dispense(p_123561_, p_123562_);
                }
            }
        };
        DispenserBlock.registerBehavior(ItemRegistry.WOODEN_WATER_BUCKET.get(), dispenseitembehavior1);
        DispenserBlock.registerBehavior(ItemRegistry.WOODEN_POWDER_SNOW_BUCKET.get(), dispenseitembehavior1);
        DispenserBlock.registerBehavior(ItemRegistry.WOODEN_BUCKET.get(), new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            public ItemStack execute(BlockSource blockSource, ItemStack stack) {
                LevelAccessor levelaccessor = blockSource.getLevel();
                BlockPos blockpos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
                BlockState blockstate = levelaccessor.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (block instanceof BucketPickup) {
                    ItemStack pickupStack = ((BucketPickup) block).pickupBlock(levelaccessor, blockpos, blockstate);
                    ItemStack bucketStack = ItemStack.EMPTY;

                    if(pickupStack.getItem() instanceof BucketItem bucket && bucket.getFluid() == Fluids.WATER)
                        bucketStack = new ItemStack(ItemRegistry.WOODEN_WATER_BUCKET.get());
                    if(pickupStack.getItem() instanceof SolidBucketItem bucket && bucket.getBlock() == Blocks.POWDER_SNOW)
                        bucketStack = new ItemStack(ItemRegistry.WOODEN_POWDER_SNOW_BUCKET.get());

                    if (bucketStack.isEmpty())
                        return super.execute(blockSource, stack);

                    levelaccessor.gameEvent((Entity) null, GameEvent.FLUID_PICKUP, blockpos);
                    Item item = bucketStack.getItem();
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        return new ItemStack(item);
                    } else {
                        if (blockSource.<DispenserBlockEntity>getEntity().addItem(new ItemStack(item)) < 0) {
                            this.defaultDispenseItemBehavior.dispense(blockSource, new ItemStack(item));
                        }

                        return stack;
                    }
                } else {
                    return super.execute(blockSource, stack);
                }
            }
        });

    }

}
