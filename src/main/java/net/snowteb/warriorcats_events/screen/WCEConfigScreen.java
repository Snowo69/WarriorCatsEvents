package net.snowteb.warriorcats_events.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.screen.clandata.GradientSwitchButton;
import net.snowteb.warriorcats_events.util.GradientToggleButton;
import net.snowteb.warriorcats_events.zconfig.WCEClientConfig;
import org.lwjgl.glfw.GLFW;

import static net.snowteb.warriorcats_events.screen.clandata.CreateClanScreen.BG_TEXTURE;

@OnlyIn(Dist.CLIENT)
public class WCEConfigScreen extends Screen {

    private final Screen parent;


    private GradientSwitchButton leapToggleButton;
    private GradientSwitchButton ownMorphNameButton;
    private GradientSwitchButton ambientMusicButton;
    private GradientSwitchButton chatEntityBubblesButton;
    private GradientSwitchButton ownChatBubblesButton;

    private GradientToggleButton doneButton;
    private GradientToggleButton changelogButton;

    private boolean leapTemp;
    private boolean ownMorphNameTemp;
    private boolean ambientMusicTemp;
    private boolean chatBubblesTemp;
    private boolean ownChatBubblesTemp;

    public WCEConfigScreen(Screen parent) {
        super(Component.literal("Warrior Cats Events"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int centerY = height / 2;

        leapTemp = WCEClientConfig.CLIENT.LEAP.get();
        ownMorphNameTemp = WCEClientConfig.CLIENT.OWN_MORPH_NAME.get();
        ambientMusicTemp = WCEClientConfig.CLIENT.AMBIENT_MUSIC.get();
        chatBubblesTemp = WCEClientConfig.CLIENT.MORPH_CHAT_BUBBLES.get();
        ownChatBubblesTemp = WCEClientConfig.CLIENT.OWN_CHAT_BUBBLES.get();

        centerY -= 10;

        leapToggleButton = new GradientSwitchButton(
                centerX - 120, centerY - 20, 100, 20,
                "Toggle Leap", leapTemp,
                btn -> {
                    leapTemp = !leapTemp;
                }
        );

        ownMorphNameButton = new GradientSwitchButton(
                centerX + 20, centerY - 20, 100, 20,
                "Show Self Morph Name", ownMorphNameTemp,
                btn -> {
                    ownMorphNameTemp = !ownMorphNameTemp;
                }
        );

        ambientMusicButton = new GradientSwitchButton(
                centerX - 120, centerY + 10, 100, 20,
                "Background Music", ambientMusicTemp,
                btn -> {
                    ambientMusicTemp = !ambientMusicTemp;
                }
                );

        chatEntityBubblesButton = new GradientSwitchButton(
                centerX + 20, centerY + 10, 100, 20,
                "Players Chat Bubbles", chatBubblesTemp,
                btn -> {
                    chatBubblesTemp = !chatBubblesTemp;
                }
        );

        ownChatBubblesButton = new GradientSwitchButton(
                centerX - 120, centerY + 40, 100, 20,
                "Own Chat Bubbles", ownChatBubblesTemp,
                btn -> {
                    ownChatBubblesTemp = !ownChatBubblesTemp;
                }
        );

        doneButton = new GradientToggleButton(
                centerX - 20, centerY + 100, 40, 17,
                Component.literal("Done"),
                btn -> {
                    save();
                },  new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                60, 20, 1f, 0xFFFFFFFF
        );

        changelogButton = new GradientToggleButton(
                centerX + 50, centerY + 100, 80, 17,
                Component.literal("Last Changelog"),
                btn -> {
                    Minecraft.getInstance().setScreen(new WCEChangelogScreen(this));
                },  new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                60, 20, 1f, 0xFFFFFFFF
        );

        this.addRenderableWidget(leapToggleButton);
        this.addRenderableWidget(ownMorphNameButton);
        this.addRenderableWidget(ambientMusicButton);
        this.addRenderableWidget(chatEntityBubblesButton);
        this.addRenderableWidget(ownChatBubblesButton);

        this.addRenderableWidget(changelogButton);

        this.addRenderableWidget(doneButton);

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        int centerX = width / 2;
        int centerY = height / 2;

        pGuiGraphics.blit(BG_TEXTURE, 0, 0, 0, 0, this.width, this.height, this.width, this.height);

        float scale = 0.78f;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(centerX-(125*scale), centerY-(165*scale), 0);

        pGuiGraphics.pose().scale(scale, scale, scale);

        pGuiGraphics.blit(WCEClient.WCE_TITLE,
                0,
                0, 0, 0,
                250, 125,250,125);

        pGuiGraphics.pose().popPose();

        pGuiGraphics.renderOutline(centerX - 140, centerY - 40, 280, 150, 0x11FFFFFF);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);


    }

    private void save() {

        WCEClientConfig.CLIENT.LEAP.set(leapTemp);
        WCEClientConfig.CLIENT.OWN_MORPH_NAME.set(ownMorphNameTemp);
        WCEClientConfig.CLIENT.AMBIENT_MUSIC.set(ambientMusicTemp);
        WCEClientConfig.CLIENT.MORPH_CHAT_BUBBLES.set(chatBubblesTemp);
        WCEClientConfig.CLIENT.OWN_CHAT_BUBBLES.set(ownChatBubblesTemp);

        WCEClientConfig.SPEC.save();
        onClose();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
