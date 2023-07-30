package de.crafty.winterskyblock.registry;


import de.crafty.winterskyblock.WinterSkyblock;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WinterSkyblock.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BlockColorRegistry {



    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, tintGetter, pos, tintIndex) -> tintIndex == 1 && tintGetter != null && pos != null ? BiomeColors.getAverageWaterColor(tintGetter, pos) : -1, BlockRegistry.LEAF_PRESS.get());
    }
}
