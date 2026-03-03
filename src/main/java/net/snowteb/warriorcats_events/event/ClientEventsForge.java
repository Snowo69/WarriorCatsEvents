package net.snowteb.warriorcats_events.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.block.custom.MossBedBlock;
import net.snowteb.warriorcats_events.client.LeapClientState;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.skilltree.ReqSkillDataPacket;
import net.snowteb.warriorcats_events.screen.EmoteMenu;
import net.snowteb.warriorcats_events.screen.SkillScreen;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import tocraft.walkers.api.PlayerShape;

import static net.snowteb.warriorcats_events.WCEClient.isRenderingEmoteMenu;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventsForge {

    private static Button extraButton;

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen screen)) return;

        int x = screen.getGuiLeft() + screen.getXSize() - 152;
        int y = screen.getGuiTop() - 18;

        extraButton = Button.builder(
                Component.literal("Skill tree"),
                btn -> {
                    Minecraft.getInstance().setScreen(new SkillScreen());
                    ModPackets.sendToServer(new ReqSkillDataPacket());
                }
        ).bounds(x, y, 52, 16).build();

        event.addListener(extraButton);
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof InventoryScreen)) return;
        if (extraButton == null) return;

        AbstractContainerScreen<?> container =
                (AbstractContainerScreen<?>) event.getScreen();

        int x = container.getGuiLeft() + container.getXSize() - 152;
        int y = container.getGuiTop() - 18;

        extraButton.setPosition(x, y);
    }



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
                        if (player != Minecraft.getInstance().player) {
                            poseStack.translate(0.0D, -0.6D, 0.0D);
                            player.setOnGround(true);
                        }
                        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));

                        player.swinging = false;
                    }

                } else if (player.level()
                        .getBlockState(bedPos)
                        .getBlock() instanceof BedBlock) {

                    PoseStack poseStack = event.getPoseStack();

                    if (PlayerShape.getCurrentShape(player) instanceof Animal) {
                        poseStack.pushPose();

                        poseStack.translate(0.5D, 0.1D, -0.3D);
                        poseStack.scale(0.95F, 0.95F, 0.95F);

                        if (player != Minecraft.getInstance().player) {
                            poseStack.translate(0.0D, -0.15D, 0.0D);
                            player.setOnGround(true);
                        }
                        BlockState state = player.level().getBlockState(bedPos);
                        Direction facing = state.getValue(BedBlock.FACING);

                        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                        if (facing == Direction.WEST) {

                        } else if (facing == Direction.EAST) {
                            poseStack.translate(0.0D, 1D, 0.0D);
                        } else if (facing == Direction.SOUTH) {
                            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                            poseStack.translate(1D, -0.0D, 0.0D);
                        } else if (facing == Direction.NORTH) {
                            poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
                            poseStack.translate(-0D, -1.0D, 0.0D);
                        }


                        player.swinging = false;
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

                } else if (player.level()
                        .getBlockState(bedPos)
                        .getBlock() instanceof BedBlock) {

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

        if (isRenderingEmoteMenu) {
            new EmoteMenu().render(event.getGuiGraphics());
        }

        if (LeapClientState.getLeapPowerCounter() <= 0) return;
        if (!LeapClientState.isLeapActive()) return;

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
