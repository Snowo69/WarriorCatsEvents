package net.snowteb.warriorcats_events.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.common.util.LazyOptional;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.*;
import net.snowteb.warriorcats_events.skills.ISkillData;
import net.snowteb.warriorcats_events.skills.PlayerSkill;
import net.snowteb.warriorcats_events.skills.PlayerSkillProvider;
import net.snowteb.warriorcats_events.stealth.IStealthData;
import net.snowteb.warriorcats_events.stealth.PlayerStealth;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;
import net.snowteb.warriorcats_events.util.ModButton;

import java.util.List;

public class SkillScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/skilltree_gui.png");
    String nextSpeedLevelCost;
    String nextHPLevelCost;
    String nextDMGLevelCost;
    String nextJumpLevelCost;
    String nextToughnessLevel;

    String stealthCost;
    Component stealthSwitchText;


    int currentSpeedLevel;
    int currentHPLevel;
    int currentDMGLevel;

    int currentJumpLevel;
    int currentToughnessLevel;


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
        guiGraphics.drawString(
                this.font,
                "Skill Tree",
                this.width / 2 - 22,
                this.height / 2 - 118,
                0xFFFFFF
        );
        guiGraphics.drawString(
                this.font,
                "Moonpool",
                this.width / 2 - 124,
                this.height / 2 + 45,
                0x20526885
        );


        LocalPlayer player = this.minecraft.player;

        if (player != null) {

            currentSpeedLevel = player.getCapability(PlayerSkillProvider.SKILL_DATA)
                    .map(ISkillData::getSpeedLevel)
                    .orElse(player.getPersistentData().getInt("skill_speed_level"));
            currentHPLevel = player.getCapability(PlayerSkillProvider.SKILL_DATA)
                    .map(ISkillData::getHPLevel)
                    .orElse(player.getPersistentData().getInt("skill_hp_level"));
            currentDMGLevel = player.getCapability(PlayerSkillProvider.SKILL_DATA)
                    .map(ISkillData::getDMGLevel)
                    .orElse(player.getPersistentData().getInt("skill_dmg_level"));
            currentJumpLevel = player.getCapability(PlayerSkillProvider.SKILL_DATA)
                    .map(ISkillData::getJumpLevel)
                    .orElse(player.getPersistentData().getInt("skill_jump_level"));
            currentToughnessLevel = player.getCapability(PlayerSkillProvider.SKILL_DATA)
                    .map(ISkillData::getArmorLevel)
                    .orElse(player.getPersistentData().getInt("skill_armor_level"));

            LazyOptional<ISkillData> cap = player.getCapability(PlayerSkillProvider.SKILL_DATA);
            cap.ifPresent(data -> {

                nextSpeedLevelCost = getNextLevelCost(data.getSpeedLevel(), PlayerSkill.maxSpeedLevel, PlayerSkill.getDefaultSpeedCost());
                nextHPLevelCost = getNextLevelCost(data.getHPLevel(), PlayerSkill.maxHPLevel, PlayerSkill.getDefaultHPcost());
                nextDMGLevelCost = getNextLevelCost(data.getDMGLevel(), PlayerSkill.maxDMGLevel, PlayerSkill.getDefaultDMGcost());
                nextJumpLevelCost = getNextLevelCost(data.getJumpLevel(), PlayerSkill.maxJumpLevel, PlayerSkill.getDefaultJumpcost());
                nextToughnessLevel = getNextLevelCost(data.getArmorLevel(), PlayerSkill.maxArmorLevel, PlayerSkill.getDefaultArmorcost());

                player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(stealth -> {

                     if (stealth.isUnlocked()) {
                        stealthCost = "MAX";
                     } else {
                         stealthSwitchText = Component.literal("--");
                         stealthCost = PlayerSkill.getDefaultStealthcost() + "xp";
                     }

                     if (stealth.isOn()) {
                         stealthSwitchText = Component.literal("On").withStyle(ChatFormatting.GREEN);
                     } else {
                         stealthSwitchText = Component.literal("Off").withStyle(ChatFormatting.YELLOW);
                     }


                });

            }
            );


        }

        this.clearWidgets();

        this.addRenderableWidget(new ModButton(
                this.width / 2 - 40,
                this.height / 2 - 17,
                80, 20,
                Component.literal("Stealth : " + stealthCost),
                b ->  ModPackets.sendToServer(new CtSUnlockStealthPacket()),
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/modbutton.png"),
                80, 20
        ));

        this.addRenderableWidget(new ModButton(
                this.width / 2 + 43,
                this.height / 2 + 33,
                80, 20,
                Component.literal("Reset Tree"),
                b ->  {
                    ModPackets.sendToServer(new ResetSkillsPacket());
                    ModPackets.sendToServer(new ReqSkillDataPacket());},
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/modbutton.png"),
                80, 20
        ));

        this.addRenderableWidget(new ModButton(
                this.width / 2 + 45,
                this.height / 2 - 97,
                80, 20,
                Component.literal("+1 Claws | " + nextDMGLevelCost ),
                b ->  {
                    ModPackets.sendToServer(new CtSMoreDMGPacket());
                    ModPackets.sendToServer(new ReqSkillDataPacket());},
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/modbutton.png"),
                80, 20
        ));

        this.addRenderableWidget(new ModButton(
                this.width / 2 - 40,
                this.height / 2 - 97,
                80, 20,
                Component.literal("+1 HP | " + nextHPLevelCost ),
                b ->  {
                    ModPackets.sendToServer(new CtSMoreHPPacket());
                    ModPackets.sendToServer(new ReqSkillDataPacket());},
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/modbutton.png"),
                80, 20
        ));

        this.addRenderableWidget(new ModButton(
                this.width / 2 - 124,
                this.height / 2 - 97,
                80, 20,
                Component.literal("+1 Speed | " + nextSpeedLevelCost ),
                b ->  {
                    ModPackets.sendToServer(new CtSMoreSpeedPacket());
                    ModPackets.sendToServer(new ReqSkillDataPacket());},
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/modbutton.png"),
                80, 20
        ));
        this.addRenderableWidget(new ModButton(
                this.width / 2 - 81,
                this.height / 2 - 57,
                80, 20,
                Component.literal("+1 Jump | " + nextJumpLevelCost),
                b ->  {
                    ModPackets.sendToServer(new CtSMoreJumpPacket());
                    ModPackets.sendToServer(new ReqSkillDataPacket());},
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/modbutton.png"),
                80, 20
        ));
        this.addRenderableWidget(new ModButton(
                this.width / 2 + 2,
                this.height / 2 - 57,
                80, 20,
                Component.literal("+1 Pelt | " + nextToughnessLevel),
                b ->  {
                    ModPackets.sendToServer(new CtSMoreArmorPacket());
                    ModPackets.sendToServer(new ReqSkillDataPacket());},
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/modbutton.png"),
                80, 20
        ));
        this.addRenderableWidget(new ModButton(
                this.width / 2 + 45,
                this.height / 2 - 17,
                20, 20,
                stealthSwitchText,
                b ->  {
                    ModPackets.sendToServer(new CtSSwitchStealthPacket());},
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/modbutton.png"),
                20, 20
        ));



        if (currentSpeedLevel < PlayerSkill.maxSpeedLevel || currentHPLevel < PlayerSkill.maxHPLevel) {
            guiGraphics.pose().pushPose();

            Component Text1 = Component.literal("");
            Component Text2 = Component.literal("");
            if(currentSpeedLevel < PlayerSkill.maxSpeedLevel) {
                Text1= Component.literal("Requiered: Speed Level MAX\n");
            }
            if(currentHPLevel < PlayerSkill.maxHPLevel) {
                Text2= Component.literal("Requiered: HP Level MAX");
            }


            float scale = 0.55f;
            guiGraphics.pose().scale(scale, scale, 1.0f);
            int x = (int) (this.width / 2 + 30 / scale);
            int y = (int) (this.height / 2  + 20/ scale);

            Component text = Text1.copy().append(Text2);


            int maxWidth = 200;
            List<FormattedCharSequence> lines = this.font.split(text, maxWidth);

            int lineHeight = this.font.lineHeight;
            int yOffset = 0;

            for (FormattedCharSequence line : lines) {
                guiGraphics.drawString(
                        this.font,
                        line,
                        x,
                        y + yOffset,
                        0xff1900
                );
                yOffset += lineHeight;
            }
            guiGraphics.pose().popPose();
        }



        if (currentDMGLevel < PlayerSkill.maxDMGLevel || currentHPLevel < PlayerSkill.maxHPLevel) {
            guiGraphics.pose().pushPose();

            Component Text1 = Component.literal("");
            Component Text2 = Component.literal("");
            if(currentDMGLevel < PlayerSkill.maxDMGLevel) {
                Text1= Component.literal("Requiered: Claws Level MAX\n");
            }
            if(currentHPLevel < PlayerSkill.maxHPLevel) {
                Text2= Component.literal("Requiered: HP Level MAX");
            }


            float scale = 0.55f;
            guiGraphics.pose().scale(scale, scale, 1.0f);
            int x = (int) (this.width / 2 + 114 / scale);
            int y = (int) (this.height / 2  + 20/ scale);

            Component text = Text1.copy().append(Text2);


            int maxWidth = 200;
            List<FormattedCharSequence> lines = this.font.split(text, maxWidth);

            int lineHeight = this.font.lineHeight;
            int yOffset = 0;

            for (FormattedCharSequence line : lines) {
                guiGraphics.drawString(
                        this.font,
                        line,
                        x,
                        y + yOffset,
                        0xff1900
                );
                yOffset += lineHeight;
            }
            guiGraphics.pose().popPose();
        }

        if (currentToughnessLevel < PlayerSkill.maxArmorLevel || currentJumpLevel < PlayerSkill.maxJumpLevel) {
            guiGraphics.pose().pushPose();

            Component Text1 = Component.literal("");
            Component Text2 = Component.literal("");
            if(currentToughnessLevel < PlayerSkill.maxArmorLevel) {
                Text1= Component.literal("Requiered: Pelt Level MAX\n");
            }
            if(currentJumpLevel < PlayerSkill.maxJumpLevel) {
                Text2= Component.literal("Requiered: Jump Level MAX");
            }


            float scale = 0.55f;
            guiGraphics.pose().scale(scale, scale, 1.0f);
            int x = (int) (this.width / 2 + 72 / scale);
            int y = (int) (this.height / 2  + 61/ scale);

            Component text = Text1.copy().append(Text2);


            int maxWidth = 200;
            List<FormattedCharSequence> lines = this.font.split(text, maxWidth);

            int lineHeight = this.font.lineHeight;
            int yOffset = 0;

            for (FormattedCharSequence line : lines) {
                guiGraphics.drawString(
                        this.font,
                        line,
                        x,
                        y + yOffset,
                        0xff1900
                );
                yOffset += lineHeight;
            }
            guiGraphics.pose().popPose();
        }







        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private String getNextLevelCost(int level, int maxLevel, int defaultCost) {

        String result;
        if (level < maxLevel) {
            result = (defaultCost * (level + 1)) + "xp";
        }
        else {
            result = "MAX";
        }
        return result;
    }

}
