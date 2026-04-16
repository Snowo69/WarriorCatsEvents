package net.snowteb.warriorcats_events.screen.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class GradientSwitchButton extends Button {

    private boolean value;
    private int color = 0xFFFFFF;
    private String text = "";
    private float scale = 1.0f;

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

    public GradientSwitchButton(int x, int y, int width, int height, String text, boolean initialValue, OnPress onPress, int color, float scale) {
        super(x, y, width, height, Component.literal(text), onPress, DEFAULT_NARRATION);
        this.value = initialValue;
        this.color = color;
        this.text = text;
        this.scale = scale;
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
    protected void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTick) {

        int alpha = 0x55;
        int colorGradient = (alpha << 24) | (this.color & 0xFFFFFF);

        pGuiGraphics.fillGradient(this.getX(), this.getY(), this.width + this.getX(), this.height + this.getY(), 0x44000000, colorGradient);
        pGuiGraphics.fillGradient(this.getX(), this.getY(), this.width + this.getX(), this.height + this.getY(), 0, 0x55000000);

//        this.renderString(guiGraphics, Minecraft.getInstance().font, 0xFFFFFF | Mth.ceil(this.alpha * 255.0F) << 24);

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scale, scale, 1.0f);
        int textX = (int)((this.getX() + this.width / 2) / scale);
        int textY = (int)((this.getY() + (this.height - 5) / 2) / scale);
        pGuiGraphics.drawCenteredString(
                Minecraft.getInstance().font,
                this.getMessage(),
                textX,
                textY,
                this.active ? (this.isHoveredOrFocused() ? 0xFFFFA0 : 0xFFFFFF) : 0xFF555555
        );
        pGuiGraphics.pose().popPose();


        int color = value ? 0xFF4ff74f : 0xFFff6464;

        pGuiGraphics.renderOutline(getX(), getY(), width, height, color);


    }
}

