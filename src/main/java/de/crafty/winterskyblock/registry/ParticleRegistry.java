package de.crafty.winterskyblock.registry;

import de.crafty.winterskyblock.WinterSkyblock;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry {


    private static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, WinterSkyblock.MODID);

    public static final RegistryObject<SimpleParticleType> HAMMER_STONE = REGISTRY.register("hammer_stone", () -> new SimpleParticleType(false));



    public static void register(IEventBus eventBus){
        REGISTRY.register(eventBus);
    }
}
