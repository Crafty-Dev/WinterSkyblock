package de.crafty.winterskyblock.registry;

import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.item.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static de.crafty.winterskyblock.registry.TabRegistry.WINTER_SKYBLOCK;
public class ItemRegistry {

    private static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, WinterSkyblock.MODID);

    public static final RegistryObject<Item> OAK_LEAF = REGISTRY.register("oak_leaf", () -> new Item(new Item.Properties().food(Foods.DRIED_KELP).tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> BIRCH_LEAF = REGISTRY.register("birch_leaf", () -> new Item(new Item.Properties().food(Foods.DRIED_KELP).tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> SPRUCE_LEAF = REGISTRY.register("spruce_leaf", () -> new Item(new Item.Properties().food(Foods.DRIED_KELP).tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> DARK_OAK_LEAF = REGISTRY.register("dark_oak_leaf", () -> new Item(new Item.Properties().food(Foods.DRIED_KELP).tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> JUNGLE_LEAF = REGISTRY.register("jungle_leaf", () -> new Item(new Item.Properties().food(Foods.DRIED_KELP).tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> ACACIA_LEAF = REGISTRY.register("acacia_leaf", () -> new Item(new Item.Properties().food(Foods.DRIED_KELP).tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> MANGROVE_LEAF = REGISTRY.register("mangrove_leaf", () -> new Item(new Item.Properties().food(Foods.DRIED_KELP).tab(WINTER_SKYBLOCK)));


    public static final RegistryObject<Item> WOODEN_BUCKET = REGISTRY.register("wooden_bucket", () -> new WoodenBucketItem(() -> Fluids.EMPTY, new Item.Properties().stacksTo(16).tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> WOODEN_WATER_BUCKET = REGISTRY.register("wooden_water_bucket", () -> new WoodenBucketItem(() -> Fluids.WATER, new Item.Properties().stacksTo(1).craftRemainder(WOODEN_BUCKET.get()).tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> WOODEN_POWDER_SNOW_BUCKET = REGISTRY.register("wooden_powder_snow_bucket", () -> new WoodenSolidBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, (new Item.Properties()).stacksTo(1).tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> WOOD_DUST = REGISTRY.register("wood_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> STONE_PIECE = REGISTRY.register("stone_piece", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> NETHERRACK_PIECE = REGISTRY.register("netherrack_piece", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));

    public static final RegistryObject<Item> ROTTEN_MIXTURE = REGISTRY.register("rotten_mixture", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> BLAZE_ENRICHED_SEEDS = REGISTRY.register("blaze_enriched_seeds", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));

    public static final RegistryObject<Item> WOODEN_HAMMER = REGISTRY.register("wooden_hammer", () -> new HammerItem(6.5F, -3.2F, Tiers.WOOD, new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> STONE_HAMMER = REGISTRY.register("stone_hammer", () -> new HammerItem(6.5F, -3.2F, Tiers.STONE, new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> IRON_HAMMER = REGISTRY.register("iron_hammer", () -> new HammerItem(6.5F, -3.2F, Tiers.IRON, new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> GOLDEN_HAMMER = REGISTRY.register("golden_hammer", () -> new HammerItem(6.5F, -3.2F, Tiers.GOLD, new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> DIAMOND_HAMMER = REGISTRY.register("diamond_hammer", () -> new HammerItem(6.5F, -3.2F, Tiers.DIAMOND, new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> NETHERITE_HAMMER = REGISTRY.register("netherite_hammer", () -> new HammerItem(6.5F, -3.2F, Tiers.NETHERITE, new Item.Properties().tab(WINTER_SKYBLOCK)));


    //Ore Dust
    public static final RegistryObject<Item> COAL_ORE_DUST = REGISTRY.register("coal_ore_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> IRON_ORE_DUST = REGISTRY.register("iron_ore_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> COPPER_ORE_DUST = REGISTRY.register("copper_ore_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> GOLD_ORE_DUST = REGISTRY.register("gold_ore_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> DIAMOND_ORE_DUST = REGISTRY.register("diamond_ore_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> EMERALD_ORE_DUST = REGISTRY.register("emerald_ore_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> LAPIS_ORE_DUST = REGISTRY.register("lapis_ore_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> REDSTONE_ORE_DUST = REGISTRY.register("redstone_ore_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));

    public static final RegistryObject<Item> NETHERITE_ORE_DUST = REGISTRY.register("netherite_ore_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> QUARTZ_ORE_DUST = REGISTRY.register("quartz_ore_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> GLOWSTONE_ORE_DUST = REGISTRY.register("glowstone_ore_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));

    public static final RegistryObject<Item> END_NETHERITE_ORE_DUST = REGISTRY.register("end_netherite_ore_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));

    //Dust
    public static final RegistryObject<Item> COAL_DUST = REGISTRY.register("coal_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> IRON_DUST = REGISTRY.register("iron_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> COPPER_DUST = REGISTRY.register("copper_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> GOLD_DUST = REGISTRY.register("gold_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> DIAMOND_DUST = REGISTRY.register("diamond_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> EMERALD_DUST = REGISTRY.register("emerald_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    //public static final RegistryObject<Item> LAPIS_DUST = REGISTRY.register("lapis_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));

    public static final RegistryObject<Item> NETHERITE_DUST = REGISTRY.register("netherite_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> QUARTZ_DUST = REGISTRY.register("quartz_dust", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));


    public static final RegistryObject<Item> COAL_ENRICHED_WHEAT = REGISTRY.register("coal_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.COAL_SHEEP, 0.5F));
    public static final RegistryObject<Item> IRON_ENRICHED_WHEAT = REGISTRY.register("iron_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.IRON_SHEEP, 0.4F));
    public static final RegistryObject<Item> COPPER_ENRICHED_WHEAT = REGISTRY.register("copper_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.COPPER_SHEEP, 0.4F));
    public static final RegistryObject<Item> GOLD_ENRICHED_WHEAT = REGISTRY.register("gold_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.GOLD_SHEEP, 0.25F));
    public static final RegistryObject<Item> LAPIS_ENRICHED_WHEAT = REGISTRY.register("lapis_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.LAPIS_SHEEP, 0.35F));
    public static final RegistryObject<Item> REDSTONE_ENRICHED_WHEAT = REGISTRY.register("redstone_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.REDSTONE_SHEEP, 0.3F));
    public static final RegistryObject<Item> DIAMOND_ENRICHED_WHEAT = REGISTRY.register("diamond_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.DIAMOND_SHEEP, 0.1F));
    public static final RegistryObject<Item> EMERALD_ENRICHED_WHEAT = REGISTRY.register("emerald_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.EMERALD_SHEEP, 0.15F));

    public static final RegistryObject<Item> QUARTZ_ENRICHED_WHEAT = REGISTRY.register("quartz_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.QUARTZ_SHEEP, 0.25F));
    public static final RegistryObject<Item> NETHERITE_ENRICHED_WHEAT = REGISTRY.register("netherite_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.NETHERITE_SHEEP, 0.15F));
    public static final RegistryObject<Item> GLOWSTONE_ENRICHED_WHEAT = REGISTRY.register("glowstone_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.GLOWSTONE_SHEEP, 0.5F));

    public static final RegistryObject<Item> NETHERRACK_ENRICHED_WHEAT = REGISTRY.register("netherrack_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.NETHERRACK_SHEEP, 0.65F));
    public static final RegistryObject<Item> COBBLESTONE_ENRICHED_WHEAT = REGISTRY.register("cobblestone_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.COBBLESTONE_SHEEP, 0.75F));
    public static final RegistryObject<Item> DIRT_ENRICHED_WHEAT = REGISTRY.register("dirt_enriched_wheat", () -> new ResourceWheatItem(EntityRegistry.DIRT_SHEEP, 0.65F));

    //Music Discs
    public static final RegistryObject<Item> DISC_LAST_CHRISTMAS = REGISTRY.register("music_disc_last_christmas", () -> new RecordItem(15, SoundRegistry.DISC_LAST_CHRISTMAS, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).tab(WINTER_SKYBLOCK), 5560));
    public static final RegistryObject<Item> DISC_ALL_I_WANT_FOR_CHRISTMAS = REGISTRY.register("music_disc_all_i_want_for_christmas", () -> new RecordItem(14, SoundRegistry.DISC_ALL_I_WANT_FOR_CHRISTMAS, new Item.Properties().stacksTo(1).rarity(Rarity.RARE).tab(WINTER_SKYBLOCK), 4680));
    public static final RegistryObject<Item> DISC_DRIVING_HOME_FOR_CHRISTMAS = REGISTRY.register("music_disc_driving_home_for_christmas", () -> new RecordItem(13, SoundRegistry.DISC_DRIVING_HOME_FOR_CHRISTMAS, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).tab(WINTER_SKYBLOCK), 4800));
    public static final RegistryObject<Item> DISC_ITS_BEGINNING_TO_LOOK = REGISTRY.register("music_disc_its_beginning_to_look", () -> new RecordItem(12, SoundRegistry.DISC_ITS_BEGINNING_TO_LOOK, new Item.Properties().stacksTo(1).rarity(Rarity.RARE).tab(WINTER_SKYBLOCK), 4140));
    public static final RegistryObject<Item> DISC_UNDERNEATH_THE_TREE = REGISTRY.register("music_disc_underneath_the_tree", () -> new RecordItem(11, SoundRegistry.DISC_UNDERNEATH_THE_TREE, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).tab(WINTER_SKYBLOCK), 4600));
    public static final RegistryObject<Item> DISC_ICH_FICKE_DICH = REGISTRY.register("music_disc_haftbefehl", () -> new RecordItem(10, SoundRegistry.DISC_ICH_FICKE_DICH, new Item.Properties().stacksTo(1).rarity(Rarity.COMMON).tab(WINTER_SKYBLOCK), 3700));

    public static final RegistryObject<Item> MOB_ORB = REGISTRY.register("mob_orb", () -> new MobOrbItem(new Item.Properties().stacksTo(1).tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> MOB_ORB_ACTIVE = REGISTRY.register("mob_orb_active", () -> new MobOrbItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).tab(WINTER_SKYBLOCK)));

    public static final RegistryObject<Item> SPAWNER_SHARD = REGISTRY.register("spawner_shard", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.EPIC).tab(WINTER_SKYBLOCK)));

    public static final RegistryObject<Item> ENDER_CORE = REGISTRY.register("ender_core", () -> new Item(new Item.Properties().stacksTo(4).rarity(Rarity.EPIC).tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> DRAGON_ARTIFACT = REGISTRY.register("dragon_artifact", () -> new Item(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).tab(WINTER_SKYBLOCK)));


    public static final RegistryObject<Item> SNOW_CRYSTAL = REGISTRY.register("snow_crystal", () -> new Item(new Item.Properties().tab(WINTER_SKYBLOCK)));
    public static final RegistryObject<Item> HEATED_SNOW_CRYSTAL = REGISTRY.register("heated_snow_crystal", () -> new Item(new Item.Properties().fireResistant().tab(WINTER_SKYBLOCK)));


    //Blocks
    public static final RegistryObject<BlockItem> LEAF_PRESS = registerBlock(BlockRegistry.LEAF_PRESS, new Item.Properties());
    public static final RegistryObject<BlockItem> DRIED_LEAVES = registerBlock(BlockRegistry.DRIED_LEAVES, new Item.Properties());

    public static final RegistryObject<BlockItem> END_DIAMOND_ORE = registerBlock(BlockRegistry.END_DIAMOND_ORE, new Item.Properties());
    public static final RegistryObject<BlockItem> END_NETHERITE_ORE = registerBlock(BlockRegistry.END_NETHERITE_ORE, new Item.Properties());

    public static final RegistryObject<BlockItem> END_PORTAL_CORE = registerBlock(BlockRegistry.END_PORTAL_CORE, new Item.Properties().rarity(Rarity.EPIC));
    public static final RegistryObject<BlockItem> END_PORTAL_FRAME = registerBlock(BlockRegistry.END_PORTAL_FRAME, new Item.Properties().rarity(Rarity.RARE));

    public static final RegistryObject<BlockItem> DRAGON_INFUSED_BEDROCK = registerBlock(BlockRegistry.DRAGON_INFUSED_BEDROCK, new Item.Properties());

    public static final RegistryObject<BlockItem> GHAST_BLOCK = registerBlock(BlockRegistry.GHAST_BLOCK, new Item.Properties().rarity(Rarity.RARE));
    public static final RegistryObject<BlockItem> PHANTOM_BLOCK = registerBlock(BlockRegistry.PHANTOM_BLOCK, new Item.Properties().rarity(Rarity.RARE));

    public static final RegistryObject<BlockItem> MAGICAL_WORKBENCH = registerBlock(BlockRegistry.MAGICAL_WORKBENCH, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final RegistryObject<BlockItem> SNOW_CRYSTAL_BLOCK = registerBlock(BlockRegistry.SNOW_CRYSTAL_BLOCK, new Item.Properties());
    public static final RegistryObject<BlockItem> HEATED_SNOW_CRYSTAL_BLOCK = registerBlock(BlockRegistry.HEATED_SNOW_CRYSTAL_BLOCK, new Item.Properties());

    public static final RegistryObject<BlockItem> CRYSTAL_CRAFTING_PEDESTAL = registerBlock(BlockRegistry.CRYSTAL_CRAFTING_PEDESTAL, new Item.Properties());

    private static RegistryObject<BlockItem> registerBlock(RegistryObject<Block> block, Item.Properties properties){
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties.tab(WINTER_SKYBLOCK)));
    }

    public static void register(IEventBus eventBus){
        REGISTRY.register(eventBus);
    }

    public static DeferredRegister<Item> get(){
        return REGISTRY;
    }
}
