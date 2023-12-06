package de.crafty.winterskyblock.blockentities;

import com.mojang.authlib.GameProfile;
import de.crafty.winterskyblock.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GraveStoneBlockEntity extends BlockEntity {


    private GameProfile playerProfile;
    private List<ItemStack> items = new ArrayList<>();


    public GraveStoneBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.GRAVE_STONE.get(), pos, state);
    }

    public void setPlayerProfile(GameProfile profile){
        this.playerProfile = profile;

        this.setChanged();
    }


    public GameProfile getPlayerProfile(){
        return this.playerProfile;
    }

    public void setContent(List<ItemStack> content){
        this.items = content;
        this.setChanged();
    }

    public List<ItemStack> getContent(){
        return this.items;
    }


    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.items.clear();

        ListTag listTag = tag.getList("items", ListTag.TAG_COMPOUND);
        for(int i = 0; i < listTag.size(); i++){
            CompoundTag compoundTag = listTag.getCompound(i);
            this.items.add(ItemStack.of(compoundTag));
        }

        this.playerProfile = NbtUtils.readGameProfile(tag.getCompound("playerProfile"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        ListTag listTag = new ListTag();
        this.items.forEach(stack -> listTag.add(stack.save(new CompoundTag())));
        tag.put("items", listTag);

        if(this.playerProfile != null)
            tag.put("playerProfile", NbtUtils.writeGameProfile(new CompoundTag(), this.playerProfile));
    }


    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("playerProfile", NbtUtils.writeGameProfile(new CompoundTag(), this.playerProfile));
        return tag;
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
}
