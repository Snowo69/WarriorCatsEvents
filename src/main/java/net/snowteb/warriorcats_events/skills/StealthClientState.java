package net.snowteb.warriorcats_events.skills;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientThirstData;
import net.snowteb.warriorcats_events.managers.ClimbDataAccessor;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.skilltree.CtSToggleStealthPacket;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;
import tocraft.walkers.api.PlayerShape;

public class StealthClientState {
    private static boolean lastState = false;

    /**
     * If the state changes, then toggle the stealth mode and play 2 cute sounds.
     * Then update the current state.
     */
    public static void tick(boolean currentState) {
        if (currentState != lastState) {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer instanceof ClimbDataAccessor data) {
                if (currentState && data.wce$isClimbing()) return;
            }
            ModPackets.sendToServer(new CtSToggleStealthPacket(currentState));
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                if (currentState) {
                    mc.player.playSound(SoundEvents.GRASS_HIT, 0.7f, 0.8f);
                } else {
                    mc.player.playSound(SoundEvents.CAT_PURREOW, 0.7f, 1.2f);

                }
            }

            lastState = currentState;
        }
    }

    public static final IGuiOverlay HUD_STEALTH = ((gui, pGuiGraphics, partialTick, screenWidth, screenHeight) -> {

        LocalPlayer player = Minecraft.getInstance().player;
        Minecraft mc = Minecraft.getInstance();
        if (player == null) return;
        if (!mc.isWindowActive()) return;
        if (mc.screen != null) return;
        if (mc.level == null) return;

        player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {

            if (!cap.isUnlocked()) return;

            ResourceLocation screen2 = ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID,
                    "textures/hud/stealth/stealthbg2.png");

            ResourceLocation topLeft = ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID,
                    "textures/hud/stealth/stealth_tl.png");
            ResourceLocation topRight = ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID,
                    "textures/hud/stealth/stealth_tr.png");
            ResourceLocation bottomLeft = ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID,
                    "textures/hud/stealth/stealth_bl.png");
            ResourceLocation bottomRight = ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID,
                    "textures/hud/stealth/stealth_br.png");

            /**
             * If the stealth is on, render an overlay.
             */
            if (cap.isStealthOn()) {


                int width = Minecraft.getInstance().getWindow().getGuiScaledWidth();
                int height = Minecraft.getInstance().getWindow().getGuiScaledHeight();

                int fragmentSize = 110;

                RenderSystem.enableBlend();
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                pGuiGraphics.blit(
                        screen2,
                        0, 0,
                        0, 0,
                        width, height,
                        width, height
                );
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.disableBlend();


                pGuiGraphics.blit(topLeft,
                        0, 0,
                        0, 0,
                        fragmentSize + 20, fragmentSize + 20,
                        fragmentSize+ 20, fragmentSize+ 20
                );
                pGuiGraphics.blit(topRight,
                        width - fragmentSize - 20, 0,
                        0, 0,
                        fragmentSize+ 20, fragmentSize+ 20,
                        fragmentSize+ 20, fragmentSize+ 20
                );

                pGuiGraphics.blit(bottomLeft,
                        0, height - fragmentSize,
                        0, 0,
                        fragmentSize, fragmentSize,
                        fragmentSize, fragmentSize
                );

                pGuiGraphics.blit(bottomRight,
                        width - fragmentSize, height - fragmentSize,
                        0, 0,
                        fragmentSize, fragmentSize,
                        fragmentSize, fragmentSize
                );

            }
        });

    });
}
