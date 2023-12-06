package de.crafty.winterskyblock.blockentities;

import de.crafty.winterskyblock.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrystalCraftingPedestalBlockEntity extends BlockEntity {

    private static final int IDLE_TICKS = 360;

    private int idleTick = 0;
    private ItemStack itemStack = ItemStack.EMPTY;

    public CrystalCraftingPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.CRYSTAL_CRAFTING_PEDESTAL.get(), pos, state);
    }


    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putInt("idleTick", this.idleTick);
        tag.put("item", this.itemStack.save(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.idleTick = tag.getInt("idleTick");
        this.itemStack = ItemStack.of(tag.getCompound("item"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = super.getUpdateTag();
        this.saveAdditional(updateTag);
        return updateTag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }


    public void setItemStack(ItemStack stack){
        this.itemStack = stack;
        this.setChanged();
    }

    public int getIdleTick(){
        return this.idleTick;
    }

    public ItemStack getItemStack(){
        return this.itemStack;
    }


    public boolean isActive(){
        return this.level.getBlockState(this.getBlockPos().above()).isAir();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CrystalCraftingPedestalBlockEntity blockEntity){

        if(blockEntity.idleTick < IDLE_TICKS)
            blockEntity.idleTick++;
        else
            blockEntity.idleTick = 0;


    }
}
