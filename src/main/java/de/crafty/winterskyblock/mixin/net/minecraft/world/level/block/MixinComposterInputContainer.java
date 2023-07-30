package de.crafty.winterskyblock.mixin.net.minecraft.world.level.block;


import de.crafty.winterskyblock.util.ModCompostables;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ComposterBlock.InputContainer.class)
public abstract class MixinComposterInputContainer extends SimpleContainer implements WorldlyContainer {



    @Redirect(method = "canPlaceItemThroughFace", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z"))
    private boolean addRecipes(Object2FloatMap<ItemLike> map, Object o){
        return map.containsKey(o) || ModCompostables.has((ItemLike) o);
    }
}
