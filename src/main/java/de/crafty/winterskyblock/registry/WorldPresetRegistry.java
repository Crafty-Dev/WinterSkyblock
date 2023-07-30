package de.crafty.winterskyblock.registry;

import de.crafty.winterskyblock.WinterSkyblock;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.presets.WorldPreset;

public class WorldPresetRegistry {

    public static final ResourceKey<WorldPreset> WINTER_SKYBLOCK = ResourceKey.create(Registry.WORLD_PRESET_REGISTRY, new ResourceLocation(WinterSkyblock.MODID, "winter_skyblock"));


}
