package de.crafty.winterskyblock.mixin.net.minecraft.data.worldgen;

import de.crafty.winterskyblock.registry.DimensionRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionTypes.class)
public abstract class MixinDimensionTypes {

    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void addWinterSkyblockDimensions(Registry<DimensionType> registry, CallbackInfoReturnable<Holder<DimensionType>> cir){
        BuiltinRegistries.register(registry, DimensionRegistry.SNOW_HEAVEN.typeRef(), DimensionRegistry.SNOW_HEAVEN.type());
    }

}
