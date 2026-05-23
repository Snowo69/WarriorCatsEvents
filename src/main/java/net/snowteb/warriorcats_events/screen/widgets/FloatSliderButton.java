package net.snowteb.warriorcats_events.screen.widgets;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class FloatSliderButton extends AbstractSliderButton {

    private final float min;
    private final float max;
    private String text = "";

    public FloatSliderButton(int x, int y, int width, int height,
                             float min, float max, float initialValue) {

        super(x, y, width, height, Component.empty(),
                (initialValue - min) / (max - min));

        this.min = min;
        this.max = max;

        updateMessage();
    }

    public FloatSliderButton(int x, int y, int width, int height,
                             float min, float max, float initialValue, String text) {

        super(x, y, width, height, Component.empty(),
                (initialValue - min) / (max - min));

        this.min = min;
        this.max = max;
        this.text = text;

        updateMessage();
    }

    @Override
    protected void updateMessage() {
        if (text.isEmpty()) {
            this.setMessage(Component.literal(String.format("Size: %.2f", getActualValue())));
        } else {
            this.setMessage(Component.literal(text + String.format(": %.2f", getActualValue())));
        }
    }

    @Override
    protected void applyValue() {
    }

    public float getActualValue() {
        return min + (float)value * (max - min);
    }
}
