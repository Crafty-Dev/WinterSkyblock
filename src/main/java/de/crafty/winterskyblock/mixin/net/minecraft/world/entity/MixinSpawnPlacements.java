package de.crafty.winterskyblock.mixin.net.minecraft.world.entity;

import de.crafty.winterskyblock.WinterSkyblock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnPlacements.class)
public abstract class MixinSpawnPlacements {


    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/SpawnPlacements;register(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/SpawnPlacements$Type;Lnet/minecraft/world/level/levelgen/Heightmap$Types;Lnet/minecraft/world/entity/SpawnPlacements$SpawnPredicate;)V", ordinal = 16))
    private static <T extends Entity> void modifyEndermanSpawns(EntityType<T> monster, SpawnPlacements.Type type, Heightmap.Types height, SpawnPlacements.SpawnPredicate<T> predicate) {
        SpawnPlacements.register(EntityType.ENDERMAN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WinterSkyblock::checkMonsterSpawnRules);
    }

}
