package net.snowteb.warriorcats_events.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.custom.MossBedBlock;
import net.snowteb.warriorcats_events.client.LeapClientState;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import tocraft.walkers.api.PlayerShape;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventsForge {

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();

        if (player.isSleeping()) {
            player.getSleepingPos().ifPresent(bedPos -> {

                if (player.level()
                        .getBlockState(bedPos)
                        .getBlock() instanceof MossBedBlock) {

                    PoseStack poseStack = event.getPoseStack();
                    poseStack.pushPose();
                    poseStack.scale(0.95F, 0.95F, 0.95F);
                    poseStack.translate(0.0D, 0.1D, -0.3D);

                    if (PlayerShape.getCurrentShape(player) instanceof Animal) {
                        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                    }

                }
            });
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();

        if (player.isSleeping()) {
            player.getSleepingPos().ifPresent(bedPos -> {

                if (player.level()
                        .getBlockState(bedPos)
                        .getBlock() instanceof MossBedBlock) {

                    event.getPoseStack().popPose();
                }
            });
        }
    }


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

    private static ResourceLocation currentMouseTexture =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/mouse_unclicked.png");
    private static ResourceLocation currentRightMouseTexture =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/hud/mouse_right_unclicked.png");

    private static int lastToggleTick = 0;


    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        Minecraft mcinstance = Minecraft.getInstance();
        if (player == null) return;
        if (!mcinstance.isWindowActive()) return;
        if (mcinstance.screen != null) return;
        if (mcinstance.level == null) return;

        if (LeapClientState.getLeapPowerCounter() <= 0) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();

        int width = mcinstance.getWindow().getGuiScaledWidth();
        int height = mcinstance.getWindow().getGuiScaledHeight();

        ResourceLocation emptyBar = new ResourceLocation(WarriorCatsEvents.MODID,
                "textures/hud/leapbar_empty.png");
        ResourceLocation fillBar = new ResourceLocation(WarriorCatsEvents.MODID,
                "textures/hud/leapbar_fill.png");
        ResourceLocation mouseClick = new ResourceLocation(WarriorCatsEvents.MODID,
                "textures/hud/mouse_clicked.png");
        ResourceLocation mouseUnclick = new ResourceLocation(WarriorCatsEvents.MODID,
                "textures/hud/mouse_unclicked.png");
        ResourceLocation mouseRightClick = new ResourceLocation(WarriorCatsEvents.MODID,
                "textures/hud/mouse_right_clicked.png");
        ResourceLocation mouseRightUnclick = new ResourceLocation(WarriorCatsEvents.MODID,
                "textures/hud/mouse_right_unclicked.png");

        ResourceLocation unlockedCross = new ResourceLocation(WarriorCatsEvents.MODID,
                "textures/hud/targetnotlocked_cross.png");
        ResourceLocation lockedCross = new ResourceLocation(WarriorCatsEvents.MODID,
                "textures/hud/targetlocked_cross.png");

        int leapPower = LeapClientState.getLeapPowerCounter();

        ResourceLocation currentCrossTexture = LeapClientState.isLockingTarget() ? lockedCross : unlockedCross;

        guiGraphics.blit(
                emptyBar,
                width - 16, height - 106,
                0, 0,
                14, 103,
                14, 103
        );

        int centerX = width / 2;
        int centerY = height / 2;

        float leapPowerPercentage = (float) leapPower / 100;

        guiGraphics.enableScissor(width - 16, (int) (height - (106 * leapPowerPercentage)), width - 2, height);
        guiGraphics.blit(
                fillBar,
                width - 16, height - 106,
                0, 0,
                14, 103,
                14, 103
        );
        guiGraphics.disableScissor();

        int tick = mcinstance.player.tickCount;
        if (tick - lastToggleTick >= 10) {
            lastToggleTick = tick;
            currentMouseTexture = currentMouseTexture.equals(mouseClick) ? mouseUnclick : mouseClick;
            currentRightMouseTexture = currentRightMouseTexture.equals(mouseRightClick) ? mouseRightUnclick : mouseRightClick;
        }

        guiGraphics.blit(
                currentMouseTexture,
                width - 27, height - 22,
                0, 0,
                9, 19,
                9, 19

        );

        guiGraphics.blit(
                currentCrossTexture,
                centerX - 21, centerY - 21,
                0, 0,
                41, 41,
                41, 41
        );

        if (!LeapClientState.isLockingTarget() && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    "Lock Target",
                    4,
                    height - 15,
                    0xFFFFFF
            );
            guiGraphics.blit(
                    currentRightMouseTexture,
                    68, height - 24,
                    0,0,
                    9,19,
                    9,19
            );
        }

    }
}
