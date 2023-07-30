package de.crafty.winterskyblock.world;

import com.google.gson.JsonObject;
import com.mojang.math.Vector3f;
import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.util.VectorUtils;
import de.crafty.winterskyblock.world.generators.OverworldChunkGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.SpruceTreeGrower;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class SpawnManager {


    @SubscribeEvent
    public void onWorldCreate(LevelEvent.CreateSpawnPosition event) {

        ServerLevel level = event.getLevel().getServer().overworld();

        if (!(level.getChunkSource().getGenerator() instanceof OverworldChunkGenerator))
            return;


        //this.genIsland(level, 0, 63, 0);
        this.genIslands(level, level.getServer().isDedicatedServer() ? WinterSkyblock.instance().getIslandCountOnServers() : WinterSkyblock.instance().getIslandCountOnClient());
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinLevelEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player))
            return;

        if (player.getStats().getValue(Stats.CUSTOM, Stats.PLAY_TIME) == 0 && !event.getLevel().isClientSide())
            this.assignSpawn(player.getLevel(), player);
    }


    private void genIslands(ServerLevel level, int amount) {

        List<BlockPos> spawns = new ArrayList<>();

        double distance = 75.0F;
        double circumference = (amount - 1) * distance;
        double radius = circumference / (Math.PI * 2);
        double angle = 360.0D / amount;
        double radian = Math.toRadians(angle);

        for (int i = 0; i < amount; i++) {

            Vector3f vec = VectorUtils.rotateAroundY(new Vector3f(1, 0, 1), radian * i);
            vec.normalize();
            vec.mul((float) radius, (float) 0, (float) radius);

            spawns.add(this.genIsland(level, vec.x(), 63, vec.z()));
        }

        this.saveSpawns(spawns, level);
    }

    private BlockPos genIsland(ServerLevel world, double x, double y, double z) {

        BlockPos src = new BlockPos(x, y, z);

        world.setBlock(src, Blocks.GRASS_BLOCK.defaultBlockState(), 2);
        new SpruceTreeGrower().growTree(world, world.getChunkSource().getGenerator(), src.above(), Blocks.SPRUCE_SAPLING.defaultBlockState(), world.random);

        BlockPos top = src.above();
        while (!world.getBlockState(top).isAir())
            top = top.above();

        return top;

    }

    private void saveSpawns(List<BlockPos> spawns, ServerLevel level) {

        LinkedHashMap<BlockPos, UUID> map = new LinkedHashMap<>();
        spawns.forEach(spawn -> map.put(spawn, null));

        SpawnData spawnData = level.getDataStorage().computeIfAbsent(SpawnData::load, SpawnData::new, "spawns");
        spawnData.setSpawnData(map);

    }

    private void assignSpawn(ServerLevel level, ServerPlayer player) {

        SpawnData spawnData = level.getDataStorage().computeIfAbsent(SpawnData::load, SpawnData::new, "spawns");
        LinkedHashMap<BlockPos, UUID> spawns = spawnData.getSpawnData();

        boolean foundSpawn = false;

        for (BlockPos pos : spawns.keySet()) {
            UUID uuid = spawns.get(pos);

            if (uuid != null)
                continue;

            this.setInitialSpawn(player, level, pos);
            spawns.put(pos, player.getUUID());
            spawnData.setSpawnData(spawns);
            foundSpawn = true;
            break;

        }

        if (!foundSpawn)
            this.setInitialSpawn(player, level, new BlockPos(0, 100, 0));
    }

    private void setInitialSpawn(ServerPlayer player, ServerLevel level, BlockPos pos) {
        player.setRespawnPosition(level.dimension(), pos, 0.0F, true, false);

        Vector3f vec = new Vector3f();
        Vector3f playerVec = new Vector3f(pos.getX(), 0, pos.getZ());
        vec.sub(playerVec);
        playerVec.normalize();

        player.connection.teleport(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, (float) (Mth.atan2(vec.z(), vec.x()) * 180F / Math.PI) - 90.0F, 0.0F);
    }

}
