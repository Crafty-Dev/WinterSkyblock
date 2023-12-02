package de.crafty.winterskyblock.client.renderer.entity;

import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.client.renderer.entity.layers.FrozenZombieOuterLayer;
import de.crafty.winterskyblock.entity.FrozenZombie;
import de.crafty.winterskyblock.registry.EntityRegistry;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FrozenZombieRenderer extends ZombieRenderer {

    private static final ResourceLocation FROZEN_ZOMBIE_LOCATION = new ResourceLocation(WinterSkyblock.MODID, "textures/entity/frozen_zombie/frozen_zombie.png");


    public FrozenZombieRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, EntityRegistry.ModelLayers.FROZEN_ZOMBIE, EntityRegistry.ModelLayers.FROZEN_ZOMBIE_INNER_ARMOR, EntityRegistry.ModelLayers.FROZEN_ZOMBIE_OUTER_ARMOR);
        this.addLayer(new FrozenZombieOuterLayer<>(this, ctx.getModelSet()));

    }

    @Override
    public ResourceLocation getTextureLocation(Zombie zombie) {
        return FROZEN_ZOMBIE_LOCATION;
    }

    @Override
    protected boolean isShaking(Zombie p_113773_) {
        return true;
    }
}
