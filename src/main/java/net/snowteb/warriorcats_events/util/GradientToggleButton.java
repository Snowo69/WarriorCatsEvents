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
    private int color = 0xFFFFFF;
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
    protected void renderWidget(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {

        boolean hovered = this.isHoveredOrFocused();

        pGuiGraphics.blit(texture,
                this.getX(), this.getY(),
                0, 0,
                this.width, this.height,
                texWidth, texHeight);


        int alpha = 0x55;
        int colorGradient = (alpha << 24) | (this.color & 0xFFFFFF);

        pGuiGraphics.fillGradient(this.getX(), this.getY(), this.width + this.getX(),this.height + this.getY(), 0x44000000, colorGradient);
        pGuiGraphics.fillGradient(this.getX(), this.getY(), this.width + this.getX(),this.height + this.getY(), 0, 0x55000000);
        pGuiGraphics.renderOutline(this.getX(), this.getY(), this.width,this.height, selected ? 0xFFFFFFA0 : 0x61FFFFFF);



        if (hovered && active) {
            pGuiGraphics.fill(this.getX(), this.getY(),
                    this.getX() + this.width,
                    this.getY() + this.height,
                    0x20FFFFFF);
        }


        float scale = this.textScale;

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


        if (!this.active) {
            pGuiGraphics.fill(this.getX(), this.getY(), this.width + this.getX(),this.height + this.getY(), 0, 0x55000000);
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {}
}
