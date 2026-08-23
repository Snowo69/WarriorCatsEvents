package net.snowteb.warriorcats_events.screen.screens.createmorph;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class FancyDoubleSelectableButtonList extends AbstractSelectionList<FancyDoubleSelectableButtonList.Entry> {

    public FancyDoubleSelectableButtonList(Minecraft mc, int width, int height, int top, int bottom, int itemHeight) {
        super(mc, width, height, top, bottom, itemHeight);
    }

    public void addButtons(ButtonEntry entry1, ButtonEntry entry2) {
        addEntry(new Entry(entry1, entry2));
    }

    public void addButtons(ButtonEntry entry) {
        addEntry(new Entry(entry, null));
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    protected void renderSelection(GuiGraphics pGuiGraphics, int pTop, int pWidth, int pHeight, int pOuterColor, int pInnerColor) {

    }

    @Override
    public int getRowWidth() {
        return width - 20;
    }

    @Override
    protected int getScrollbarPosition() {
        return getLeft() + width - 6;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
    }

    public class Entry extends AbstractSelectionList.Entry<Entry> {
        private final List<FancySelectableButton> buttons = NonNullList.createWithCapacity(2);

        public Entry(ButtonEntry entry1, ButtonEntry entry2) {

            int buttonWidth = ((FancyDoubleSelectableButtonList.this.width - 10)/2)-15;
            int buttonHeight = (FancyDoubleSelectableButtonList.this.itemHeight - 10);
            int color = 0xFFFFFFFF;
            float textScale = 0.8f;

            if (entry1 != null) {
                FancySelectableButton b = new FancySelectableButton(buttonWidth, buttonHeight,
                        entry1.label, entry1.action,
                        textScale, color, "");
                b.setSelected(entry1.selected);
                buttons.add(b);
            }
            if (entry2 != null) {
                FancySelectableButton b = new FancySelectableButton(buttonWidth, buttonHeight,
                        entry2.label, entry2.action,
                        textScale, color, "");
                b.setSelected(entry2.selected);
                buttons.add(b);
            }

        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            for (FancySelectableButton b : buttons) {
                if (b.isHovered()) {
                    return b.mouseClicked(mouseX, mouseY, button);
                }
            }
            return false;
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int pIndex, int pTop, int pLeft,
                           int pWidth, int pHeight, int pMouseX, int pMouseY,
                           boolean pHovering, float pPartialTick) {

            int x = pLeft;
            for (FancySelectableButton b : buttons) {
                b.setX(x);
                b.setY(pTop);
                b.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

                if (b.isSelected()) {
                    pGuiGraphics.renderOutline(b.getX(), b.getY(), b.getWidth(), b.getHeight(), 0xFF94FC58);
                }

                x = (pLeft + pWidth) - (5 + b.getWidth());
            }


        }
    }

    public static class ButtonEntry {
        public final Component label;
        public final FancySelectableButton.PressAction action;
        public final boolean selected;

        public ButtonEntry(Component label, FancySelectableButton.PressAction action, boolean selected) {
            this.label = label;
            this.action = action;
            this.selected = selected;
        }
    }
}
