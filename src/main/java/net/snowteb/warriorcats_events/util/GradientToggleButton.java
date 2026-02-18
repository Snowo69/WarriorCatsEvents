package net.snowteb.warriorcats_events.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * A custom button that allows to have a custom texture and other aesthetic settings.
 */
public class GradientToggleButton extends AbstractButton {

    public interface PressAction {
        void onPress(GradientToggleButton button);
    }

    private final ResourceLocation texture;
    private final int texWidth;
    private final int texHeight;
    private final PressAction onPress;
    private float textScale = 0.82f;
    private int color = 0;
    private boolean selected;


    public GradientToggleButton(int x, int y, int width, int height,
                                Component text,
                                PressAction onPress,
                                ResourceLocation texture,
                                int texWidth, int texHeight, int color) {

        super(x, y, width, height, text);
        this.onPress = onPress;
        this.texture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.color = color;
    }

    public GradientToggleButton(int x, int y, int width, int height,
                                Component text,
                                PressAction onPress,
                                ResourceLocation texture,
                                int texWidth, int texHeight, float textScale, int color) {

        super(x, y, width, height, text);
        this.onPress = onPress;
        this.texture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.textScale = textScale;
        this.color = color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean value) {
        this.selected = value;
    }


    @Override
    public void onPress() {
        this.selected = !this.selected;
        this.onPress.onPress(this);
    }


    @Override
    protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {

        boolean hovered = this.isHoveredOrFocused();

        gui.blit(texture,
                this.getX(), this.getY(),
                0, 0,
                this.width, this.height,
                texWidth, texHeight);


        int alpha = 0x55;
        int colorGradient = (alpha << 24) | (this.color & 0xFFFFFF);

        gui.fillGradient(this.getX(), this.getY(), this.width + this.getX(),this.height + this.getY(), 0x44000000, colorGradient);
        gui.fillGradient(this.getX(), this.getY(), this.width + this.getX(),this.height + this.getY(), 0, 0x55000000);
        gui.renderOutline(this.getX(), this.getY(), this.width,this.height, selected ? 0xFFFFFFA0 : 0x61FFFFFF);



        if (hovered) {
            gui.fill(this.getX(), this.getY(),
                    this.getX() + this.width,
                    this.getY() + this.height,
                    0x20FFFFFF);
        }


        float scale = this.textScale;

        gui.pose().pushPose();
        gui.pose().scale(scale, scale, 1.0f);
        int textX = (int)((this.getX() + this.width / 2) / scale);
        int textY = (int)((this.getY() + (this.height - 5) / 2) / scale);
        gui.drawCenteredString(
                Minecraft.getInstance().font,
                this.getMessage(),
                textX,
                textY,
                this.isHoveredOrFocused() ? 0xFFFFA0 : 0xFFFFFF
        );
        gui.pose().popPose();



//        if (selected) {
//            int x = getX();
//            int y = getY();
//            int w = width;
//            int h = height;
//            int borderColor = 0xFFe8ae1c;
//
//            gui.fill(x, y, x + w, y + 1, borderColor);
//            gui.fill(x, y + h - 1, x + w, y + h, borderColor);
//            gui.fill(x, y, x + 1, y + h, borderColor);
//            gui.fill(x + w - 1, y, x + w, y + h, borderColor);
//        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {}
}
