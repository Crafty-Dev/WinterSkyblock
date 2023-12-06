package de.crafty.winterskyblock.mixin.net.minecraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import de.crafty.winterskyblock.blockentities.renderer.CrystalCraftingRenderer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.spongepowered.asm.mixin.*;

@Mixin(EndCrystalRenderer.class)
public abstract class MixinTest extends EntityRenderer<EndCrystal> {


    @Shadow @Final private ModelPart glass;

    @Shadow @Final private ModelPart base;

    @Shadow @Final private static RenderType RENDER_TYPE;

    @Shadow
    public static float getY(EndCrystal p_114159_, float p_114160_) {
        return 0;
    }

    @Shadow @Final private static float SIN_45;

    @Shadow @Final private ModelPart cube;

    protected MixinTest(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    /**
     * @author Hafti Abi
     * @reason chabos wissen wer der babo ist
     */
    @Overwrite
    public void render(EndCrystal p_114162_, float p_114163_, float p_114164_, PoseStack p_114165_, MultiBufferSource p_114166_, int p_114167_) {
        winterSkyblock$renderCool(p_114162_, p_114163_, p_114164_, p_114165_, p_114166_, p_114167_);
    }

    @Unique
    public void winterSkyblock$renderCool(EndCrystal p_114162_, float p_114163_, float p_114164_, PoseStack p_114165_, MultiBufferSource p_114166_, int p_114167_) {
        p_114165_.pushPose();
        float f = getY(p_114162_, p_114164_);
        float f1 = ((float)p_114162_.time + p_114164_) * 3.0F;
        VertexConsumer vertexconsumer = p_114166_.getBuffer(RENDER_TYPE);
        p_114165_.pushPose();
        p_114165_.scale(2.0F, 2.0F, 2.0F);
        p_114165_.translate(0.0D, -0.5D, 0.0D);
        int i = OverlayTexture.NO_OVERLAY;
        if (p_114162_.showsBottom()) {
            this.base.render(p_114165_, vertexconsumer, p_114167_, i);
        }

        p_114165_.mulPose(Vector3f.YP.rotationDegrees(f1));
        p_114165_.translate(0.0D, (double)(1.5F + f / 2.0F), 0.0D);
        p_114165_.mulPose(new Quaternion(new Vector3f(SIN_45, 0.0F, SIN_45), 60.0F, true));
        this.glass.render(p_114165_, vertexconsumer, p_114167_, i);
        float f2 = 0.875F;
        /*p_114165_.scale(0.875F, 0.875F, 0.875F);
        p_114165_.mulPose(new Quaternion(new Vector3f(SIN_45, 0.0F, SIN_45), 60.0F, true));
        p_114165_.mulPose(Vector3f.YP.rotationDegrees(f1));
        this.glass.render(p_114165_, vertexconsumer, p_114167_, i);
        p_114165_.scale(0.875F, 0.875F, 0.875F);
        p_114165_.mulPose(new Quaternion(new Vector3f(SIN_45, 0.0F, SIN_45), 60.0F, true));
        p_114165_.mulPose(Vector3f.YP.rotationDegrees(f1));
        this.cube.render(p_114165_, vertexconsumer, p_114167_, i);*/
        p_114165_.popPose();
        p_114165_.popPose();
        BlockPos blockpos = p_114162_.getBeamTarget();
        if (blockpos != null) {
            float f3 = (float)blockpos.getX() + 0.5F;
            float f4 = (float)blockpos.getY() + 0.5F;
            float f5 = (float)blockpos.getZ() + 0.5F;
            float f6 = (float)((double)f3 - p_114162_.getX());
            float f7 = (float)((double)f4 - p_114162_.getY());
            float f8 = (float)((double)f5 - p_114162_.getZ());
            p_114165_.translate((double)f6, (double)f7, (double)f8);
            EnderDragonRenderer.renderCrystalBeams(-f6, -f7 + f, -f8, p_114164_, p_114162_.time, p_114165_, p_114166_, p_114167_);
        }

        super.render(p_114162_, p_114163_, p_114164_, p_114165_, p_114166_, p_114167_);
    }
}
