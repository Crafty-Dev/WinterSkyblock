package de.crafty.winterskyblock;


import de.crafty.winterskyblock.blockentities.renderer.*;
import de.crafty.winterskyblock.fastdecay.FastDecay;
import de.crafty.winterskyblock.fastgrowth.FastGrowth;
import de.crafty.winterskyblock.handler.*;
import de.crafty.winterskyblock.item.HammerItem;
import de.crafty.winterskyblock.network.SkyblockNetworkManager;
import de.crafty.winterskyblock.registry.*;
import de.crafty.winterskyblock.util.ModCompostables;
import de.crafty.winterskyblock.world.SpawnManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.server.dedicated.DedicatedServerSettings;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import java.nio.file.Path;
import java.nio.file.Paths;

@Mod(WinterSkyblock.MODID)
public class WinterSkyblock {

    public static final String MODID = "winterskyblock";


    public int islandCount;
    private static WinterSkyblock instance;
    private final DedicatedServerSettings serverSettings;

    public WinterSkyblock() {

        instance = this;
        this.islandCount = 1;

        Path path = Paths.get("server.properties");
        this.serverSettings = new DedicatedServerSettings(path);
        this.serverSettings.getProperties().get("islandCount", 10);
        this.serverSettings.forceSave();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegistry);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerBlockEntityRenderers);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(EntityRegistry::registerAttributes);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EntityRegistry::registerRenderers);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EntityRegistry::registerLayers);


        MinecraftForge.EVENT_BUS.register(new SpawnManager());
        MinecraftForge.EVENT_BUS.register(new FastGrowth());
        MinecraftForge.EVENT_BUS.register(new FastDecay());
        MinecraftForge.EVENT_BUS.register(new LeavesDropHandler());
        MinecraftForge.EVENT_BUS.register(new HammerDropHandler());
        MinecraftForge.EVENT_BUS.register(new MeltingCobbleHandler());
        MinecraftForge.EVENT_BUS.register(new GraveStoneHandler());
        MinecraftForge.EVENT_BUS.register(new LavaDropHandler());
        MinecraftForge.EVENT_BUS.register(new BlockTransformationHandler());
        MinecraftForge.EVENT_BUS.register(new BlockBreakHandler());

        SkyblockNetworkManager.registerPackets();


        ItemRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        EntityRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());

        BlockEntityRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParticleRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
        SoundRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());

        HammerItem.bootstrap();
        LavaDropHandler.bootstrap();
        BlockTransformationHandler.bootstrap();


        ModCompostables.bootstrap();


    }

    public static boolean checkMonsterSpawnRules(EntityType<? extends Monster> monster, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource randomSource) {
        return level.getDifficulty() != Difficulty.PEACEFUL && (Monster.isDarkEnoughToSpawn(level, pos, randomSource) || (level.getBiome(pos).is(BiomeTags.IS_END) && monster.equals(EntityType.ENDERMAN) && level.getLightEmission(pos) <= 10)) && Monster.checkMobSpawnRules(monster, level, spawnType, pos, randomSource);
    }


    private void onRegistry(RegisterEvent event) {

        if (!event.getRegistryKey().equals(Registry.STRUCTURE_REGISTRY))
            return;

        StructureRegistry.DESERT_ISLAND.register();
        StructureRegistry.SAVANNA_ISLAND.register();
        StructureRegistry.JUNGLE_ISLAND.register();
        StructureRegistry.SPRUCE_ISLAND.register();
        StructureRegistry.OAK_ISLAND.register();
        StructureRegistry.BIRCH_ISLAND.register();
        StructureRegistry.LUSH_ISLAND.register();
        StructureRegistry.CRIMSON_ISLAND.register();
        StructureRegistry.WARPED_ISLAND.register();
        StructureRegistry.DRIPSTONE_ISLAND.register();
        StructureRegistry.AMETHYST_ISLAND.register();
        StructureRegistry.END_ISLAND.register();

    }

    private void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityRegistry.LEAF_PRESS.get(), LeafPressRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.MELTING_COBBLESTONE.get(), MeltingCobblestoneRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.GRAVE_STONE.get(), GraveStoneRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.END_PORTAL_CORE.get(), EndPortalCoreRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityRegistry.MAGICAL_WORKBENCH.get(), MagicalWorkbenchRenderer::new);
    }

    public static WinterSkyblock instance() {
        return instance;
    }

    public int getIslandCountOnServers() {
        return this.serverSettings.getProperties().get("islandCount", 10);
    }

    public int getIslandCountOnClient() {
        return this.islandCount;
    }
}
