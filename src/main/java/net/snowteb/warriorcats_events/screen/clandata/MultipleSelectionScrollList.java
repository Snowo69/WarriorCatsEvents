package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import java.util.ArrayList;
import java.util.List;

public class MultipleSelectionScrollList extends AbstractSelectionList<MultipleSelectionScrollList.VariantEntry> {

    private final boolean isRecipient;
    private final List<Integer> selectedIds = new ArrayList<>();
    private MultipleSelectionScrollList otherList;

    public MultipleSelectionScrollList(Minecraft mc, int width, int height, int top, int bottom, int itemHeight, boolean isRecipient) {
        super(mc, width, height, top, bottom, itemHeight);
        this.isRecipient = isRecipient;

    }

    public void setOtherList(MultipleSelectionScrollList other) {
        this.otherList = other;
    }

    public List<Integer> getSelectedIds() {
        return selectedIds;
    }

    private void transferEntry(VariantEntry entry) {
        if (otherList == null) return;

        this.removeEntry(entry);
        this.selectedIds.remove((Integer) entry.id);

        otherList.addOption(entry.name, entry.id);
        otherList.selectedIds.add(entry.id);
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

    protected void removeAnEntry(VariantEntry entry) {
        this.children().remove(entry);
        this.setSelected(null);
    }

    public class VariantEntry extends Entry<VariantEntry> {

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
            MultipleSelectionScrollList.this.transferEntry(this);
            return true;
        }


        @Override
        public void render(GuiGraphics pGuiGraphics, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pHovering, float pPartialTick) {
            boolean selected = MultipleSelectionScrollList.this.getSelected() == this;
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

