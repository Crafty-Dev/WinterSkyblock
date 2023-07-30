package de.crafty.winterskyblock.registry;

import com.mojang.serialization.Codec;
import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.structure.NatureIslandStructure;
import de.crafty.winterskyblock.structure.piece.NatureIslandPiece;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StructureRegistry {

    private static final List<StructureRegistryObject> REGISTRY_OBJECTS = new ArrayList<>();

    public static final StructureRegistryObject DESERT_ISLAND = ObjectBuilder.of("desert_island")
            .pieces(NatureIslandPiece.Desert::new)
            .codec(NatureIslandStructure.Desert.CODEC)
            .structure(new NatureIslandStructure.Desert(structure(Tags.Biomes.IS_DESERT, Map.of(), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)))
            .placement(new RandomSpreadStructurePlacement(8, 4, RandomSpreadType.LINEAR, 187))
            .build();


    public static final StructureRegistryObject SAVANNA_ISLAND = ObjectBuilder.of("savanna_island")
            .pieces(NatureIslandPiece.Savanna::new)
            .codec(NatureIslandStructure.Savanna.CODEC)
            .structure(new NatureIslandStructure.Savanna(structure(BiomeTags.IS_SAVANNA, Map.of(), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)))
            .placement(new RandomSpreadStructurePlacement(8, 4, RandomSpreadType.LINEAR, 187))
            .build();

    public static final StructureRegistryObject JUNGLE_ISLAND = ObjectBuilder.of("jungle_island")
            .pieces(NatureIslandPiece.Jungle::new)
            .codec(NatureIslandStructure.Jungle.CODEC)
            .structure(new NatureIslandStructure.Jungle(structure(BiomeTags.IS_JUNGLE, Map.of(), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)))
            .placement(new RandomSpreadStructurePlacement(8, 4, RandomSpreadType.LINEAR, 187))
            .build();

    public static final StructureRegistryObject SPRUCE_ISLAND = ObjectBuilder.of("spruce_island")
            .pieces(NatureIslandPiece.Spruce::new)
            .codec(NatureIslandStructure.Spruce.CODEC)
            .structure(new NatureIslandStructure.Spruce(structure(BiomeTags.IS_TAIGA, Map.of(), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)))
            .placement(new RandomSpreadStructurePlacement(8, 4, RandomSpreadType.LINEAR, 187))
            .build();

    public static final StructureRegistryObject OAK_ISLAND = ObjectBuilder.of("oak_island")
            .pieces(NatureIslandPiece.Oak::new)
            .codec(NatureIslandStructure.Oak.CODEC)
            .structure(new NatureIslandStructure.Oak(structure(Tags.Biomes.IS_PLAINS, Map.of(), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)))
            .placement(new RandomSpreadStructurePlacement(8, 4, RandomSpreadType.LINEAR, 187))
            .build();

    public static final StructureRegistryObject BIRCH_ISLAND = ObjectBuilder.of("birch_island")
            .pieces(NatureIslandPiece.Birch::new)
            .codec(NatureIslandStructure.Birch.CODEC)
            .structure(new NatureIslandStructure.Birch(structure(TagRegistry.IS_BIRCH_FOREST, Map.of(), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)))
            .placement(new RandomSpreadStructurePlacement(8, 4, RandomSpreadType.LINEAR, 187))
            .build();

    public static final StructureRegistryObject LUSH_ISLAND = ObjectBuilder.of("lush_island")
            .pieces(NatureIslandPiece.Lush::new)
            .codec(NatureIslandStructure.Lush.CODEC)
            .structure(new NatureIslandStructure.Lush(structure(Tags.Biomes.IS_LUSH, Map.of(), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)))
            .placement(new RandomSpreadStructurePlacement(8, 4, RandomSpreadType.LINEAR, 187))
            .build();

    public static final StructureRegistryObject CRIMSON_ISLAND = ObjectBuilder.of("crimson_island")
            .pieces(NatureIslandPiece.Crimson::new)
            .codec(NatureIslandStructure.Crimson.CODEC)
            .structure(new NatureIslandStructure.Crimson(structure(TagRegistry.IS_CRIMSON, Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE, NetherFortressStructure.FORTRESS_ENEMIES)), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)))
            .placement(new RandomSpreadStructurePlacement(8, 4, RandomSpreadType.LINEAR, 187))
            .build();

    public static final StructureRegistryObject WARPED_ISLAND = ObjectBuilder.of("warped_island")
            .pieces(NatureIslandPiece.Warped::new)
            .codec(NatureIslandStructure.Warped.CODEC)
            .structure(new NatureIslandStructure.Warped(structure(TagRegistry.IS_WARPED, Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.PIECE, NetherFortressStructure.FORTRESS_ENEMIES)), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)))
            .placement(new RandomSpreadStructurePlacement(8, 4, RandomSpreadType.LINEAR, 187))
            .build();


    public static final StructureRegistryObject DRIPSTONE_ISLAND = ObjectBuilder.of("dripstone_island")
            .pieces(NatureIslandPiece.Dripstone::new)
            .codec(NatureIslandStructure.Dripstone.CODEC)
            .structure(new NatureIslandStructure.Dripstone(structure(TagRegistry.IS_DRIPPY, Map.of(), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)))
            .placement(new RandomSpreadStructurePlacement(8, 4, RandomSpreadType.LINEAR, 187))
            .build();


    public static final StructureRegistryObject AMETHYST_ISLAND = ObjectBuilder.of("amethyst_island")
            .pieces(NatureIslandPiece.Amethyst::new)
            .codec(NatureIslandStructure.Amethyst.CODEC)
            .structure(new NatureIslandStructure.Amethyst(structure(BiomeTags.IS_OVERWORLD, Map.of(), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)))
            .placement(new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 187))
            .build();


    public static final StructureRegistryObject END_ISLAND = ObjectBuilder.of("end_island")
            .pieces(NatureIslandPiece.End::new)
            .codec(NatureIslandStructure.End.CODEC)
            .structure(new NatureIslandStructure.End(structure(BiomeTags.IS_END, Map.of(), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)))
            .placement(new RandomSpreadStructurePlacement(16, 8, RandomSpreadType.TRIANGULAR, 187))
            .build();

    private static Structure.StructureSettings structure(TagKey<Biome> tag, Map<MobCategory, StructureSpawnOverride> spawnOverrideMap, GenerationStep.Decoration decoration, TerrainAdjustment adjustment) {
        return new Structure.StructureSettings(biomes(tag), spawnOverrideMap, decoration, adjustment);
    }

    private static HolderSet<Biome> biomes(TagKey<Biome> tag) {
        return BuiltinRegistries.BIOME.getOrCreateTag(tag);
    }


    public static class ObjectBuilder {

        public static ObjectBuilder of(String id) {
            return new ObjectBuilder(id);
        }


        private final String id;
        private Structure structure;
        private List<StructurePieceType.ContextlessType> pieces;
        private Codec<? extends Structure> codec;
        private StructurePlacement placement;

        private ObjectBuilder(String id) {
            this.id = id;
        }


        private ObjectBuilder pieces(StructurePieceType.ContextlessType... pieces) {
            this.pieces = List.of(pieces);
            return this;
        }

        private ObjectBuilder codec(Codec<? extends Structure> codec) {
            this.codec = codec;
            return this;
        }

        private ObjectBuilder structure(Structure structure) {
            this.structure = structure;
            return this;
        }

        private ObjectBuilder placement(StructurePlacement placement) {
            this.placement = placement;
            return this;
        }

        private StructureRegistryObject build() {
            return new StructureRegistryObject(this.id, this.structure, this.pieces, this.codec, this.placement);
        }

    }

    interface StructurePieceProvider {

        StructurePiece get(StructureRegistryObject registryObject, CompoundTag tag);
    }

    interface StructureProvider {
        Structure get(StructureRegistryObject registryObject);
    }

    public static class StructureRegistryObject {


        private final String id;
        private final Structure structureInstance;
        private final List<StructurePieceType.ContextlessType> pieceBuilder;
        private final Codec<? extends Structure> codec;
        private final StructurePlacement placement;

        private ResourceKey<Structure> rkStructure = null;
        private ResourceKey<StructureSet> rkStructureSet = null;
        private StructurePieceType[] pieces = null;
        private StructureType<? extends Structure> structureType;
        private Holder<Structure> structure = null;
        private Holder<StructureSet> structureSet = null;

        private StructureRegistryObject(String id, Structure structure, List<StructurePieceType.ContextlessType> pieces, Codec<? extends Structure> codec, StructurePlacement placement) {
            this.id = id;
            this.structureInstance = structure;
            this.pieceBuilder = pieces;
            this.codec = codec;
            this.placement = placement;

            REGISTRY_OBJECTS.add(this);
        }

        public void register() {
            ResourceKey<Structure> rkStructure = ResourceKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(WinterSkyblock.MODID, this.id));
            ResourceKey<StructureSet> rkStructureSet = ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, new ResourceLocation(WinterSkyblock.MODID, this.id));

            List<StructurePieceType> pieceTypes = new ArrayList<>();
            this.pieceBuilder.forEach(piece -> pieceTypes.add(Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(WinterSkyblock.MODID, this.id), piece)));

            StructureType<? extends Structure> structureType = Registry.register(Registry.STRUCTURE_TYPES, new ResourceLocation(WinterSkyblock.MODID, this.id), () -> (Codec<Structure>) this.codec);

            Holder<Structure> structure = BuiltinRegistries.register(BuiltinRegistries.STRUCTURES, new ResourceLocation(WinterSkyblock.MODID, this.id), this.structureInstance);
            Holder<StructureSet> structureSet = BuiltinRegistries.register(BuiltinRegistries.STRUCTURE_SETS, new ResourceLocation(WinterSkyblock.MODID, this.id), new StructureSet(structure, this.placement));

            this.rkStructure = rkStructure;
            this.rkStructureSet = rkStructureSet;
            this.pieces = pieceTypes.toArray(new StructurePieceType[0]);
            this.structure = structure;
            this.structureSet = structureSet;

            this.structureType = structureType;
            System.out.println("Registering Structure: " + this.id + " (" + pieceTypes.size() + " pieces)");
        }


        public ResourceKey<Structure> structureResourceKey() {
            return this.rkStructure;
        }

        public ResourceKey<StructureSet> structureSetResourceKey() {
            return this.rkStructureSet;
        }

        public StructurePieceType[] pieces() {
            return this.pieces;
        }

        public StructureType<? extends Structure> structureType() {
            return this.structureType;
        }

        public Structure structure() {
            return this.structure.get();
        }

        public StructureSet structureSet() {
            return this.structureSet.get();
        }
    }
}
