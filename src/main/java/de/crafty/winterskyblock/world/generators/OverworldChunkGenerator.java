package de.crafty.winterskyblock.world.generators;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.crafty.winterskyblock.registry.StructureRegistry;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.SharedConstants;
import net.minecraft.core.*;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OverworldChunkGenerator extends NoiseBasedChunkGenerator {

    public static final Codec<NoiseBasedChunkGenerator> CODEC = RecordCodecBuilder.create((p_224323_) -> {
        return commonCodec(p_224323_).and(p_224323_.group(RegistryOps.retrieveRegistry(Registry.NOISE_REGISTRY).forGetter((p_188716_) -> {
            return p_188716_.noises;
        }), BiomeSource.CODEC.fieldOf("biome_source").forGetter((p_188711_) -> {
            return p_188711_.biomeSource;
        }), NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter((p_224278_) -> {
            return p_224278_.settings;
        }))).apply(p_224323_, p_224323_.stable(OverworldChunkGenerator::new));
    });

    public OverworldChunkGenerator(Registry<StructureSet> structureSets, Registry<NormalNoise.NoiseParameters> noiseParameters, BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settingsHolder) {
        super(structureSets, noiseParameters, biomeSource, settingsHolder);
    }


    @Override
    public CompletableFuture<ChunkAccess> createBiomes(Registry<Biome> biomeRegistry, Executor executor, RandomState rand, Blender blender, StructureManager structureManager, ChunkAccess chunkAccess) {
        if (chunkAccess.getPos().x < 16 && chunkAccess.getPos().x > -16 && chunkAccess.getPos().z < 16 && chunkAccess.getPos().z > -16)
            return this.createWinterBiomes(chunkAccess, biomeRegistry);

        return super.createBiomes(biomeRegistry, executor, rand, blender, structureManager, chunkAccess);
    }

    private CompletableFuture<ChunkAccess> createWinterBiomes(ChunkAccess chunkAccess, Registry<Biome> biomeRegistry) {
        LevelHeightAccessor levelheightaccessor = chunkAccess.getHeightAccessorForGeneration();

        for (int i = levelheightaccessor.getMinSection(); i < levelheightaccessor.getMaxSection(); ++i) {
            LevelChunkSection levelchunksection = chunkAccess.getSection(chunkAccess.getSectionIndexFromSectionY(i));
            PalettedContainer<Holder<Biome>> palettedcontainer = (PalettedContainer<Holder<Biome>>) levelchunksection.biomes;


            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    for (int z = 0; z < 4; z++) {
                        palettedcontainer.getAndSetUnchecked(x, y, z, biomeRegistry.getHolderOrThrow(Biomes.SNOWY_TAIGA));
                    }
                }
            }

            levelchunksection.biomes = palettedcontainer;
        }

        return CompletableFuture.completedFuture(chunkAccess);
    }

    @Override
    public void createStructures(RegistryAccess registryAccess, RandomState rand, StructureManager structureManager, ChunkAccess chunkAccess, StructureTemplateManager templateManager, long seed) {
        if (chunkAccess.getPos().x < 16 && chunkAccess.getPos().x > -16 && chunkAccess.getPos().z < 16 && chunkAccess.getPos().z > -16)
            return;

        super.createStructures(registryAccess, rand, structureManager, chunkAccess, templateManager, seed);
    }

    @Override
    public void buildSurface(WorldGenRegion genRegion, StructureManager structureManager, RandomState rand, ChunkAccess chunkAccess) {
        //super.buildSurface(genRegion, structureManager, rand, chunkAccess);
    }

    @Override
    public void applyCarvers(WorldGenRegion p_224224_, long p_224225_, RandomState p_224226_, BiomeManager p_224227_, StructureManager p_224228_, ChunkAccess p_224229_, GenerationStep.Carving p_224230_) {
        //super.applyCarvers(p_224224_, p_224225_, p_224226_, p_224227_, p_224228_, p_224229_, p_224230_);
    }


    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState rand, StructureManager structureManager, ChunkAccess chunkAccess) {
        //return super.fillFromNoise(p_224312_, p_224313_, p_224314_, p_224315_, p_224316_);

        return CompletableFuture.completedFuture(chunkAccess);
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel worldGenLevel, ChunkAccess chunkAccess, StructureManager structureManager) {

        ChunkPos chunkpos = chunkAccess.getPos();
        if (!SharedConstants.debugVoidTerrain(chunkpos)) {
            SectionPos sectionpos = SectionPos.of(chunkpos, worldGenLevel.getMinSection());
            BlockPos blockpos = sectionpos.origin();
            Registry<Structure> registry = worldGenLevel.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
            Map<Integer, List<Structure>> map = registry.stream().collect(Collectors.groupingBy((p_223103_) -> {
                return p_223103_.step().ordinal();
            }));
            List<FeatureSorter.StepFeatureData> list = this.featuresPerStep.get();
            WorldgenRandom worldgenrandom = new WorldgenRandom(new XoroshiroRandomSource(RandomSupport.generateUniqueSeed()));
            long i = worldgenrandom.setDecorationSeed(worldGenLevel.getSeed(), blockpos.getX(), blockpos.getZ());
            Set<Holder<Biome>> set = new ObjectArraySet<>();
            ChunkPos.rangeClosed(sectionpos.chunk(), 1).forEach((p_223093_) -> {
                ChunkAccess chunkaccess = worldGenLevel.getChunk(p_223093_.x, p_223093_.z);

                for (LevelChunkSection levelchunksection : chunkaccess.getSections()) {
                    levelchunksection.getBiomes().getAll(set::add);
                }

            });
            set.retainAll(this.biomeSource.possibleBiomes());
            int j = list.size();

            try {
                int i1 = Math.max(GenerationStep.Decoration.values().length, j);

                for (int k = 0; k < i1; ++k) {
                    int l = 0;
                    if (structureManager.shouldGenerateStructures()) {
                        for (Structure structure : map.getOrDefault(k, Collections.emptyList())) {
                            worldgenrandom.setFeatureSeed(i, l, k);
                            Supplier<String> supplier = () -> {
                                return registry.getResourceKey(structure).map(Object::toString).orElseGet(structure::toString);
                            };

                            try {
                                worldGenLevel.setCurrentlyGenerating(supplier);
                                structureManager.startsForStructure(sectionpos, structure).forEach((p_223086_) -> {
                                    p_223086_.placeInChunk(worldGenLevel, structureManager, this, worldgenrandom, getWritableArea(chunkAccess), chunkpos);
                                });
                            } catch (Exception exception) {
                                CrashReport crashreport1 = CrashReport.forThrowable(exception, "Feature placement");
                                crashreport1.addCategory("Feature").setDetail("Description", supplier::get);
                                throw new ReportedException(crashreport1);
                            }

                            ++l;
                        }
                    }
                }

                worldGenLevel.setCurrentlyGenerating(null);
            } catch (Exception exception2) {
                CrashReport crashreport = CrashReport.forThrowable(exception2, "Biome decoration");
                crashreport.addCategory("Generation").setDetail("CenterX", chunkpos.x).setDetail("CenterZ", chunkpos.z).setDetail("Seed", i);
                throw new ReportedException(crashreport);
            }
        }
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion genRegion) {
        super.spawnOriginalMobs(genRegion);
    }


    @Override
    public Stream<Holder<StructureSet>> possibleStructureSets() {

        Set<ResourceKey<StructureSet>> structures = ImmutableSet.of(
                StructureRegistry.DESERT_ISLAND.structureSetResourceKey(),
                StructureRegistry.SAVANNA_ISLAND.structureSetResourceKey(),
                StructureRegistry.JUNGLE_ISLAND.structureSetResourceKey(),
                StructureRegistry.SPRUCE_ISLAND.structureSetResourceKey(),
                StructureRegistry.OAK_ISLAND.structureSetResourceKey(),
                StructureRegistry.BIRCH_ISLAND.structureSetResourceKey(),
                StructureRegistry.LUSH_ISLAND.structureSetResourceKey(),
                StructureRegistry.CRIMSON_ISLAND.structureSetResourceKey(),
                StructureRegistry.WARPED_ISLAND.structureSetResourceKey(),
                StructureRegistry.DRIPSTONE_ISLAND.structureSetResourceKey(),
                StructureRegistry.AMETHYST_ISLAND.structureSetResourceKey()
        );

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
