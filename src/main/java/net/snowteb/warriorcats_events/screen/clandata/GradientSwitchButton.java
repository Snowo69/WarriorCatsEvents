package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class GradientSwitchButton extends Button {

    private boolean value;
    private int color = 0xFFFFFF;
    private String text = "";

    public GradientSwitchButton(int x, int y, int width, int height, String text, boolean initialValue, OnPress onPress) {
        super(x, y, width, height, Component.literal(text), onPress, DEFAULT_NARRATION);
        this.value = initialValue;
        this.text = text;
    }

    public GradientSwitchButton(int x, int y, int width, int height, String text, boolean initialValue, OnPress onPress, int color) {
        super(x, y, width, height, Component.literal(text), onPress, DEFAULT_NARRATION);
        this.value = initialValue;
        this.color = color;
        this.text = text;
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
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        int alpha = 0x55;
        int colorGradient = (alpha << 24) | (this.color & 0xFFFFFF);

        guiGraphics.fillGradient(this.getX(), this.getY(), this.width + this.getX(), this.height + this.getY(), 0x44000000, colorGradient);
        guiGraphics.fillGradient(this.getX(), this.getY(), this.width + this.getX(), this.height + this.getY(), 0, 0x55000000);

        this.renderString(guiGraphics, Minecraft.getInstance().font, 0xFFFFFF | Mth.ceil(this.alpha * 255.0F) << 24);

        int color = value ? 0xFF4ff74f : 0xFFff6464;
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();
        guiGraphics.fill(x, y, x + w, y + 1, color);
        guiGraphics.fill(x, y + h - 1, x + w, y + h, color);
        guiGraphics.fill(x, y, x + 1, y + h, color);
        guiGraphics.fill(x + w - 1, y, x + w, y + h, color);


    }
}

