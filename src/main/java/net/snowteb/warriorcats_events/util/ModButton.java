package net.snowteb.warriorcats_events.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


public class ModButton extends AbstractButton {

    public interface PressAction {
        void onPress(ModButton button);
    }

    private final ResourceLocation texture;
    private final int texWidth;
    private final int texHeight;
    private final PressAction onPress;

    public ModButton(int x, int y, int width, int height,
                     Component text,
                     PressAction onPress,
                     ResourceLocation texture,
                     int texWidth, int texHeight) {

        super(x, y, width, height, text);
        this.onPress = onPress;
        this.texture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
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

        if (hovered) {
            gui.fill(this.getX(), this.getY(),
                    this.getX() + this.width,
                    this.getY() + this.height,
                    0x20FFFFFF);
        }


        float scale = 0.95f;

        gui.pose().pushPose();
        gui.pose().scale(scale, scale, 1.0f);

        int textX = (int)((this.getX() + this.width / 2) / scale);
        int textY = (int)((this.getY() + (this.height - 8) / 2) / scale);

        gui.drawCenteredString(
                Minecraft.getInstance().font,
                this.getMessage(),
                textX,
                textY,
                this.isHoveredOrFocused() ? 0xFFFFA0 : 0xFFFFFF
        );
        gui.pose().popPose();


    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {}
}
