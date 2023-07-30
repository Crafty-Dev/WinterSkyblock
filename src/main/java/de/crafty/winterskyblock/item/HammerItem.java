package de.crafty.winterskyblock.item;

import de.crafty.winterskyblock.registry.ItemRegistry;
import de.crafty.winterskyblock.registry.TagRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class HammerItem extends DiggerItem {


    private static final HashMap<Block, List<HammerDrop>> DROPS = new HashMap<>();

    public HammerItem(float attackDamage, float attackSpeed, Tier tier, Properties properties) {
        super(attackDamage, attackSpeed, tier, TagRegistry.MINEABLE_WITH_HAMMER, properties);
    }


    public static List<ItemStack> getRandomDrop(Block block, ServerLevel level) {

        List<HammerDrop> availableDrops = DROPS.getOrDefault(block, List.of());

        List<ItemStack> drops = new ArrayList<>();

        for (HammerDrop drop : availableDrops) {

            if (level.getRandom().nextFloat() >= drop.chance())
                continue;

            int count = drop.min();

            for (int i = drop.min(); i < drop.max(); i++) {
                if (level.getRandom().nextFloat() < drop.bonusChance())
                    count++;
            }
            drops.add(new ItemStack(drop.itemSupplier.get(), count));
        }

        return drops;
    }

    public static boolean isHammerable(Block block) {
        return DROPS.containsKey(block);
    }


    public static void bootstrap() {

        registerDrops(Blocks.MUD,
                new HammerDrop(() -> Items.KELP, 0.5F, 1, 2, 0.75F),
                new HammerDrop(() -> Items.SEA_PICKLE, 0.2F, 1, 3, 0.25F)
        );

        registerDrops(Blocks.ANDESITE, new HammerDrop(() -> Items.TUFF, 1.0F, 1, 1, 0.0F));

        registerDrops(Blocks.DIRT,
                new HammerDrop(ItemRegistry.STONE_PIECE, 1.0F, 2, 5, 0.65F)
        );

        registerDrops(List.of(Blocks.COBBLESTONE),
                new HammerDrop(ItemRegistry.COAL_ORE_DUST, 0.5F, 1, 3, 0.5F),
                new HammerDrop(ItemRegistry.IRON_ORE_DUST, 0.25F, 1, 2, 0.25F),
                new HammerDrop(ItemRegistry.COPPER_ORE_DUST, 0.25F, 1, 2, 0.25F),
                new HammerDrop(ItemRegistry.STONE_PIECE, 1.0F, 2, 5, 0.4F)
        );


        registerDrops(Blocks.STONE,
                new HammerDrop(ItemRegistry.COAL_ORE_DUST, 0.65F, 1, 4, 0.5F),
                new HammerDrop(ItemRegistry.COPPER_ORE_DUST, 0.4F, 1, 3, 0.4F),
                new HammerDrop(ItemRegistry.IRON_ORE_DUST, 0.35F, 1, 2, 0.5F),
                new HammerDrop(ItemRegistry.GOLD_ORE_DUST, 0.1F, 1, 2, 0.2F),
                new HammerDrop(ItemRegistry.REDSTONE_ORE_DUST, 0.5F, 1, 4, 0.5F),
                new HammerDrop(ItemRegistry.LAPIS_ORE_DUST, 0.4F, 1, 3, 0.4F),
                new HammerDrop(ItemRegistry.STONE_PIECE, 1.0F, 2, 5, 0.5F)
        );

        registerDrops(Blocks.DEEPSLATE,
                new HammerDrop(ItemRegistry.IRON_ORE_DUST, 0.75F, 1, 3, 0.65F),
                new HammerDrop(ItemRegistry.REDSTONE_ORE_DUST, 0.65F, 1, 4, 0.5F),
                new HammerDrop(ItemRegistry.LAPIS_ORE_DUST, 0.5F, 1, 4, 0.5F),
                new HammerDrop(ItemRegistry.DIAMOND_ORE_DUST, 0.4F, 1, 2, 0.35F),
                new HammerDrop(ItemRegistry.EMERALD_ORE_DUST, 0.5F, 1, 3, 0.2F),
                new HammerDrop(ItemRegistry.GOLD_ORE_DUST, 0.25F, 1, 2, 0.4F)

                );


        registerDrops(Blocks.NETHERRACK,
                new HammerDrop(ItemRegistry.GLOWSTONE_ORE_DUST, 0.65F, 1, 3, 0.5F),
                new HammerDrop(ItemRegistry.QUARTZ_ORE_DUST, 0.3F, 1, 4, 0.2F),
                new HammerDrop(ItemRegistry.NETHERITE_ORE_DUST, 0.05F, 1, 1, 0.0F),
                new HammerDrop(ItemRegistry.NETHERRACK_PIECE, 0.75F, 1, 2, 0.5F)
        );


        registerDrops(List.of(
                Blocks.OAK_LOG,
                Blocks.BIRCH_LOG,
                Blocks.SPRUCE_LOG,
                Blocks.DARK_OAK_LOG,
                Blocks.ACACIA_LOG,
                Blocks.JUNGLE_LOG,
                Blocks.MANGROVE_LOG,
                Blocks.OAK_WOOD,
                Blocks.BIRCH_WOOD,
                Blocks.SPRUCE_WOOD,
                Blocks.DARK_OAK_WOOD,
                Blocks.ACACIA_WOOD,
                Blocks.JUNGLE_WOOD,
                Blocks.MANGROVE_WOOD
        ), new HammerDrop(ItemRegistry.WOOD_DUST, 1.0F, 2, 4, 0.5F));

        registerDrops(List.of(
                Blocks.OAK_PLANKS,
                Blocks.BIRCH_PLANKS,
                Blocks.SPRUCE_PLANKS,
                Blocks.DARK_OAK_PLANKS,
                Blocks.ACACIA_PLANKS,
                Blocks.JUNGLE_PLANKS,
                Blocks.MANGROVE_PLANKS
        ), new HammerDrop(ItemRegistry.WOOD_DUST, 1.0F, 1, 3, 0.5F));

        registerDrops(Blocks.ROOTED_DIRT,
                new HammerDrop(() -> Items.WHEAT_SEEDS, 0.5F, 1, 3, 0.35F),
                new HammerDrop(() -> Items.BEETROOT_SEEDS, 0.3F, 1, 2, 0.35F),
                new HammerDrop(() -> Items.PUMPKIN_SEEDS, 0.15F, 1, 1, 0.0F),
                new HammerDrop(() -> Items.MELON_SEEDS, 0.15F, 1, 1, 0.0F),
                new HammerDrop(() -> Items.POTATO, 0.05F, 1, 2, 0.1F),
                new HammerDrop(() -> Items.CARROT, 0.05F, 1, 2, 0.1F)
        );


        registerDrops(Blocks.PODZOL,
                new HammerDrop(() -> Items.RED_MUSHROOM, 0.2F, 1, 2, 0.35F),
                new HammerDrop(() -> Items.BROWN_MUSHROOM, 0.2F, 1, 2, 0.35F)
        );

        registerDrops(List.of(Blocks.SAND, Blocks.RED_SAND),
                new HammerDrop(() -> Items.SUGAR_CANE, 0.1F, 1, 2, 0.1F),
                new HammerDrop(() -> Items.CACTUS, 0.1F, 1, 2, 0.1F)
        );

        registerDrops(Blocks.SOUL_SAND,
                new HammerDrop(() -> Items.CRIMSON_FUNGUS, 0.25F, 1, 1, 0.0F),
                new HammerDrop(() -> Items.WARPED_FUNGUS, 0.25F, 1, 1, 0.0F)
        );
    }


    private static void registerDrops(Block block, HammerDrop... drops) {
        DROPS.put(block, List.of(drops));
    }

    private static void registerDrops(List<Block> blocks, HammerDrop... drops) {
        blocks.forEach(block -> DROPS.put(block, List.of(drops)));
    }

    public record HammerDrop(Supplier<? extends Item> itemSupplier, float chance, int min, int max, float bonusChance) {
    }
}
