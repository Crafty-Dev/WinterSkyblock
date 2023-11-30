package de.crafty.winterskyblock.mixin.net.minecraft.world.level.dimension;

import de.crafty.winterskyblock.registry.DimensionRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LevelStem.class)
public abstract class MixinLevelStem {


    @Shadow @Final public static ResourceKey<LevelStem> OVERWORLD;

    @Shadow @Final public static ResourceKey<LevelStem> NETHER;

    @Shadow @Final public static ResourceKey<LevelStem> END;

    @Inject(method = "stable", at = @At("HEAD"), cancellable = true)
    private static void injectWinterSkyblockDimension(Registry<LevelStem> registry, CallbackInfoReturnable<Boolean> cir){


        Optional<LevelStem> optional = registry.getOptional(DimensionRegistry.SNOW_HEAVEN.levelStem());

        if(winterSkyblock$checkDefaults(registry) && optional.isPresent())
            cir.setReturnValue(true);
    }


    @Unique
    private static boolean winterSkyblock$checkDefaults(Registry<LevelStem> registry){

        Optional<LevelStem> optional = registry.getOptional(OVERWORLD);
        Optional<LevelStem> optional1 = registry.getOptional(NETHER);
        Optional<LevelStem> optional2 = registry.getOptional(END);

        if (optional.isPresent() && optional1.isPresent() && optional2.isPresent()) {
            if (!optional.get().typeHolder().is(BuiltinDimensionTypes.OVERWORLD) && !optional.get().typeHolder().is(BuiltinDimensionTypes.OVERWORLD_CAVES)) {
                return false;
            } else if (!optional1.get().typeHolder().is(BuiltinDimensionTypes.NETHER)) {
                return false;
            } else if (!optional2.get().typeHolder().is(BuiltinDimensionTypes.END)) {
                return false;
            } else if (optional1.get().generator() instanceof NoiseBasedChunkGenerator && optional2.get().generator() instanceof NoiseBasedChunkGenerator) {
                NoiseBasedChunkGenerator noisebasedchunkgenerator = (NoiseBasedChunkGenerator)optional1.get().generator();
                NoiseBasedChunkGenerator noisebasedchunkgenerator1 = (NoiseBasedChunkGenerator)optional2.get().generator();
                if (!noisebasedchunkgenerator.stable(NoiseGeneratorSettings.NETHER)) {
                    return false;
                } else if (!noisebasedchunkgenerator1.stable(NoiseGeneratorSettings.END)) {
                    return false;
                } else if (!(noisebasedchunkgenerator.getBiomeSource() instanceof MultiNoiseBiomeSource)) {
                    return false;
                } else {
                    MultiNoiseBiomeSource multinoisebiomesource = (MultiNoiseBiomeSource)noisebasedchunkgenerator.getBiomeSource();
                    if (!multinoisebiomesource.stable(MultiNoiseBiomeSource.Preset.NETHER)) {
                        return false;
                    } else {
                        BiomeSource biomesource = optional.get().generator().getBiomeSource();
                        if (biomesource instanceof MultiNoiseBiomeSource && !((MultiNoiseBiomeSource)biomesource).stable(MultiNoiseBiomeSource.Preset.OVERWORLD)) {
                            return false;
                        } else {
                            return noisebasedchunkgenerator1.getBiomeSource() instanceof TheEndBiomeSource;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }
}
