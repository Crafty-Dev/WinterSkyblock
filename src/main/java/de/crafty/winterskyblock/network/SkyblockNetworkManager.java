package de.crafty.winterskyblock.network;

import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.network.packets.ClientboundPlayBonemealEffect;
import de.crafty.winterskyblock.network.packets.ClientboundPlayDestroyBlockEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class SkyblockNetworkManager {

    private static final String PROTOCOL_VERSION = "187";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(WinterSkyblock.MODID, "main"),
            () -> PROTOCOL_VERSION,
            (absent) -> true,
            (acceptVanilla) -> true
    );


    public static void registerPackets() {
        int id = 0;
        CHANNEL.registerMessage(id++, ClientboundPlayBonemealEffect.class, ClientboundPlayBonemealEffect::encode, ClientboundPlayBonemealEffect::decode, ClientboundPlayBonemealEffect::handle);
        CHANNEL.registerMessage(id++, ClientboundPlayDestroyBlockEffect.class, ClientboundPlayDestroyBlockEffect::encode, ClientboundPlayDestroyBlockEffect::decode, ClientboundPlayDestroyBlockEffect::handle);

    }






    public static void sendBonemealEffectPacket(BlockPos pos, LevelChunk chunk){
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new ClientboundPlayBonemealEffect(pos));
    }

    public static void sendDestroyBlockEffectPacket(BlockPos pos, LevelChunk chunk){
        CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new ClientboundPlayDestroyBlockEffect(pos));
    }


}
