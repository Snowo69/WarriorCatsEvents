package net.snowteb.warriorcats_events.screen.menus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.wcat.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.others.SetCosmeticPacket;
import net.snowteb.warriorcats_events.network.packet.c2s.others.SetPosePacket;
import net.snowteb.warriorcats_events.screen.screens.createmorph.FancySelectableButton;
import net.snowteb.warriorcats_events.screen.screens.createmorph.FancyStringWidget;
import net.snowteb.warriorcats_events.screen.screens.createmorph.FancySubRenderablesSquare;
import tocraft.walkers.api.PlayerShape;

import java.util.ArrayList;
import java.util.List;

public class SetPoseMenu extends Screen {

    public SetPoseMenu() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;


        FancySubRenderablesSquare square1 = new FancySubRenderablesSquare(10, 10,
                100, height - 50);
        this.addRenderableWidget(square1);

        int currentPose = -1;
        boolean brokenPaw = false;
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            if (PlayerShape.getCurrentShape(localPlayer) instanceof WCatEntity cat) {
                currentPose = cat.getIdlePose();
                brokenPaw = cat.isBrokenPaw();
            }
        }

        {
            FancyStringWidget text1 = new FancyStringWidget(Component.translatable("screen.options.morph_pose"),
                    square1.getLeft() + 5, square1.getRight() - 5, square1.getTop() + 5, 1.1f, true,
                    square1.getBottom() - square1.getTop() - 18);
            square1.addWidget(text1);

            int y = text1.getOriginalYPos1() + 5;
            int x = square1.getLeft() + (square1.getRight()-square1.getLeft())/2;

            List<FancySelectableButton> buttons = new ArrayList<>();

            FancySelectableButton b1 = new FancySelectableButton(80, 15,
                    x - 40, y,
                    Component.translatable("screen.pose.no_pose"),
                    b -> {
                        setPose(0);
                        for (FancySelectableButton button : buttons) {
                            if (button != b) button.setSelected(false);
                        }
                    }, 0.8f, -1, "");
            square1.addWidget(b1);
            y += 20;
            FancySelectableButton b2 = new FancySelectableButton(80, 15,
                    x - 40, y,
                    Component.translatable("screen.pose.gracious"),
                    b -> {
                        setPose(1);
                        for (FancySelectableButton button : buttons) {
                            if (button != b) button.setSelected(false);
                        }
                    }, 0.8f, -1, "");
            square1.addWidget(b2);
            y += 20;
            FancySelectableButton b3 = new FancySelectableButton(80, 15,
                    x - 40, y,
                    Component.translatable("screen.pose.dominant"),
                    b -> {
                        setPose(2);
                        for (FancySelectableButton button : buttons) {
                            if (button != b) button.setSelected(false);
                        }
                    }, 0.8f, -1, "");
            square1.addWidget(b3);
            y += 20;
            FancySelectableButton b4 = new FancySelectableButton(80, 15,
                    x - 40, y,
                    Component.translatable("screen.pose.refined"),
                    b -> {
                        setPose(3);
                        for (FancySelectableButton button : buttons) {
                            if (button != b) button.setSelected(false);
                        }
                    }, 0.8f, -1, "");
            square1.addWidget(b4);
            y += 20;
            FancySelectableButton b5 = new FancySelectableButton(80, 15,
                    x - 40, y,
                    Component.translatable("screen.pose.limping"),
                    b -> {
                        setPose(4);
                    }, 0.8f, -1, "");
            square1.addWidget(b5);

            buttons.add(b1);
            buttons.add(b2);
            buttons.add(b3);
            buttons.add(b4);

            b1.setSelected(currentPose == 0);
            b2.setSelected(currentPose == 1);
            b3.setSelected(currentPose == 2);
            b4.setSelected(currentPose == 3);
            b5.setSelected(brokenPaw);

            y += 20;
        }


        if (localPlayer != null && PlayerShape.getCurrentShape(localPlayer) instanceof WCatEntity catShape) {

            FancySubRenderablesSquare square2 = new FancySubRenderablesSquare(width - 110, 10,
                    100, height - 50);
            this.addRenderableWidget(square2);


            FancyStringWidget text1 = new FancyStringWidget(Component.translatable("screen.cosmetics"),
                    square2.getLeft() + 5, square2.getRight() - 5, square2.getTop() + 5, 1.1f, true,
                    square2.getBottom() - square2.getTop() - 18);
            square2.addWidget(text1);

            int y = text1.getOriginalYPos1() + 5;
            int x = square2.getLeft() + (square2.getRight()-square2.getLeft())/2;

            if (WarriorCatsEvents.Collaborators.isContributor(localPlayer.getUUID())) {
                FancySelectableButton b1 = new FancySelectableButton(80, 15,
                        x - 40, y,
                        Component.translatable("screen.cosmetics.cool_sunglasses"),
                        b -> {
                            setCosmetic(0);
                        }, 0.8f, -1, "");
                square2.addWidget(b1);
                y += 20;
                b1.setSelected(catShape.embeddedAccessories().hasSunGlasses());

                FancySelectableButton b2 = new FancySelectableButton(80, 15,
                        x - 40, y,
                        Component.translatable("screen.cosmetics.moss_coat"),
                        b -> {
                            setCosmetic(1);
                        }, 0.8f, -1, "");
                square2.addWidget(b2);
                y += 20;
                b2.setSelected(catShape.embeddedAccessories().hasMossCoat());

            }

        }



        Button close = Button.builder(
                Component.translatable("screen.pose.close"),
                btn -> {
                    onClose();
                }
        ).bounds(this.width - 90, this.height - 28, 80, 18).build();

        this.addRenderableWidget(close);

        super.init();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        for (Renderable renderable : this.renderables) {
            renderable.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        Minecraft.getInstance().setScreen(null);
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    private void setPose(int i) {
        ModPackets.sendToServer(new SetPosePacket(i));
    }

    private void setCosmetic(int i) {
        ModPackets.sendToServer(new SetCosmeticPacket(i));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}

