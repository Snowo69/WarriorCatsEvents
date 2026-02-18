package net.snowteb.warriorcats_events.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.snowteb.warriorcats_events.client.ClanInfo;
import net.snowteb.warriorcats_events.screen.clandata.SpecificClanScreen;

public class ClanScrollList extends AbstractSelectionList<ClanScrollList.ClanEntry> {

    public ClanScrollList(Minecraft mc, int width, int height, int top, int bottom, int itemHeight) {
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
    public void updateNarration(NarrationElementOutput narration) {
    }

    public void addClan(ClanInfo clan) {
        this.addEntry(new ClanEntry(clan));
    }

    public ClanEntry getSelectedEntry() {
        return this.getSelected();
    }

    public class ClanEntry extends AbstractSelectionList.Entry<ClanEntry> {

        private final ClanInfo clan;

        public ClanEntry(ClanInfo clan) {
            this.clan = clan;
        }

        public ClanInfo getClan() {
            return clan;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                ClanScrollList.this.setSelected(this);

                Minecraft.getInstance().setScreen(
                        new SpecificClanScreen(clan.name, clan.uuid)
                );
                return true;
            }
            return false;
        }


        @Override
        public void render(GuiGraphics pGuiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {

            boolean selected = ClanScrollList.this.getSelected() == this;

            int bgColor = selected
                    ? 0x66FFFFFF
                    : hovering ? 0x33FFFFFF : 0x22000000;

            pGuiGraphics.fill(left, top, left + width, top + height, bgColor);

            pGuiGraphics.drawString(
                    Minecraft.getInstance().font,
                    clan.name,
                    left + 6,
                    top + 2,
                    clan.color
            );

            pGuiGraphics.drawString(
                    Minecraft.getInstance().font,
                    "\uD83D\uDC51 : " + clan.leaderName,
                    left + 6,
                    top + 12,
                    0xAAAAAA
            );

            pGuiGraphics.drawString(
                    Minecraft.getInstance().font,
                    " \uD83D\uDC08 : " + (clan.memberCount + clan.clanCats.size()),
                    left + width - 70,
                    top + 12,
                    0x888888
            );

            int color = clan.color;

            float a = ((color >> 24) & 0xFF) / 255f;
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8) & 0xFF) / 255f;
            float b = (color & 0xFF) / 255f;

            pGuiGraphics.setColor(r, g, b, a);

            pGuiGraphics.blit(
                    ClanSymbol.SPRITE,
                    width, top + 3,
                    (float) ((float) ClanSymbol.getSymbolCoordinate(clan.symbolIndex) / 3.3), (float) 0,
                    (int) (ClanSymbol.SYMBOL_SIZE / 3.3), (int) (ClanSymbol.SYMBOL_SIZE/3.3),
                    (int) ((ClanSymbol.SYMBOL_SIZE * ClanSymbol.SYMBOLS_AMOUNT) / 3.3),
                    (int) (ClanSymbol.SYMBOL_SIZE/3.3)
            );

            pGuiGraphics.setColor(1f, 1f, 1f, 1f);

        }
    }
}
