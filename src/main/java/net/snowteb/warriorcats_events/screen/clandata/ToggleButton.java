package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ToggleButton extends Button {

    private boolean selected;
    private int color = 0;

    public ToggleButton(int x, int y, int width, int height, String text, OnPress onPress) {
        super(x, y, width, height, Component.literal(text), onPress, DEFAULT_NARRATION);
    }

    public ToggleButton(int x, int y, int width, int height, String text, OnPress onPress, int color) {
        super(x, y, width, height, Component.literal(text), onPress, DEFAULT_NARRATION);
        this.color = color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean value) {
        this.selected = value;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {


        super.renderWidget(graphics, mouseX, mouseY, partialTick);
//
//        int bgColor = selected ? 0xFF6FAF6F : 0xFF444444;
//        graphics.fill(getX(), getY(), getX() + width, getY() + height, bgColor);



        if (color != 0) {
            int alpha = 0xbd;
            int colorGradient = (alpha << 24) | (this.color & 0xFFFFFF);

            graphics.fillGradient(this.getX(), this.getY(), this.width + this.getX(),this.height + this.getY(), 0x44000000, colorGradient);
        }


        if (selected) {
            int x = getX();
            int y = getY();
            int w = getWidth();
            int h = getHeight();

            int colorToBorder = 0xFFe8ae1c;
            if (color != 0) {
                colorToBorder = color;
            }
            graphics.fill(x, y, x + w, y + 1, colorToBorder);
            graphics.fill(x, y + h - 1, x + w, y + h, colorToBorder);
            graphics.fill(x, y, x + 1, y + h, colorToBorder);
            graphics.fill(x + w - 1, y, x + w, y + h, colorToBorder);
        }
    }
}

