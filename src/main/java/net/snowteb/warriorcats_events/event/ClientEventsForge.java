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
        if (player == null) return;

        player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {

            if (!cap.isUnlocked()) return;

            ResourceLocation screen = new ResourceLocation(WarriorCatsEvents.MODID,
                    "textures/gui/stealth_overlay_2.png");

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

            }
        });
    }
}
