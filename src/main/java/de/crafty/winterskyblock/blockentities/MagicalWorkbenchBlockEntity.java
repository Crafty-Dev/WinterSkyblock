package de.crafty.winterskyblock.blockentities;

import de.crafty.winterskyblock.block.MagicalWorkbenchBlock;
import de.crafty.winterskyblock.registry.BlockEntityRegistry;
import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class MagicalWorkbenchBlockEntity extends BlockEntity {


    private int tick = 0;
    private final int animationStartDelay = 120;
    private final int movementDelay = 40;
    private ItemStack[] ritualBlocks = null;


    public MagicalWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.MAGICAL_WORKBENCH.get(), pos, state);
    }


    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.tick = tag.getInt("tick");

        ItemStack[] blocks = new ItemStack[8];

        for (int i = 0; i < 8; i++) {

            if (!tag.contains("ritualBlock." + i)) {
                blocks = null;
                break;
            }

            blocks[i] = ItemStack.of(tag.getCompound("ritualBlock." + i));

        }

        this.ritualBlocks = blocks;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putInt("tick", this.tick);

        if (this.ritualBlocks == null)
            return;

        for (int i = 0; i < 8; i++) {
            tag.put("ritualBlock." + i, this.ritualBlocks[i].save(new CompoundTag()));
        }
    }


    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();
        this.saveAdditional(updateTag);
        return updateTag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }


    public void setRitualBlocks(ItemStack[] blocks) {
        this.ritualBlocks = blocks;
        this.setChanged();
    }

    public ItemStack[] getRitualItems() {
        return this.ritualBlocks;
    }

    public boolean hasAnimationStarted() {
        return this.tick >= this.animationStartDelay;
    }

    public boolean hasAnimationFinished() {
        return this.getAnimationTick() > this.getMorphAnimationTime() + this.getTransformationAnimationTime();
    }

    public int getAnimationTick() {
        return this.tick - this.animationStartDelay;
    }


    public double getMorphAnimationSpeed() {
        return 0.005D;
    }

    public double getTransformationAnimationSpeed() {
        return 0.005D;
    }

    public double getMorphAnimationTime() {
        return 1 / this.getMorphAnimationSpeed();
    }

    public double getTransformationAnimationTime() {
        return 1 / this.getTransformationAnimationSpeed();
    }

    public double getAnimationTime(){
        return this.getTransformationAnimationTime() + this.getMorphAnimationTime();
    }


    public List<BlockPos> getRitualBlocks() {
        BlockPos pos = this.getBlockPos();
        List<BlockPos> positions = new ArrayList<>();

        positions.add(pos.offset(-1, 0, 3));
        positions.add(pos.offset(1, 0, 3));
        positions.add(pos.offset(-1, 0, -3));
        positions.add(pos.offset(1, 0, -3));

        positions.add(pos.offset(3, 0, -1));
        positions.add(pos.offset(3, 0, 1));
        positions.add(pos.offset(-3, 0, -1));
        positions.add(pos.offset(-3, 0, 1));

        return positions;
    }


    public static void tick(Level level, BlockPos pos, BlockState state, MagicalWorkbenchBlockEntity blockEntity) {

        if (state.getValue(MagicalWorkbenchBlock.ACTIVITY_STATE) == 0)
            return;

        if (state.getValue(MagicalWorkbenchBlock.ACTIVITY_STATE) == 1 || state.getValue(MagicalWorkbenchBlock.ACTIVITY_STATE) == 2)
            blockEntity.tick++;


        if (blockEntity.tick == blockEntity.animationStartDelay / 3 * 2) {

            ItemStack[] stacks = checkValidMultiBlock(blockEntity);
            if(stacks == null){
                blockEntity.tick = 0;
                level.setBlock(pos, state.setValue(MagicalWorkbenchBlock.ACTIVITY_STATE, 0), 3);
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F, false);

                level.addDestroyBlockEffect(pos, state);
                Block.popResource(level, pos.above(), new ItemStack(ItemRegistry.DRAGON_ARTIFACT.get()));
                return;
            }

            blockEntity.setRitualBlocks(stacks);
            level.setBlock(pos, state.setValue(MagicalWorkbenchBlock.ACTIVITY_STATE, 2), 3);

            if(level.isClientSide())
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENDER_EYE_DEATH, SoundSource.BLOCKS, 1.0F, 2.0F, false);

            for (BlockPos p : blockEntity.getRitualBlocks()) {
                level.addDestroyBlockEffect(p, level.getBlockState(p));
            }
        }

        if (blockEntity.tick == blockEntity.animationStartDelay) {
            for (BlockPos p : blockEntity.getRitualBlocks()) {
                level.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
            }
        }

        if(level.isClientSide() && blockEntity.hasAnimationStarted() && !blockEntity.hasAnimationFinished()){

            int soundStep;

            if(blockEntity.getAnimationTick() / blockEntity.getAnimationTime() > 0.75D)
                soundStep = 2;
            else if(blockEntity.getAnimationTick() / blockEntity.getAnimationTime() > 0.5D)
                soundStep = 4;
            else if(blockEntity.getAnimationTick() / blockEntity.getAnimationTime() > 0.25D)
                soundStep = 6;
            else
                soundStep = 8;

            System.out.println(soundStep);
            if(blockEntity.getAnimationTick() % soundStep == 0)
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BOAT_PADDLE_LAND, SoundSource.BLOCKS, 5.0F, 0.25F, false);
        }

        if (blockEntity.hasAnimationFinished()) {
            blockEntity.tick = 0;
            level.setBlock(pos, state.setValue(MagicalWorkbenchBlock.ACTIVITY_STATE, 0), 3);
            Block.popResource(level, pos.offset(0, 5, 0), new ItemStack(Items.ELYTRA));
            level.playLocalSound(pos.getX(), pos.offset(0, 5, 0).getY(), pos.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 0.5F, 0.75F, false);

            for (int i = 0; i < 20; i++) {
                level.addParticle(ParticleTypes.ELECTRIC_SPARK, pos.getX() + 0.5D, pos.getY() + 5.5D, pos.getZ() + 0.5D, (level.getRandom().nextDouble() - 0.5D) * 2.0D, (level.getRandom().nextDouble() - 0.5D) * 2.0D, (level.getRandom().nextDouble() - 0.5D) * 2.0D);
            }
        }
    }


    private static ItemStack[] checkValidMultiBlock(MagicalWorkbenchBlockEntity blockEntity) {

        ItemStack[] stacks = new ItemStack[8];
        List<BlockPos> ritualBlockPositions = blockEntity.getRitualBlocks();

        for (int i = 0; i < 8; i++)
            stacks[i] = new ItemStack(blockEntity.getLevel().getBlockState(ritualBlockPositions.get(i)).getBlock());

        int foundGhastBlocks = 0;
        int foundPhantomBlocks = 0;

        for(ItemStack stack : stacks){
            if(stack.is(ItemRegistry.GHAST_BLOCK.get()))
                foundGhastBlocks++;

            if(stack.is(ItemRegistry.PHANTOM_BLOCK.get()))
                foundPhantomBlocks++;
        }

        return foundGhastBlocks == 4 && foundPhantomBlocks == 4 ? stacks : null;
    }

}
