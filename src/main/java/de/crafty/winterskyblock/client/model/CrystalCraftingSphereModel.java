package de.crafty.winterskyblock.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class CrystalCraftingSphereModel extends Model {


    private final ModelPart sphere;

    public CrystalCraftingSphereModel(ModelPart root) {
        super(RenderType::entityTranslucent);

        this.sphere = root.getChild("sphere");
    }

    public static LayerDefinition createLayerDefinition() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition sphere = partDefinition.addOrReplaceChild("sphere", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -4.0F, 5.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 9).addBox(5.0F, -4.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(18, 9).addBox(4.0F, -5.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(18, 18).addBox(-4.0F, 4.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 9).addBox(4.0F, -4.0F, -5.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, 5.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 9).addBox(-6.0F, -4.0F, -4.0F, 1.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 9).addBox(4.0F, 4.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(18, 9).addBox(-5.0F, 4.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(18, 9).addBox(-5.0F, -5.0F, -4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(18, 18).addBox(-4.0F, -5.0F, -5.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 18).addBox(-4.0F, -5.0F, 4.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 18).addBox(-4.0F, 4.0F, 4.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 9).addBox(-5.0F, -4.0F, -5.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 9).addBox(-5.0F, -4.0F, 4.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 9).addBox(4.0F, -4.0F, 4.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 64, 32);
    }


    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float r, float g, float b, float a) {
        this.sphere.render(stack, vertexConsumer, packedLight, packedOverlay, r, g, b, a);
    }

    public void render(PoseStack stack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float r, float g, float b, float a){
        this.sphere.render(stack, vertexConsumer, packedLight, packedOverlay, r, g, b, a);
    }


    public void setupAngles(float rotation){

        this.sphere.setRotation(0, rotation, 0);

    }


}
