package de.crafty.winterskyblock.handler;

import de.crafty.winterskyblock.item.MobOrbItem;
import de.crafty.winterskyblock.registry.BlockRegistry;
import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class BlockTransformationHandler {


    private static final List<BlockTransformation> TRANSFORMATIONS = new ArrayList<>();
    private static final Condition NO_CONDITION = (player, hand, level, state, pos, side) -> true;


    @SubscribeEvent
    public void onTransformation(PlayerInteractEvent.RightClickBlock event) {

        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        List<BlockTransformation> transformations = getTransformations(stack.getItem());
        if (transformations.isEmpty())
            return;

        for (BlockTransformation transformation : transformations) {

            if (transformation.block().get() != state.getBlock() || !transformation.condition().check(player, event.getHand(), level, state, pos, event.getFace()))
                continue;

            level.setBlock(pos, transformation.result().get(), 3);
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), transformation.sound(), SoundSource.BLOCKS, 1.0F, 1.0F, false);

            if (transformation.remainder() != null)
                player.setItemInHand(event.getHand(), transformation.remainder().get());
            else
                stack.shrink(1);

            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }

    }


    public static void bootstrap() {
        registerTransformation(new BlockTransformation(() -> Items.BONE_MEAL, () -> Blocks.DIRT, Blocks.ROOTED_DIRT::defaultBlockState, NO_CONDITION, SoundEvents.ROOTED_DIRT_PLACE, null));
        registerTransformation(new BlockTransformation(() -> Items.WHEAT_SEEDS, () -> Blocks.DIRT, Blocks.GRASS_BLOCK::defaultBlockState, (player, hand, level, state, pos, side) -> side == Direction.UP, SoundEvents.GRASS_HIT, null));
        registerTransformation(new BlockTransformation(() -> Items.CRIMSON_FUNGUS, () -> Blocks.NETHERRACK, Blocks.CRIMSON_NYLIUM::defaultBlockState, (player, hand, level, state, pos, side) -> player.isCrouching() && side == Direction.UP, SoundEvents.NETHERRACK_PLACE, null));
        registerTransformation(new BlockTransformation(() -> Items.WARPED_FUNGUS, () -> Blocks.NETHERRACK, Blocks.WARPED_NYLIUM::defaultBlockState, (player, hand, level, state, pos, side) -> player.isCrouching() && side == Direction.UP, SoundEvents.NETHERRACK_PLACE, null));
        registerTransformation(new BlockTransformation(ItemRegistry.ROTTEN_MIXTURE, () -> Blocks.ROOTED_DIRT, Blocks.MOSS_BLOCK::defaultBlockState, NO_CONDITION, SoundEvents.MOSS_STEP, null));
        registerTransformation(new BlockTransformation(() -> Items.COAL, () -> Blocks.COBBLESTONE, Blocks.BLACKSTONE::defaultBlockState, NO_CONDITION, SoundEvents.STONE_PLACE, null));

        registerTransformation(new BlockTransformation(ItemRegistry.MOB_ORB_ACTIVE, () -> Blocks.SOUL_SAND, () -> BlockRegistry.GHAST_BLOCK.get().defaultBlockState(), (player, hand, level, state, pos, side) -> MobOrbItem.readEntityType(player.getItemInHand(hand).getOrCreateTag()) == EntityType.GHAST, SoundEvents.GHAST_DEATH, () -> new ItemStack(ItemRegistry.MOB_ORB.get())));
        registerTransformation(new BlockTransformation(ItemRegistry.MOB_ORB_ACTIVE, () -> Blocks.SNOW_BLOCK, () -> BlockRegistry.PHANTOM_BLOCK.get().defaultBlockState(), (player, hand, level, state, pos, side) -> MobOrbItem.readEntityType(player.getItemInHand(hand).getOrCreateTag()) == EntityType.PHANTOM, SoundEvents.PHANTOM_AMBIENT, () -> new ItemStack(ItemRegistry.MOB_ORB.get())));

    }


    private static void registerTransformation(BlockTransformation... transformations) {
        TRANSFORMATIONS.addAll(List.of(transformations));
    }

    private static List<BlockTransformation> getTransformations(Item item) {
        List<BlockTransformation> transformations = new ArrayList<>();
        TRANSFORMATIONS.stream().filter(transformation -> transformation.item.get().equals(item)).forEach(transformations::add);
        return transformations;
    }


    public record BlockTransformation(Supplier<Item> item, Supplier<Block> block, Supplier<BlockState> result,
                                      Condition condition, SoundEvent sound, Supplier<ItemStack> remainder) {

    }


    interface Condition {
        boolean check(Player player, InteractionHand hand, Level level, BlockState state, BlockPos pos, Direction side);
    }

}
