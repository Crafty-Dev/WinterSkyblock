package de.crafty.winterskyblock.structure.piece;

import com.sun.source.doctree.BlockTagTree;
import de.crafty.winterskyblock.registry.BlockRegistry;
import de.crafty.winterskyblock.registry.StructureRegistry;
import de.crafty.winterskyblock.registry.TagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public abstract class NatureIslandPiece extends ScatteredFeaturePiece {


    private static final BlockSelector STONE_SELECTOR = new IslandStoneSelector(0.0F, Blocks.STONE.defaultBlockState());
    private static final BlockSelector NETHERRACK_SELECTOR = new IslandNetherrackSelector();

    //Normal
    private static final BlockSelector STONE_SELECTOR_0 = new IslandStoneSelector(0.25F, Blocks.DIRT.defaultBlockState());
    private static final BlockSelector STONE_SELECTOR_1 = new IslandStoneSelector(0.5F, Blocks.DIRT.defaultBlockState());

    //Desert
    private static final BlockSelector DESERT_SELECTOR_0 = new IslandStoneSelector(0.25F, Blocks.SANDSTONE.defaultBlockState());
    private static final BlockSelector DESERT_SELECTOR_1 = new IslandStoneSelector(0.5F, Blocks.SANDSTONE.defaultBlockState());

    //Nether
    private static final BlockSelector NETHER_SELECTOR = new IslandStoneSelector(1.0F, Blocks.NETHERRACK.defaultBlockState());

    //Lush Caves
    private static final BlockSelector LUSH_SELECTOR = new IslandLushSelector(0.0F, Blocks.MOSS_BLOCK.defaultBlockState());
    private static final BlockSelector LUSH_CAVE_SELECTOR_0 = new IslandLushSelector(0.25F, Blocks.MOSS_BLOCK.defaultBlockState());
    private static final BlockSelector LUSH_CAVE_SELECTOR_1 = new IslandLushSelector(0.5F, Blocks.MOSS_BLOCK.defaultBlockState());

    //Dripstone
    private static final BlockSelector DRIPSTONE_SELECTOR = new IslandStoneSelector(0.55F, Blocks.DRIPSTONE_BLOCK.defaultBlockState());

    //Amethyst
    private static final BlockSelector AMETHYST_SELECTOR = new IslandAmethystSelector(0.0F);
    private static final BlockSelector AMETHYST_SELECTOR_0 = new IslandAmethystSelector(0.25F);
    private static final BlockSelector AMETHYST_SELECTOR_1 = new IslandAmethystSelector(0.5F);

    private static final BlockSelector END_SELECTOR = new IslandEndSelector();

    private final List<BlockPos> topBlocks = new ArrayList<>();
    private final List<BlockPos> bottomBlocks = new ArrayList<>();

    private NatureIslandPiece(StructurePieceType type, RandomSource source, int x, int y) {
        super(type, x, 0, y, 11, 24, 11, getRandomHorizontalDirection(source));
    }

    private NatureIslandPiece(StructurePieceType type, CompoundTag tag) {
        super(type, tag);
    }


    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {


        StructurePieceType pieceType = this.getType();

        int yOff = level.getMaxBuildHeight() / 5 + randomSource.nextInt(level.getMaxBuildHeight() / 3);


        if (pieceType.equals(StructureRegistry.LUSH_ISLAND.pieces()[0]) || pieceType.equals(StructureRegistry.DRIPSTONE_ISLAND.pieces()[0])) {

            TagKey<Biome> searchedBiome = pieceType.equals(StructureRegistry.LUSH_ISLAND.pieces()[0]) ? Tags.Biomes.IS_LUSH : TagRegistry.IS_DRIPPY;

            int undergroundBiomeStart = -64;
            int undergroundBiomeEnd = -64;

            for (int y = -64; y < 320; y++) {
                if (!level.getBiome(new BlockPos(pos.getX(), y, pos.getZ())).is(searchedBiome))
                    continue;

                undergroundBiomeStart = y;
                break;
            }

            for (int y = undergroundBiomeStart; y < 320; y++) {
                if (level.getBiome(new BlockPos(pos.getX(), y, pos.getZ())).is(searchedBiome))
                    continue;

                undergroundBiomeEnd = y - 1;
                break;
            }

            yOff = undergroundBiomeStart + randomSource.nextInt(Math.max(1, (undergroundBiomeEnd - 7) - undergroundBiomeStart));
        }

        this.boundingBox.move(0, yOff, 0);


        BlockState topBlock = Blocks.GRASS_BLOCK.defaultBlockState();

        BlockState fillerBlock = Blocks.DIRT.defaultBlockState();
        BlockSelector fillerSelector = null;

        BlockSelector mainSelector = STONE_SELECTOR;
        BlockSelector selector_0 = STONE_SELECTOR_0;
        BlockSelector selector_1 = STONE_SELECTOR_1;

        if (pieceType.equals(StructureRegistry.DESERT_ISLAND.pieces()[0])) {
            topBlock = Blocks.SAND.defaultBlockState();
            fillerBlock = Blocks.SANDSTONE.defaultBlockState();

            selector_0 = DESERT_SELECTOR_0;
            selector_1 = DESERT_SELECTOR_1;
        }
        if (pieceType.equals(StructureRegistry.CRIMSON_ISLAND.pieces()[0])) {
            topBlock = Blocks.CRIMSON_NYLIUM.defaultBlockState();
            fillerBlock = Blocks.NETHERRACK.defaultBlockState();
            mainSelector = NETHERRACK_SELECTOR;

            selector_0 = NETHERRACK_SELECTOR;
            selector_1 = NETHERRACK_SELECTOR;
        }
        if (pieceType.equals(StructureRegistry.WARPED_ISLAND.pieces()[0])) {
            topBlock = Blocks.WARPED_NYLIUM.defaultBlockState();
            fillerBlock = Blocks.NETHERRACK.defaultBlockState();
            mainSelector = NETHERRACK_SELECTOR;

            selector_0 = NETHERRACK_SELECTOR;
            selector_1 = NETHERRACK_SELECTOR;
        }
        if (pieceType.equals(StructureRegistry.LUSH_ISLAND.pieces()[0])) {
            topBlock = Blocks.MOSS_BLOCK.defaultBlockState();
            fillerBlock = Blocks.MOSS_BLOCK.defaultBlockState();
            mainSelector = LUSH_SELECTOR;

            selector_0 = LUSH_CAVE_SELECTOR_0;
            selector_1 = LUSH_CAVE_SELECTOR_1;
        }
        if (pieceType.equals(StructureRegistry.DRIPSTONE_ISLAND.pieces()[0])) {
            fillerBlock = null;
            fillerSelector = DRIPSTONE_SELECTOR;

            mainSelector = DRIPSTONE_SELECTOR;
            selector_0 = DRIPSTONE_SELECTOR;
            selector_1 = DRIPSTONE_SELECTOR;
        }

        if (pieceType.equals(StructureRegistry.AMETHYST_ISLAND.pieces()[0])) {
            topBlock = Blocks.AMETHYST_BLOCK.defaultBlockState();
            fillerBlock = Blocks.AMETHYST_BLOCK.defaultBlockState();

            mainSelector = AMETHYST_SELECTOR;
            selector_0 = AMETHYST_SELECTOR_0;
            selector_1 = AMETHYST_SELECTOR_1;
        }

        if (pieceType.equals(StructureRegistry.END_ISLAND.pieces()[0])) {
            fillerBlock = null;
            fillerSelector = END_SELECTOR;

            mainSelector = END_SELECTOR;
            selector_0 = END_SELECTOR;
            selector_1 = END_SELECTOR;
        }

        //y = 0
        this.generateHorizontalLine(level, boundingBox, 4, 5, 0, 3, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 3, 7, 0, 4, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 4, 8, 0, 5, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 4, 7, 0, 6, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 5, 6, 0, 7, mainSelector);

        //y = 1
        this.generateHorizontalLine(level, boundingBox, 5, 7, 1, 1, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 3, 8, 1, 2, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 2, 9, 1, 3, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 2, 9, 1, 4, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 2, 10, 1, 5, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 2, 9, 1, 6, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 3, 9, 1, 7, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 3, 8, 1, 8, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 4, 6, 1, 9, mainSelector);

        //y = 2
        this.generateHorizontalLine(level, boundingBox, 5, 6, 2, 0, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 3, 8, 2, 1, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 2, 9, 2, 2, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 1, 9, 2, 3, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 1, 10, 2, 4, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 1, 10, 2, 5, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 1, 10, 2, 6, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 2, 10, 2, 7, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 2, 9, 2, 8, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 3, 7, 2, 9, mainSelector);
        this.generateHorizontalLine(level, boundingBox, 4, 5, 2, 10, mainSelector);

        //y = 3
        this.generateHorizontalLine(level, boundingBox, 4, 7, 3, 0, selector_0);
        this.generateHorizontalLine(level, boundingBox, 2, 8, 3, 1, selector_0);
        this.generateHorizontalLine(level, boundingBox, 1, 9, 3, 2, selector_0);
        this.generateHorizontalLine(level, boundingBox, 0, 10, 3, 3, selector_0);
        this.generateHorizontalLine(level, boundingBox, 0, 10, 3, 4, selector_0);
        this.generateHorizontalLine(level, boundingBox, 0, 10, 3, 5, selector_0);
        this.generateHorizontalLine(level, boundingBox, 1, 10, 3, 6, selector_0);
        this.generateHorizontalLine(level, boundingBox, 1, 10, 3, 7, selector_0);
        this.generateHorizontalLine(level, boundingBox, 2, 9, 3, 8, selector_0);
        this.generateHorizontalLine(level, boundingBox, 2, 8, 3, 9, selector_0);
        this.generateHorizontalLine(level, boundingBox, 3, 6, 3, 10, selector_0);


        //y = 4
        this.generateHorizontalLine(level, boundingBox, 4, 7, 4, 0, selector_1);
        this.generateHorizontalLine(level, boundingBox, 2, 8, 4, 1, selector_1);
        this.generateHorizontalLine(level, boundingBox, 1, 9, 4, 2, selector_1);
        this.generateHorizontalLine(level, boundingBox, 0, 10, 4, 3, selector_1);
        this.generateHorizontalLine(level, boundingBox, 0, 10, 4, 4, selector_1);
        this.generateHorizontalLine(level, boundingBox, 0, 10, 4, 5, selector_1);
        this.generateHorizontalLine(level, boundingBox, 1, 10, 4, 6, selector_1);
        this.generateHorizontalLine(level, boundingBox, 1, 10, 4, 7, selector_1);
        this.generateHorizontalLine(level, boundingBox, 2, 9, 4, 8, selector_1);
        this.generateHorizontalLine(level, boundingBox, 2, 8, 4, 9, selector_1);
        this.generateHorizontalLine(level, boundingBox, 3, 5, 4, 10, selector_1);


        if (fillerBlock != null) {
            //y = 5
            this.generateHorizontalLine(level, boundingBox, 4, 6, 5, 0, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 2, 8, 5, 1, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 1, 8, 5, 2, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 1, 9, 5, 3, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 0, 10, 5, 4, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 0, 10, 5, 5, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 1, 10, 5, 6, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 2, 9, 5, 7, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 2, 9, 5, 8, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 2, 8, 5, 9, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 3, 4, 5, 10, fillerBlock);


            //y = 6
            this.generateHorizontalLine(level, boundingBox, 3, 7, 6, 1, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 2, 8, 6, 2, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 2, 8, 6, 3, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 1, 9, 6, 4, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 1, 9, 6, 5, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 1, 9, 6, 6, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 2, 9, 6, 7, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 2, 8, 6, 8, fillerBlock);
            this.generateHorizontalLine(level, boundingBox, 3, 6, 6, 9, fillerBlock);
        }
        if (fillerBlock == null) {
            //y = 5
            this.generateHorizontalLine(level, boundingBox, 4, 6, 5, 0, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 2, 8, 5, 1, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 1, 8, 5, 2, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 1, 9, 5, 3, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 0, 10, 5, 4, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 0, 10, 5, 5, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 1, 10, 5, 6, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 2, 9, 5, 7, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 2, 9, 5, 8, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 2, 8, 5, 9, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 3, 4, 5, 10, fillerSelector);


            //y = 6
            this.generateHorizontalLine(level, boundingBox, 3, 7, 6, 1, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 2, 8, 6, 2, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 2, 8, 6, 3, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 1, 9, 6, 4, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 1, 9, 6, 5, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 1, 9, 6, 6, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 2, 9, 6, 7, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 2, 8, 6, 8, fillerSelector);
            this.generateHorizontalLine(level, boundingBox, 3, 6, 6, 9, fillerSelector);
        }

        this.topBlocks.clear();
        this.bottomBlocks.clear();

        for (int x = 0; x < 11; x++) {
            for (int z = 0; z < 11; z++) {
                for (int y = 6; y > 0; y--) {

                    if (this.getBlock(level, x, y + 1, z, boundingBox).isAir() && !this.getBlock(level, x, y, z, boundingBox).isAir()) {
                        if (fillerBlock != null && this.getBlock(level, x, y, z, boundingBox).is(fillerBlock.getBlock()))
                            this.placeBlock(level, topBlock, x, y, z, boundingBox);
                        this.topBlocks.add(this.getWorldPos(x, y, z));
                    }

                }
            }
        }

        for (int x = 0; x < 11; x++) {
            for (int z = 0; z < 11; z++) {
                for (int y = 0; y < 6; y++) {
                    if (!this.getBlock(level, x, y, z, boundingBox).isAir() && this.getBlock(level, x, y - 1, z, boundingBox).isAir())
                        this.bottomBlocks.add(this.getWorldPos(x, y, z));

                }
            }
        }
    }

    public List<BlockPos> getTopBlocks() {
        return this.topBlocks;
    }

    public List<BlockPos> getBottomBlocks() {
        return this.bottomBlocks;
    }

    private void generateHorizontalLine(WorldGenLevel level, BoundingBox box, int xStart, int xEnd, int y, int z, BlockState state) {
        this.generateBox(level, box, xStart, y, z, xEnd, y, z, state, state, false);
    }

    private void generateHorizontalLine(WorldGenLevel level, BoundingBox box, int xStart, int xEnd, int y, int z, BlockSelector blockSelector) {
        this.generateBox(level, box, xStart, y, z, xEnd, y, z, false, level.getRandom(), blockSelector);
    }


    static class IslandNetherrackSelector extends BlockSelector {

        @Override
        public void next(RandomSource random, int x, int y, int z, boolean replaceAll) {

            float f = random.nextFloat();

            if (f < 0.65F)
                this.next = Blocks.NETHERRACK.defaultBlockState();
            else if (f < 0.9F)
                this.next = Blocks.MAGMA_BLOCK.defaultBlockState();
            else
                this.next = Blocks.SOUL_SAND.defaultBlockState();
        }
    }

    static class IslandStoneSelector extends BlockSelector {


        private final float topBlockChance;
        private final BlockState topBlock;

        private IslandStoneSelector(float topBlockChance, BlockState topBlock) {
            this.topBlockChance = topBlockChance;
            this.topBlock = topBlock;
        }

        @Override
        public void next(RandomSource random, int x, int y, int z, boolean replaceAll) {

            float f = random.nextFloat();

            if (f < 0.65F * (1.0F - this.topBlockChance))
                this.next = Blocks.STONE.defaultBlockState();
            else if (f < 0.9F * (1.0F - this.topBlockChance))
                this.next = Blocks.COBBLESTONE.defaultBlockState();
            else if (f < (1.0F - this.topBlockChance))
                this.next = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
            else
                this.next = this.topBlock;
        }
    }

    public static class IslandLushSelector extends BlockSelector {

        private final float topBlockChance;
        private final BlockState topBlock;

        private IslandLushSelector(float topBlockChance, BlockState topBlock) {
            this.topBlockChance = topBlockChance;
            this.topBlock = topBlock;
        }

        @Override
        public void next(RandomSource random, int x, int y, int z, boolean replaceAll) {

            float f = random.nextFloat();

            if (f < 0.6F * (1.0F - this.topBlockChance))
                this.next = Blocks.STONE.defaultBlockState();
            else if (f < 0.7F * (1.0F - this.topBlockChance))
                this.next = Blocks.COBBLESTONE.defaultBlockState();
            else if (f < (1.0F - this.topBlockChance))
                this.next = Blocks.CLAY.defaultBlockState();
            else
                this.next = this.topBlock;
        }

    }

    public static class IslandAmethystSelector extends BlockSelector {

        private final float amethystChance;

        private IslandAmethystSelector(float amethystChance) {
            this.amethystChance = amethystChance;
        }

        @Override
        public void next(RandomSource random, int x, int y, int z, boolean replaceAll) {

            float f = random.nextFloat();

            if (f < amethystChance)
                this.next = Blocks.AMETHYST_BLOCK.defaultBlockState();
            else
                this.next = Blocks.CALCITE.defaultBlockState();
        }

    }

    public static class IslandEndSelector extends BlockSelector {

        private IslandEndSelector() {
        }

        @Override
        public void next(RandomSource random, int x, int y, int z, boolean replaceAll) {

            float f = random.nextFloat();

            if (f < 0.06F)
                this.next = BlockRegistry.END_DIAMOND_ORE.get().defaultBlockState();
            else if (f < 0.09F)
                this.next = BlockRegistry.END_NETHERITE_ORE.get().defaultBlockState();
            else
                this.next = Blocks.END_STONE.defaultBlockState();
        }

    }


    public static class Desert extends NatureIslandPiece {

        public Desert(RandomSource source, int x, int y) {
            super(StructureRegistry.DESERT_ISLAND.pieces()[0], source, x, y);
        }

        public Desert(CompoundTag tag) {
            super(StructureRegistry.DESERT_ISLAND.pieces()[0], tag);
        }


    }

    public static class Savanna extends NatureIslandPiece {

        public Savanna(RandomSource source, int x, int y) {
            super(StructureRegistry.SAVANNA_ISLAND.pieces()[0], source, x, y);
        }

        public Savanna(CompoundTag tag) {
            super(StructureRegistry.SAVANNA_ISLAND.pieces()[0], tag);
        }


    }

    public static class Jungle extends NatureIslandPiece {

        public Jungle(RandomSource source, int x, int y) {
            super(StructureRegistry.JUNGLE_ISLAND.pieces()[0], source, x, y);
        }

        public Jungle(CompoundTag tag) {
            super(StructureRegistry.JUNGLE_ISLAND.pieces()[0], tag);
        }


    }

    public static class Spruce extends NatureIslandPiece {

        public Spruce(RandomSource source, int x, int y) {
            super(StructureRegistry.SPRUCE_ISLAND.pieces()[0], source, x, y);
        }

        public Spruce(CompoundTag tag) {
            super(StructureRegistry.SPRUCE_ISLAND.pieces()[0], tag);
        }


    }


    public static class Oak extends NatureIslandPiece {

        public Oak(RandomSource source, int x, int y) {
            super(StructureRegistry.OAK_ISLAND.pieces()[0], source, x, y);
        }

        public Oak(CompoundTag tag) {
            super(StructureRegistry.OAK_ISLAND.pieces()[0], tag);
        }


    }

    public static class Birch extends NatureIslandPiece {

        public Birch(RandomSource source, int x, int y) {
            super(StructureRegistry.BIRCH_ISLAND.pieces()[0], source, x, y);
        }

        public Birch(CompoundTag tag) {
            super(StructureRegistry.BIRCH_ISLAND.pieces()[0], tag);
        }


    }

    public static class Lush extends NatureIslandPiece {

        public Lush(RandomSource source, int x, int y) {
            super(StructureRegistry.LUSH_ISLAND.pieces()[0], source, x, y);
        }

        public Lush(CompoundTag tag) {
            super(StructureRegistry.LUSH_ISLAND.pieces()[0], tag);
        }


    }

    public static class Crimson extends NatureIslandPiece {

        public Crimson(RandomSource source, int x, int y) {
            super(StructureRegistry.CRIMSON_ISLAND.pieces()[0], source, x, y);
        }

        public Crimson(CompoundTag tag) {
            super(StructureRegistry.CRIMSON_ISLAND.pieces()[0], tag);
        }


    }

    public static class Warped extends NatureIslandPiece {

        public Warped(RandomSource source, int x, int y) {
            super(StructureRegistry.WARPED_ISLAND.pieces()[0], source, x, y);
        }

        public Warped(CompoundTag tag) {
            super(StructureRegistry.WARPED_ISLAND.pieces()[0], tag);
        }


    }

    public static class Dripstone extends NatureIslandPiece {

        public Dripstone(RandomSource source, int x, int y) {
            super(StructureRegistry.DRIPSTONE_ISLAND.pieces()[0], source, x, y);
        }

        public Dripstone(CompoundTag tag) {
            super(StructureRegistry.DRIPSTONE_ISLAND.pieces()[0], tag);
        }
    }

    public static class Amethyst extends NatureIslandPiece {

        public Amethyst(RandomSource source, int x, int y) {
            super(StructureRegistry.AMETHYST_ISLAND.pieces()[0], source, x, y);
        }

        public Amethyst(CompoundTag tag) {
            super(StructureRegistry.AMETHYST_ISLAND.pieces()[0], tag);
        }
    }

    public static class End extends NatureIslandPiece {

        public End(RandomSource source, int x, int y) {
            super(StructureRegistry.END_ISLAND.pieces()[0], source, x, y);
        }

        public End(CompoundTag tag) {
            super(StructureRegistry.END_ISLAND.pieces()[0], tag);
        }
    }
}
