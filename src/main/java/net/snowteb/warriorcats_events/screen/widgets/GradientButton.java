package net.snowteb.warriorcats_events.screen.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * A custom button that allows to have a custom texture and other aesthetic settings.
 */
public class GradientButton extends AbstractButton {

    public interface PressAction {
        void onPress(GradientButton button);
    }

    private final ResourceLocation texture;
    private final int texWidth;
    private final int texHeight;
    private final PressAction onPress;
    private float textScale = 0.82f;

    private int color = 0xFFFFFF;
    private boolean outline = true;

    public GradientButton(int x, int y, int width, int height,
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

    public GradientButton(int x, int y, int width, int height,
                          Component text,
                          PressAction onPress,
                          ResourceLocation texture,
                          int texWidth, int texHeight, int color, float scale) {

        super(x, y, width, height, text);
        this.onPress = onPress;
        this.texture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.color = color;
        this.textScale = scale;
    }

    public GradientButton(int x, int y, int width, int height,
                          Component text,
                          PressAction onPress,
                          ResourceLocation texture,
                          int texWidth, int texHeight, float textScale) {

        super(x, y, width, height, text);
        this.onPress = onPress;
        this.texture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.textScale = textScale;
    }

    @Override
    public void onPress() {
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

        gui.fillGradient(
                this.getX(), this.getY(),
                this.getX() + this.width,
                this.getY() + this.height,
                0x44000000,
                colorGradient
        );

        gui.fillGradient(
                this.getX(), this.getY(),
                this.getX() + this.width,
                this.getY() + this.height,
                0,
                0x55000000
        );

        if (outline) {
            gui.renderOutline(
                    this.getX(),
                    this.getY(),
                    this.width,
                    this.height,
                    0x61FFFFFF
            );
        }

        if (hovered && this.active) {
            gui.fill(this.getX(), this.getY(),
                    this.getX() + this.width,
                    this.getY() + this.height,
                    0x20FFFFFF);
        }

        gui.pose().pushPose();
        gui.pose().scale(this.textScale, this.textScale, 1.0f);

        int textX = (int)((this.getX() + this.width / 2) / this.textScale);
        int textY = (int)((this.getY() + (this.height - 5) / 2) / this.textScale);

        gui.drawCenteredString(
                Minecraft.getInstance().font,
                this.getMessage(),
                textX,
                textY,
                (this.active ? (this.isHoveredOrFocused() ? 0xFFFFA0 : 0xFFFFFF) : 0x77ffffff)
        );
        gui.pose().popPose();


    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {}
}
