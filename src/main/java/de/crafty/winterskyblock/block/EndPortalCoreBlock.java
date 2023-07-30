package de.crafty.winterskyblock.block;

import de.crafty.winterskyblock.blockentities.EndPortalCoreBlockEntity;
import de.crafty.winterskyblock.item.MobOrbItem;
import de.crafty.winterskyblock.registry.BlockEntityRegistry;
import de.crafty.winterskyblock.registry.BlockRegistry;
import de.crafty.winterskyblock.registry.EntityRegistry;
import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EndPortalCoreBlock extends BaseEntityBlock {


    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final IntegerProperty CHARGE = IntegerProperty.create("charge", 0, 12);

    public EndPortalCoreBlock() {
        super(Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(25.0F, 1200.0F).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(CHARGE, 0).setValue(ACTIVE, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        ItemStack stack = player.getItemInHand(hand);
        int currentCharge = level.getBlockState(pos).getValue(CHARGE);

        if (stack.is(Items.ENDER_EYE) && currentCharge != 12) {
            stack.shrink(1);
            level.setBlock(pos, state.setValue(CHARGE, currentCharge + 1), 3);
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, true);
            return InteractionResult.SUCCESS;
        }

        if (!(stack.getItem() instanceof MobOrbItem))
            return InteractionResult.PASS;

        EntityType<?> entity = MobOrbItem.readEntityType(stack.getOrCreateTag());
        if (entity != null && entity.equals(EntityType.ENDERMAN) && currentCharge == 12) {
            if (level.getBlockEntity(pos) instanceof EndPortalCoreBlockEntity blockEntity && this.checkValidMultiblock(level, pos, blockEntity.getPortalFrameLocations(pos)) && level.dimension() != Level.END && level.dimension() != Level.NETHER) {
                level.setBlock(pos, state.setValue(ACTIVE, true), 3);
                blockEntity.startAnimation();
                for(Vec3 posVec : blockEntity.getPortalFrameLocations(pos)){
                    level.setBlock(new BlockPos(posVec), level.getBlockState(new BlockPos(posVec)).setValue(EndPortalFrameBlock.TRANSFORMING, true), 3);
                }
                player.setItemInHand(hand, new ItemStack(ItemRegistry.MOB_ORB.get()));
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.BLOCKS, 0.5F, 0.25F, false);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }


    private boolean checkValidMultiblock(Level level, BlockPos centerPos, Vec3[] portalFrames) {

        for (Vec3 frameLocVec : portalFrames) {
            BlockState state = level.getBlockState(new BlockPos(frameLocVec));
            if (!state.is(BlockRegistry.END_PORTAL_FRAME.get()) || state.getValue(EndPortalFrameBlock.FILLED) || state.getValue(EndPortalFrameBlock.TRANSFORMING))
                return false;
        }

        for (int xOff = -1; xOff <= 1; xOff++) {
            for (int zOff = -1; zOff <= 1; zOff++) {
                if (xOff == 0 && zOff == 0)
                    continue;

                BlockState state = level.getBlockState(centerPos.offset(xOff, 0, zOff));
                if (state.getMaterial().isSolid())
                    return false;
            }
        }

        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARGE, ACTIVE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EndPortalCoreBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityRegistry.END_PORTAL_CORE.get(), EndPortalCoreBlockEntity::tick);
    }
}
