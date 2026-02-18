package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.snowteb.warriorcats_events.client.ClanInfo;
import net.snowteb.warriorcats_events.client.ClientClanCache;
import net.snowteb.warriorcats_events.util.ClanScrollList;
import org.lwjgl.glfw.GLFW;

public class ClanListScreen extends Screen {

    public ClanListScreen() {
        super(Component.literal("Clans"));
    }

    private ClanScrollList clanList;

    @Override
    protected void init() {
        clanList = new ClanScrollList(
                minecraft,
                200,
                this.height - 40,
                40,
                this.height - 20,
                26
        );

        this.clanList.setLeftPos(((10)));
        this.clanList.setRenderTopAndBottom(false);

        for (ClanInfo info : ClientClanCache.getClans()) {
            clanList.addClan(info);
        }

        this.addRenderableWidget(clanList);
    }


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        pGuiGraphics.blit(Screen.BACKGROUND_LOCATION, 10, 10, 0.0F, 0, 200, 40, 32, 32);
        pGuiGraphics.fillGradient(RenderType.guiOverlay(), 10, 10,210, 100, 0xbd000000, 0, 0);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1.2f,1.2f,1.2f);
        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, "Existing Clans", 92, 20, 0xFFFFFF);
        pGuiGraphics.pose().popPose();

        pGuiGraphics.renderOutline(10,10,200,this.height - 30,0xFFFFFFFF);

    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == GLFW.GLFW_KEY_E) {
            this.onClose();
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
