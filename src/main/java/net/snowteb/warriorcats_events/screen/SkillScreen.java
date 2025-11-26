package net.snowteb.warriorcats_events.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.*;
import net.snowteb.warriorcats_events.skills.ISkillData;
import net.snowteb.warriorcats_events.skills.PlayerSkill;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import net.snowteb.warriorcats_events.util.ModButton;

public class SkillScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/skilltree_gui.png");
    String nextSpeedLevelCost;
    String nextHPLevelCost;
    String stealthCost;
    private int imageHeight;
    private int imageWidth;


    public SkillScreen() {
        super(Component.literal("Skills"));
    }

    @Override
    protected void init() {

        this.imageWidth = 256;
        this.imageHeight = 166;

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(TEXTURE,
                (width - imageWidth) / 2,
                (height - imageHeight - 50) / 2,
                0, 0,
                imageWidth, imageHeight,
                256, 166
        );


        LocalPlayer player = this.minecraft.player;
        if (player != null) {
            LazyOptional<ISkillData> cap = player.getCapability(PlayerSkillProvider.SKILL_DATA);
            cap.ifPresent(data -> {

                nextSpeedLevelCost = getNextLevelCost(data.getSpeedLevel(), PlayerSkill.maxSpeedLevel, PlayerSkill.defaultSpeedCost);
                nextHPLevelCost = getNextLevelCost(data.getHPLevel(), PlayerSkill.maxHPLevel, PlayerSkill.defaultHPcost);

                player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(stealth -> {

                     if (stealth.isUnlocked()) {
                        stealthCost = "MAX";
                           } else {
                                stealthCost = String.valueOf(PlayerSkill.defaultStealthcost);
                           }

                });


                    }
            );
        }

        this.clearWidgets();
        this.addRenderableWidget(
                Button.builder(Component.literal("Speed +1: " + nextSpeedLevelCost).withStyle(ChatFormatting.AQUA), button -> {
            ModPackets.sendToServer(new CtSMoreSpeedPacket());
                    ModPackets.sendToServer(new ReqSkillDataPacket());

                }).bounds(this.width/2 - 40, this.height/2 - 10, 80, 20).build());

        this.addRenderableWidget(
                Button.builder(Component.literal("Reset Skills"), button -> {
                    if (player != null) {
                        ModPackets.sendToServer(new ResetSkillsPacket());
                        ModPackets.sendToServer(new ReqSkillDataPacket());

                    }
                }).bounds(this.width/2 - 40, this.height/2 + 20, 80, 20).build());

        this.addRenderableWidget(
                Button.builder(Component.literal("HP +1: " + nextHPLevelCost).withStyle(ChatFormatting.AQUA), button -> {
                    ModPackets.sendToServer(new CtSMoreHPPacket());
                    ModPackets.sendToServer(new ReqSkillDataPacket());

                }).bounds(this.width/2 - 40, this.height/2 + 50, 80, 20).build());

        this.addRenderableWidget(new ModButton(
                this.width / 2 - 40,
                this.height / 2 - 50,
                80, 20,
                Component.literal("This Button"),
                b ->  player.sendSystemMessage(Component.literal("Button Pressed").withStyle(ChatFormatting.RED)),
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/modbutton.png"),
                80, 20
        ));

        this.addRenderableWidget(new ModButton(
                this.width / 2 - 40,
                this.height / 2 - 80,
                80, 20,
                Component.literal("Stealth : " + stealthCost),
                b ->  ModPackets.sendToServer(new CtSUnlockStealthPacket()),
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/modbutton.png"),
                80, 20
        ));








        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private String getNextLevelCost(int level, int maxLevel, int defaultCost) {

        String result;
        if (level < maxLevel) {
            result = String.valueOf((defaultCost * (level + 1)));
        }
        else {
            result = "MAX";
        }
        return result;
    }

}
