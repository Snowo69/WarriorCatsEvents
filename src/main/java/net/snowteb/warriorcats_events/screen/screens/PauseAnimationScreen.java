package net.snowteb.warriorcats_events.screen.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PauseAnimationScreen extends Screen {
    public PauseAnimationScreen() {
        super(Component.literal(""));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
    }

    @Override
    protected void init() {
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
