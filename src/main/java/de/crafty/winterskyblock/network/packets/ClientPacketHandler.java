package de.crafty.winterskyblock.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.level.block.Blocks;
import oshi.hardware.platform.mac.MacNetworkIF;

public class ClientPacketHandler {


    public static void handleBonemealEffect(ClientboundPlayBonemealEffect packet) {

        ClientLevel level = Minecraft.getInstance().player.clientLevel;
        BoneMealItem.addGrowthParticles(level, packet.getPos(), 0);
        level.playLocalSound(packet.getPos(), SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F, false);

    }

    public static void handleDestroyEffect(ClientboundPlayDestroyBlockEffect packet){

        ClientLevel level = Minecraft.getInstance().player.clientLevel;
        level.addDestroyBlockEffect(packet.getPos(), Blocks.END_PORTAL.defaultBlockState());
        level.playLocalSound(packet.getPos(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F, false);
    }

}
