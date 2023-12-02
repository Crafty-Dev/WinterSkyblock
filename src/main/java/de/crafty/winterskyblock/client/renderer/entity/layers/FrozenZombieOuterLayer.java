package de.crafty.winterskyblock.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.entity.FrozenZombie;
import de.crafty.winterskyblock.registry.EntityRegistry;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FrozenZombieOuterLayer<T extends Zombie, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private static final ResourceLocation FROZEN_ZOMBIE_OUTER_LAYER = new ResourceLocation(WinterSkyblock.MODID, "textures/entity/frozen_zombie/frozen_zombie_outer_layer.png");

    private final ZombieModel<T> model;
    public FrozenZombieOuterLayer(RenderLayerParent<T, M> layerParent, EntityModelSet model) {
        super(layerParent);
        this.model = new ZombieModel<>(model.bakeLayer(EntityRegistry.ModelLayers.FROZEN_ZOMBIE_OUTER_LAYER));

    }

    @Override
    public void render(PoseStack p_117553_, MultiBufferSource p_117554_, int p_117555_, T p_117556_, float p_117557_, float p_117558_, float p_117559_, float p_117560_, float p_117561_, float p_117562_) {
        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, FROZEN_ZOMBIE_OUTER_LAYER, p_117553_, p_117554_, p_117555_, p_117556_, p_117557_, p_117558_, p_117560_, p_117561_, p_117562_, p_117559_, 1.0F, 1.0F, 1.0F);
    }
}
