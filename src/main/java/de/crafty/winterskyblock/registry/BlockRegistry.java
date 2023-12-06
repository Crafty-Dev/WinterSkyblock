package de.crafty.winterskyblock.registry;

import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistry {


    private static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, WinterSkyblock.MODID);


    public static final RegistryObject<Block> LEAF_PRESS = REGISTRY.register("leaf_press", LeafPressBlock::new);

    public static final RegistryObject<Block> DRIED_LEAVES = REGISTRY.register("dried_leaves", () -> new Block(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).sound(SoundType.GRASS).isViewBlocking(BlockRegistry::never).isSuffocating(BlockRegistry::never).noOcclusion()));
    public static final RegistryObject<Block> END_DIAMOND_ORE = REGISTRY.register("end_diamond_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(6.9F, 69.0F).lightLevel(value -> 5)));
    public static final RegistryObject<Block> END_NETHERITE_ORE = REGISTRY.register("end_netherite_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(40.0F, 1200.0F).lightLevel(value -> 5)));

    public static final RegistryObject<Block> END_PORTAL_CORE = REGISTRY.register("end_portal_core", EndPortalCoreBlock::new);
    public static final RegistryObject<Block> END_PORTAL_FRAME = REGISTRY.register("end_portal_frame", EndPortalFrameBlock::new);

    public static final RegistryObject<Block> DRAGON_INFUSED_BEDROCK = REGISTRY.register("dragon_infused_bedrock", DragonInfusedBedrockBlock::new);


    public static final RegistryObject<Block> GHAST_BLOCK = REGISTRY.register("ghast_block", () -> new Block(BlockBehaviour.Properties.of(Material.SAND).sound(SoundType.SAND).strength(1.0F)));
    public static final RegistryObject<Block> PHANTOM_BLOCK = REGISTRY.register("phantom_block", () -> new Block(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.SNOW).strength(1.0F)));
    public static final RegistryObject<Block> MAGICAL_WORKBENCH = REGISTRY.register("magical_workbench", MagicalWorkbenchBlock::new);

    public static final RegistryObject<Block> SNOW_CRYSTAL_BLOCK = REGISTRY.register("snow_crystal_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.GLASS).strength(0.25F)));
    public static final RegistryObject<Block> HEATED_SNOW_CRYSTAL_BLOCK = REGISTRY.register("heated_snow_crystal_block", () -> new HeatedSnowCrystalBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).strength(0.25F)));

    public static final RegistryObject<Block> CRYSTAL_CRAFTING_PEDESTAL = REGISTRY.register("crystal_crafting_pedestal", () -> new CrystalCraftingPedestalBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F).noOcclusion()));

    //Non-Item
    public static final RegistryObject<Block> GRAVE_STONE = REGISTRY.register("grave_stone", GraveStoneBlock::new);
    public static final RegistryObject<Block> MELTING_COBBLESTONE = REGISTRY.register("melting_cobblestone", MeltingCobblestone::new);

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }


    public static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return false;
    }

    private static Boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos, EntityType<?> entityType) {
        return false;
    }
}
