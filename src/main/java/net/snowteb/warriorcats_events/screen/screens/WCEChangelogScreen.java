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
        line1 = "- Moss and hay nest recipes now accept other feathers";
        line2 = "- Reworked the shape ability. Now it displays a client side outline of different colors depending on the entity. A mobs color can be configured with datapacks.";
        line3 = "- Reworked stealth mode HUD overlay";
        line4 = "- Morph names will now display beside the player name in chat. You can toggle this using `/wce chatMorphName toggleChatMorphName` and toggle the fancy font using `/wce chatMorphName toggleFancyFont`";
        line5 = "- Changed whiskers texture";
        line6 = "- Makeshift nests will no longer reset respawn positions";
        line7 = "- Added Randomize button in morph create menu";
        line8 = "- Added many more orange patterns!";
        line9 = "- Added scars";
        line10 = "- Added blind eyes";
        line11 = "- Added player mates! You can now take a player as a mate by Right-clicking them with a Rare Flowers Bouquet. You can also request to bring kits to life through the menu when using Whiskers on a player.";
        line12 = "- Added `/wce summon`! You can now summon your own custom cats. Available for server ops and creative mode.";
        line13 = "- Added poses! You can now make your character have one of 3 different poses for now. Such as gracious, dominant, and refined. Use `/wce info morphPose`";
        line14 = "- Added Pebbles, Squirrel Skull, Badger Skull, Golden Eagle Skull and Prey bones. Place bones to make a bone pile.";
        line15 = "- Added Stone Nest and Lavender Nest";
        line16 = "- Added Moss balls! You can now make Moss Balls through the Crafting rock. You can fill them with water by right clicking any water source with them, and drink from them. You can throw them, and kits will love to play with them too!";
        line17 = "- Other minor adjustements, reworks, and fixes";
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
