package de.crafty.winterskyblock.mixin.net.minecraft.world.level.levelgen.presets;

import de.crafty.winterskyblock.registry.DimensionRegistry;
import de.crafty.winterskyblock.registry.BiomeSourceRegistry;
import de.crafty.winterskyblock.registry.WorldPresetRegistry;
import de.crafty.winterskyblock.world.generators.EndChunkGenerator;
import de.crafty.winterskyblock.world.generators.NetherChunkGenerator;
import de.crafty.winterskyblock.world.generators.OverworldChunkGenerator;
import de.crafty.winterskyblock.world.generators.SnowHeavenChunkGenerator;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(WorldPresets.Bootstrap.class)
public abstract class MixinWorldPresets {


    @Shadow @Final private Registry<StructureSet> structureSets;

    @Shadow @Final private Registry<NormalNoise.NoiseParameters> noises;

    @Shadow @Final private Registry<Biome> biomes;

    @Shadow @Final private Holder<NoiseGeneratorSettings> netherNoiseSettings;

    @Shadow @Final private Holder<NoiseGeneratorSettings> endNoiseSettings;

    @Shadow @Final private Registry<NoiseGeneratorSettings> noiseSettings;

    @Shadow @Final private Registry<WorldPreset> presets;

    @Shadow @Final private Holder<DimensionType> overworldDimensionType;

    @Shadow @Final private Holder<DimensionType> netherDimensionType;

    @Shadow @Final private Holder<DimensionType> endDimensionType;

    @Shadow @Final private Registry<DimensionType> dimensionTypes;

    @Inject(method = "run", at = @At("HEAD"))
    private void addWinterSkyblockPreset(CallbackInfoReturnable<Holder<WorldPreset>> cir){

        Holder<NoiseGeneratorSettings> overworldNoiseSettings = this.noiseSettings.getOrCreateHolderOrThrow(NoiseGeneratorSettings.OVERWORLD);
        OverworldChunkGenerator overworldChunkGenerator = new OverworldChunkGenerator(this.structureSets, this.noises, MultiNoiseBiomeSource.Preset.OVERWORLD.biomeSource(this.biomes), overworldNoiseSettings);
        NetherChunkGenerator netherChunkGenerator = new NetherChunkGenerator(this.structureSets, this.noises, MultiNoiseBiomeSource.Preset.NETHER.biomeSource(this.biomes), this.netherNoiseSettings);
        EndChunkGenerator endChunkGenerator = new EndChunkGenerator(this.structureSets, this.noises, new TheEndBiomeSource(this.biomes), this.endNoiseSettings);

        LevelStem overworldStem = new LevelStem(this.overworldDimensionType, overworldChunkGenerator);
        LevelStem netherStem = new LevelStem(this.netherDimensionType, netherChunkGenerator);
        LevelStem endStem = new LevelStem(this.endDimensionType, endChunkGenerator);

        //Snow Heaven
        Holder<DimensionType> snowHeavenDimensionType = this.dimensionTypes.getOrCreateHolderOrThrow(DimensionRegistry.SNOW_HEAVEN.typeRef());
        SnowHeavenChunkGenerator snowHeavenChunkGenerator = new SnowHeavenChunkGenerator(this.structureSets, this.noises, BiomeSourceRegistry.SNOW_HEAVEN.biomeSource(this.biomes), overworldNoiseSettings);

        LevelStem snowHeavenStem = new LevelStem(snowHeavenDimensionType, snowHeavenChunkGenerator);

        //Without snow heaven
        WorldPreset preset = new WorldPreset(Map.of(LevelStem.OVERWORLD, overworldStem, LevelStem.NETHER, netherStem, LevelStem.END, endStem));
        BuiltinRegistries.register(this.presets, WorldPresetRegistry.WINTER_SKYBLOCK, preset);

    }

}
