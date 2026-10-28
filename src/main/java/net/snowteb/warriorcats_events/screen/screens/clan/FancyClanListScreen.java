package net.snowteb.warriorcats_events.screen.screens.clan;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.client.ClientClanData;
import net.snowteb.warriorcats_events.client.ClientClans;
import net.snowteb.warriorcats_events.screen.screens.TerritoryMapScreen;
import net.snowteb.warriorcats_events.screen.screens.createmorph.FancySimpleButton;
import net.snowteb.warriorcats_events.screen.widgets.ClanScrollList;
import net.snowteb.warriorcats_events.sound.ModSounds;
import net.snowteb.warriorcats_events.util.ModKeybinds;
import org.lwjgl.glfw.GLFW;

import java.util.UUID;

public class FancyClanListScreen extends Screen {

    public FancyClanListScreen(boolean seeingMyClan) {
        super(Component.literal("Clans"));
        this.seeingMyClan = seeingMyClan;
        menuX = -200;
    }

    private ClanScrollList clanList;
    private float menuX;
    private final boolean seeingMyClan;

    @Override
    protected void init() {

        if (seeingMyClan) {
            UUID myClanUUID = ClientClanData.get().getCurrentClanUUID();
            ClientClans.Entry clan = ClientClans.getClan(myClanUUID);
            if (!myClanUUID.equals(ClanData.EMPTY_UUID) && clan != null) {
                Minecraft.getInstance().setScreen(new FancySpecificClanScreen(clan));
                return;
            } else {
                LocalPlayer localPlayer = Minecraft.getInstance().player;
                if (localPlayer != null) Minecraft.getInstance().player
                        .sendSystemMessage(Component.translatable("clan.player_not_clan")
                                .withStyle(ChatFormatting.YELLOW));
            }
        }

        clanList = new ClanScrollList(
                minecraft,
                200,
                this.height - 90,
                40,
                30
        );

        this.clanList.setX(((10)));

        clanList.addClans(ClientClans.getClans().values().stream().toList());

        int buttonX = clanList.getX() + ((clanList.getWidth()) / 2) - 40;
        FancySimpleButton territoryMapButton = new FancySimpleButton(
                80, 20,
                buttonX, this.height - 45,
                Component.translatable("screen.clanlist.territory_map"),
                btn -> {
                    this.onClose();
                    WCEClient.playLocalSound(ModSounds.MENU_ACCEPT.get(), SoundSource.AMBIENT, 0.2f, 1.0f);
                    Minecraft.getInstance().setScreen(new TerritoryMapScreen(this));
                },
                0.9f, true
        );

        this.addRenderableWidget(clanList);
        this.addRenderableWidget(territoryMapButton);
    }


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        float targetX = 0;
        if (menuX < targetX) {
            menuX += (targetX - menuX) * 0.10f;
            if (menuX > targetX) menuX = targetX;
        }

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(menuX, 0, 0);

        pGuiGraphics.blit(Screen.MENU_BACKGROUND, 10, 10, 0.0F, 0, 200, 40, 32, 32);
        pGuiGraphics.fillGradient(RenderType.gui(), 10, 10,210, 100, 0xbd000000, 0, 0);

        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.fill(clanList.getX(), clanList.getBottom(),
                clanList.getRight(), clanList.getBottom() + 30,
                0xFF000000);

        for (Renderable renderable : this.renderables) {
            renderable.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(clanList.getX() + ((float) clanList.getWidth() /2), clanList.getY() - 20, 0);
        float scale = 1.2f;
        pGuiGraphics.pose().scale(scale, scale, scale);
        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font,
                Component.translatable("screen.clanlist.existing_clans"),
                0, 0,
                0xFFFFFF);
        pGuiGraphics.pose().popPose();

        pGuiGraphics.renderOutline(clanList.getX(),clanList.getY() - 30,
                clanList.getWidth(),clanList.getBottom() + 20,
                0xFFFFFFFF);

        pGuiGraphics.pose().popPose();

    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == GLFW.GLFW_KEY_E || pKeyCode == ModKeybinds.OPTIONS_KEY.getKey().getValue()) {
            this.onClose();
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(10f,10f,1f);
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.pose().popPose();
    }
}
