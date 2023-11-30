package de.crafty.winterskyblock.mixin.net.minecraft.client.gui.screens.worldselection;

import com.mojang.blaze3d.vertex.PoseStack;
import de.crafty.winterskyblock.WinterSkyblock;
import de.crafty.winterskyblock.registry.WorldPresetRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.client.gui.screens.worldselection.WorldGenSettingsComponent;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;


@Mixin(CreateWorldScreen.class)
public abstract class MixinCreateWorldScreen extends Screen {


    @Shadow @Final public WorldGenSettingsComponent worldGenSettingsComponent;
    @Shadow private boolean worldGenSettingsVisible;

    @Shadow protected abstract void removeTempDataPackDir();

    @Shadow protected abstract LevelSettings createLevelSettings(boolean p_205448_);

    @Shadow protected abstract Optional<LevelStorageSource.LevelStorageAccess> createNewWorldDirectory();

    @Shadow public boolean hardCore;
    @Shadow @Final private static Component PREPARING_WORLD_DATA;

    @Shadow
    protected static void queueLoadScreen(Minecraft p_232900_, Component p_232901_) {
    }

    private EditBox islandCountBox;
    private String prevValue;

    protected MixinCreateWorldScreen(Component title) {
        super(title);
    }


    @Inject(method = "init", at = @At("RETURN"))
    private void addIslandCountBox$0(CallbackInfo ci) {

        CycleButton<Holder<WorldPreset>> typeButton = this.worldGenSettingsComponent.typeButton;

        this.islandCountBox = this.addRenderableWidget(new EditBox(this.font, typeButton.x + typeButton.getWidth() / 2 - typeButton.getWidth() / 8, typeButton.y + typeButton.getHeight() + 14, typeButton.getWidth() / 4, typeButton.getHeight(), Component.translatable("winterskyblock.islandCount")));
        this.islandCountBox.setValue(String.valueOf(WinterSkyblock.instance().islandCount));
        this.prevValue = this.islandCountBox.getValue();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void addIslandCountBox$1(PoseStack poseStack, int p_100891_, int p_100892_, float p_100893_, CallbackInfo ci) {

        if (!this.prevValue.equals(this.islandCountBox.getValue()) && !this.islandCountBox.getValue().isBlank()) {
            try {
                WinterSkyblock.instance().islandCount = Integer.parseInt(this.islandCountBox.getValue());
                this.prevValue = this.islandCountBox.getValue();
            } catch (NumberFormatException e) {
                this.islandCountBox.setValue(this.prevValue);
            }
        }


        CycleButton<Holder<WorldPreset>> typeButton = this.worldGenSettingsComponent.typeButton;
        boolean shouldRender = typeButton.getValue().is(WorldPresetRegistry.WINTER_SKYBLOCK) && this.worldGenSettingsVisible;

        this.islandCountBox.visible = shouldRender;

        if (shouldRender)
            drawCenteredString(poseStack, this.font, Component.translatable("winterskyblock.islandCount").append(":"), typeButton.x + typeButton.getWidth() / 2, typeButton.y + typeButton.getHeight() + 2, -1);
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    private void createNewWorld() {
        queueLoadScreen(this.minecraft, PREPARING_WORLD_DATA);
        Optional<LevelStorageSource.LevelStorageAccess> optional = this.createNewWorldDirectory();
        if (!optional.isEmpty()) {
            this.removeTempDataPackDir();
            WorldCreationContext worldcreationcontext = this.worldGenSettingsComponent.createFinalSettings(this.hardCore);
            LevelSettings levelsettings = this.createLevelSettings(worldcreationcontext.worldGenSettings().isDebug());
            System.out.println(worldcreationcontext.worldSettingsStability());
            WorldData worlddata = new PrimaryLevelData(levelsettings, worldcreationcontext.worldGenSettings(), worldcreationcontext.worldSettingsStability());
            this.minecraft.createWorldOpenFlows().createLevelFromExistingSettings(optional.get(), worldcreationcontext.dataPackResources(), worldcreationcontext.registryAccess(), worlddata);
        }
    }
}
