package de.crafty.winterskyblock.blockentities.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.blockentities.CrystalCraftingPedestalBlockEntity;
import de.crafty.winterskyblock.client.model.CrystalCraftingSphereModel;
import de.crafty.winterskyblock.registry.EntityRegistry;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.ScreenEvent;
import org.lwjgl.opengl.GL11;

public class CrystalCraftingRenderer implements BlockEntityRenderer<CrystalCraftingPedestalBlockEntity> {

    public static final Material SPHERE_LOCATION = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(WinterSkyblock.MODID, "entity/crystal_crafting_sphere"));

    private final ItemRenderer itemRenderer;
    private final BlockRenderDispatcher blockRenderer;

    private final CrystalCraftingSphereModel sphereModel;

    public CrystalCraftingRenderer(BlockEntityRendererProvider.Context context){
        this.itemRenderer = context.getItemRenderer();
        this.blockRenderer = context.getBlockRenderDispatcher();

        this.sphereModel = new CrystalCraftingSphereModel(context.bakeLayer(EntityRegistry.ModelLayers.CRYSTAL_CRAFTING_SPHERE));
    }


    @Override
    public void render(CrystalCraftingPedestalBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        if(!blockEntity.isActive())
            return;


        this.renderItem(blockEntity, partialTicks, poseStack, bufferSource, packedLight, packedOverlay);
        this.renderSphere(blockEntity, partialTicks, poseStack, bufferSource, packedLight, packedOverlay);
    }


    private void renderItem(CrystalCraftingPedestalBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay){

        ItemStack stack = blockEntity.getItemStack();
        if(stack.isEmpty())
            return;

        poseStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        poseStack.translate(0.5F, 1.0F, 0.5F);
        poseStack.mulPose(Vector3f.YP.rotation((blockEntity.getIdleTick() + partialTicks) / 180.0F * Mth.PI));
        this.itemRenderer.renderStatic(stack, ItemTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, packedOverlay);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    private void renderSphere(CrystalCraftingPedestalBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay){
        float rotation = (blockEntity.getIdleTick() + partialTicks) / 180 * Mth.PI * 2;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        poseStack.translate(0.5F, 0.35F, 0.5F);
        poseStack.scale(0.85F, 0.85F, 0.85F);

        this.sphereModel.setupAngles(rotation);
        VertexConsumer vertexConsumer = SPHERE_LOCATION.buffer(bufferSource, RenderType::entityTranslucent);
        this.sphereModel.render(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }
}
