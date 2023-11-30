package de.crafty.winterskyblock.item;

import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.function.Supplier;

public class WoodenBucketItem extends BucketItem {


    private final Fluid content;

    public WoodenBucketItem(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
        this.content = supplier.get();
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, this.content == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(player, level, itemstack, blockhitresult);
        if (ret != null) return ret;
        if (blockhitresult.getType() == HitResult.Type.MISS)
            return InteractionResultHolder.pass(itemstack);


        if (blockhitresult.getType() != HitResult.Type.BLOCK)
            return InteractionResultHolder.pass(itemstack);


        BlockPos blockpos = blockhitresult.getBlockPos();
        Direction direction = blockhitresult.getDirection();
        BlockPos blockpos1 = blockpos.relative(direction);

        if (level.mayInteract(player, blockpos) && player.mayUseItemAt(blockpos1, direction, itemstack)) {
            if (this.content == Fluids.EMPTY) {
                BlockState blockstate1 = level.getBlockState(blockpos);
                if (blockstate1.getBlock() instanceof BucketPickup bucketPickup && !blockstate1.getBlock().equals(Blocks.LAVA)) {
                    ItemStack pickupStack = bucketPickup.pickupBlock(level, blockpos, blockstate1);
                    ItemStack bucketStack = ItemStack.EMPTY;

                    if(pickupStack.getItem() instanceof BucketItem bucket && bucket.getFluid() == Fluids.WATER)
                        bucketStack = new ItemStack(ItemRegistry.WOODEN_WATER_BUCKET.get());
                    if(pickupStack.getItem() instanceof SolidBucketItem bucket && bucket.getBlock() == Blocks.POWDER_SNOW)
                        bucketStack = new ItemStack(ItemRegistry.WOODEN_POWDER_SNOW_BUCKET.get());

                    if (!bucketStack.isEmpty()) {
                        player.awardStat(Stats.ITEM_USED.get(this));
                        bucketPickup.getPickupSound(blockstate1).ifPresent((p_150709_) -> {
                            player.playSound(p_150709_, 1.0F, 1.0F);
                        });
                        level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                        ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, player, bucketStack);
                        if (!level.isClientSide) {
                            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, bucketStack);
                        }

                        return InteractionResultHolder.sidedSuccess(itemstack2, level.isClientSide());
                    }
                }

                if(blockstate1.getBlock() instanceof BucketPickup bucketPickup && blockstate1.getBlock().equals(Blocks.LAVA)){
                    player.awardStat(Stats.ITEM_USED.get(this));

                    if(!player.isCreative())
                        itemstack.shrink(1);
                    player.setSecondsOnFire(10);
                    player.playSound(SoundEvents.MUDDY_MANGROVE_ROOTS_STEP, 4.0F, 0.75F);

                    return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
                }

                return InteractionResultHolder.fail(itemstack);
            }
            BlockState blockstate = level.getBlockState(blockpos);
            BlockPos blockpos2 = canBlockContainFluid(level, blockpos, blockstate) ? blockpos : blockpos1;
            if (this.emptyContents(player, level, blockpos2, blockhitresult)) {
                this.checkExtraContent(player, level, itemstack, blockpos2);
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos2, itemstack);
                }

                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(!player.getAbilities().instabuild ? new ItemStack(ItemRegistry.WOODEN_BUCKET.get()) : itemstack, level.isClientSide());
            }
            return InteractionResultHolder.fail(itemstack);


        }
        return InteractionResultHolder.fail(itemstack);

    }

    private boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate) {
        return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, this.content);
    }
}
