package net.snowteb.warriorcats_events.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.screen.clandata.GradientSwitchButton;
import net.snowteb.warriorcats_events.util.GradientToggleButton;
import net.snowteb.warriorcats_events.zconfig.WCEClientConfig;
import org.lwjgl.glfw.GLFW;

import java.util.List;

import static net.snowteb.warriorcats_events.screen.clandata.CreateClanScreen.BG_TEXTURE;

@OnlyIn(Dist.CLIENT)
public class WCEChangelogScreen extends Screen {

    private final Screen parent;

    private String line1 = "";
    private String line2 = "";
    private String line3 = "";
    private String line4 = "";
    private String line5 = "";
    private String line6 = "";
    private String line7 = "";
    private String line8 = "";
    private String line9 = "";
    private String line10 = "";
    private String line11 = "";
    private String line12 = "";
    private String line13 = "";
    private String line14 = "";
    private String line15 = "";
    private String line16 = "";
    private String line17 = "";
    private String line18 = "";

    private GradientToggleButton backButton;

    public WCEChangelogScreen(Screen parent) {
        super(Component.literal("Warrior Cats Events"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int centerY = height / 2;

        defineChangelogLines();

        backButton = new GradientToggleButton(
                centerX - 135, centerY + 90, 40, 17,
                Component.literal("Back"),
                btn -> {
                    onClose();
                },  new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                60, 20, 1f, 0xFFFFFFFF
        );

        this.addRenderableWidget(backButton);

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        int centerX = width / 2;
        int centerY = height / 2;

        pGuiGraphics.blit(BG_TEXTURE, 0, 0, 0, 0, this.width, this.height, this.width, this.height);

        float scale = 0.60f;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(centerX-(125*scale), centerY-(215*scale), 0);

        pGuiGraphics.pose().scale(scale, scale, scale);

        pGuiGraphics.blit(WCEClient.WCE_TITLE,
                0,
                0, 0, 0,
                250, 125,250,125);

        pGuiGraphics.pose().popPose();

        pGuiGraphics.renderOutline(centerX - 140, centerY -60, 280, 170, 0x11FFFFFF);

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(centerX-135, centerY-55, 0);

        float textScale = 0.50f;
        pGuiGraphics.pose().scale(textScale, textScale, textScale);

        int y = 0;

        List<String> lines = List.of(line1, line2, line3, line4, line5, line6,
                line7, line8, line9, line10, line11, line12, line13, line14, line15, line16, line17, line18);

        for (String line : lines) {
            List<FormattedCharSequence> wrapped = this.font.split(FormattedText.of(line), 550);
            for (FormattedCharSequence subLine : wrapped) {


                pGuiGraphics.drawString(this.font,subLine, 0, y, 0xFFFFFFFF);
                y += this.font.lineHeight;
            }
            y+=6;
        }


        pGuiGraphics.pose().popPose();



        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);


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

    private void defineChangelogLines() {
        line1 = "- Added chimerism!";
        line2 = "- Adjustements to some GUIs";
        line3 = "- Added XP drop to fishing";
        line4 = "- Own morph name should now display correctly";
        line5 = "- Warrior's Guide updated";
        line6 = "- Adjustments to feather drops";
        line7 = "- Added a config GUI, accessed through the \"Mods\" section";
        line8 = "- Added more white spot variants!";
        line9 = "- Added chat bubbles! (configurable and disableable)";
        line10 = "- Added more tortie variants!";
        line11 = "- Added a changelog GUI, accessed through the config GUI";
        line12 = "- Added more types of feather and body feathers accessory";
        line13 = "- Added red eyes to the create morph menu";
        line14 = "- Prey items balanced";
        line15 = "- Added 4 more advancements";
        line16 = "- Added an admin command to force delete clans in the server";
        line17 = "- Other minor adjustments";
        line18 = "";
    }

//    private void defineChangelogLines() {
//        line1 = "";
//        line2 = "";
//        line3 = "";
//        line4 = "";
//        line5 = "";
//        line6 = "";
//        line7 = "";
//        line8 = "";
//        line9 = "";
//        line10 = "";
//        line11 = "";
//        line12 = "";
//        line13 = "";
//        line14 = "";
//        line15 = "";
//        line16 = "";
//        line17 = "";
//        line18 = "";
//    }



}
