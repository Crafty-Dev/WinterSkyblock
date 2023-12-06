package de.crafty.winterskyblock.block;

import de.crafty.winterskyblock.blockentities.CrystalCraftingPedestalBlockEntity;
import de.crafty.winterskyblock.registry.BlockEntityRegistry;
import de.crafty.winterskyblock.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CrystalCraftingPedestalBlock extends BaseEntityBlock {


    private static final VoxelShape BASE_0 = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
    private static final VoxelShape BASE_1 = Shapes.join(Block.box(1.0D, 12.0D, 1.0D, 15.0D, 13.0D, 15.0D), Block.box(4.0D, 12.0D, 4.0D, 12.0D, 13.0D, 12.0D), BooleanOp.ONLY_FIRST);
    private static final VoxelShape BASE_2 = Shapes.join(Block.box(2.0D, 13.0D, 2.0D, 14.0D, 14.0D, 14.0D), Block.box(3.0D, 13.0D, 3.0D, 13.0D, 14.0D, 13.0D), BooleanOp.ONLY_FIRST);


    private static final VoxelShape BASE = Shapes.or(BASE_0, BASE_1, BASE_2);


    public CrystalCraftingPedestalBlock(Properties properties) {
        super(properties);
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext context) {
        return BASE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrystalCraftingPedestalBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return createTickerHelper(entityType, BlockEntityRegistry.CRYSTAL_CRAFTING_PEDESTAL.get(), CrystalCraftingPedestalBlockEntity::tick);
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if(!(level.getBlockEntity(blockPos) instanceof CrystalCraftingPedestalBlockEntity blockEntity))
            return InteractionResult.PASS;

        if(!blockEntity.isActive())
            return InteractionResult.PASS;

        if(player.getItemInHand(hand).isEmpty() && !blockEntity.getItemStack().isEmpty()){
            player.setItemInHand(hand, blockEntity.getItemStack());
            blockEntity.setItemStack(ItemStack.EMPTY);

            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        if(!player.getItemInHand(hand).isEmpty() && blockEntity.getItemStack().isEmpty()){
            blockEntity.setItemStack(player.getItemInHand(hand));
            player.setItemInHand(hand, ItemStack.EMPTY);

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }
}
