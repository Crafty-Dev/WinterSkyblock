package de.crafty.winterskyblock.mixin.net.minecraft.data.worldgen.biome;

import de.crafty.winterskyblock.registry.BiomeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biomes.class)
public abstract class MixinBiomes {


    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void addWinterSkyblockBiomes(Registry<Biome> registry, CallbackInfoReturnable<Holder<Biome>> cir){
        BuiltinRegistries.register(registry, BiomeRegistry.SNOWY_ISLANDS.biomeRef(), BiomeRegistry.SNOWY_ISLANDS.get());
    }

}
