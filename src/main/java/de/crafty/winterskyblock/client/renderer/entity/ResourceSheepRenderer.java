package de.crafty.winterskyblock.client.renderer.entity;


import de.crafty.winterskyblock.client.model.entity.ResourceSheepModel;
import de.crafty.winterskyblock.client.renderer.entity.layers.ResourceSheepFurLayer;
import de.crafty.winterskyblock.entity.ResourceSheep;
import de.crafty.winterskyblock.registry.EntityRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ResourceSheepRenderer extends MobRenderer<ResourceSheep, ResourceSheepModel<ResourceSheep>> {


    public ResourceSheepRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ResourceSheepModel<>(ctx.bakeLayer(EntityRegistry.ModelLayers.RESOURCE_SHEEP)), 0.7F);
        this.addLayer(new ResourceSheepFurLayer(this, ctx.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(ResourceSheep resourceSheep) {
        return resourceSheep.getResourceType().getTexture();
    }
}
