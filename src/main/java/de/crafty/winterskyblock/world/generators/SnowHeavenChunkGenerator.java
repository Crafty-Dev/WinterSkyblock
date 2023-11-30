package de.crafty.winterskyblock.world.generators;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SnowHeavenChunkGenerator extends NoiseBasedChunkGenerator {

    public static final Codec<NoiseBasedChunkGenerator> CODEC = RecordCodecBuilder.create((p_224323_) -> {
        return commonCodec(p_224323_).and(p_224323_.group(RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter((p_188716_) -> {
            return p_188716_.noises;
        }), BiomeSource.CODEC.fieldOf("biome_source").forGetter((p_188711_) -> {
            return p_188711_.biomeSource;
        }), NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter((p_224278_) -> {
            return p_224278_.settings;
        }))).apply(p_224323_, p_224323_.stable(SnowHeavenChunkGenerator::new));
    });

    public SnowHeavenChunkGenerator(Registry<StructureSet> registry, Registry<NormalNoise.NoiseParameters> noiseParameters, BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settingsHolder) {
        super(registry, noiseParameters, biomeSource, settingsHolder);
    }


    @Override
    public void buildSurface(WorldGenRegion worldGenRegion, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess) {
        //super.buildSurface(p_224232_, p_224233_, p_224234_, p_224235_);
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess) {
        return CompletableFuture.completedFuture(chunkAccess);
    }

    @Override
    public Stream<Holder<StructureSet>> possibleStructureSets() {

        Set<ResourceKey<StructureSet>> structures = ImmutableSet.of();

        HolderSet.Direct<StructureSet> direct = HolderSet.direct(structures.stream().flatMap((key) -> {
            return this.structureSets.getHolder(key).stream();
        }).collect(Collectors.toList()));

        return Optional.of(direct).get().stream();
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }
}
