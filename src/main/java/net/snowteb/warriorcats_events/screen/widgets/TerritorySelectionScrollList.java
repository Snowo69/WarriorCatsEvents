package net.snowteb.warriorcats_events.screen.widgets;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.world.level.ChunkPos;
import net.snowteb.warriorcats_events.client.ClientTerritoryData;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;

import java.util.List;
import java.util.UUID;

public class TerritorySelectionScrollList extends AbstractSelectionList<TerritorySelectionScrollList.ChunkEntry> {

    private List<ChunkPos> linkedList;
    private final UUID clanUUID;


    public TerritorySelectionScrollList(Minecraft mc, int width, int height, int top, int bottom, int itemHeight, UUID clanUUID) {
        super(mc, width, height, top, bottom, itemHeight);
        this.clanUUID = clanUUID;
    }

    public void setLinkedList(List<ChunkPos> linkedList) {
        this.linkedList = linkedList;
    }

    public void updateSelection() {
        if (linkedList == null) return;

        this.children().clear();

        for (int j = 0; j < linkedList.size(); j++) {
            this.addOption(linkedList.get(j), j);
        }
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

    public void addOption(ChunkPos pos, int id) {
        this.addEntry(new ChunkEntry(pos, id));
    }

    public ChunkEntry getSelectedEntry() {
        return this.getSelected();
    }

    protected void removeAnEntry(ChunkEntry entry) {
        this.children().remove(entry);
        this.setSelected(null);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.renderOutline(this.getLeft(), this.getTop(), this.width, this.getBottom() - this.getTop(), 0x33FFFFFF);

    }

    public class ChunkEntry extends Entry<ChunkEntry> {

        private final ChunkPos pos;
        private final int id;

        public ChunkEntry(ChunkPos pos, int id) {
            this.pos = pos;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return false;
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pHovering, float pPartialTick) {
            boolean selected = TerritorySelectionScrollList.this.getSelected() == this;
            int color = selected ? 0xFFf0ed43 : (pHovering ? 0xFFFFAA00 : 0xFFFFFFFF);

            ClientTerritoryData.ClientClanTerritories territory = ClientTerritoryData.CLIENT_TERRITORIES.get(TerritorySelectionScrollList.this.clanUUID);
            if (territory == null) {
                pGuiGraphics.drawString(
                        Minecraft.getInstance().font,
                        "Invalid clan provided",
                        pLeft + 6,
                        pTop + 6,
                        color
                );
                return;
            }

            ClientTerritoryData.ClientChunk chunk = null;

            for (ClientTerritoryData.ClientChunk c : territory.claimedTerritory()) {
                if (c.chunkPos.equals(this.pos)) {
                    chunk = c;
                    break;
                }
            }
            if (chunk == null) return;

            pGuiGraphics.renderOutline(pLeft - 5, pTop, pWidth + 10, itemHeight-2, 0x99FFFFFF);

            pGuiGraphics.drawString(Minecraft.getInstance().font,this.id+1 + ") " + territory.clanName(), pLeft, pTop + 6, territory.color());

            pGuiGraphics.drawString(Minecraft.getInstance().font, chunk.name, pLeft, pTop + 16, 0xFFFFFFFF);

            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(pLeft, pTop + pHeight - 8, 0);
            float scale1 = 0.8f;
            pGuiGraphics.pose().scale(scale1, scale1, scale1);
            pGuiGraphics.drawString(
                    Minecraft.getInstance().font,
                    "X: " + chunk.chunkPos.x +", " + "Z: " + chunk.chunkPos.z,
                    0,
                    0,
                    ChatFormatting.GRAY.getColor()
            );

            float percentage = (float) chunk.time / (WCEServerConfig.SERVER.MAX_TERRITORY_TIME.get()*20*60);
            float scale2 = 0.6f;
            pGuiGraphics.pose().scale(scale2, scale2, scale2);
            if (percentage > 0.358) {
                pGuiGraphics.drawString(Minecraft.getInstance().font,
                        "Might contain prey and enemies", 0, -28, ChatFormatting.GRAY.getColor());
            }
            pGuiGraphics.pose().scale(1/scale2, 1/scale2, 1/scale2);

            String percentageString = String.format("%.1f", percentage*100) + "%";

            pGuiGraphics.drawString(
                    Minecraft.getInstance().font,
                    percentageString,
                    0,
                    -10,
                    ChatFormatting.GRAY.getColor()
            );

            pGuiGraphics.pose().popPose();

        }
    }

}

