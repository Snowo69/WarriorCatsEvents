package net.snowteb.warriorcats_events.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.snowteb.warriorcats_events.WCEConfig;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.CtSFishFailed;
import net.snowteb.warriorcats_events.network.packet.c2s.CtSFishSuccesful;
import org.lwjgl.glfw.GLFW;

public class FishingScreen extends Screen {
    private static final ResourceLocation TEXTUREBACKGROUND =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/fishing/fishing_gui_1.png");
    private static final ResourceLocation TEXTUREWAVES =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/fishing/fishing_gui_2.png");

    private static final ResourceLocation TEXTUREFISH_1 =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/fishing/fish_1_gui.png");
    private static final ResourceLocation TEXTUREFISH_2 =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/fishing/fish_2_gui.png");
    private static final ResourceLocation TEXTUREFISH_3 =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/fishing/fish_3_gui.png");
    private static final ResourceLocation TEXTUREFISH_4 =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/fishing/fish_4_gui.png");
    private static final ResourceLocation TEXTUREFISH_5 =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/fishing/fish_5_gui.png");
    private static final ResourceLocation TEXTUREFISH_6 =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/fishing/fish_6_gui.png");
    private static final ResourceLocation TEXTUREFISH_7 =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/fishing/fish_7_gui.png");
    private static final ResourceLocation TEXTUREFISH_8 =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/fishing/fish_8_gui.png");



    private static final ResourceLocation TEXTURECLAWOVERLAY =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/fishing/fishing_gui_4.png");

    private ResourceLocation currentFishTexture;

    private int imageHeight;
    private int imageWidth;
    Player pPlayer = Minecraft.getInstance().player;
    private int randomTiming;
    private int randomYOffset;
    private int randomFish;


    private double offset = 0;
    private int offsetWaves = 0;
    private int offsetWaves2 = 0;
    boolean clicked = false;

    public FishingScreen() {
        super(Component.literal("Fishing"));
    }

    @Override
    protected void init() {
        this.imageWidth = 176;
        this.imageHeight = 48;
        this.randomTiming = 20 * 15 + RandomSource.create().nextInt(10) * 20;
        this.randomYOffset = -(RandomSource.create().nextInt(16) * 2);
        this.randomFish = RandomSource.create().nextInt(8) + 1;

        switch (randomFish) {
            case 1 -> currentFishTexture = TEXTUREFISH_1;
            case 2 -> currentFishTexture = TEXTUREFISH_2;
            case 3 -> currentFishTexture = TEXTUREFISH_3;
            case 4 -> currentFishTexture = TEXTUREFISH_4;
            case 5 -> currentFishTexture = TEXTUREFISH_5;
            case 6 -> currentFishTexture = TEXTUREFISH_6;
            case 7 -> currentFishTexture = TEXTUREFISH_7;
            case 8 -> currentFishTexture = TEXTUREFISH_8;
            default -> currentFishTexture = TEXTUREFISH_1;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        int guiX = ((width - imageWidth) / 2);
        int guiY = ((height - imageHeight - 145 + WCEConfig.COMMON.FISHING_SCREEN_Y_OFFSET.get()) / 2);

        int scissorX = guiX + 2;
        int scissorY = guiY + 2;
        int scissorX2 = guiX + 174;
        int scissorY2 = guiY + 46;


        guiGraphics.blit(TEXTUREBACKGROUND, guiX, guiY, 0, 0, imageWidth, imageHeight, 176, 48);
        guiGraphics.enableScissor(
                scissorX,
                scissorY,
                scissorX2,
                scissorY2
        );
        guiGraphics.blit(TEXTUREWAVES, guiX + offsetWaves + 176, guiY, 0, 0, imageWidth, imageHeight, 176, 48);
        guiGraphics.blit(TEXTUREWAVES, guiX + offsetWaves2, guiY, 0, 0, imageWidth, imageHeight, 176, 48);
        guiGraphics.blit(currentFishTexture, guiX + (int) (offset + 176), guiY + this.randomYOffset, 0, 0, imageWidth, imageHeight, 176, 48);

        guiGraphics.blit(TEXTURECLAWOVERLAY, guiX, guiY, 0, 0, imageWidth, imageHeight, 176, 48);

        guiGraphics.disableScissor();

        guiGraphics.drawString(
                this.font,
                "Fishing",
                guiX + 71,
                guiY - 11,
                0xFFFFFF
        );
        guiGraphics.drawString(
                this.font,
                "Click any part of the screen to grab fish",
                guiX + 9,
                guiY + 51,
                0xe0dcd1
        );

        if (pPlayer.tickCount % 2 == 0) {
            this.offsetWaves--;
            this.offsetWaves2--;
            if (this.offsetWaves <= -352) {
                this.offsetWaves = 0;
            }
            if (this.offsetWaves2 <= -176) {
                this.offsetWaves2 = 176;
            }
        }

        if (this.randomTiming <= 0) {
            this.offset -= 1.25;
            if ((this.offset <= -196)) {
                this.offset = 0;
                Minecraft.getInstance().setScreen(null);
                if (pPlayer != null) {
                    pPlayer.displayClientMessage(
                            Component.literal("¡You missed the catch!").withStyle(ChatFormatting.GRAY),
                            true
                    );
                }
            }
        }



        super.render(guiGraphics, mouseX, mouseY, partialTick);

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        if (this.randomTiming > 0) {
            this.randomTiming--;
        }

        // DEBUG
//        {
//            if (this.offset <= -88 && this.offset > -98 && this.fishTimer <= 5 && this.fishTimer > 0) {
//                if (pPlayer != null) {
//                    pPlayer.displayClientMessage(
//                            Component.literal("¡Appears!").withStyle(ChatFormatting.GREEN),
//                            false
//                    );
//                }
//            }
//            if (this.offset <= -98 && this.fishTimer <= 5 && this.fishTimer > 0) {
//                if (pPlayer != null) {
//                    pPlayer.displayClientMessage(
//                            Component.literal("¡Goes!").withStyle(ChatFormatting.RED),
//                            false
//                    );
//                }
//            }
//        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        clicked = true;

        if (this.offset <= -90 && this.offset > -103) {

            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.displayClientMessage(
                        Component.literal("¡Perfect catch!").withStyle(ChatFormatting.DARK_AQUA),
                        true
                );
                ModPackets.sendToServer(new CtSFishSuccesful());

            }

            Minecraft.getInstance().setScreen(null);
            return true;
        } else {
            Minecraft.getInstance().setScreen(null);
            if (pPlayer != null) {
                pPlayer.displayClientMessage(
                        Component.literal("¡You missed the catch!").withStyle(ChatFormatting.GRAY),
                        true
                );
                ModPackets.sendToServer(new CtSFishFailed());
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == GLFW.GLFW_KEY_E) {
            this.onClose();
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
