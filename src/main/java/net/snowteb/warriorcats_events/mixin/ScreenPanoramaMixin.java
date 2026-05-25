package net.snowteb.warriorcats_events.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.zconfig.WCEClientConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenPanoramaMixin {

    @Unique
    private static final PanoramaRenderer panoramaRenderer = new PanoramaRenderer(
            new CubeMap(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID,"textures/gui/title/background/panorama"))
    );

    @Inject(method = "renderPanorama", at = @At("HEAD"), cancellable = true)
    public void renderCustomPanorama(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        if (!WCEClientConfig.CLIENT.CUSTOM_PANORAMA.get()) return;

        Minecraft mc = Minecraft.getInstance();

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        panoramaRenderer.render(guiGraphics, width, height, 1.0f, partialTick);
        ci.cancel();
    }

}
