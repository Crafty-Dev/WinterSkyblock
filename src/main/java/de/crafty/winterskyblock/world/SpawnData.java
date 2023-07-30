package de.crafty.winterskyblock.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SpawnData extends SavedData {


    private LinkedHashMap<BlockPos, UUID> spawnData;

    public SpawnData() {
        this.spawnData = new LinkedHashMap<>();
    }


    public void setSpawnData(LinkedHashMap<BlockPos, UUID> spawnData) {
        this.spawnData = spawnData;
        this.setDirty();
    }

    public LinkedHashMap<BlockPos, UUID> getSpawnData() {
        return this.spawnData;
    }

    public static SpawnData load(CompoundTag tag) {
        SpawnData spawnData = new SpawnData();

        CompoundTag spawnTag = tag.getCompound("spawnData");

        LinkedHashMap<BlockPos, UUID> map = new LinkedHashMap<>();

        spawnTag.getAllKeys().forEach(key -> {

            String[] data = key.split(";");
            int x = Integer.parseInt(data[0]);
            int y = Integer.parseInt(data[1]);
            int z = Integer.parseInt(data[2]);

            UUID uuid = spawnTag.getString(key).equals("none") ? null : UUID.fromString(spawnTag.getString(key));

            map.put(new BlockPos(x, y, z), uuid);

        });
        spawnData.spawnData = map;

        return spawnData;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {

        CompoundTag spawnTag = new CompoundTag();
        spawnData.forEach((blockPos, uuid) -> spawnTag.putString(blockPos.getX() + ";" + blockPos.getY() + ";" + blockPos.getZ(), uuid == null ? "none" : uuid.toString()));

        tag.put("spawnData", spawnTag);
        return tag;
    }


}
