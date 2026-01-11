package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class SwitchButton extends Button {

    private boolean value;

    public SwitchButton(int x, int y, int width, int height, String text, boolean initialValue, OnPress onPress) {
        super(x, y, width, height, Component.literal(text), onPress, DEFAULT_NARRATION);
        this.value = initialValue;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void toggle() {
        this.value = !this.value;
    }

    @Override
    public void onPress() {
        toggle();
        super.onPress();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        super.renderWidget(graphics, mouseX, mouseY, partialTick);

        int color = value ? 0xFF4ff74f : 0xFFff4747;
            int x = getX();
            int y = getY();
            int w = getWidth();
            int h = getHeight();
            graphics.fill(x, y, x + w, y + 1, color);
            graphics.fill(x, y + h - 1, x + w, y + h, color);
            graphics.fill(x, y, x + 1, y + h, color);
            graphics.fill(x + w - 1, y, x + w, y + h, color);


    }
}

