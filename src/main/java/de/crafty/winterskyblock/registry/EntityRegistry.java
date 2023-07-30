package de.crafty.winterskyblock.registry;

import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.client.model.ResourceSheepFurModel;
import de.crafty.winterskyblock.client.model.ResourceSheepModel;
import de.crafty.winterskyblock.client.renderer.entity.ResourceSheepRenderer;
import de.crafty.winterskyblock.entity.ResourceSheep;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class EntityRegistry {


    private static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WinterSkyblock.MODID);
    private static final List<RegistryObject<EntityType<ResourceSheep>>> RESOURCE_SHEEPS = new ArrayList<>();
    private static final Map<RegistryObject<EntityType<ResourceSheep>>, Supplier<AttributeSupplier>> RESOURCE_SHEEP_ATTRIBUTES = new HashMap<>();



    public static final RegistryObject<EntityType<ResourceSheep>> COAL_SHEEP = registerResourceSheep("coal_sheep", ResourceSheep.Type.COAL);
    public static final RegistryObject<EntityType<ResourceSheep>> IRON_SHEEP = registerResourceSheep("iron_sheep", ResourceSheep.Type.IRON);
    public static final RegistryObject<EntityType<ResourceSheep>> COPPER_SHEEP = registerResourceSheep("copper_sheep", ResourceSheep.Type.COPPER);
    public static final RegistryObject<EntityType<ResourceSheep>> GOLD_SHEEP = registerResourceSheep("gold_sheep", ResourceSheep.Type.GOLD);
    public static final RegistryObject<EntityType<ResourceSheep>> LAPIS_SHEEP = registerResourceSheep("lapis_sheep", ResourceSheep.Type.LAPIS);
    public static final RegistryObject<EntityType<ResourceSheep>> REDSTONE_SHEEP = registerResourceSheep("redstone_sheep", ResourceSheep.Type.REDSTONE);
    public static final RegistryObject<EntityType<ResourceSheep>> DIAMOND_SHEEP = registerResourceSheep("diamond_sheep", ResourceSheep.Type.DIAMOND);
    public static final RegistryObject<EntityType<ResourceSheep>> EMERALD_SHEEP = registerResourceSheep("emerald_sheep", ResourceSheep.Type.EMERALD);

    public static final RegistryObject<EntityType<ResourceSheep>> QUARTZ_SHEEP = registerResourceSheep("quartz_sheep", ResourceSheep.Type.QUARTZ);
    public static final RegistryObject<EntityType<ResourceSheep>> NETHERITE_SHEEP = registerResourceSheep("netherite_sheep", ResourceSheep.Type.NETHERITE);
    public static final RegistryObject<EntityType<ResourceSheep>> GLOWSTONE_SHEEP = registerResourceSheep("glowstone_sheep", ResourceSheep.Type.GLOWSTONE);

    public static final RegistryObject<EntityType<ResourceSheep>> NETHERRACK_SHEEP = registerResourceSheep("netherrack_sheep", ResourceSheep.Type.NETHERRACK);
    public static final RegistryObject<EntityType<ResourceSheep>> COBBLESTONE_SHEEP = registerResourceSheep("cobblestone_sheep", ResourceSheep.Type.COBBLESTONE);
    public static final RegistryObject<EntityType<ResourceSheep>> DIRT_SHEEP = registerResourceSheep("dirt_sheep", ResourceSheep.Type.DIRT);



    private static RegistryObject<EntityType<ResourceSheep>> registerResourceSheep(String id, ResourceSheep.Type resourceType){
        RegistryObject<EntityType<ResourceSheep>> resourceObject = REGISTRY.register(id, () -> EntityType.Builder.<ResourceSheep>of((entityType, level) -> new ResourceSheep(entityType, level, resourceType), MobCategory.CREATURE).sized(0.9F, 1.3F).clientTrackingRange(10).build(id));
        RESOURCE_SHEEPS.add(resourceObject);
        RESOURCE_SHEEP_ATTRIBUTES.put(resourceObject, () -> ResourceSheep.createAttributes(resourceType).build());
        return resourceObject;
    }

    public static void registerAttributes(EntityAttributeCreationEvent event){
        RESOURCE_SHEEP_ATTRIBUTES.forEach((entityTypeRegistryObject, attributeSupplier) -> {
            event.put(entityTypeRegistryObject.get(), attributeSupplier.get());
        });
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        RESOURCE_SHEEPS.forEach(entityTypeRegistryObject -> event.registerEntityRenderer(entityTypeRegistryObject.get(), ResourceSheepRenderer::new));
    }

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModelLayers.RESOURCE_SHEEP, ResourceSheepModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.RESOURCE_SHEEP_FUR, ResourceSheepFurModel::createFurLayer);
    }

    public static void register(IEventBus eventBus){
        REGISTRY.register(eventBus);
    }




    public static class ModelLayers {

        public static final ModelLayerLocation RESOURCE_SHEEP = new ModelLayerLocation(new ResourceLocation(WinterSkyblock.MODID, "resourcesheep"), "main");
        public static final ModelLayerLocation RESOURCE_SHEEP_FUR = new ModelLayerLocation(new ResourceLocation(WinterSkyblock.MODID, "resourcesheep"), "fur");

    }

}
