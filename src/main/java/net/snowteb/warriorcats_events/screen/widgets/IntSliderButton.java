package net.snowteb.warriorcats_events.screen.widgets;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class IntSliderButton extends AbstractSliderButton {

    private final int min;
    private final int max;
    private String text = "";

    public IntSliderButton(int x, int y, int width, int height,
                           int min, int max, int initialValue) {

        super(x, y, width, height, Component.empty(),
                (double) (initialValue - min) / (max - min));

        this.min = min;
        this.max = max;

        updateMessage();
    }

    public IntSliderButton(int x, int y, int width, int height,
                           int min, int max, int initialValue, String text) {

        super(x, y, width, height, Component.empty(),
                (double) (initialValue - min) / (max - min));

        this.min = min;
        this.max = max;

        updateMessage();
    }

    @Override
    protected void updateMessage() {
        if (text.isEmpty()) {
            this.setMessage(Component.literal("Value: " + getActualValue()));
        } else {
            this.setMessage(Component.literal(text + ": " +  getActualValue()));
        }
    }

    @Override
    protected void applyValue() {
    }

    public int getActualValue() {
        return (int) (min + (float)value * (max - min));
    }

    public void setValue(int value) {
        this.value = (double)(value - min) / (max - min);
        this.value = Math.max(0.0, Math.min(1.0, this.value));
        updateMessage();
    }

}
