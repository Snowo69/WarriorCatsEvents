package net.snowteb.warriorcats_events.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.AnimationClientData;
import net.snowteb.warriorcats_events.client.ClientClanData;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.integration.WCatTypeProvider;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.CtSSwitchShape;
import net.snowteb.warriorcats_events.util.ModButton;

@OnlyIn(Dist.CLIENT)
public class EmoteWheelScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/emote_wheel.png");

    private int imageHeight;
    private int imageWidth;
    private String hoverText = "";

    private ModButton emShape;
    private ModButton em1;
    private ModButton em2;
    private ModButton em3;
    private ModButton em4;
    private ModButton em5;
    private ModButton em6;

    public EmoteWheelScreen() {
        super(Component.literal("Emotes"));
    }

    @Override
    protected void init() {
        this.imageWidth = 208;
        this.imageHeight = 208;




    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(TEXTURE,
                (width - imageWidth) / 2,
                (height - imageHeight - 15) / 2,
                0, 0,
                imageWidth, imageHeight,
                208, 208
        );
        guiGraphics.drawString(
                this.font,
                "Emotes",
                this.width / 2 - 16,
                this.height / 2 - 120,
                0xFFFFFF
        );

        LocalPlayer player = this.minecraft.player;
        this.clearWidgets();
        emShape = this.addRenderableWidget(new ModButton(
                this.width / 2 - 15,
                this.height / 2 + 58,
                30, 30,
                Component.literal(""),
                btn -> {
                    ModPackets.sendToServer(new CtSSwitchShape());
                    this.minecraft.setScreen(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/button_empty.png"),
                30, 30
        ));
        em1 = this.addRenderableWidget(new ModButton(
                this.width / 2 - 15,
                this.height / 2 - 104,
                30, 30,
                Component.literal(""),
                btn -> {
                    AnimationClientData.anim1 = 1;
                    this.minecraft.setScreen(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/button_empty.png"),
                30, 30
        ));
        em2 = this.addRenderableWidget(new ModButton(
                this.width / 2 - 68,
                this.height / 2 - 76,
                30, 30,
                Component.literal(""),
                btn -> {
                    AnimationClientData.anim2 = 1;
                    this.minecraft.setScreen(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/button_empty.png"),
                30, 30
        ));
        em3 = this.addRenderableWidget(new ModButton(
                this.width / 2 - 96,
                this.height / 2 - 23,
                30, 30,
                Component.literal(""),
                btn -> {
                    AnimationClientData.anim3 = 1;
                    this.minecraft.setScreen(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/button_empty.png"),
                30, 30
        ));
        em4 = this.addRenderableWidget(new ModButton(
                this.width / 2 - 68,
                this.height / 2 + 29,
                30, 30,
                Component.literal(""),
                btn -> {
                    AnimationClientData.anim4 = 1;
                    this.minecraft.setScreen(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/button_empty.png"),
                30, 30
        ));
        em5 = this.addRenderableWidget(new ModButton(
                this.width / 2 + 38,
                this.height / 2 + 28,
                30, 30,
                Component.literal(""),
                btn -> {
                    AnimationClientData.anim5 = 1;
                    this.minecraft.setScreen(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/button_empty.png"),
                30, 30
        ));
        em6 = this.addRenderableWidget(new ModButton(
                this.width / 2 + 66,
                this.height / 2 - 24,
                30, 30,
                Component.literal(""),
                btn -> {
                    AnimationClientData.anim6 = 1;
                    this.minecraft.setScreen(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/button_empty.png"),
                30, 30
        ));





        ResourceLocation ARROW = new ResourceLocation(WarriorCatsEvents.MODID,
                "textures/gui/emote_arrow.png");


        int centerX = this.width / 2 + 1;
        int centerY = this.height / 2 - 8;

        double dx = mouseX - centerX;
        double dy = mouseY - centerY;
        double angleRad = Math.atan2(dy, dx);
        double angleDeg = Math.toDegrees(angleRad);

        angleDeg += 90;

        int arrowW = 32;
        int arrowH = 32;
        int arrowX = centerX - arrowW / 2;
        int arrowY = centerY - 55;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(centerX, centerY, 0);
        guiGraphics.pose().mulPose(com.mojang.math.Axis.ZP.rotationDegrees((float) angleDeg));
        guiGraphics.pose().translate(-centerX, -centerY, 0);

        guiGraphics.blit(
                ARROW,
                arrowX,
                arrowY,
                0, 0,
                arrowW, arrowH,
                arrowW, arrowH
        );
        guiGraphics.pose().popPose();





        super.render(guiGraphics, mouseX, mouseY, partialTick);





        hoverText = "";
        if (emShape != null && emShape.isMouseOver(mouseX, mouseY)) {
            hoverText = "Morph";
        }
        if (em1 != null && em1.isMouseOver(mouseX, mouseY)) {
            hoverText = "Groom";
        }
        if (em2 != null && em2.isMouseOver(mouseX, mouseY)) {
            hoverText = "Stretch";
        }
        if (em3 != null && em3.isMouseOver(mouseX, mouseY)) {
            hoverText = "Scratch";
        }
        if (em4 != null && em4.isMouseOver(mouseX, mouseY)) {
            hoverText = "Attack";
        }
        if (em5 != null && em5.isMouseOver(mouseX, mouseY)) {
            hoverText = "Stand";
        }
        if (em6 != null && em6.isMouseOver(mouseX, mouseY)) {
            hoverText = "Lay";
        }

        if (!hoverText.isEmpty()) {
            guiGraphics.drawString(
                    this.font,
                    hoverText,
                    this.width / 2 - this.font.width(hoverText) / 2,
                    this.height / 2 - 12,
                    0xFFFFFF
            );
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }



}
