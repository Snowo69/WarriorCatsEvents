package net.snowteb.warriorcats_events.screen.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.sounds.SoundSource;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.client.ClientStoredMorphs;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.sound.ModSounds;

import static net.snowteb.warriorcats_events.screen.screens.ManageClanScreen.pose;
import static net.snowteb.warriorcats_events.screen.screens.ManageClanScreen.rotation;

public class MorphsScrollList extends AbstractSelectionList<MorphsScrollList.MemberEntry> {

    public MorphsScrollList(Minecraft mc, int width, int height, int top, int bottom, int itemHeight) {

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

    public void clearOptions(){
        this.clearEntries();
    }

    public void addOption(String name, ClientStoredMorphs.MorphsFile.MorphData morphData) {
        this.addEntry(new MemberEntry(name, morphData));
    }

    public MemberEntry getSelectedEntry() {
        return this.getSelected();
    }

    public class MemberEntry extends Entry<MemberEntry> {

        private final String name;
        private WCatEntity wCatEntity;

        ClientStoredMorphs.MorphsFile.MorphData morphData;

        public MemberEntry(String name, ClientStoredMorphs.MorphsFile.MorphData data) {
            this.name = name;
            this.morphData = data;

            this.wCatEntity = new WCatEntity(ModEntities.WCAT.get(), Minecraft.getInstance().level);
            this.wCatEntity.setAnImage(true);
            this.wCatEntity.setOnGround(true);
            this.wCatEntity.setYRot(0);
            this.wCatEntity.yHeadRot = 0;
            this.wCatEntity.yBodyRot = 0;
            this.wCatEntity.setXRot(0);

            this.wCatEntity.setOnGeneticalSkin(true);
            this.wCatEntity.setGenetics(data.genetics());
            this.wCatEntity.setChimeraGenetics(data.chimeraGenetics());
            this.wCatEntity.setGeneticalVariants(data.variants());
            this.wCatEntity.setGeneticalVariantsChimera(data.chimeraVariants());
        }

        public String getKey() {
            return name;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            MorphsScrollList.this.setSelected(this);
            WCEClient.playLocalSound(ModSounds.MENU_CLICK.get(), SoundSource.NEUTRAL, 0.1f,1.3f);

            return true;
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pHovering, float pPartialTick) {
            boolean selected = MorphsScrollList.this.getSelected() == this;
            int color = selected ? 0xFFf0ed43 : (pHovering ? 0xFFFFAA00 : 0xFFFFFFFF);


            int bgColor = 0x22000000;
            pGuiGraphics.fill(pLeft, pTop, pLeft + width, pTop + height, bgColor);

            pGuiGraphics.drawString(
                    Minecraft.getInstance().font,
                    name,
                    pLeft + 6,
                    pTop + 6,
                    color
            );

                int imageX = pLeft + (pWidth - 15);
                int centerY = pTop + 14;

                InventoryScreen.renderEntityInInventory(pGuiGraphics, imageX, centerY, 3, pose, rotation, wCatEntity);


        }
    }

}


