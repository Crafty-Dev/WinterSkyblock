package de.crafty.winterskyblock.mixin.net.minecraft.world.level.levelgen;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PhantomSpawner.class)
public abstract class MixinPhantomSpawner implements CustomSpawner {


    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"))
    private boolean makePhantomsSpawnLater(Player player) {
        return player.isSpectator() || ((ServerPlayer)player).getStats().getValue(Stats.CUSTOM.get(Stats.PLAY_TIME)) < 240000;
    }

}
