package de.crafty.winterskyblock.item;

import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class WoodenSolidBucketItem extends SolidBucketItem {

    public WoodenSolidBucketItem(Block block, SoundEvent sound, Properties builder) {
        super(block, sound, builder);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult interactionresult = super.useOn(context);
        Player player = context.getPlayer();
        if (interactionresult.consumesAction() && player != null && !player.isCreative()) {
            InteractionHand interactionhand = context.getHand();
            player.setItemInHand(interactionhand, ItemRegistry.WOODEN_BUCKET.get().getDefaultInstance());
        }

        return interactionresult;    }
}
