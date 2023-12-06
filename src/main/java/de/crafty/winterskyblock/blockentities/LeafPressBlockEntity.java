package de.crafty.winterskyblock.blockentities;

import de.crafty.winterskyblock.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LeafPressBlockEntity extends BlockEntity {


    private ItemStack content = ItemStack.EMPTY;

    public LeafPressBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.LEAF_PRESS.get(), pos, state);
    }


    public ItemStack getContent() {
        return this.content;
    }

    public void setContent(ItemStack content) {
        this.content = content;
        this.setChanged();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);


        CompoundTag contentTag = tag.getCompound("content");
        this.content = ItemStack.of(contentTag);
    }


    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        CompoundTag contentTag = this.content.save(new CompoundTag());
        tag.put("content", contentTag);
    }


    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
