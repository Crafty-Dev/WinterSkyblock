package de.crafty.winterskyblock.mixin.net.minecraft.world.level.block;

import de.crafty.winterskyblock.util.ModCompostables;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(ComposterBlock.class)
public abstract class MixinComposterBlock extends Block implements WorldlyContainerHolder {


    @Shadow
    protected static void add(float p_51921_, ItemLike p_51922_) {
    }

    public MixinComposterBlock(Properties p_49795_) {
        super(p_49795_);
    }


    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z"))
    private boolean addRecipes$0(Object2FloatMap<ItemLike> map, Object o){
        return map.containsKey(o) || ModCompostables.has((ItemLike) o);
    }

    @Redirect(method = "insertItem", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;containsKey(Ljava/lang/Object;)Z"))
    private static boolean addRecipes$1(Object2FloatMap<ItemLike> map, Object o){
        return map.containsKey(o) || ModCompostables.has((ItemLike) o);
    }

    @Redirect(method = "addItem", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2FloatMap;getFloat(Ljava/lang/Object;)F"))
    private static float addRecipes$2(Object2FloatMap<ItemLike> map, Object o){
        return map.getFloat(o) > 0.0F ? map.getFloat(o) : ModCompostables.getFloat((ItemLike) o);
    }

    @Redirect(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ComposterBlock;add(FLnet/minecraft/world/level/ItemLike;)V", ordinal = 0))
    private static void modifyRecipe$0(float chance, ItemLike item){
        add(0.75F, item);
    }

    @Redirect(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ComposterBlock;add(FLnet/minecraft/world/level/ItemLike;)V", ordinal = 1))
    private static void modifyRecipe$1(float chance, ItemLike item){
        add(0.75F, item);
    }

    @Redirect(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ComposterBlock;add(FLnet/minecraft/world/level/ItemLike;)V", ordinal = 2))
    private static void modifyRecipe$2(float chance, ItemLike item){
        add(0.75F, item);
    }

    @Redirect(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ComposterBlock;add(FLnet/minecraft/world/level/ItemLike;)V", ordinal = 3))
    private static void modifyRecipe$3(float chance, ItemLike item){
        add(0.75F, item);
    }

    @Redirect(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ComposterBlock;add(FLnet/minecraft/world/level/ItemLike;)V", ordinal = 4))
    private static void modifyRecipe$4(float chance, ItemLike item){
        add(0.75F, item);
    }

    @Redirect(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ComposterBlock;add(FLnet/minecraft/world/level/ItemLike;)V", ordinal = 5))
    private static void modifyRecipe$5(float chance, ItemLike item){
        add(0.75F, item);
    }

    @Redirect(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ComposterBlock;add(FLnet/minecraft/world/level/ItemLike;)V", ordinal = 6))
    private static void modifyRecipe$6(float chance, ItemLike item){
        add(0.75F, item);
    }

    @Redirect(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/ComposterBlock;add(FLnet/minecraft/world/level/ItemLike;)V", ordinal = 7))
    private static void modifyRecipe$7(float chance, ItemLike item){
        add(0.75F, item);
    }


}
