package net.snowteb.warriorcats_events.screen.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import java.util.List;

public class DataScrollList extends AbstractSelectionList<DataScrollList.MemberEntry> {

    public DataScrollList(Minecraft mc, int width, int height, int top, int bottom, int itemHeight) {
        super(mc, width, height, top, bottom, itemHeight);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getLeft() + this.getRowWidth() + 6;
    }

    @Override
    public int getRowWidth() {
        return this.width - 10;
    }

    @Override
    public void updateNarration(NarrationElementOutput narration) {
    }

    public void setMembers(List<String> memberMorphs) {
        this.clearEntries();
        for (String name : memberMorphs) {
            this.addEntry(new MemberEntry(name));
        }
    }

    public static class MemberEntry extends AbstractSelectionList.Entry<MemberEntry> {
        private final String morphName;

        public MemberEntry(String morphName) {
            this.morphName = morphName;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return false;
        }

        @Override
        public void render(GuiGraphics gfx, int index, int top, int left, int width,
                           int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            int bgColor = 0x22000000;

            gfx.fill(left, top, left + width, top + height, bgColor);

            gfx.drawString(
                    Minecraft.getInstance().font,
                    morphName,
                    left + 6,
                    top + 4,
                    0xFFFFFF
            );
        }
    }
}

