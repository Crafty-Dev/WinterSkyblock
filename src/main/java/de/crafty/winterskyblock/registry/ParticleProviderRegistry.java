package de.crafty.winterskyblock.registry;


import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.particle.HammerParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WinterSkyblock.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleProviderRegistry {


    @SubscribeEvent
    public static void registerProviders(RegisterParticleProvidersEvent event){
        event.register(ParticleRegistry.HAMMER_STONE.get(), HammerParticle.Provider::new);
    }

}
