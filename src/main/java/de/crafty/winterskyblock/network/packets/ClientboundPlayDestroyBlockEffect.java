package de.crafty.winterskyblock.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundPlayDestroyBlockEffect {

    private BlockPos pos;

    public ClientboundPlayDestroyBlockEffect(BlockPos pos) {
        this.pos = pos;
    }

    public ClientboundPlayDestroyBlockEffect() {

    }

    public BlockPos getPos() {
        return this.pos;
    }

    public static void encode(ClientboundPlayDestroyBlockEffect packet, FriendlyByteBuf out) {
        out.writeInt(packet.pos.getX());
        out.writeInt(packet.pos.getY());
        out.writeInt(packet.pos.getZ());

    }

    public static ClientboundPlayDestroyBlockEffect decode(FriendlyByteBuf in) {
        ClientboundPlayDestroyBlockEffect packet = new ClientboundPlayDestroyBlockEffect();

        int x = in.readInt();
        int y = in.readInt();
        int z = in.readInt();

        packet.pos = new BlockPos(x, y, z);
        return packet;
    }

    public static void handle(ClientboundPlayDestroyBlockEffect packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleDestroyEffect(packet)));
        ctx.get().setPacketHandled(true);
    }

}
