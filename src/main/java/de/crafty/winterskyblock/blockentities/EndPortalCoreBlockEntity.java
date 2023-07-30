package de.crafty.winterskyblock.blockentities;

import de.crafty.winterskyblock.block.EndPortalFrameBlock;
import de.crafty.winterskyblock.registry.BlockEntityRegistry;
import de.crafty.winterskyblock.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EndPortalCoreBlockEntity extends BlockEntity {


    private int animationTick = -1;

    public EndPortalCoreBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.END_PORTAL_CORE.get(), pos, state);
    }


    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.animationTick = tag.getInt("animationTick");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putInt("animationTick", this.animationTick);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();
        this.saveAdditional(updateTag);
        return updateTag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }


    public boolean hasAnimationStarted() {
        return this.animationTick >= 0;
    }

    public boolean hasAnimationFinished() {
        return this.animationTick > this.getPositioningAnimationTime() + this.getCircleAnimationTime() + this.getTransformationAnimationTime();
    }

    public void startAnimation() {
        this.animationTick = 0;
    }

    public int getAnimationTick() {
        return this.animationTick;
    }

    public double getPositioningAnimationSpeed() {
        return 0.025D;
    }

    public double getCircleAnimationSpeed() {
        return 2.5D;
    }

    public double getTransformationAnimationSpeed() {
        return 0.025D;
    }

    public double getPositioningAnimationTime() {
        return new Vec3(Math.sin(0.0D) * 0.75D, 0.75D, Math.cos(0.0D) * 0.75D).length() / this.getPositioningAnimationSpeed();
    }

    public double getCircleAnimationTime() {
        return 360.0D / this.getCircleAnimationSpeed();
    }

    public double getTransformationAnimationTime() {
        return 1.0D / this.getTransformationAnimationSpeed();
    }

    public Vec3[] getPortalFrameLocations(BlockPos pos) {

        List<Vec3> positions = new ArrayList<>();

        positions.add(new Vec3(pos.getX() + 0.5D + 2, pos.getY() + 0.5D, pos.getZ() + 0.5D));
        positions.add(new Vec3(pos.getX() + 0.5D + 2, pos.getY() + 0.5D, pos.getZ() + 0.5D + 1));
        positions.add(new Vec3(pos.getX() + 0.5D + 1, pos.getY() + 0.5D, pos.getZ() + 0.5D + 2));
        positions.add(new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D + 2));
        positions.add(new Vec3(pos.getX() + 0.5D - 1, pos.getY() + 0.5D, pos.getZ() + 0.5D + 2));
        positions.add(new Vec3(pos.getX() + 0.5D - 2, pos.getY() + 0.5D, pos.getZ() + 0.5D + 1));
        positions.add(new Vec3(pos.getX() + 0.5D - 2, pos.getY() + 0.5D, pos.getZ() + 0.5D));
        positions.add(new Vec3(pos.getX() + 0.5D - 2, pos.getY() + 0.5D, pos.getZ() + 0.5D - 1));
        positions.add(new Vec3(pos.getX() + 0.5D - 1, pos.getY() + 0.5D, pos.getZ() + 0.5D - 2));
        positions.add(new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D - 2));
        positions.add(new Vec3(pos.getX() + 0.5D + 1, pos.getY() + 0.5D, pos.getZ() + 0.5D - 2));
        positions.add(new Vec3(pos.getX() + 0.5D + 2, pos.getY() + 0.5D, pos.getZ() + 0.5D - 1));

        return positions.toArray(new Vec3[0]);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, EndPortalCoreBlockEntity blockEntity) {

        if (blockEntity.hasAnimationStarted())
            blockEntity.animationTick++;

        if (blockEntity.hasAnimationFinished()) {
            blockEntity.animationTick = -1;
            for (Vec3 frameLocVec : blockEntity.getPortalFrameLocations(pos)) {
                level.setBlock(new BlockPos(frameLocVec), BlockRegistry.END_PORTAL_FRAME.get().defaultBlockState().setValue(EndPortalFrameBlock.FILLED, true).setValue(EndPortalFrameBlock.TRANSFORMING, false), 3);
                level.playLocalSound(frameLocVec.x(), frameLocVec.y(), frameLocVec.z(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 0.125F, 1.0F, false);
            }

            for(int xOff = -1; xOff <= 1; xOff++){
                for(int zOff = -1; zOff <= 1; zOff++){
                    level.setBlock(pos.offset(xOff, 0, zOff), Blocks.END_PORTAL.defaultBlockState(), 3);
                    if(xOff == 0 && zOff == 0)
                        level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 0.5F, 1.0F, false);
                }
            }
        }
    }

}
