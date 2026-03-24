package net.snowteb.warriorcats_events.screen;

import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
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

    private int pulsationCicle = 0;

    private boolean pulsationSwitch = false;

    private float pulsationIncrease = 0f;

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


        float versionScale = 1.4f;

        versionScale += pulsationIncrease;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(centerX + 70, centerY-90, 0.1);

        pGuiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(-20f));

        pGuiGraphics.pose().scale(versionScale, versionScale, versionScale);

        pGuiGraphics.drawCenteredString(this.font, WarriorCatsEvents.MOD_VERSION, 0, 0, ChatFormatting.GOLD.getColor());

        pGuiGraphics.pose().popPose();


        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);


    }

    @Override
    public void tick() {
        if (pulsationCicle >= 0 && pulsationCicle <= 10) {
            pulsationCicle++;

            if (pulsationSwitch) {
                pulsationIncrease += 0.02f;
            } else {
                pulsationIncrease -= 0.02f;
            }

            if (pulsationCicle >= 10) {
                pulsationSwitch = !pulsationSwitch;
                pulsationCicle = 0;
            }
        }
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
        line1 = "- Added Lavender and Lavender petals. You can now clean nests using Lavender petals.";
        line2 = "- Added morphs storage. You can now save, load, and even share in chat the morphs you make. This menu is accessible through the morph create menu. ";
        line3 = "- Added the new Golden Eagle entity. The Golden Eagle is agressive most of the time. If you ever find a friendly eagle, you might even be able to tame it! Tame Eagles can be called through the Ancient Stick. Model provided by bem te vi, feathers by Lightley.";
        line4 = "- Added new type of recipes. This update introduces a type of recipe using the Crafting Rock. Vanilla recipes such as for Freshkill and herbs, poultice, and traveling herb are now obtained through the new crafting system. The vanilla recipes will be removed in future updates to adapt to this new system.";
        line5 = "- Added cat collars! You can create your own collar and add spikes, a bell, and even make it glowy! Put a collar and a bell in a crafting table to add a bell to it. Put an iron ingot to add spikes, and glowstone, glowshroom, or glowberries to make it glow.";
        line6 = "- Added Footprints! Every entity will now leave footprints which will only be visible while having the Sharp Nose effect. You can get this effect with the integrated shape ability woodwalkers provides.";
        line7 = "- Improved AI for Wild Cats and added new behaviours. Also they will now periocally take naps and chill around hang out zones defined by Lavender Petals.";
        line8 = "- Fixed an issue with Clans that kicked players when changing dimensions.";
        line9 = "- Fixed and improved Sprint Leap.";
        line10 = "- Befriending cats should now be easier";
        line11 = "- Increased health for Wild Cats";
        line12 = "- Added Sharp Nose effect and Eagle Escapist effect";
        line13 = "- Added new Wiki for WCE";
        line14 = "------ For full changelog, visit the website. -------";
        line15 = "";
        line16 = "";
        line17 = "";
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
