package de.crafty.winterskyblock.handler;

import de.crafty.winterskyblock.item.MobOrbItem;
import de.crafty.winterskyblock.registry.BlockRegistry;
import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BlockTransformationHandler {


    public static final List<BlockTransformation> TRANSFORMATIONS = new ArrayList<>();
    private static final Condition NO_CONDITION = (player, hand, level, state, side) -> true;


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

            if (transformation.block().get() != state.getBlock() || !transformation.condition.check(player, event.getHand(), level, state, event.getFace()))
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
        registerTransformation(new BlockTransformation(() -> Items.WHEAT_SEEDS, () -> Blocks.DIRT, Blocks.GRASS_BLOCK::defaultBlockState, (player, hand, level, state, side) -> side == Direction.UP, SoundEvents.GRASS_HIT, null));
        registerTransformation(new BlockTransformation(() -> Items.CRIMSON_FUNGUS, () -> Blocks.NETHERRACK, Blocks.CRIMSON_NYLIUM::defaultBlockState, (player, hand, level, state, side) -> side == Direction.UP && player.isCrouching(), SoundEvents.NETHERRACK_PLACE, null));
        registerTransformation(new BlockTransformation(() -> Items.WARPED_FUNGUS, () -> Blocks.NETHERRACK, Blocks.WARPED_NYLIUM::defaultBlockState, (player, hand, level, state, side) -> side == Direction.UP && player.isCrouching(), SoundEvents.NETHERRACK_PLACE, null));
        registerTransformation(new BlockTransformation(ItemRegistry.ROTTEN_MIXTURE, () -> Blocks.ROOTED_DIRT, Blocks.MOSS_BLOCK::defaultBlockState, NO_CONDITION, SoundEvents.MOSS_STEP, null));
        registerTransformation(new BlockTransformation(() -> Items.COAL, () -> Blocks.COBBLESTONE, Blocks.BLACKSTONE::defaultBlockState, NO_CONDITION, SoundEvents.STONE_PLACE, null));

        registerTransformation(new BlockTransformation(ItemRegistry.MOB_ORB_ACTIVE, () -> Blocks.SOUL_SAND, () -> BlockRegistry.GHAST_BLOCK.get().defaultBlockState(), (player, hand, level, state, side) -> MobOrbItem.readEntityType(player.getItemInHand(hand).getOrCreateTag()) == EntityType.GHAST, SoundEvents.GHAST_DEATH, () -> new ItemStack(ItemRegistry.MOB_ORB.get()), createDisplayableMobOrb(EntityType.GHAST)));
        registerTransformation(new BlockTransformation(ItemRegistry.MOB_ORB_ACTIVE, () -> Blocks.SNOW_BLOCK, () -> BlockRegistry.PHANTOM_BLOCK.get().defaultBlockState(), (player, hand, level, state, side) -> MobOrbItem.readEntityType(player.getItemInHand(hand).getOrCreateTag()) == EntityType.PHANTOM, SoundEvents.PHANTOM_AMBIENT, () -> new ItemStack(ItemRegistry.MOB_ORB.get()), createDisplayableMobOrb(EntityType.PHANTOM)));

    }

    private static Supplier<ItemStack> createDisplayableMobOrb(EntityType<?> entityType){

        return () -> {
            ItemStack stack = new ItemStack(ItemRegistry.MOB_ORB_ACTIVE.get());
            CompoundTag tag = stack.getOrCreateTag();

            CompoundTag typeTag = new CompoundTag();
            typeTag.putString("id", EntityType.getKey(entityType).toString());

            tag.put("savedEntity", typeTag);
            stack.setTag(tag);
            return stack;
        };

    }


    private static void registerTransformation(BlockTransformation... transformations) {
        TRANSFORMATIONS.addAll(List.of(transformations));
    }

    private static List<BlockTransformation> getTransformations(Item item) {
        List<BlockTransformation> transformations = new ArrayList<>();
        TRANSFORMATIONS.stream().filter(transformation -> transformation.converter().get().equals(item)).forEach(transformations::add);
        return transformations;
    }


    public record BlockTransformation(Supplier<Item> converter, Supplier<Block> block, Supplier<BlockState> result, Condition condition, SoundEvent sound, Supplier<ItemStack> remainder, Supplier<ItemStack> representable) {

        public BlockTransformation(Supplier<Item> converter, Supplier<Block> block, Supplier<BlockState> result, Condition condition, SoundEvent sound, Supplier<ItemStack> remainder){
            this(converter, block, result, condition, sound, remainder, () -> new ItemStack(converter.get()));
        }

    }





    interface Condition {

        boolean check(Player player, InteractionHand hand, Level level, BlockState state, Direction side);

    }

}
