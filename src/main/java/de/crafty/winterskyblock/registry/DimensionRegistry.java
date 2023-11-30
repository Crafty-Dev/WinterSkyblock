package de.crafty.winterskyblock.registry;

import de.crafty.winterskyblock.WinterSkyblock;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.OptionalLong;

public class DimensionRegistry {

    public static final DimensionEntry SNOW_HEAVEN = new DimensionEntry("snow_heaven", new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0D, true, false, 0, 384, 384, BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS, 0.0F, new DimensionType.MonsterSettings(false, false, UniformInt.of(0, 7), 0)));



    public static class DimensionEntry {

        private final ResourceKey<DimensionType> typeKey;
        private final ResourceKey<LevelStem> leveStemKey;
        private final DimensionType type;

        public DimensionEntry(String id, DimensionType type){
            this.typeKey = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(WinterSkyblock.MODID, id));
            this.leveStemKey = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, new ResourceLocation(WinterSkyblock.MODID, id));

            this.type = type;
        }


        public ResourceKey<DimensionType> typeRef(){
            return this.typeKey;
        }

        public DimensionType type(){
            return this.type;
        }

        public ResourceKey<LevelStem> levelStem(){
            return this.leveStemKey;
        }

    }
}
