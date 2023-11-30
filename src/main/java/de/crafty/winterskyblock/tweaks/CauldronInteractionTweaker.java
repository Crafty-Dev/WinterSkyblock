package de.crafty.winterskyblock.tweaks;

import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class CauldronInteractionTweaker {


    public static void bootstrap(){

        //Water
        CauldronInteraction.WATER.put(ItemRegistry.WOODEN_BUCKET.get(), (state, level, blockPos, player, interactionHand, stack) -> {
            return CauldronInteraction.fillBucket(state, level, blockPos, player, interactionHand, stack, new ItemStack(ItemRegistry.WOODEN_WATER_BUCKET.get()), (state1) -> {
                return state1.getValue(LayeredCauldronBlock.LEVEL) == 3;
            }, SoundEvents.BUCKET_FILL);
        });

        CauldronInteraction FILL_WATER = (state, level, blockPos, player, interactionHand, stack) -> {
            return CauldronInteractionTweaker.emptyBucket(level, blockPos, player, interactionHand, stack, Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, Integer.valueOf(3)), SoundEvents.BUCKET_EMPTY);
        };

        CauldronInteraction.WATER.put(ItemRegistry.WOODEN_WATER_BUCKET.get(), FILL_WATER);

        //Powder Snow
        CauldronInteraction.POWDER_SNOW.put(ItemRegistry.WOODEN_BUCKET.get(), (state, level, blockPos, player, interactionHand, stack) -> {
            return CauldronInteraction.fillBucket(state, level, blockPos, player, interactionHand, stack, new ItemStack(ItemRegistry.WOODEN_POWDER_SNOW_BUCKET.get()), (state1) -> {
                return state1.getValue(LayeredCauldronBlock.LEVEL) == 3;
            }, SoundEvents.BUCKET_FILL_POWDER_SNOW);
        });

        CauldronInteraction FILL_POWDER_SNOW = (state, level, blockPos, player, interactionHand, stack) -> {
            return CauldronInteractionTweaker.emptyBucket(level, blockPos, player, interactionHand, stack, Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, Integer.valueOf(3)), SoundEvents.BUCKET_EMPTY_POWDER_SNOW);
        };


        CauldronInteraction.EMPTY.put(ItemRegistry.WOODEN_POWDER_SNOW_BUCKET.get(), FILL_POWDER_SNOW);
        CauldronInteraction.EMPTY.put(ItemRegistry.WOODEN_WATER_BUCKET.get(), FILL_WATER);


        //Lava
        CauldronInteraction.LAVA.put(ItemRegistry.WOODEN_BUCKET.get(), (state, level, blockPos, player, interactionHand, stack) -> {

            if (!level.isClientSide) {
                Item item = stack.getItem();
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                if(!player.isCreative())
                    stack.shrink(1);
                player.setSecondsOnFire(10);
                level.playSound(null, blockPos, SoundEvents.MUDDY_MANGROVE_ROOTS_STEP, SoundSource.NEUTRAL, 4.0F, 0.75F);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        });
    }


    private static InteractionResult emptyBucket(Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack stack, BlockState state, SoundEvent sound) {
        if (!level.isClientSide) {
            Item item = stack.getItem();
            player.setItemInHand(interactionHand, ItemUtils.createFilledResult(stack, player, new ItemStack(ItemRegistry.WOODEN_BUCKET.get())));
            player.awardStat(Stats.FILL_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            level.setBlockAndUpdate(blockPos, state);
            level.playSound((Player)null, blockPos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent((Entity)null, GameEvent.FLUID_PLACE, blockPos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

}
