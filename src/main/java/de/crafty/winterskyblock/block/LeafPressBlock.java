package de.crafty.winterskyblock.block;

import de.crafty.winterskyblock.blockentities.LeafPressBlockEntity;
import de.crafty.winterskyblock.registry.BlockRegistry;
import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class LeafPressBlock extends Block implements EntityBlock, BucketPickup {


    public static final List<Block> VALID_LEAVES = List.of(
            Blocks.OAK_LEAVES,
            Blocks.SPRUCE_LEAVES,
            Blocks.BIRCH_LEAVES,
            Blocks.DARK_OAK_LEAVES,
            Blocks.ACACIA_LEAVES,
            Blocks.JUNGLE_LEAVES,
            Blocks.MANGROVE_LEAVES,
            Blocks.AZALEA_LEAVES,
            Blocks.FLOWERING_AZALEA_LEAVES
    );

    public static final IntegerProperty PROGRESS = IntegerProperty.create("progress", 0, 6);
    public static final IntegerProperty FLUID_LEVEL = IntegerProperty.create("fluid_level", 0, 4);

    private static final VoxelShape PILLAR_0 = Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 2.0D);
    private static final VoxelShape PILLAR_1 = Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D);
    private static final VoxelShape PILLAR_2 = Block.box(0.0D, 0.0D, 14.0D, 2.0D, 16.0D, 16.0D);
    private static final VoxelShape PILLAR_3 = Block.box(14.0D, 0.0D, 14.0D, 16.0D, 16.0D, 16.0D);

    private static final VoxelShape BASE_0 = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape BASE_1 = Block.box(1.0D, 1.0D, 1.0D, 15.0D, 2.0D, 15.0D);
    private static final VoxelShape BASE_2 = Block.box(2.0D, 1.0D, 2.0D, 14.0D, 2.0D, 14.0D);
    private static final VoxelShape BASE_3 = Block.box(4.0D, 1.0D, 4.0D, 12.0D, 2.0D, 12.0D);


    private static final VoxelShape BASE = Shapes.or(Shapes.join(Shapes.or(BASE_0, BASE_1), BASE_2, BooleanOp.ONLY_FIRST), BASE_3);
    private static final VoxelShape PILLARS = Shapes.or(PILLAR_0, PILLAR_1, PILLAR_2, PILLAR_3);

    private static final VoxelShape[] PRESS_PLATE = Util.make(new VoxelShape[7], voxelShapes -> {
        for (int i = 0; i < 7; i++) {
            voxelShapes[i] = Block.box(0.0D, 14.0D - (i * 2.0D), 0.0D, 16.0D, 16.0D - (i * 2.0D), 16.0D);
        }
    });

    public LeafPressBlock() {
        super(Properties.of(Material.WOOD).sound(SoundType.WOOD).noOcclusion().strength(2.0F));
        this.registerDefaultState(this.getStateDefinition().any().setValue(PROGRESS, 0));
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
        return Shapes.or(PILLARS, BASE, PRESS_PLATE[state.getValue(PROGRESS)]);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PROGRESS, FLUID_LEVEL);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LeafPressBlockEntity(pos, state);
    }


    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, level, pos, explosion);

        if(level.getBlockEntity(pos) instanceof LeafPressBlockEntity blockEntity)
            Block.popResource(level, pos, blockEntity.getContent());
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {

        if(level.getBlockEntity(pos) instanceof LeafPressBlockEntity blockEntity)
            Block.popResource(level, pos, blockEntity.getContent());

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {


        int i = state.getValue(PROGRESS);
        BlockEntity be = level.getBlockEntity(pos);

        if (!(be instanceof LeafPressBlockEntity blockEntity))
            return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);

        if (i == 0 && blockEntity.getContent().isEmpty()) {

            if (stack.getItem() instanceof BlockItem b && VALID_LEAVES.contains(b.getBlock())) {
                ItemStack content = stack.copy();
                content.setCount(1);
                blockEntity.setContent(content);

                stack.shrink(1);
                player.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.5F);
                return InteractionResult.SUCCESS;
            }
        }

        if (i == 0 && blockEntity.getContent().is(ItemRegistry.DRIED_LEAVES.get())) {
            player.addItem(blockEntity.getContent());
            player.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
            blockEntity.setContent(ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        }


        if (!(blockEntity.getContent().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof LeavesBlock) && i == 0)
            return InteractionResult.PASS;


        BlockState newState;

        if (state.getValue(PROGRESS) < 6){
            newState = state.setValue(PROGRESS, i + 1);
            player.playSound(SoundEvents.WOOD_HIT, 1.0F, 1.5F);
        }
        else{
            newState = state.setValue(PROGRESS, 0);
            player.playSound(SoundEvents.WOOD_PLACE, 1.0F, 1.0F);
        }


        if (newState.getValue(PROGRESS) == 6) {
            blockEntity.setContent(new ItemStack(BlockRegistry.DRIED_LEAVES.get()));
            if (newState.getValue(FLUID_LEVEL) < 4){
                newState = newState.setValue(FLUID_LEVEL, state.getValue(FLUID_LEVEL) + 1);
                player.playSound(SoundEvents.BUCKET_FILL, 0.5F, 0.5F);
            }
        }

        level.setBlock(pos, newState, 3);

        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        if(state.getValue(FLUID_LEVEL) == 4)
            level.setBlock(pos, state.setValue(FLUID_LEVEL, 0), 3);

        return state.getValue(FLUID_LEVEL) == 4 ? new ItemStack(Items.WATER_BUCKET) : ItemStack.EMPTY;
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }
}
