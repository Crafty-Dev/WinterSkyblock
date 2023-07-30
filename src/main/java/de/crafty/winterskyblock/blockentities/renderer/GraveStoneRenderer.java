package de.crafty.winterskyblock.blockentities.renderer;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import de.crafty.winterskyblock.blockentities.GraveStoneBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.awt.*;

public class GraveStoneRenderer implements BlockEntityRenderer<GraveStoneBlockEntity> {



    public GraveStoneRenderer(BlockEntityRendererProvider.Context ctx){

    }

    @Override
    public void render(GraveStoneBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

        if(blockEntity.getPlayerProfile() == null)
            return;

        Font font = Minecraft.getInstance().font;
        GameProfile gameProfile = blockEntity.getPlayerProfile();

        FormattedCharSequence playerName = Component.literal(gameProfile.getName()).getVisualOrderText();


        poseStack.pushPose();
        poseStack.translate(0.5D, 0.9D, 0.835D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        poseStack.scale(0.0085F, -0.0085F, 0.0085F);
        Matrix4f matrix4f = poseStack.last().pose();
        font.drawInBatch8xOutline(playerName, -font.width(playerName) / 2.0F, 0.0F, new Color(0, 0, 0).getRGB(), -1, matrix4f, bufferSource, 15728880);
        poseStack.popPose();


        poseStack.pushPose();
        poseStack.translate(0.5D, 0.775D, 0.835D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        poseStack.scale(0.065F, -0.065F, 0.065F);

        ResourceLocation skinTexture = Minecraft.getInstance().getSkinManager().getInsecureSkinLocation(gameProfile);
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, skinTexture);
        PlayerFaceRenderer.draw(poseStack, -4, 0, 8);

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        poseStack.popPose();
    }
}
