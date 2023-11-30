package de.crafty.winterskyblock.registry;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import de.crafty.winterskyblock.WinterSkyblock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;

public class BiomeSourceRegistry {

    public static final MultiNoiseBiomeSource.Preset SNOW_HEAVEN = new MultiNoiseBiomeSource.Preset(new ResourceLocation(WinterSkyblock.MODID, "snow_heaven"), (biomeRegistry) -> {
        return new Climate.ParameterList<>(ImmutableList.of(
                Pair.of(Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrCreateHolderOrThrow(BiomeRegistry.SNOWY_ISLANDS.biomeRef()))

        ));
    });

}
