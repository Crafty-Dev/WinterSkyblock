package de.crafty.winterskyblock.registry;

import de.crafty.winterskyblock.WinterSkyblock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class TagRegistry {

    //Blocks
    public static final TagKey<Block> MINEABLE_WITH_HAMMER = BlockTags.create(new ResourceLocation(WinterSkyblock.MODID, "mineable_with_hammer"));

    //Items
    public static final TagKey<Item> HAMMERS = ItemTags.create(new ResourceLocation(WinterSkyblock.MODID, "hammers"));
    public static final TagKey<Item> BUCKETS = ItemTags.create(new ResourceLocation(WinterSkyblock.MODID, "buckets"));
    public static final TagKey<Item> WATER_BUCKETS = ItemTags.create(new ResourceLocation(WinterSkyblock.MODID, "water_buckets"));
    public static final TagKey<Item> RESSOURCE_WHEAT = ItemTags.create(new ResourceLocation(WinterSkyblock.MODID, "ressource_wheat"));
    public static final TagKey<Item> NETHER_WHEAT = ItemTags.create(new ResourceLocation(WinterSkyblock.MODID, "nether_wheat"));

    //Biomes
    public static final TagKey<Biome> IS_BIRCH_FOREST = BiomeTags.create("is_birch_forest");
    public static final TagKey<Biome> IS_CRIMSON = BiomeTags.create("is_crimson");
    public static final TagKey<Biome> IS_WARPED = BiomeTags.create("is_warped");

    public static final TagKey<Biome> IS_DRIPPY = BiomeTags.create("is_dripstone");

}
