package de.crafty.winterskyblock.blockentities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import de.crafty.winterskyblock.block.LeafPressBlock;
import de.crafty.winterskyblock.blockentities.LeafPressBlockEntity;
import de.crafty.winterskyblock.registry.ItemRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LeafPressRenderer implements BlockEntityRenderer<LeafPressBlockEntity> {


    private static final ResourceLocation WATER = new ResourceLocation("textures/blocks/water_still.png");

    private final ItemRenderer itemRenderer;
    private final BlockRenderDispatcher blockRenderer;

    public LeafPressRenderer(BlockEntityRendererProvider.Context ctx){
        this.itemRenderer = ctx.getItemRenderer();
        this.blockRenderer = ctx.getBlockRenderDispatcher();
    }

    @Override
    public void render(LeafPressBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

        if(blockEntity.getContent() == ItemStack.EMPTY)
            return;


        BlockState state = blockEntity.getBlockState();

        poseStack.pushPose();

        float f = switch (state.getValue(LeafPressBlock.PROGRESS)) {
            case 2 -> 0.9F;
            case 3 -> 0.8F;
            case 4 -> 0.7F;
            case 5 -> 0.5F;
            case 6 -> 0.3F;
            default -> 1.0F;
        };

        if(state.getValue(LeafPressBlock.PROGRESS) == 0 && blockEntity.getContent().is(ItemRegistry.DRIED_LEAVES.get()))
            f = 0.4F;

        poseStack.scale(1.25F, 1.25F * f, 1.25F);
        poseStack.translate(0.5F / 1.25F, 0.4F / 1.25F, 0.5F / 1.25F);

        this.itemRenderer.renderStatic(blockEntity.getContent(), ItemTransforms.TransformType.FIXED, combinedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, combinedOverlay);
        poseStack.popPose();


    }
}
