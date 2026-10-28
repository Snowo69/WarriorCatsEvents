package net.snowteb.warriorcats_events.screen.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.snowteb.warriorcats_events.managers.ClanSymbol;

public class ImageScrollList extends AbstractSelectionList<ImageScrollList.SymbolEntry> {

    public ImageScrollList(Minecraft mc, int width, int height, int top, int bottom) {

        super(mc, width, bottom-top,top, 35);

    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRight() - 6;
    }


    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public void addOption(int index) {
        this.addEntry(new SymbolEntry(index));
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.fill(this.getX(), this.getY(),
                this.getX() + width, this.getY() + height,
                0x55000000);

        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        int color = 0x990D0C08;

        pGuiGraphics.fillGradient(RenderType.guiOverlay(), this.getX(), this.getY(), this.getRight(), this.getY() + 6, color, 0, 0);
        pGuiGraphics.fillGradient(RenderType.guiOverlay(), this.getX(), this.getBottom() - 6, this.getRight(), this.getBottom(), 0, color, 0);

        pGuiGraphics.renderOutline(this.getX(), this.getY() - 1,
                this.width, this.getBottom() - this.getY() + 2,
                0xFF59533B);
    }

    @Override
    protected void renderSelection(GuiGraphics pGuiGraphics, int pTop, int pWidth, int pHeight, int pOuterColor, int pInnerColor) {
    }

    public class SymbolEntry extends AbstractSelectionList.Entry<SymbolEntry> {

        private final int index;

        public SymbolEntry(int index) {
            this.index = index;
        }

        public int getId() {
            return index;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            ImageScrollList.this.setSelected(this);
            return true;
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pHovering, float pPartialTick) {
            boolean selected = ImageScrollList.this.getSelected() == this;
            int color = selected ? 0x33f0ed43 : (pHovering ? 0x22FFFFFF : 0x11FFFFFF);

            int iHeight = itemHeight - 5;
            int listWidth = width;

            int x0 = pLeft + 2;
            int y0 = pTop;
            int x1 = pLeft + listWidth - 12;
            int y1 = pTop + iHeight;
            int entryWidth = x1 - x0;
            int entryHeight = y1 - y0;

            pGuiGraphics.pose().pushPose();

            float symbolScale = 0.5f;
            int symbolSize = (int) (ClanSymbol.SYMBOL_SIZE*symbolScale);

            int symbolX = x0 + entryWidth/2 - (symbolSize)/2;
            int symbolY = y0 + entryHeight/2 - (symbolSize)/2;

            pGuiGraphics.pose().translate(symbolX, symbolY, 0);
            float scale = 1f;
            pGuiGraphics.pose().scale(scale, scale, scale);

            pGuiGraphics.blit(
                    ClanSymbol.SPRITE,
                    0, 0,
                    (float) ClanSymbol.getSymbolCoordinate(index) * symbolScale, 0,
                    symbolSize, symbolSize,
                    (symbolSize * ClanSymbol.SYMBOLS_AMOUNT),
                    symbolSize
            );

            pGuiGraphics.pose().popPose();

            pGuiGraphics.fill(x0, y0, x1, y1, color);

        }
    }

}



