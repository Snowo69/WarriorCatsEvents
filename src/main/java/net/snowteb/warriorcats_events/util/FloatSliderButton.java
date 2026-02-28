package net.snowteb.warriorcats_events.util;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class FloatSliderButton extends AbstractSliderButton {

    private final float min;
    private final float max;

    public FloatSliderButton(int x, int y, int width, int height,
                             float min, float max, float initialValue) {

        super(x, y, width, height, Component.empty(),
                (initialValue - min) / (max - min));

        this.min = min;
        this.max = max;

        updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(String.format("Size: %.2f", getActualValue())));
    }

    @Override
    protected void applyValue() {
    }

    public float getActualValue() {
        return min + (float)value * (max - min);
    }
}
