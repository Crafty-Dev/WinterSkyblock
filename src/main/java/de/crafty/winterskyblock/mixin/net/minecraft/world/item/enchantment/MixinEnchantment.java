package de.crafty.winterskyblock.mixin.net.minecraft.world.item.enchantment;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {


    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private void addFortuneToShears(ItemStack stack, CallbackInfoReturnable<Boolean> cir){

        if(stack.is(Items.SHEARS) && ((Enchantment) (Object) this).equals(Enchantments.BLOCK_FORTUNE) || ((Enchantment) (Object) this).equals(Enchantments.BLOCK_EFFICIENCY))
            cir.setReturnValue(true);

    }

}
