package de.crafty.winterskyblock.client.model;


import de.crafty.winterskyblock.entity.ResourceSheep;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ResourceSheepModel<T extends ResourceSheep> extends QuadrupedModel<T> {


    private float headXRot;

    public ResourceSheepModel(ModelPart modelPart) {
        super(modelPart, false, 8.0F, 4.0F, 2.0F, 2.0F, 24);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = QuadrupedModel.createBodyMesh(12, CubeDeformation.NONE);
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 6.0F, 8.0F), PartPose.offset(0.0F, 6.0F, -8.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 8).addBox(-4.0F, -10.0F, -7.0F, 8.0F, 16.0F, 6.0F), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, ((float)Math.PI / 2F), 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void prepareMobModel(T resourceSheep, float p_102615_, float p_102616_, float p_102617_) {
        super.prepareMobModel(resourceSheep, p_102615_, p_102616_, p_102617_);
        this.head.y = 6.0F + resourceSheep.getHeadEatPositionScale(p_102617_) * 9.0F;
        this.headXRot = resourceSheep.getHeadEatAngleScale(p_102617_);
    }

    @Override
    public void setupAnim(T resourceSheep, float p_103510_, float p_103511_, float p_103512_, float p_103513_, float p_103514_) {
        super.setupAnim(resourceSheep, p_103510_, p_103511_, p_103512_, p_103513_, p_103514_);
        this.head.xRot = this.headXRot;
    }
}
