package de.crafty.winterskyblock.handler;

import de.crafty.winterskyblock.registry.EntityRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingGetProjectileEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ZombieConversionHandler {


    @SubscribeEvent
    public void onZombieSnowball(LivingHurtEvent event){


        if(!(event.getEntity() instanceof Zombie zombie))
            return;

        if(!(event.getSource().isProjectile() && event.getSource().getDirectEntity() instanceof Snowball))
            return;

        Level level = zombie.getLevel();

        if(!level.isClientSide()){
            zombie.convertTo(EntityRegistry.FROZEN_ZOMBIE.get(), true);
        }

    }

}
