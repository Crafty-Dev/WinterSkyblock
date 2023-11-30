package de.crafty.winterskyblock.util;

import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;

public class SnowHeavenBiomes {

    private static final int NORMAL_WATER_COLOR = 4159204;
    private static final int NORMAL_WATER_FOG_COLOR = 329011;
    private static final int OVERWORLD_FOG_COLOR = 12638463;


    public static Biome snowy_islands(){

        MobSpawnSettings.Builder mobSettingsBuilder = new MobSpawnSettings.Builder();
        BiomeGenerationSettings.Builder biomeSettingsBuilder = new BiomeGenerationSettings.Builder();

        Biome biome = new Biome.BiomeBuilder()
        .precipitation(Biome.Precipitation.SNOW)
        .temperature(-0.7F)
        .downfall(0.9F)
        .specialEffects((new BiomeSpecialEffects.Builder())
                .waterColor(NORMAL_WATER_COLOR)
                .waterFogColor(NORMAL_WATER_FOG_COLOR)
                .fogColor(OVERWORLD_FOG_COLOR)
                .skyColor(calculateSkyColor(-0.7f))
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(null).build())
        .mobSpawnSettings(mobSettingsBuilder.build()).generationSettings(biomeSettingsBuilder.build()).build();

        return biome;
    }


    private static int calculateSkyColor(float temeperature) {
        float $$1 = temeperature / 3.0F;
        $$1 = Mth.clamp($$1, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
    }

}
