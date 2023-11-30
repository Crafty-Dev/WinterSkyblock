package de.crafty.winterskyblock.handler;

import de.crafty.winterskyblock.item.HammerItem;
import de.crafty.winterskyblock.registry.ItemRegistry;
import de.crafty.winterskyblock.registry.ParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class HammerDropHandler {

    public static final HashMap<Block, List<HammerDrop>> HAMMER_DROPS = new HashMap<>();
    //Just for JEI
    public static final List<List<Block>> BLOCK_GROUPS = new ArrayList<>();


    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {

        Player player = event.getPlayer();
        BlockState state = event.getState();
        BlockPos pos = event.getPos();

        ItemStack heldStack = player.getItemInHand(InteractionHand.MAIN_HAND);

        if (!(heldStack.getItem() instanceof HammerItem hammer))
            return;

        if (!(event.getLevel() instanceof ServerLevel level) || !(player instanceof ServerPlayer serverPlayer))
            return;

        if (!isHammerable(state.getBlock()))
            return;


        if (state.getMaterial().equals(Material.STONE))
            handleClientStone(serverPlayer, pos, level);

        for (ItemStack stack : getRandomDrop(state.getBlock(), level)) {
            if(this.isGoodHammer(hammer)){
                ItemEntity item = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.125D, pos.getZ() + 0.5D, stack);
                item.setDeltaMovement(0.0D, 0.0D, 0.0D);
                level.addFreshEntity(item);
                continue;
            }

            Block.popResource(level, pos, stack);
        }
    }


    private boolean isGoodHammer(HammerItem item){
        return !item.getTier().equals(Tiers.WOOD) && !item.getTier().equals(Tiers.STONE) && !item.getTier().equals(Tiers.IRON);
    }

    private static void handleClientStone(ServerPlayer serverPlayer, BlockPos pos, ServerLevel level) {

        serverPlayer.connection.send(new ClientboundSoundPacket(SoundEvents.GRAVEL_BREAK, serverPlayer.getSoundSource(), pos.getX(), pos.getY(), pos.getZ(), 0.5F, 0.5F, level.random.nextLong()));

        int xParticles = 3;
        int yParticles = 3;
        int zParticles = 3;

        for (int x = 0; x < xParticles; x++) {
            for (int y = 0; y < yParticles; y++) {
                for (int z = 0; z < zParticles; z++) {
                    level.sendParticles(ParticleRegistry.HAMMER_STONE.get(), pos.getX() + (x * (1.0F / (xParticles - 1))), pos.getY() + (y * (1.0F / (yParticles - 1))), pos.getZ() + (z * (1.0F / (zParticles - 1))), 20, 0.0D, 0.0D, 0.0D, 1.0D);
                }
            }
        }

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
        BLOCK_GROUPS.add(List.of(block));
        HAMMER_DROPS.put(block, List.of(drops));
    }

    private static void registerDrops(List<Block> blocks, HammerDrop... drops) {
        BLOCK_GROUPS.add(blocks);
        blocks.forEach(block -> HAMMER_DROPS.put(block, List.of(drops)));
    }


    public record HammerDrop(Supplier<? extends Item> itemSupplier, float chance, int min, int max, float bonusChance) {
    }

    public static List<ItemStack> getRandomDrop(Block block, ServerLevel level) {

        List<HammerDrop> availableDrops = HAMMER_DROPS.getOrDefault(block, List.of());

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
        return HAMMER_DROPS.containsKey(block);
    }

}
