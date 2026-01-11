package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ToggleButton extends Button {

    private boolean selected;

    public ToggleButton(int x, int y, int width, int height, String text, OnPress onPress) {
        super(x, y, width, height, Component.literal(text), onPress, DEFAULT_NARRATION);
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

        if (selected) {
            int x = getX();
            int y = getY();
            int w = getWidth();
            int h = getHeight();
            int color = 0xFFe8ae1c;
            graphics.fill(x, y, x + w, y + 1, color);
            graphics.fill(x, y + h - 1, x + w, y + h, color);
            graphics.fill(x, y, x + 1, y + h, color);
            graphics.fill(x + w - 1, y, x + w, y + h, color);
        }
    }
}

