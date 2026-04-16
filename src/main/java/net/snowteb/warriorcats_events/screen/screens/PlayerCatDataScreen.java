package net.snowteb.warriorcats_events.screen.screens;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.clan.WCEPlayerData;
import net.snowteb.warriorcats_events.client.ClientClanData;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.others.PlayerKitPacket;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public class PlayerCatDataScreen extends Screen {
    private final WCEPlayerData.PackedData data;
    private final UUID targetUUID;

    Component preText = Component.literal("✧ ").withStyle(ChatFormatting.GOLD);

    Component name = Component.literal("...");

    Component clanName = Component.literal("...");
    Component genderText = Component.literal("...");
    Component ageText = Component.literal("...");

    Component catMate = Component.literal("...");
    Component friendshipLevelText = Component.literal("...");

    boolean interactionCooldownTooltip = false;

    private final int myKitCooldown;

    private static final ResourceLocation CAT_NAME_TOAST =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/catname_toast.png");

    public PlayerCatDataScreen(WCEPlayerData.PackedData playerData, UUID targetUUID, int myKitCooldown) {
        super(Component.empty());
        this.data = playerData;
        this.targetUUID = targetUUID;
        this.myKitCooldown = myKitCooldown;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        name = Component.literal(data.name);
        genderText = Component.literal("Non-binary");
        if (data.gender == 0) {
            genderText = Component.literal("Tom-cat");
        } else if (data.gender == 1) {
            genderText = Component.literal("She-cat");
        }

        catMate = Component.literal("Mate: ").append(Component.literal(data.mateName).withStyle(ChatFormatting.AQUA));

        {
            clanName = Component.literal("From ").append(Component.literal(data.clanName));
        }

        ageText = Component.nullToEmpty(data.age.name());

//        talkInteractButton = Button.builder(
//                Component.literal("Talk"),
//                btn -> {
//                    ModPackets.sendToServer(new PerformInteractionTalkPacket(wCatEntity.getId()));
//                    this.onClose();
//                    Minecraft.getInstance().setScreen(null);
//                }
//        ).bounds(this.width - 85, 10, 80, 20).build();
//
//        givePreyInteractButton = Button.builder(
//                Component.literal("Give Prey"),
//                btn -> {
//                    ModPackets.sendToServer(new PerformInteractionGivePreyPacket(wCatEntity.getId()));
//                    this.onClose();
//                    Minecraft.getInstance().setScreen(null);
//                }
//        ).bounds(this.width - 85, 35, 80, 20).build();
//
//        showAffectionInteractButton = Button.builder(
//                Component.literal("Show Affection"),
//                btn -> {
//                    ModPackets.sendToServer(new PerformInteractionShowAffectionPacket(wCatEntity.getId()));
//                    this.onClose();
//                    Minecraft.getInstance().setScreen(null);
//                }
//        ).bounds(this.width - 85, 60, 80, 20).build();
//
//        if (wCatEntity.isTame() && wCatEntity.getInteractionCooldown() <=0) {
//            this.addRenderableWidget(givePreyInteractButton);
//            this.addRenderableWidget(talkInteractButton);
//            this.addRenderableWidget(showAffectionInteractButton);
//        } else {
//            interactionCooldownTooltip = true;
//        }

        drawMainMenu();


        super.init();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int xPositionPanel = 10;

        pGuiGraphics.blit(CAT_NAME_TOAST, 0, 0, 0, 0
                , 172, 45, 172, 45);


        float scale1 = 1.25f;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scale1, scale1, 1f);
        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, name, 75, 11, 0xFFFFFF);
        pGuiGraphics.pose().popPose();

        boolean renderFriendshipText = pMouseX >= 43 && pMouseX <= 142 && pMouseY >= 28 && pMouseY <= 37;

        if (interactionCooldownTooltip) {
            pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append("Cannot have kits right now."), this.width, 30);
        }

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(0.9f,0.9f,0.9f);

        if (renderFriendshipText) {
            pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(friendshipLevelText) , pMouseX, pMouseY);
        }


        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(clanName) , xPositionPanel, 70);
        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(genderText), xPositionPanel, 90);
        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(ageText), xPositionPanel, 110);
        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(catMate), xPositionPanel, 130);

        pGuiGraphics.pose().popPose();

    }

    @Override
    public void tick() {
        super.tick();
    }

    private void drawMainMenu() {
        this.clearWidgets();

        int centerX = width / 2;
        int centerY = height / 2;


        boolean isAnyNonBinary = data.gender == 2 || ClientClanData.get().getGenderData() == 2;

        boolean canInitiallyHaveKits = isAnyNonBinary || (data.gender != ClientClanData.get().getGenderData());

        if (Minecraft.getInstance().player != null) {
            if (ClientClanData.get().getMateUUID() != null) {
                if (ClientClanData.get().getMateUUID().equals(targetUUID)) {
                    if (myKitCooldown <= 0 && data.kitCooldown <= 0 && canInitiallyHaveKits) {
                        this.addRenderableWidget(Button.builder(
                                Component.literal("Have kits"),
                                btn -> {
                                    ModPackets.sendToServer(new PlayerKitPacket(targetUUID));

                                    this.onClose();
                                }
                        ).bounds(this.width - 85, 10, 80, 20).build());
                    } else {
                        interactionCooldownTooltip = true;
                    }
                }
            }
        }

        this.addRenderableWidget(Button.builder(
                Component.literal("Close"),
                btn -> {
                    this.onClose();
                }
        ).bounds(this.width - 85, this.height - 30, 80, 20).build());

    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == GLFW.GLFW_KEY_E) {
            this.onClose();
            return true;
        }
        if (pKeyCode == GLFW.GLFW_KEY_T) {
            this.onClose();
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
