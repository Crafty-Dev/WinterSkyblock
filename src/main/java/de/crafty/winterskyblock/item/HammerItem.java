package de.crafty.winterskyblock.item;

import de.crafty.winterskyblock.handler.HammerDropHandler;
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
import de.crafty.winterskyblock.handler.HammerDropHandler.*;
import static de.crafty.winterskyblock.handler.HammerDropHandler.HAMMER_DROPS;

public class HammerItem extends DiggerItem {



    public HammerItem(float attackDamage, float attackSpeed, Tier tier, Properties properties) {
        super(attackDamage, attackSpeed, tier, TagRegistry.MINEABLE_WITH_HAMMER, properties);
    }


}
