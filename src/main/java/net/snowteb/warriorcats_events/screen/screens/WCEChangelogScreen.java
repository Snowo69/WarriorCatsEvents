package net.snowteb.warriorcats_events.screen.screens;

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
import net.snowteb.warriorcats_events.screen.widgets.GradientToggleButton;
import org.lwjgl.glfw.GLFW;

import java.util.List;

import static net.snowteb.warriorcats_events.screen.screens.CreateClanScreen.BG_TEXTURE;

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
        line1 = "- Added Climbing system! Unlock the climb skill through the skill tree, use the keybind \"C\" by default to start climbing.";
        line2 = "- Thirst system can now be disabled through world config";
        line3 = "- Skill tree attributes now only apply when on cat form";
        line4 = "- Fixed some screens not showing cat npcs correctly ";
        line5 = "- You can now rename clans through `/wce clan manage`";
        line6 = "- Fixed scars and silver variant not loading from saved morphs";
        line7 = "- Reworked territory claiming. The cost is now more expensive, but the claimed territory has been expanded from 1 chunk to 9 chunks.";
        line8 = "- Added bio for characters and a custom gender field. Use `/wce info profile`";
        line9 = "- Added honey mossball. Put a moss-ball and a honey bottle or honey block on a crafting rock to get a honey moss-ball. These not only restore food, but will also bounce a lot more!";
        line10 = "- Removed vanilla (Crafting Table) recipes for: Freshkill & herbs, Traveling Herbs. These are now crafted through the Crafting Rock";
        line11 = "- Added JEI compatibility. Recipes for certain items now will show their Crafting Rock recipe. ";
        line12 = "- Drinking water from bottles now also restores thirst";
        line13 = "- Added kittypet bowls of different colors.";
        line14 = "- Now its not possible to attack cats too close to moss-balls";
        line15 = "- Added accesories only obtained from villages chests: Pink bow, red bow, black bow, cat hat. And added Skull Mask";
        line16 = "- Saved eagles from drowning (now for good)";
        line17 = "- Other minor adjustements and fixes";
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
