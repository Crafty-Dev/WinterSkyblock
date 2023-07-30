package de.crafty.winterskyblock.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import de.crafty.winterskyblock.blockentities.MagicalWorkbenchBlockEntity;
import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class MagicalWorkbenchRenderer implements BlockEntityRenderer<MagicalWorkbenchBlockEntity> {


    private final ItemRenderer itemRenderer;
    private final EntityRenderDispatcher entityRenderer;


    public MagicalWorkbenchRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
        this.entityRenderer = ctx.getEntityRenderer();
    }

    @Override
    public void render(MagicalWorkbenchBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {


        if (!blockEntity.hasAnimationStarted() || blockEntity.hasAnimationFinished())
            return;


        float currentTick = blockEntity.getAnimationTick() + partialTicks;

        BlockPos pos = blockEntity.getBlockPos();
        Vec3 srcPosVec = new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);

        poseStack.pushPose();

        double morphAnimationSpeed = 0.005D;
        double morphAnimationTime = 1 / morphAnimationSpeed;

        if (currentTick <= morphAnimationTime) {
            this.renderMorphAnimation(blockEntity, morphAnimationSpeed, currentTick, poseStack, bufferSource, packedLight, packedOverlay);
            poseStack.popPose();
            return;
        }

        double transformationAnimationSpeed = 0.005D;
        double transformationAnimationTime = 1 / transformationAnimationSpeed;

        if (currentTick <= morphAnimationTime + transformationAnimationTime) {
            this.renderTransformationAnimation(blockEntity, (float) 0.1F * Math.pow(morphAnimationTime, 2), transformationAnimationSpeed, (float) (currentTick - morphAnimationTime), poseStack, bufferSource, packedLight, packedOverlay);
            poseStack.popPose();
            return;
        }


        poseStack.popPose();

    }

    private void renderMorphAnimation(MagicalWorkbenchBlockEntity blockEntity, double speed, float animationTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        BlockPos pos = blockEntity.getBlockPos();
        Vec3 srcPosVecInt = new Vec3(pos.getX(), pos.getY(), pos.getZ());


        for (BlockPos p : blockEntity.getRitualBlocks()) {

            Vec3 posVec = new Vec3(p.getX() + 0.5D, p.getY() + 0.5D, p.getZ() + 0.5D);
            Vec3 pointingVec = posVec.subtract(srcPosVecInt);

            Vec3 moveVec = new Vec3(0.0D, 2.5D, 0.0D);

            poseStack.pushPose();
            poseStack.translate(pointingVec.x(), pointingVec.y() + moveVec.multiply(0.0D, speed * animationTick, 0.0D).y(), pointingVec.z());
            poseStack.pushPose();

            float scale = (float) (2.0F - (1.5F * speed * animationTick));

            poseStack.scale(scale, scale, scale);
            poseStack.pushPose();
            poseStack.mulPose(Vector3f.YP.rotationDegrees((float) (0.1F * Math.pow(animationTick, 2))));
            this.itemRenderer.renderStatic(blockEntity.getRitualItems()[blockEntity.getRitualBlocks().indexOf(p)], ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, packedOverlay);
            poseStack.popPose();
            poseStack.popPose();
            poseStack.popPose();

        }

    }

    private void renderTransformationAnimation(MagicalWorkbenchBlockEntity blockEntity, double rotationSpeed, double speed, float animationTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        BlockPos pos = blockEntity.getBlockPos();
        Vec3 srcPosVecInt = new Vec3(pos.getX(), pos.getY(), pos.getZ());

        for(BlockPos p : blockEntity.getRitualBlocks()){

            Vec3 posVec = new Vec3(p.getX() + 0.5D, p.getY() + 0.5D, p.getZ() + 0.5D);
            Vec3 pointingVec = posVec.subtract(srcPosVecInt).add(0.0D, 2.5D, 0.0D);

            Vec3 moveVec = new Vec3(pos.getX() + 0.5D, pos.getY() + 0.5D + 5.0D, pos.getZ() + 0.5D).subtract(posVec.add(0.0D, 2.5D, 0.0D)).multiply(new Vec3(speed * animationTick, speed * animationTick, speed * animationTick));

            poseStack.pushPose();
            poseStack.translate(pointingVec.x(), pointingVec.y(), pointingVec.z());
            poseStack.pushPose();
            poseStack.translate(moveVec.x(), moveVec.y(), moveVec.z());
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.pushPose();
            poseStack.mulPose(Vector3f.YP.rotationDegrees((float) rotationSpeed * animationTick));
            this.itemRenderer.renderStatic(blockEntity.getRitualItems()[blockEntity.getRitualBlocks().indexOf(p)], ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, packedOverlay);
            poseStack.popPose();
            poseStack.popPose();
            poseStack.popPose();
            poseStack.popPose();

        }


        Vec3 moveVec = new Vec3(pos.getX() + 0.5D, pos.getY() + 5.5D, pos.getZ() + 0.5D).subtract(srcPosVecInt.add(0.5D, 0.5D, 0.5D)).multiply(new Vec3(speed * animationTick, speed * animationTick, speed * animationTick));

        poseStack.pushPose();
        poseStack.translate(0.5D + moveVec.x(), 0.5D + moveVec.y(), 0.5D + moveVec.z());
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees((float) (0.4F * Math.pow(animationTick, 2))));
        this.itemRenderer.renderStatic(new ItemStack(ItemRegistry.DRAGON_ARTIFACT.get()), ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, packedOverlay);
        poseStack.popPose();
        poseStack.popPose();
        poseStack.popPose();

    }
}
