package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.client.ClanInfo;
import net.snowteb.warriorcats_events.client.ClientClanCache;
import net.snowteb.warriorcats_events.sound.ModSounds;
import net.snowteb.warriorcats_events.util.ClanScrollList;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class ClanListScreen extends Screen {

    public ClanListScreen() {
        super(Component.literal("Clans"));
    }

    private ClanScrollList clanList;
    private float menuX;
    private final float targetX = 0;

    private Button territoryMapButton;

    @Override
    protected void init() {

        int centerX = width / 2;
        int centerY = height / 2;

        menuX = -200;

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

        territoryMapButton = Button.builder(
                Component.literal("Territory Map"),
                btn -> {
                    this.onClose();
                    WCEClient.playLocalSound(ModSounds.MENU_ACCEPT.get(), SoundSource.AMBIENT, 0.2f, 1.0f);
                    Minecraft.getInstance().setScreen(new TerritoryMapScreen(this));
                }
        ).bounds(70, this.height - 50, 80, 20).build();

        this.addRenderableWidget(clanList);
        this.addRenderableWidget(territoryMapButton);
    }


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        if (menuX < targetX) {
            menuX += (targetX - menuX) * 0.10f;
            if (menuX > targetX) menuX = targetX;
        }

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(menuX, 0, 0);

        pGuiGraphics.blit(Screen.BACKGROUND_LOCATION, 10, 10, 0.0F, 0, 200, 40, 32, 32);
        pGuiGraphics.fillGradient(RenderType.guiOverlay(), 10, 10,210, 100, 0xbd000000, 0, 0);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.pose().scale(1.2f,1.2f,1.2f);
        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, "Existing Clans", 92, 20, 0xFFFFFF);
        pGuiGraphics.pose().scale(5/6f,5/6f,5/6f);

        pGuiGraphics.renderOutline(10,10,200,this.height - 30,0xFFFFFFFF);

        pGuiGraphics.pose().popPose();

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
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (territoryMapButton.isHovered()) {
            territoryMapButton.mouseClicked(pMouseX, pMouseY, pButton);
            return true;
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
