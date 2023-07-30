package de.crafty.winterskyblock.util;

import de.crafty.winterskyblock.registry.ItemRegistry;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class ModCompostables {


    private static final Object2FloatMap<Supplier<Item>> MOD_COMPOSTABLES = new Object2FloatOpenHashMap<>();


    public static void bootstrap() {

        add(0.5F, ItemRegistry.OAK_LEAF);
        add(0.5F, ItemRegistry.BIRCH_LEAF);
        add(0.5F, ItemRegistry.SPRUCE_LEAF);
        add(0.5F, ItemRegistry.DARK_OAK_LEAF);
        add(0.5F, ItemRegistry.JUNGLE_LEAF);
        add(0.5F, ItemRegistry.ACACIA_LEAF);
        add(0.5F, ItemRegistry.MANGROVE_LEAF);

    }

    private static void add(float chance, Supplier<Item> item){
        MOD_COMPOSTABLES.put(item, chance);
    }

    private static void add(float chance, Item item){
        MOD_COMPOSTABLES.put(() -> item, chance);
    }


    public static boolean has(ItemLike item) {
        for (Supplier<Item> supplier : MOD_COMPOSTABLES.keySet()) {
            if (supplier.get().equals(item))
                return true;
        }
        return false;
    }

    public static float getFloat(ItemLike item){
        for(Supplier<Item> supplier : MOD_COMPOSTABLES.keySet()){
            if(supplier.get().equals(item))
                return MOD_COMPOSTABLES.getFloat(supplier);
        }
        return -1.0F;
    }


    public static Object2FloatMap<Supplier<Item>> get() {
        return MOD_COMPOSTABLES;
    }
}
