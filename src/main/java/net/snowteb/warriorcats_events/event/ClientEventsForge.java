package net.snowteb.warriorcats_events.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventsForge {
    @SubscribeEvent
    public static void onOverlayRender(RenderGuiOverlayEvent.Pre event) {

        LocalPlayer player = Minecraft.getInstance().player;
        Minecraft mc = Minecraft.getInstance();
        if (player == null) return;
        if (!mc.isWindowActive()) return;
        if (mc.screen != null) return;
        if (mc.level == null) return;

        player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {

            if (!cap.isUnlocked()) return;

            ResourceLocation screen = new ResourceLocation(WarriorCatsEvents.MODID,
                    "textures/hud/stealth_overlay_4.png");

            /**
             * If the stealth is on, render an overlay.
             */
            if (cap.isStealthOn()) {

                GuiGraphics gui = event.getGuiGraphics();
                int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();

                RenderSystem.enableBlend();

                gui.blit(
                        screen,
                        0, 0,
                        0, 0,
                        w, h,
                        w, h
                );

                RenderSystem.disableBlend();
            }
        });
    }
}
