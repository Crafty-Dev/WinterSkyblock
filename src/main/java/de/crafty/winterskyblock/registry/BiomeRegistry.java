package de.crafty.winterskyblock.registry;

import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.util.SnowHeavenBiomes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BiomeRegistry {



    public static final BiomeEntry SNOWY_ISLANDS = new BiomeEntry("snowy_islands", SnowHeavenBiomes::snowy_islands);








    public static class BiomeEntry {

        private final ResourceKey<Biome> biomeRef;
        private final Supplier<Biome> biome;

        public BiomeEntry(String id, Supplier<Biome> biome){
            this.biomeRef = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(WinterSkyblock.MODID, id));
            this.biome = biome;
        }



        public ResourceKey<Biome> biomeRef(){
            return this.biomeRef;
        }

        public Biome get(){
            return this.biome.get();
        }
    }

}
