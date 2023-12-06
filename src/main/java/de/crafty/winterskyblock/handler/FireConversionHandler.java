package de.crafty.winterskyblock.handler;

import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FireConversionHandler {


    public static final List<FireConversion> FIRE_CONVERSIONS = new ArrayList<>();

    @SubscribeEvent
    public void onFireConversion(EntityLeaveLevelEvent event){

        Level level = event.getLevel();

        if(!(event.getEntity() instanceof ItemEntity itemEntity))
            return;

        ItemStack stack = itemEntity.getItem();
        Item item = stack.getItem();

        for(FireConversion conversion : FIRE_CONVERSIONS){
            if(conversion.ingredient.get().equals(item)){
                level.addFreshEntity(new ItemEntity(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), conversion.output().get(), 0.0D, 0.0D, 0.0D));
                break;
            }
        }

    }


    public static void bootstrap(){

        registerFireConversion(ItemRegistry.SNOW_CRYSTAL, () -> new ItemStack(ItemRegistry.HEATED_SNOW_CRYSTAL.get()));

    }

    private static void registerFireConversion(Supplier<Item> ingredient, Supplier<ItemStack> output){
        FIRE_CONVERSIONS.add(new FireConversion(ingredient, output));
    }

    public record FireConversion(Supplier<Item> ingredient, Supplier<ItemStack> output){

    }
}
