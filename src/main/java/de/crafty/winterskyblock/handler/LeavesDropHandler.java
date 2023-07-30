package de.crafty.winterskyblock.handler;

import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LeavesDropHandler {


    @SubscribeEvent
    public void onLeavesBreak(BlockEvent.BreakEvent event) {

        BlockState state = event.getState();
        BlockPos pos = event.getPos();
        Player player = event.getPlayer();

        ItemStack used = player.getMainHandItem();


        if (!(used.getItem() instanceof HoeItem hoeItem) || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, used) > 0)
            return;

        if (!(event.getLevel() instanceof ServerLevel level))
            return;


        if (!(state.getBlock() instanceof LeavesBlock block))
            return;


        ItemStack dropStack = null;

        if (block.equals(Blocks.OAK_LEAVES))
            dropStack = new ItemStack(ItemRegistry.OAK_LEAF.get(), level.getRandom().nextInt(2));

        if (block.equals(Blocks.BIRCH_LEAVES))
            dropStack = new ItemStack(ItemRegistry.BIRCH_LEAF.get(), level.getRandom().nextInt(2));

        if (block.equals(Blocks.SPRUCE_LEAVES))
            dropStack = new ItemStack(ItemRegistry.SPRUCE_LEAF.get(), level.getRandom().nextInt(2));

        if (block.equals(Blocks.DARK_OAK_LEAVES))
            dropStack = new ItemStack(ItemRegistry.DARK_OAK_LEAF.get(), level.getRandom().nextInt(2));

        if (block.equals(Blocks.ACACIA_LEAVES))
            dropStack = new ItemStack(ItemRegistry.ACACIA_LEAF.get(), level.getRandom().nextInt(2));

        if (block.equals(Blocks.JUNGLE_LEAVES))
            dropStack = new ItemStack(ItemRegistry.JUNGLE_LEAF.get(), level.getRandom().nextInt(2));

        if (block.equals(Blocks.MANGROVE_LEAVES))
            dropStack = new ItemStack(ItemRegistry.MANGROVE_LEAF.get(), level.getRandom().nextInt(2));


        if (dropStack != null && dropStack.getCount() > 0)
            Block.popResource(level, pos, dropStack);
    }
}
