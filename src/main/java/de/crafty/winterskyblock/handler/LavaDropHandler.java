package de.crafty.winterskyblock.handler;

import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LavaCauldronBlock;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class LavaDropHandler {

    private static final HashMap<Supplier<Item>, List<LavaDrop>> LAVA_DROPS = new HashMap<>();

    public static void bootstrap() {
        registerDrops(Items.COBBLESTONE, new LavaDrop(ItemRegistry.NETHERRACK_PIECE, 1, 3, 0.5F, true));


        registerModDrops(List.of(
                Items.SAND,
                Items.RED_SAND
        ), new LavaDrop(() -> Items.SOUL_SAND, 1, 1, 0.0F, true));

        registerDrops(Items.INK_SAC, new LavaDrop(() -> Items.GLOW_INK_SAC, 1, 1, 0.0F, false));
        registerModDrops(List.of(Items.DEEPSLATE, Items.COBBLED_DEEPSLATE), new LavaDrop(() -> Items.BASALT, 4, 4, 0.0F, true));
        registerDrops(Items.GUNPOWDER, new LavaDrop(() -> Items.BLAZE_POWDER, 1, 1, 0.0F, true));
        registerModDrops(ItemRegistry.BLAZE_ENRICHED_SEEDS, new LavaDrop(() -> Items.NETHER_WART, 1, 1, 0.0F, true));
    }

    private static void registerModDrops(Supplier<Item> item, LavaDrop... drops) {
        LAVA_DROPS.put(item, List.of(drops));
    }

    private static void registerDrops(Item item, LavaDrop... drops) {
        LAVA_DROPS.put(() -> item, List.of(drops));
    }

    private static void registerModDrops(List<Item> items, LavaDrop... drops) {
        items.forEach(item -> LAVA_DROPS.put(() -> item, List.of(drops)));
    }

    private static void registerDrops(List<Supplier<Item>> items, LavaDrop... drops) {
        items.forEach(item -> LAVA_DROPS.put(item, List.of(drops)));
    }

    private static List<LavaDrop> getDropsFor(Item item) {
        for(Supplier<Item> sup : LAVA_DROPS.keySet()){
            if(sup.get().equals(item))
                return LAVA_DROPS.get(sup);
        }
        return List.of();
    }

    @SubscribeEvent
    public void onLavaProcess(EntityLeaveLevelEvent event) {

        if (!(event.getEntity() instanceof ItemEntity item) || !(event.getLevel() instanceof ServerLevel level))
            return;


        BlockPos pos = item.blockPosition();
        ItemStack stack = item.getItem();

        if (!event.getLevel().isLoaded(pos))
            return;

        if (!(level.getBlockState(pos).getBlock() instanceof LavaCauldronBlock))
            return;

        List<LavaDrop> drops = getDropsFor(stack.getItem());
        if (drops.isEmpty())
            return;

        level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
        level.sendParticles(ParticleTypes.LAVA, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 20, 0.0F, 0.0F, 0.0F, 1.0F);


        if (stack.getCount() > 1) {
            stack.setCount(stack.getCount() - 1);
            ItemEntity entity = new ItemEntity(level, item.getX(), item.getY(), item.getZ(), stack);
            level.addFreshEntity(entity);
        }


        for (LavaDrop drop : drops) {

            int amount = drop.min();
            for (int i = drop.min(); i < drop.max(); i++) {
                if (level.getRandom().nextFloat() < drop.bonusChance())
                    amount++;
            }

            if (drop.dropSeperately()) {
                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(drop.itemSupplier.get(), amount)));
                continue;
            }

            for (int i = 0; i < amount; i++) {
                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(drop.itemSupplier.get())));
            }

        }
    }


    public record LavaDrop(Supplier<Item> itemSupplier, int min, int max, float bonusChance, boolean dropSeperately) {
    }
}
