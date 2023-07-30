package de.crafty.winterskyblock.blockentities.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.crafty.winterskyblock.blockentities.EndPortalCoreBlockEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EndPortalCoreRenderer implements BlockEntityRenderer<EndPortalCoreBlockEntity> {


    private final ItemRenderer itemRenderer;
    private final BlockRenderDispatcher blockRenderer;

    public EndPortalCoreRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
        this.blockRenderer = ctx.getBlockRenderDispatcher();
    }


    @Override
    public void render(EndPortalCoreBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        if (!blockEntity.hasAnimationStarted() || blockEntity.hasAnimationFinished())
            return;

        BlockPos pos = blockEntity.getBlockPos();

        float currentTick = blockEntity.getAnimationTick() + partialTicks;

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);

        Vec3[] sourceLocations = this.getEyeSourceLocations(pos);

        if (currentTick <= blockEntity.getPositioningAnimationTime()) {
            this.renderPositioningAnimation(sourceLocations, blockEntity.getBlockPos(), blockEntity.getPositioningAnimationSpeed(), currentTick, poseStack, bufferSource, packedLight, packedOverlay);
            poseStack.popPose();
            return;
        }

        if (currentTick <= blockEntity.getPositioningAnimationTime() + blockEntity.getCircleAnimationTime()) {
            this.renderCircleAnimation(sourceLocations, pos, blockEntity.getCircleAnimationSpeed(), (float) (currentTick - blockEntity.getPositioningAnimationTime()), poseStack, bufferSource, packedLight, packedOverlay);
            poseStack.popPose();
            return;
        }


        if (currentTick <= blockEntity.getPositioningAnimationTime() + blockEntity.getCircleAnimationTime() + blockEntity.getTransformationAnimationTime()) {
            this.renderTransformationAnimation(sourceLocations, blockEntity.getPortalFrameLocations(pos), pos, blockEntity.getTransformationAnimationSpeed(), (float) (currentTick - (blockEntity.getPositioningAnimationTime() + blockEntity.getCircleAnimationTime())), poseStack, bufferSource, packedLight, packedOverlay);
            poseStack.popPose();
            return;
        }

        poseStack.popPose();
    }


    private Vec3[] getEyeSourceLocations(BlockPos pos) {

        Vec3[] positions = new Vec3[12];

        double posX = pos.getX() + 0.5D;
        double posY = pos.getY() + 1.25D;
        double posZ = pos.getZ() + 0.5D;

        for (int i = 0; i < 12; i++) {

            double angle = Math.toRadians(360.0D / 12 * i);

            double xOff = Math.cos(angle) * 0.75F;
            double zOff = Math.sin(angle) * 0.75F;

            positions[i] = new Vec3(posX + xOff, posY, posZ + zOff);
        }

        return positions;
    }

    private void renderPositioningAnimation(Vec3[] eyeSourceLocations, BlockPos pos, double speed, float animationTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Vec3 posVec = new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);

        for (Vec3 vec : eyeSourceLocations) {

            Vec3 pointingVec = vec.subtract(posVec);
            Vec3 positionVec = pointingVec.multiply(animationTick * speed, animationTick * speed, animationTick * speed);

            poseStack.pushPose();
            poseStack.translate(positionVec.x(), positionVec.y(), positionVec.z());
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.75F);
            this.itemRenderer.renderStatic(new ItemStack(Items.ENDER_EYE), ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, packedOverlay);
            poseStack.popPose();
            poseStack.popPose();

        }
    }

    private void renderCircleAnimation(Vec3[] eyeSourceLocations, BlockPos pos, double rotationSpeed, float animationTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Vec3 posVec = new Vec3(pos.getX() + 0.5D, pos.getY() + 0.75D, pos.getZ() + 0.5D);

        for (Vec3 eyePosVec : eyeSourceLocations) {

            Vec3 vec = eyePosVec.subtract(posVec);
            vec = vec.yRot((float) Math.toRadians(animationTick * rotationSpeed));

            poseStack.pushPose();
            poseStack.translate(vec.x, 0.75D, vec.z);
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.75F);
            this.itemRenderer.renderStatic(new ItemStack(Items.ENDER_EYE), ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, packedOverlay);
            poseStack.popPose();
            poseStack.popPose();
        }
    }

    private void renderTransformationAnimation(Vec3[] eyeSourceLocations, Vec3[] portalFrames, BlockPos pos, double speed, float animationTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        for (int i = 0; i < 12; i++) {

            Vec3 portalFrameVec = portalFrames[i];
            Vec3 eyePosVec = eyeSourceLocations[i];

            Vec3 pointingVec = portalFrameVec.add(0.0D, 0.75D, 0.0D).subtract(eyePosVec);
            Vec3 sourcePointingVec = eyePosVec.subtract(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);

            double y = Math.sin(Math.toRadians(Math.min(180, 180.0D * speed * animationTick))) * 1.5D;
            Vec3 posVec = sourcePointingVec.add(pointingVec.multiply(animationTick * speed, 0.0D, animationTick * speed));


            poseStack.pushPose();
            poseStack.translate(posVec.x, 0.75D + y, posVec.z);
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.75F);
            this.itemRenderer.renderStatic(new ItemStack(Items.ENDER_EYE), ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, packedOverlay);
            poseStack.popPose();
            poseStack.popPose();
        }
    }
}
