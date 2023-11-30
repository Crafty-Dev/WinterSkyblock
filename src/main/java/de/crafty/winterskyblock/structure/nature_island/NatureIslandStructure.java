package de.crafty.winterskyblock.structure.nature_island;

import com.mojang.serialization.Codec;
import de.crafty.winterskyblock.registry.StructureRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.data.worldgen.features.NetherFeatures;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.*;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class NatureIslandStructure extends Structure {


    private NatureIslandStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public @NotNull Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();

        return Optional.of(new GenerationStub(new BlockPos(x, 100, z), structurePiecesBuilder -> structurePiecesBuilder.addPiece(this.createPiece(structurePiecesBuilder, context))));
    }


    protected abstract NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, Structure.GenerationContext context);


    @Override
    public void afterPlace(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource rand, BoundingBox boundingBox, ChunkPos pos, PiecesContainer piecesContainer) {
        BlockPos location = boundingBox.getCenter();

        NatureIslandPiece piece = (NatureIslandPiece) piecesContainer.pieces().get(0);
        Holder<Biome> biome = level.getBiome(location);

        boolean isDesert = piece.getType().equals(StructureRegistry.DESERT_ISLAND.pieces()[0]);
        boolean isJungle = piece.getType().equals(StructureRegistry.JUNGLE_ISLAND.pieces()[0]);


        int maxEndPosts = 3;
        AtomicInteger endPosts = new AtomicInteger();

        piece.getTopBlocks().forEach(blockPos -> {

            //Snow
            if (biome.value().coldEnoughToSnow(location) && !piece.getType().equals(StructureRegistry.LUSH_ISLAND.pieces()[0]) && rand.nextFloat() <= 0.5F)
                level.setBlock(blockPos.above(), Blocks.SNOW.defaultBlockState(), 3);


            //Desert Features
            if (isDesert && level.getRandom().nextFloat() < 0.025F && canPlaceCactus(level, blockPos.above())) {
                for (int i = 0; i <= rand.nextInt(3); i++) {
                    level.setBlock(blockPos.above().offset(0, i, 0), Blocks.CACTUS.defaultBlockState(), 3);
                }
                return;
            }

            if (isDesert && level.getRandom().nextFloat() < 0.2F)
                level.setBlock(blockPos.above(), Blocks.DEAD_BUSH.defaultBlockState(), 3);

            if (isDesert)
                return;


            //Bamboo Placement
            if (isJungle && rand.nextFloat() <= 0.1F) {

                int height = 1 + rand.nextInt(6);
                for (int i = 1; i <= height; i++) {
                    BlockPos p = new BlockPos(blockPos.getX(), blockPos.getY() + i, blockPos.getZ());
                    if (!(level.getBlockState(p).isAir() || level.getBlockState(p).is(Blocks.SNOW)))
                        continue;

                    if (height == 1) {
                        level.setBlock(p, Blocks.BAMBOO_SAPLING.defaultBlockState(), 3);
                        break;
                    }

                    if (i > 1 && !level.getBlockState(p.below()).is(Blocks.BAMBOO))
                        continue;

                    if ((i == height && height >= 4) || (i == height - 1 && height > 4))
                        level.setBlock(p, Blocks.BAMBOO.defaultBlockState().setValue(BambooBlock.LEAVES, BambooLeaves.LARGE).setValue(BambooBlock.AGE, height >= 4 ? 1 : 0), 3);
                    else if ((i == height && height < 4) || (i == height - 1 && height <= 4) || (i == height - 2 && height >= 4))
                        level.setBlock(p, Blocks.BAMBOO.defaultBlockState().setValue(BambooBlock.LEAVES, BambooLeaves.SMALL).setValue(BambooBlock.AGE, height >= 4 ? 1 : 0), 3);
                    else
                        level.setBlock(p, Blocks.BAMBOO.defaultBlockState().setValue(BambooBlock.AGE, height >= 4 ? 1 : 0), 3);
                }
                return;
            }


            if (canTreeGenerate(level, blockPos.above())) {
                //Tree Definition
                Holder<ConfiguredFeature<TreeConfiguration, ?>> tree = null;
                Holder<ConfiguredFeature<HugeFungusConfiguration, ?>> fungus = null;

                if (piece.getType().equals(StructureRegistry.BIRCH_ISLAND.pieces()[0]))
                    tree = TreeFeatures.BIRCH;
                else if (piece.getType().equals(StructureRegistry.SPRUCE_ISLAND.pieces()[0]))
                    tree = TreeFeatures.SPRUCE;
                else if (piece.getType().equals(StructureRegistry.JUNGLE_ISLAND.pieces()[0]))
                    tree = TreeFeatures.JUNGLE_TREE;
                else if (piece.getType().equals(StructureRegistry.SAVANNA_ISLAND.pieces()[0]))
                    tree = TreeFeatures.ACACIA;
                else if (piece.getType().equals(StructureRegistry.LUSH_ISLAND.pieces()[0]))
                    tree = TreeFeatures.AZALEA_TREE;
                else if (piece.getType().equals(StructureRegistry.OAK_ISLAND.pieces()[0]))
                    tree = TreeFeatures.OAK;
                else if (piece.getType().equals(StructureRegistry.CRIMSON_ISLAND.pieces()[0]))
                    fungus = TreeFeatures.CRIMSON_FUNGUS;
                else if (piece.getType().equals(StructureRegistry.WARPED_ISLAND.pieces()[0]))
                    fungus = TreeFeatures.WARPED_FUNGUS;


                //Tree Placement
                if ((tree != null || fungus != null) && level.getRandom().nextFloat() < 0.025F) {
                    if (tree != null)
                        tree.value().place(level, level.getLevel().getChunkSource().getGenerator(), level.getRandom(), blockPos.above());
                    else
                        fungus.value().place(level, level.getLevel().getChunkSource().getGenerator(), level.getRandom(), blockPos.above());
                }
            }

            //Decoration Placement
            if (rand.nextFloat() < 0.75F && level.getBlockState(blockPos).getBlock() instanceof GrassBlock)
                this.decorateGrass(level, blockPos, rand);

            if (rand.nextFloat() < 0.15F && level.getBlockState(blockPos).getBlock() instanceof NyliumBlock)
                this.decorateNylium(level, blockPos, rand);

            if (rand.nextFloat() < 0.15F && level.getBlockState(blockPos).getBlock() instanceof MossBlock)
                this.decorateMoss(level, blockPos, rand);


            //Dripstone
            if (piece.getType().equals(StructureRegistry.DRIPSTONE_ISLAND.pieces()[0])) {

                if (rand.nextFloat() < 0.2F && canPlaceLiquid(level, blockPos)) {
                    level.setBlock(blockPos, Blocks.LAVA.defaultBlockState(), 3);
                    return;
                }

                if (level.getBlockState(blockPos).is(Blocks.DRIPSTONE_BLOCK) && rand.nextFloat() < 0.35F) {
                    int height = rand.nextInt(4);
                    for (int i = 1; i <= height; i++) {
                        level.setBlock(blockPos.offset(0, i, 0), Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(PointedDripstoneBlock.THICKNESS, i == height ? DripstoneThickness.TIP : i == height - 1 ? DripstoneThickness.FRUSTUM : i == 1 ? DripstoneThickness.BASE : DripstoneThickness.MIDDLE), 3);
                    }
                }

            }

            //Amethyst
            if (piece.getType().equals(StructureRegistry.AMETHYST_ISLAND.pieces()[0])) {

                if (rand.nextFloat() < 0.2F && level.getBlockState(blockPos).is(Blocks.AMETHYST_BLOCK))
                    level.setBlock(blockPos.above(), Blocks.AMETHYST_CLUSTER.defaultBlockState(), 3);

            }

            //End
            if (piece.getType().equals(StructureRegistry.END_ISLAND.pieces()[0])) {

                if (rand.nextFloat() < 0.05F && !this.isBlockNearby(level, blockPos.above(), false)) {
                    Feature.CHORUS_PLANT.place(FeatureConfiguration.NONE, level, level.getLevel().getChunkSource().getGenerator(), rand, blockPos.above());
                    return;
                }


                if (rand.nextFloat() < 0.05F && !this.isBlockNearby(level, blockPos.above(), true) && endPosts.get() != maxEndPosts) {
                    this.genEndPost(level, blockPos.above(), rand);
                    endPosts.getAndIncrement();
                }
            }

        });

        piece.getBottomBlocks().forEach(blockPos -> {

            if (piece.getType().equals(StructureRegistry.DRIPSTONE_ISLAND.pieces()[0])) {

                if (level.getBlockState(blockPos).is(Blocks.DRIPSTONE_BLOCK) && rand.nextFloat() < 0.35F) {
                    int height = rand.nextInt(5);
                    for (int i = 1; i <= height; i++) {
                        BlockPos p = new BlockPos(blockPos.getX(), blockPos.getY() - i, blockPos.getZ());
                        if (level.getBlockState(p).isAir())
                            level.setBlock(p, Blocks.POINTED_DRIPSTONE.defaultBlockState().setValue(PointedDripstoneBlock.TIP_DIRECTION, Direction.DOWN).setValue(PointedDripstoneBlock.THICKNESS, i == height ? DripstoneThickness.TIP : i == height - 1 ? DripstoneThickness.FRUSTUM : i == 1 ? DripstoneThickness.BASE : DripstoneThickness.MIDDLE), 3);

                    }
                }
            }

            if (piece.getType().equals(StructureRegistry.AMETHYST_ISLAND.pieces()[0])) {
                level.setBlock(blockPos, Blocks.BASALT.defaultBlockState(), 3);
            }

        });


        if (piece.getType().equals(StructureRegistry.END_ISLAND.pieces()[0])) {
            if (rand.nextFloat() < 0.1F)
                Feature.END_GATEWAY.place(EndGatewayConfiguration.delayedExitSearch(), level, level.getLevel().getChunkSource().getGenerator(), rand, new BlockPos(rand.nextBoolean() ? (boundingBox.minX() + 1) : (boundingBox.maxX() - 1), location.getY() + 15, rand.nextBoolean() ? (boundingBox.minZ() + 1) : (boundingBox.maxZ() - 1)));

        }


        //Animals
        MobSpawnSettings spawnSettings = biome.get().getMobSettings();
        WeightedRandomList<MobSpawnSettings.SpawnerData> randList = spawnSettings.getMobs(MobCategory.CREATURE);

        if (randList.isEmpty() || piece.getTopBlocks().isEmpty())
            return;

        Optional<MobSpawnSettings.SpawnerData> optional = randList.getRandom(rand);
        if (optional.isEmpty())
            return;

        MobSpawnSettings.SpawnerData spawnerData = optional.get();
        int min = 1;
        int max = Mth.clamp(spawnerData.maxCount, min, 3);

        int amount = min + rand.nextInt(1 + max - min);

        SpawnGroupData spawngroupdata = null;

        for (int i = 0; i < amount; i++) {

            boolean success = false;

            for (int j = 0; !success && j < 4; j++) {

                BlockPos spawnPos = piece.getTopBlocks().get(rand.nextInt(piece.getTopBlocks().size())).above();
                if (spawnerData.type.canSummon() && NaturalSpawner.isSpawnPositionOk(SpawnPlacements.getPlacementType(spawnerData.type), level, spawnPos, spawnerData.type)) {
                    float f = spawnerData.type.getWidth();
                    if (!level.noCollision(spawnerData.type.getAABB(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ())) || !SpawnPlacements.checkSpawnRules(spawnerData.type, level, MobSpawnType.STRUCTURE, spawnPos, level.getRandom())) {
                        continue;
                    }


                    Entity entity = spawnerData.type.create(level.getLevel());

                    if(entity == null)
                        continue;

                     entity.moveTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), rand.nextFloat() * 360.0F, 0.0F);
                    if (entity instanceof Mob) {
                        Mob mob = (Mob)entity;
                        if (net.minecraftforge.common.ForgeHooks.canEntitySpawn(mob, level, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), null, MobSpawnType.STRUCTURE) == -1) continue;
                        if (mob.checkSpawnRules(level, MobSpawnType.STRUCTURE) && mob.checkSpawnObstruction(level)) {
                            spawngroupdata = mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.STRUCTURE, spawngroupdata, (CompoundTag)null);
                            level.addFreshEntityWithPassengers(mob);
                            success = true;
                        }
                    }
                }

            }
        }


    }


    private void genEndPost(WorldGenLevel level, BlockPos pos, RandomSource rand) {

        level.setBlock(pos, Blocks.STONE_BRICK_WALL.defaultBlockState(), 3);
        level.setBlock(pos.above(), Blocks.SOUL_LANTERN.defaultBlockState(), 3);

        for (int xOff = -1; xOff <= 1; xOff++) {
            for (int zOff = -1; zOff <= 1; zOff++) {

                for (int yOff = -2; yOff < 0; yOff++) {
                    BlockPos p = pos.offset(xOff, yOff, zOff);

                    if (rand.nextFloat() < 0.5F && !level.getBlockState(p).isAir())
                        level.setBlock(p, rand.nextBoolean() ? Blocks.SOUL_SAND.defaultBlockState() : Blocks.STONE_BRICKS.defaultBlockState(), 3);
                }

            }
        }

    }

    private void decorateGrass(WorldGenLevel level, BlockPos pos, RandomSource rand) {

        BlockState state = level.getBlockState(pos.above());

        if (state.isAir()) {
            Holder<PlacedFeature> holder;
            if (rand.nextInt(8) == 0) {
                List<ConfiguredFeature<?, ?>> list = level.getBiome(pos).value().getGenerationSettings().getFlowerFeatures();
                if (list.isEmpty()) {
                    return;
                }

                holder = ((RandomPatchConfiguration) list.get(0).config()).feature();
            } else {
                holder = VegetationPlacements.GRASS_BONEMEAL;
            }

            holder.value().place(level, level.getLevel().getChunkSource().getGenerator(), rand, pos.above());
        }

    }

    private void decorateNylium(WorldGenLevel level, BlockPos pos, RandomSource rand) {
        BlockState blockstate = level.getBlockState(pos);
        BlockPos blockpos = pos.above();
        ChunkGenerator chunkgenerator = level.getLevel().getChunkSource().getGenerator();
        if (blockstate.is(Blocks.CRIMSON_NYLIUM)) {
            NetherFeatures.CRIMSON_FOREST_VEGETATION_BONEMEAL.value().place(level, chunkgenerator, rand, blockpos);
        } else if (blockstate.is(Blocks.WARPED_NYLIUM)) {
            NetherFeatures.WARPED_FOREST_VEGETATION_BONEMEAL.value().place(level, chunkgenerator, rand, blockpos);
            NetherFeatures.NETHER_SPROUTS_BONEMEAL.value().place(level, chunkgenerator, rand, blockpos);
            if (rand.nextInt(8) == 0) {
                NetherFeatures.TWISTING_VINES_BONEMEAL.value().place(level, chunkgenerator, rand, blockpos);
            }
        }
    }

    private void decorateMoss(WorldGenLevel level, BlockPos pos, RandomSource rand) {
        CaveFeatures.MOSS_PATCH_BONEMEAL.value().place(level, level.getLevel().getChunkSource().getGenerator(), rand, pos.above());
    }

    private boolean canPlaceCactus(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos.north()).isAir() && level.getBlockState(pos.east()).isAir() && level.getBlockState(pos.south()).isAir() && level.getBlockState(pos.west()).isAir();
    }

    private boolean isBlockNearby(WorldGenLevel level, BlockPos pos, boolean checkDiagonals) {
        if (checkDiagonals) {
            for (int xOff = -1; xOff <= 1; xOff++) {
                for (int zOff = -1; zOff <= 1; zOff++) {
                    if (!level.getBlockState(pos.offset(xOff, 0, zOff)).isAir())
                        return true;
                }
            }
            return false;
        }

        return !(level.getBlockState(pos.north()).isAir() && level.getBlockState(pos.east()).isAir() && level.getBlockState(pos.south()).isAir() && level.getBlockState(pos.west()).isAir());
    }

    private boolean canTreeGenerate(WorldGenLevel level, BlockPos pos) {
        for (int xOff = -1; xOff <= 1; xOff++) {
            for (int zOff = -1; zOff <= 1; zOff++) {
                if (level.getBlockState(pos.offset(xOff, 0, zOff)).is(BlockTags.LOGS))
                    return false;
            }
        }
        return true;
    }

    private boolean canPlaceLiquid(WorldGenLevel level, BlockPos pos) {
        return !level.getBlockState(pos.north()).isAir() && !level.getBlockState(pos.east()).isAir() && !level.getBlockState(pos.south()).isAir() && !level.getBlockState(pos.west()).isAir();
    }


    public static class Desert extends NatureIslandStructure {


        public static final Codec<Desert> CODEC = simpleCodec(Desert::new);

        public Desert(StructureSettings settings) {
            super(settings);
        }

        @Override
        protected NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, GenerationContext context) {
            return new NatureIslandPiece.Desert(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
        }

        @Override
        public @NotNull StructureType<?> type() {
            return StructureRegistry.DESERT_ISLAND.structureType();
        }


    }

    public static class Savanna extends NatureIslandStructure {


        public static final Codec<Savanna> CODEC = simpleCodec(Savanna::new);

        public Savanna(StructureSettings settings) {
            super(settings);
        }

        @Override
        protected NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, GenerationContext context) {
            return new NatureIslandPiece.Savanna(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
        }

        @Override
        public @NotNull StructureType<?> type() {
            return StructureRegistry.SAVANNA_ISLAND.structureType();
        }


    }

    public static class Jungle extends NatureIslandStructure {


        public static final Codec<Jungle> CODEC = simpleCodec(Jungle::new);

        public Jungle(StructureSettings settings) {
            super(settings);
        }

        @Override
        protected NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, GenerationContext context) {
            return new NatureIslandPiece.Jungle(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
        }

        @Override
        public @NotNull StructureType<?> type() {
            return StructureRegistry.JUNGLE_ISLAND.structureType();
        }


    }

    public static class Spruce extends NatureIslandStructure {


        public static final Codec<Spruce> CODEC = simpleCodec(Spruce::new);

        public Spruce(StructureSettings settings) {
            super(settings);
        }

        @Override
        protected NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, GenerationContext context) {
            return new NatureIslandPiece.Spruce(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
        }

        @Override
        public @NotNull StructureType<?> type() {
            return StructureRegistry.SPRUCE_ISLAND.structureType();
        }


    }

    public static class Oak extends NatureIslandStructure {


        public static final Codec<Oak> CODEC = simpleCodec(Oak::new);

        public Oak(StructureSettings settings) {
            super(settings);
        }

        @Override
        protected NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, GenerationContext context) {
            return new NatureIslandPiece.Oak(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
        }

        @Override
        public @NotNull StructureType<?> type() {
            return StructureRegistry.OAK_ISLAND.structureType();
        }
    }

    public static class Birch extends NatureIslandStructure {


        public static final Codec<Birch> CODEC = simpleCodec(Birch::new);

        public Birch(StructureSettings settings) {
            super(settings);
        }

        @Override
        protected NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, GenerationContext context) {
            return new NatureIslandPiece.Birch(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
        }

        @Override
        public @NotNull StructureType<?> type() {
            return StructureRegistry.BIRCH_ISLAND.structureType();
        }
    }

    public static class Lush extends NatureIslandStructure {


        public static final Codec<Lush> CODEC = simpleCodec(Lush::new);

        public Lush(StructureSettings settings) {
            super(settings);
        }

        @Override
        protected NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, GenerationContext context) {
            return new NatureIslandPiece.Lush(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
        }

        @Override
        public @NotNull StructureType<?> type() {
            return StructureRegistry.LUSH_ISLAND.structureType();
        }
    }

    public static class Crimson extends NatureIslandStructure {


        public static final Codec<Crimson> CODEC = simpleCodec(Crimson::new);

        public Crimson(StructureSettings settings) {
            super(settings);
        }

        @Override
        protected NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, GenerationContext context) {
            return new NatureIslandPiece.Crimson(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
        }

        @Override
        public @NotNull StructureType<?> type() {
            return StructureRegistry.CRIMSON_ISLAND.structureType();
        }
    }

    public static class Warped extends NatureIslandStructure {


        public static final Codec<Warped> CODEC = simpleCodec(Warped::new);

        public Warped(StructureSettings settings) {
            super(settings);
        }

        @Override
        protected NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, GenerationContext context) {
            return new NatureIslandPiece.Warped(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
        }

        @Override
        public @NotNull StructureType<?> type() {
            return StructureRegistry.WARPED_ISLAND.structureType();
        }
    }

    public static class Dripstone extends NatureIslandStructure {

        public static final Codec<Dripstone> CODEC = simpleCodec(Dripstone::new);


        public Dripstone(StructureSettings settings) {
            super(settings);
        }

        @Override
        protected NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, GenerationContext context) {
            return new NatureIslandPiece.Dripstone(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
        }

        @Override
        public StructureType<?> type() {
            return StructureRegistry.DRIPSTONE_ISLAND.structureType();
        }
    }

    public static class Amethyst extends NatureIslandStructure {

        public static final Codec<Amethyst> CODEC = simpleCodec(Amethyst::new);


        public Amethyst(StructureSettings settings) {
            super(settings);
        }

        @Override
        protected NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, GenerationContext context) {
            return new NatureIslandPiece.Amethyst(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
        }

        @Override
        public StructureType<?> type() {
            return StructureRegistry.AMETHYST_ISLAND.structureType();
        }

    }

    public static class End extends NatureIslandStructure {

        public static final Codec<End> CODEC = simpleCodec(End::new);


        public End(StructureSettings settings) {
            super(settings);
        }

        @Override
        protected NatureIslandPiece createPiece(StructurePiecesBuilder structurePiecesBuilder, GenerationContext context) {
            return new NatureIslandPiece.End(context.random(), context.chunkPos().getMinBlockX(), context.chunkPos().getMinBlockZ());
        }

        @Override
        public StructureType<?> type() {
            return StructureRegistry.END_ISLAND.structureType();
        }

    }

}
