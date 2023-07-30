package de.crafty.winterskyblock.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.crafty.winterskyblock.client.model.ResourceSheepFurModel;
import de.crafty.winterskyblock.client.model.ResourceSheepModel;
import de.crafty.winterskyblock.entity.ResourceSheep;
import de.crafty.winterskyblock.registry.EntityRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class ResourceSheepFurLayer extends RenderLayer<ResourceSheep, ResourceSheepModel<ResourceSheep>> {


    private final ResourceSheepFurModel<ResourceSheep> model;

    public ResourceSheepFurLayer(RenderLayerParent<ResourceSheep, ResourceSheepModel<ResourceSheep>> model, EntityModelSet modelSet) {
        super(model);
        this.model = new ResourceSheepFurModel<>(modelSet.bakeLayer(EntityRegistry.ModelLayers.RESOURCE_SHEEP_FUR));
    }

    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int p_117423_, ResourceSheep resourceSheep, float p_117425_, float p_117426_, float p_117427_, float p_117428_, float p_117429_, float p_117430_) {

        ResourceLocation texture = resourceSheep.getResourceType().getFurTexture();

        if (!resourceSheep.isSheared()) {
            if (resourceSheep.isInvisible()) {
                Minecraft minecraft = Minecraft.getInstance();
                boolean flag = minecraft.shouldEntityAppearGlowing(resourceSheep);
                if (flag) {
                    this.getParentModel().copyPropertiesTo(this.model);
                    this.model.prepareMobModel(resourceSheep, p_117425_, p_117426_, p_117427_);
                    this.model.setupAnim(resourceSheep, p_117425_, p_117426_, p_117428_, p_117429_, p_117430_);
                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.outline(texture));
                    this.model.renderToBuffer(poseStack, vertexconsumer, p_117423_, LivingEntityRenderer.getOverlayCoords(resourceSheep, 0.0F), 0.0F, 0.0F, 0.0F, 1.0F);
                }
                return;

            }


            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(resourceSheep, p_117425_, p_117426_, p_117427_);
            this.model.setupAnim(resourceSheep, p_117425_, p_117426_, p_117428_, p_117429_, p_117430_);
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
            this.model.renderToBuffer(poseStack, vertexConsumer, p_117423_, LivingEntityRenderer.getOverlayCoords(resourceSheep, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);


        }
    }

}
