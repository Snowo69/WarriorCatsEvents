package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;

public class VariantScrollList extends AbstractSelectionList<VariantScrollList.VariantEntry> {

    public VariantScrollList(Minecraft mc, int width, int height, int top, int bottom, int itemHeight) {

        super(mc, width, height, top, bottom, itemHeight);

    }


    @Override
    protected int getScrollbarPosition() {
        return this.getLeft() + this.getRowWidth() + 20;
    }


    @Override
    public int getRowWidth() {
        return this.width - 20;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    public void addOption(String name, int id) {
        this.addEntry(new VariantEntry(name, id));
    }

    public VariantEntry getSelectedEntry() {
        return this.getSelected();
    }

    public class VariantEntry extends AbstractSelectionList.Entry<VariantEntry> {

        private final String name;
        private final int id;

        public VariantEntry(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            VariantScrollList.this.setSelected(this);
            return true;
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pHovering, float pPartialTick) {
            boolean selected = VariantScrollList.this.getSelected() == this;
            int color = selected ? 0xFFf0ed43 : (pHovering ? 0xFFFFAA00 : 0xFFFFFFFF);

            pGuiGraphics.drawString(
                    Minecraft.getInstance().font,
                    name,
                    pLeft + 6,
                    pTop + 6,
                    color
            );
        }
    }

}

