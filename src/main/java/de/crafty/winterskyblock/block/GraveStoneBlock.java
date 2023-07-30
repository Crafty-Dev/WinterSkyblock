package de.crafty.winterskyblock.block;

import de.crafty.winterskyblock.blockentities.GraveStoneBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GraveStoneBlock extends Block implements EntityBlock {



    private static final VoxelShape BOTTOM_0 = Block.box(1.0D, 0.0D, 0.0D, 15.0D, 1.0D, 16.0D);
    private static final VoxelShape BOTTOM_1 = Block.box(0.0D, 0.0D, 1.0D, 1.0D, 1.0D, 16.0D);
    private static final VoxelShape BOTTOM_2 = Block.box(15.0D, 0.0D, 1.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape BOTTOM_3 = Block.box(1.0D, 1.0D, 1.0D, 15.0D, 2.0D, 16.0D);



    private static final VoxelShape BOTTOM = Shapes.or(BOTTOM_0, BOTTOM_1, BOTTOM_2, BOTTOM_3);

    public GraveStoneBlock() {
        super(Properties.of(Material.DIRT).noOcclusion().noLootTable().strength(0.5F).sound(SoundType.GRAVEL));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return BOTTOM;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GraveStoneBlockEntity(pos, state);
    }


    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {

        if(level.getBlockEntity(pos) instanceof GraveStoneBlockEntity blockEntity){
            Player player = level.getPlayerByUUID(blockEntity.getPlayerProfile().getId());

            if(explosion.getHitPlayers().containsKey(player) && player.isDeadOrDying())
                return;

            blockEntity.getContent().forEach(stack -> Block.popResource(level, pos, stack));
        }

        super.onBlockExploded(state, level, pos, explosion);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {

        if(level.getBlockEntity(pos) instanceof GraveStoneBlockEntity blockEntity && level instanceof ServerLevel)
            blockEntity.getContent().forEach(stack -> {
                ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.25D, pos.getZ() + 0.5D, stack);
                level.addFreshEntity(entity);
            });

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}
