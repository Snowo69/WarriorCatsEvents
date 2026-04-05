package net.snowteb.warriorcats_events.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.LeapClientState;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.skilltree.ReqSkillDataPacket;
import net.snowteb.warriorcats_events.screen.EmoteMenu;
import net.snowteb.warriorcats_events.screen.SkillScreen;
import net.snowteb.warriorcats_events.screen.clandata.PlaySoundMenu;
import net.snowteb.warriorcats_events.sound.ModSounds;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import tocraft.walkers.api.PlayerShape;

import static net.minecraft.client.renderer.LevelRenderer.getLightColor;
import static net.snowteb.warriorcats_events.WCEClient.*;

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
             {
                if (PlayerShape.getCurrentShape(player) instanceof WCatEntity) {
                    PoseStack poseStack = event.getPoseStack();
                    poseStack.pushPose();
                    poseStack.scale(0.95F, 0.95F, 0.95F);
                    poseStack.translate(0.0D, 0.0D, -0.0D);

                    if (player != Minecraft.getInstance().player) {
                        poseStack.translate(0.0D, -0.6D, 0.0D);
                        player.setOnGround(true);
                    }
                    poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                    poseStack.mulPose(Axis.YP.rotationDegrees(180f));

                    player.swinging = false;
                }

//                if (player.level()
//                        .getBlockState(bedPos)
//                        .getBlock() instanceof MossBedBlock) {
//
//                    PoseStack poseStack = event.getPoseStack();
//                    poseStack.pushPose();
//                    poseStack.scale(0.95F, 0.95F, 0.95F);
//                    poseStack.translate(0.0D, 0.0D, -0.0D);
//
//                    if (PlayerShape.getCurrentShape(player) instanceof Animal) {
//                        if (player != Minecraft.getInstance().player) {
//                            poseStack.translate(0.0D, -0.6D, 0.0D);
//                            player.setOnGround(true);
//                        }
//                        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
//                        poseStack.mulPose(Axis.YP.rotationDegrees(180f));
//
//                        player.swinging = false;
//                    }
//
//                } else if (player.level()
//                        .getBlockState(bedPos)
//                        .getBlock() instanceof BedBlock) {
//
//                    PoseStack poseStack = event.getPoseStack();
//
//                    if (PlayerShape.getCurrentShape(player) instanceof Animal) {
//                        poseStack.pushPose();
//
//                        poseStack.translate(0.5D, 0.1D, -0.3D);
//                        poseStack.scale(0.95F, 0.95F, 0.95F);
//
//                        if (player != Minecraft.getInstance().player) {
//                            poseStack.translate(0.0D, -0.15D, 0.0D);
//                            player.setOnGround(true);
//                        }
//                        BlockState state = player.level().getBlockState(bedPos);
//                        Direction facing = state.getValue(BedBlock.FACING);
//
//                        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
//                        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
//                        if (facing == Direction.WEST) {
//
//                        } else if (facing == Direction.EAST) {
//                            poseStack.translate(0.0D, 1D, 0.0D);
//                        } else if (facing == Direction.SOUTH) {
//                            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
//                            poseStack.translate(1D, -0.0D, 0.0D);
//                        } else if (facing == Direction.NORTH) {
//                            poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
//                            poseStack.translate(-0D, -1.0D, 0.0D);
//                        }
//
//
//                        player.swinging = false;
//                    }
//
//                }
            };
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();

        if (player.isSleeping()) {
            {

                if (PlayerShape.getCurrentShape(player) instanceof WCatEntity)  event.getPoseStack().popPose();


//                if (player.level()
//                        .getBlockState(bedPos)
//                        .getBlock() instanceof MossBedBlock) {
//
//                    event.getPoseStack().popPose();
//
//                } else if (player.level()
//                        .getBlockState(bedPos)
//                        .getBlock() instanceof BedBlock) {
//
//                    if (PlayerShape.getCurrentShape(player) instanceof Animal) {
//                        event.getPoseStack().popPose();
//                    }
//                }
            };
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
        } else if (isRenderingSoundMenu) {
            new PlaySoundMenu().render(event.getGuiGraphics());
        }

        if (isBeingLatched && setFreeCounter < 199) {
            mcinstance.player.displayClientMessage(Component.literal(""), true);
            GuiGraphics guiGraphics = event.getGuiGraphics();
            int width = mcinstance.getWindow().getGuiScaledWidth();
            int height = mcinstance.getWindow().getGuiScaledHeight();
            int centerX = width / 2;
            int centerY = height / 2 + 10;

            ResourceLocation emptyBar = new ResourceLocation(WarriorCatsEvents.MODID,
                    "textures/hud/escape_bar_empty.png");
            ResourceLocation fillBar = new ResourceLocation(WarriorCatsEvents.MODID,
                    "textures/hud/escape_bar_fill.png");

            int toFill = centerX - 100 + setFreeCounter;

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            guiGraphics.blit(emptyBar,centerX-105, centerY + 20, 0,0,210, 24, 210, 24);
            RenderSystem.disableBlend();


            guiGraphics.enableScissor(centerX - 100, centerY + 23, toFill, centerY + 43);
            guiGraphics.blit(fillBar,centerX-100, centerY + 23, 0,0,200, 18,200, 18);
            guiGraphics.disableScissor();

            guiGraphics.drawCenteredString(Minecraft.getInstance().font,"Press 'Left Shift' to unlatch", centerX, centerY + 28, 0xFFFFFF );

        }

        if (!lookingAtParticle.isEmpty()) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            int width = mcinstance.getWindow().getGuiScaledWidth();
            int height = mcinstance.getWindow().getGuiScaledHeight();
            int centerX = width / 2;
            int centerY = height / 2;

            Component text = Component.literal("The footprint smells like " + lookingAtParticle).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);

            float scale = 1f;

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(centerX, height - 70, 0);
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.drawCenteredString(Minecraft.getInstance().font, text, 0, 0, 0);
            guiGraphics.pose().popPose();

            if (newParticleTime <= 0) lookingAtParticle = "";
        }

        if (LeapClientState.getSprintingCounter() > 100) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            int width = mcinstance.getWindow().getGuiScaledWidth();
            int height = mcinstance.getWindow().getGuiScaledHeight();

            ResourceLocation emptyBar = new ResourceLocation(WarriorCatsEvents.MODID,
                    "textures/hud/sprintbar_empty.png");
            ResourceLocation fillBar = new ResourceLocation(WarriorCatsEvents.MODID,
                    "textures/hud/sprintbar_fill.png");
            ResourceLocation readyBar = new ResourceLocation(WarriorCatsEvents.MODID,
                    "textures/hud/sprintbar_ready.png");



            guiGraphics.blit(
                    emptyBar,
                    width - 16, height - 106,
                    0, 0,
                    14, 103,
                    14, 103
            );

            int centerX = width / 2;
            int centerY = height / 2;
            int sprintPower = (int) (LeapClientState.getSprintingCounter() - 100);

            float sprintPowerPercentage = (float) sprintPower / 200;

            guiGraphics.enableScissor(width - 16, (int) (height - (106 * sprintPowerPercentage)), width - 2, height);
            guiGraphics.blit(
                    fillBar,
                    width - 16, height - 106,
                    0, 0,
                    14, 103,
                    14, 103
            );
            guiGraphics.disableScissor();

            if (sprintPowerPercentage >= 0.9) {
                guiGraphics.blit(
                        readyBar,
                        width - 16, height - 106,
                        0, 0,
                        14, 103,
                        14, 103
                );
            }

        }

        if (LeapClientState.getLeapPowerCounter() <= 0) return;
        if (!LeapClientState.isLeapActive()) return;
        if (LeapClientState.getSprintingCounter() > 200) return;

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



    @SubscribeEvent
    public static void renderWind(RenderLevelStageEvent event) {

        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_WEATHER) return;

        if (LeapClientState.getSprintingCounter() < 200) return;

        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 pos = cam.getPosition();

        renderWindEffect(Minecraft.getInstance().gameRenderer.lightTexture(),
                event.getPartialTick(), pos.x, pos.y, pos.z);
    }

    private static final ResourceLocation WIND = new ResourceLocation(WarriorCatsEvents.MODID, "textures/environment/wind.png");

    private static final float[] windSizeX = new float[1024];
    private static final float[] windSizeZ = new float[1024];

    static {
        for (int i = 0; i < 32; ++i) {
            for (int j = 0; j < 32; ++j) {
                float f = j - 16;
                float f1 = i - 16;
                float f2 = Mth.sqrt(f * f + f1 * f1);

                windSizeX[i << 5 | j] = -f1 / f2;
                windSizeZ[i << 5 | j] =  f  / f2;
            }
        }
    }

    private static double smoothSpeed = 0;

    private static void renderWindEffect(LightTexture pLightTexture, float pPartialTick, double pCamX, double pCamY, double pCamZ) {
        Minecraft minecraft = Minecraft.getInstance();
        int ticks = Minecraft.getInstance().player.tickCount;

        float f = ((LeapClientState.getSprintingCounter()-200) / 100f);

        if (LeapClientState.getSprintCounterThreshold() < 5 && minecraft.player.onGround()) {
            f = (float) LeapClientState.getSprintCounterThreshold() /8;
        }

        WCEClient.playLocalSound(ModSounds.STEALTH_WOOSH.get(), SoundSource.AMBIENT, f*0.10f, f*minecraft.player.getRandom().nextFloat());

        if (!(f <= 0.0f)) {
            pLightTexture.turnOnLightLayer();
            Level level = minecraft.level;
            int i = Mth.floor(pCamX);
            int j = Mth.floor(pCamY);
            int k = Mth.floor(pCamZ);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            int l = 2;

            RenderSystem.depthMask(Minecraft.useShaderTransparency());
            int i1 = -1;
            float f1 = (float)ticks + pPartialTick;
            RenderSystem.setShader(GameRenderer::getParticleShader);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            double targetSpeed = minecraft.player.getDeltaMovement().length() * 10.0;
            smoothSpeed = Mth.lerp(pPartialTick, smoothSpeed, targetSpeed);

            for(int j1 = k - l; j1 <= k + l; ++j1) {
                for(int k1 = i - l; k1 <= i + l; ++k1) {
                    int l1 = (j1 - k + 16) * 32 + k1 - i + 16;
                    double d0 = (double)windSizeX[l1] * 0.5D;
                    double d1 = (double)windSizeZ[l1] * 0.5D;
                    blockpos$mutableblockpos.set(k1, pCamY, j1);

                    int j2 = j - l;
                    int k2 = j + l;
                    int l2 = j;

                        if (j2 != k2) {
                            RandomSource randomsource = RandomSource.create((long)(k1 * k1 * 3121 + k1 * 45238971 ^ j1 * j1 * 418711 + j1 * 13761));
                            blockpos$mutableblockpos.set(k1, j2, j1);

                            {
                                if (i1 != 1) {
                                    if (i1 >= 0) {
                                        tesselator.end();
                                    }

                                    i1 = 1;
                                    RenderSystem.setShaderTexture(0, WIND);
                                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                                }

                                float f5 = 0.0F;

                                float f6 = 0;
                                float f7 = (float)(randomsource.nextDouble() + (double)(f1) * (minecraft.player.getDeltaMovement().length() > 0.15 ? -0.01D : -0.0005D));
                                double d3 = (double)k1 + 0.5D - pCamX;
                                double d5 = (double)j1 + 0.5D - pCamZ;
                                float f8 = (float)Math.sqrt(d3 * d3 + d5 * d5) / (float)l;
                                float f9 = ((1.0F - f8 * f8) * 0.3F + 0.5F) * f;
                                blockpos$mutableblockpos.set(k1, l2, j1);
                                int k3 = getLightColor(level, blockpos$mutableblockpos);
                                int l3 = k3 >> 16 & '\uffff';
                                int i4 = k3 & '\uffff';
                                int j4 = (l3 * 3 + 240) / 4;
                                int k4 = (i4 * 3 + 240) / 4;
                                bufferbuilder.vertex((double)k1 - pCamX - d0 + 0.5D, (double)k2 - pCamY, (double)j1 - pCamZ - d1 + 0.5D).uv(0.0F + f6, (float)j2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                                bufferbuilder.vertex((double)k1 - pCamX + d0 + 0.5D, (double)k2 - pCamY, (double)j1 - pCamZ + d1 + 0.5D).uv(1.0F + f6, (float)j2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                                bufferbuilder.vertex((double)k1 - pCamX + d0 + 0.5D, (double)j2 - pCamY, (double)j1 - pCamZ + d1 + 0.5D).uv(1.0F + f6, (float)k2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                                bufferbuilder.vertex((double)k1 - pCamX - d0 + 0.5D, (double)j2 - pCamY, (double)j1 - pCamZ - d1 + 0.5D).uv(0.0F + f6, (float)k2 * 0.25F + f5 + f7).color(1.0F, 1.0F, 1.0F, f9).uv2(k4, j4).endVertex();
                            }
                        }

                }
            }

            if (i1 >= 0) {
                tesselator.end();
            }

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            pLightTexture.turnOffLightLayer();
        }
    }
}
