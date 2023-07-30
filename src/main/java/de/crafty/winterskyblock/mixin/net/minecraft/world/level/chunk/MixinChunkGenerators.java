package de.crafty.winterskyblock.mixin.net.minecraft.world.level.chunk;


import com.mojang.serialization.Codec;
import de.crafty.winterskyblock.world.generators.EndChunkGenerator;
import de.crafty.winterskyblock.world.generators.NetherChunkGenerator;
import de.crafty.winterskyblock.world.generators.OverworldChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGenerators;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerators.class)
public abstract class MixinChunkGenerators {



    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void addChunkGenerators(Registry<Codec<? extends ChunkGenerator>> registry, CallbackInfoReturnable<Codec<? extends ChunkGenerator>> cir){
        Registry.register(registry, "overworld", OverworldChunkGenerator.CODEC);
        Registry.register(registry, "nether", NetherChunkGenerator.CODEC);
        Registry.register(registry, "end", EndChunkGenerator.CODEC);
    }


}
