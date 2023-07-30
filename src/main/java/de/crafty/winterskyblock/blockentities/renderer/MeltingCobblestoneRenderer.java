package de.crafty.winterskyblock.blockentities.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import de.crafty.winterskyblock.blockentities.MeltingCobblestoneBlockEntity;
import de.crafty.winterskyblock.registry.BlockRegistry;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class MeltingCobblestoneRenderer implements BlockEntityRenderer<MeltingCobblestoneBlockEntity> {


    protected static final RenderStateShard.LightmapStateShard LIGHTMAP = new RenderStateShard.LightmapStateShard(true);
    private static final RenderType OVERLAY = RenderType.create("overlay", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionShader)).setLightmapState(LIGHTMAP).createCompositeState(false));

    private final ItemRenderer itemRenderer;
    private final BlockRenderDispatcher blockRenderer;

    public MeltingCobblestoneRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
        this.blockRenderer = ctx.getBlockRenderDispatcher();
    }


    @Override
    public void render(MeltingCobblestoneBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {

        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() != BlockRegistry.MELTING_COBBLESTONE.get())
            return;

        AABB boundingBox = state.getShape(level, pos).bounds().expandTowards(-0.0001F, -0.0001F, -0.0001F).expandTowards(0.0001F, 0.0001F, 0.0001F);
        //AABB boundingBox = state.getShape(level, pos).bounds().expandTowards(1.0F, 1.0F, 1.0F);


        float minX = (float) boundingBox.minX;
        float minY = (float) boundingBox.minY;
        float minZ = (float) boundingBox.minZ;
        float maxX = (float) boundingBox.maxX;
        float maxY = (float) boundingBox.maxY;
        float maxZ = (float) boundingBox.maxZ;


        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int r = 187;
        int g = 0;
        int b = 0;
        int alpha = (int) (blockEntity.getProgress() * 175);

        BufferBuilder builder = Tesselator.getInstance().getBuilder();

        //Top
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.vertex(matrix4f, minX, maxY, minZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, minX, maxY, maxZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, maxX, maxY, maxZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, maxX, maxY, minZ).color(r, g, b, alpha).endVertex();
        BufferUploader.drawWithShader(builder.end());


        //Bottom
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.vertex(matrix4f, minX, minY, minZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, maxX, minY, minZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, maxX, minY, maxZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, minX, minY, maxZ).color(r, g, b, alpha).endVertex();
        BufferUploader.drawWithShader(builder.end());

        //North
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.vertex(matrix4f, minX, minY, minZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, minX, maxY, minZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, maxX, maxY, minZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, maxX, minY, minZ).color(r, g, b, alpha).endVertex();
        BufferUploader.drawWithShader(builder.end());

        //South
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.vertex(matrix4f, minX, minY, maxZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, maxX, minY, maxZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, maxX, maxY, maxZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, minX, maxY, maxZ).color(r, g, b, alpha).endVertex();
        BufferUploader.drawWithShader(builder.end());

        //EAST
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.vertex(matrix4f, maxX, minY, minZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, maxX, maxY, minZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, maxX, maxY, maxZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, maxX, minY, maxZ).color(r, g, b, alpha).endVertex();
        BufferUploader.drawWithShader(builder.end());

        //WEST
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.vertex(matrix4f, minX, minY, minZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, minX, minY, maxZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, minX, maxY, maxZ).color(r, g, b, alpha).endVertex();
        builder.vertex(matrix4f, minX, maxY, minZ).color(r, g, b, alpha).endVertex();
        BufferUploader.drawWithShader(builder.end());

        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        poseStack.popPose();
    }




}
