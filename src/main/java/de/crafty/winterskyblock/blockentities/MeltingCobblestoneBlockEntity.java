package de.crafty.winterskyblock.blockentities;

import de.crafty.winterskyblock.registry.BlockEntityRegistry;
import de.crafty.winterskyblock.util.ValidHeatSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;

public class MeltingCobblestoneBlockEntity extends BlockEntity {


    public static final float BASE_MELTING_TIME = 20 * 20;
    private float progression;
    private final float baseEfficiency = 1.0F;

    public MeltingCobblestoneBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.MELTING_COBBLESTONE.get(), pos, state);
    }


    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.progression = tag.getFloat("progression");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putFloat("progression", this.progression);
    }


    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    public void increaseProgression(float amount) {
        this.progression += amount;
        this.setChanged();
    }

    public void decreaseProgression(float amount) {
        this.progression -= amount;
        this.setChanged();
    }

    public float currentProgression() {
        return this.progression;
    }

    public float getMeltingTime(){
        return BASE_MELTING_TIME;
    }

    public float getProgress(){
        return Math.min(this.progression / BASE_MELTING_TIME, 1.0F);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, MeltingCobblestoneBlockEntity blockEntity){


        BlockState below = level.getBlockState(pos.below());

        float heatEfficiency = ValidHeatSource.getHeatEfficiency(level, below, pos);

        if(heatEfficiency > 0.0F)
            blockEntity.increaseProgression((blockEntity.baseEfficiency * heatEfficiency) / (level.getBlockState(pos.above()).getBlock() == Blocks.WATER ? 2.0F : 1.0F));

        if(heatEfficiency == 0.0F && blockEntity.currentProgression() >= 0)
            blockEntity.decreaseProgression(1);

        if(!(level instanceof ServerLevel serverLevel))
            return;

        if(blockEntity.currentProgression() < 0){
            level.setBlock(pos, Blocks.COBBLESTONE.defaultBlockState(), 3);
            serverLevel.getServer().getPlayerList().getPlayers().forEach(player -> {
                player.connection.send(new ClientboundSoundPacket(SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), 0.25F, 2.6F, serverLevel.getSeed()));
            });
        }

        if(blockEntity.currentProgression() >= BASE_MELTING_TIME){
            level.setBlock(pos, Blocks.LAVA.defaultBlockState(), 3);
            serverLevel.getServer().getPlayerList().getPlayers().forEach(player -> {
                player.connection.send(new ClientboundSoundPacket(SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), 1.0F, 1.0F, serverLevel.getSeed()));
            });
        }
    }
}
