package de.crafty.winterskyblock.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundPlayBonemealEffect {

    private BlockPos pos;

    public ClientboundPlayBonemealEffect(BlockPos pos) {
        this.pos = pos;
    }

    public ClientboundPlayBonemealEffect() {

    }

    public BlockPos getPos() {
        return this.pos;
    }

    public static void encode(ClientboundPlayBonemealEffect packet, FriendlyByteBuf out) {
        out.writeInt(packet.pos.getX());
        out.writeInt(packet.pos.getY());
        out.writeInt(packet.pos.getZ());

    }

    public static ClientboundPlayBonemealEffect decode(FriendlyByteBuf in) {
        ClientboundPlayBonemealEffect packet = new ClientboundPlayBonemealEffect();

        int x = in.readInt();
        int y = in.readInt();
        int z = in.readInt();

        packet.pos = new BlockPos(x, y, z);
        return packet;
    }

    public static void handle(ClientboundPlayBonemealEffect packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleBonemealEffect(packet)));
        ctx.get().setPacketHandled(true);
    }

}
