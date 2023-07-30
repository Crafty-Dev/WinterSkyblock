package de.crafty.winterskyblock.item;

import de.crafty.winterskyblock.entity.ResourceSheep;
import de.crafty.winterskyblock.registry.TabRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ResourceWheatItem extends Item {

    private final Supplier<EntityType<ResourceSheep>> sheep;
    private final float spawnChance;

    public ResourceWheatItem(Supplier<EntityType<ResourceSheep>> ressourceSheep, float spawnChance) {
        super(new Item.Properties().tab(TabRegistry.WINTER_SKYBLOCK));

        this.sheep = ressourceSheep;
        this.spawnChance = spawnChance;
    }

    public EntityType<ResourceSheep> getSheepType() {
        return this.sheep.get();
    }

    public float getSpawnChance() {
        return this.spawnChance;
    }
}
