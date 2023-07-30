package de.crafty.winterskyblock.block;

import de.crafty.winterskyblock.blockentities.MagicalWorkbenchBlockEntity;
import de.crafty.winterskyblock.registry.BlockEntityRegistry;
import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class MagicalWorkbenchBlock extends BaseEntityBlock {

    public static final IntegerProperty ACTIVITY_STATE = IntegerProperty.create("activity_state", 0, 2);

    public MagicalWorkbenchBlock() {
        super(Properties.of(Material.STONE).strength(4.0F).requiresCorrectToolForDrops().noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVITY_STATE, 0));
    }


    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {


        if (state.getValue(ACTIVITY_STATE) == 0 && hitResult.getDirection() == Direction.NORTH && player.getItemInHand(hand).is(ItemRegistry.DRAGON_ARTIFACT.get())) {
            level.setBlock(pos, state.setValue(ACTIVITY_STATE, 1), 3);
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.5F, false);
            player.getItemInHand(hand).shrink(1);
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVITY_STATE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MagicalWorkbenchBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return createTickerHelper(entityType, BlockEntityRegistry.MAGICAL_WORKBENCH.get(), MagicalWorkbenchBlockEntity::tick);
    }
}
