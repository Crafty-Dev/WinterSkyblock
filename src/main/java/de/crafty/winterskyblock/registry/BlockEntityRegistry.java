package de.crafty.winterskyblock.registry;

import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.blockentities.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegistry {


    private static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, WinterSkyblock.MODID);

    public static final RegistryObject<BlockEntityType<LeafPressBlockEntity>> LEAF_PRESS = REGISTRY.register("leaf_press", () -> BlockEntityType.Builder.of(LeafPressBlockEntity::new, BlockRegistry.LEAF_PRESS.get()).build(null));
    public static final RegistryObject<BlockEntityType<MeltingCobblestoneBlockEntity>> MELTING_COBBLESTONE = REGISTRY.register("melting_cobblestone", () -> BlockEntityType.Builder.of(MeltingCobblestoneBlockEntity::new, BlockRegistry.MELTING_COBBLESTONE.get()).build(null));
    public static final RegistryObject<BlockEntityType<GraveStoneBlockEntity>> GRAVE_STONE = REGISTRY.register("grave_stone", () -> BlockEntityType.Builder.of(GraveStoneBlockEntity::new, BlockRegistry.GRAVE_STONE.get()).build(null));

    public static final RegistryObject<BlockEntityType<EndPortalCoreBlockEntity>> END_PORTAL_CORE = REGISTRY.register("end_portal_core", () -> BlockEntityType.Builder.of(EndPortalCoreBlockEntity::new, BlockRegistry.END_PORTAL_CORE.get()).build(null));

    public static final RegistryObject<BlockEntityType<MagicalWorkbenchBlockEntity>> MAGICAL_WORKBENCH = REGISTRY.register("magical_workbench", () -> BlockEntityType.Builder.of(MagicalWorkbenchBlockEntity::new, BlockRegistry.MAGICAL_WORKBENCH.get()).build(null));

    public static final RegistryObject<BlockEntityType<CrystalCraftingPedestalBlockEntity>> CRYSTAL_CRAFTING_PEDESTAL = REGISTRY.register("crystal_crafting_pedestal", () -> BlockEntityType.Builder.of(CrystalCraftingPedestalBlockEntity::new, BlockRegistry.CRYSTAL_CRAFTING_PEDESTAL.get()).build(null));

    public static void register(IEventBus eventBus){
        REGISTRY.register(eventBus);
    }

}
