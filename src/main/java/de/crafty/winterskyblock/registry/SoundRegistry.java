package de.crafty.winterskyblock.registry;

import de.crafty.winterskyblock.WinterSkyblock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {


    private static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WinterSkyblock.MODID);

    public static final RegistryObject<SoundEvent> DISC_LAST_CHRISTMAS = REGISTRY.register("music_disc.last_christmas", () -> new SoundEvent(new ResourceLocation(WinterSkyblock.MODID, "music_disc.last_christmas")));
    public static final RegistryObject<SoundEvent> DISC_ALL_I_WANT_FOR_CHRISTMAS = REGISTRY.register("music_disc.all_i_want_for_christmas", () -> new SoundEvent(new ResourceLocation(WinterSkyblock.MODID, "music_disc.all_i_want_for_christmas")));
    public static final RegistryObject<SoundEvent> DISC_DRIVING_HOME_FOR_CHRISTMAS = REGISTRY.register("music_disc.driving_home_for_christmas", () -> new SoundEvent(new ResourceLocation(WinterSkyblock.MODID, "music_disc.driving_home_for_christmas")));
    public static final RegistryObject<SoundEvent> DISC_ITS_BEGINNING_TO_LOOK = REGISTRY.register("music_disc.its_beginning_to_look", () -> new SoundEvent(new ResourceLocation(WinterSkyblock.MODID, "music_disc.its_beginning_to_look")));
    public static final RegistryObject<SoundEvent> DISC_UNDERNEATH_THE_TREE = REGISTRY.register("music_disc.underneath_the_tree", () -> new SoundEvent(new ResourceLocation(WinterSkyblock.MODID, "music_disc.underneath_the_tree")));
    public static final RegistryObject<SoundEvent> DISC_ICH_FICKE_DICH = REGISTRY.register("music_disc.haftbefehl", () -> new SoundEvent(new ResourceLocation(WinterSkyblock.MODID, "music_disc.haftbefehl")));


    public static void register(IEventBus eventBus){
        REGISTRY.register(eventBus);
    }
}
