package net.snowteb.warriorcats_events.screen.screens;

import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.screen.widgets.ChangelogScrollList;
import net.snowteb.warriorcats_events.screen.widgets.GradientToggleButton;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static net.snowteb.warriorcats_events.screen.screens.CreateClanScreen.BG_TEXTURE;

@OnlyIn(Dist.CLIENT)
public class WCEChangelogScreen extends Screen {

    private final Screen parent;

    private List<String> lines = new ArrayList<>();

    private GradientToggleButton backButton;

    private ChangelogScrollList changelogList;

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

        lines.clear();
        defineChangelogLines();

        changelogList = new ChangelogScrollList(Minecraft.getInstance(), 270, 200,
                centerY-50, centerY+80, 50);
        changelogList.setX(centerX-136);
        changelogList.setLogs(lines);
//        changelogList.setRenderTopAndBottom(false);



        backButton = new GradientToggleButton(
                centerX - 135, centerY + 90, 40, 17,
                Component.literal("Back"),
                btn -> {
                    onClose();
                },  ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/empty.png"),
                60, 20, 1f, 0xFFFFFFFF
        );

        this.addRenderableWidget(changelogList);
        this.addRenderableWidget(backButton);

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        int centerX = width / 2;
        int centerY = height / 2;

        pGuiGraphics.blit(BG_TEXTURE, 0, 0, 0, 0, this.width, this.height, this.width, this.height);

        float scale = 0.60f;
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

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

        pGuiGraphics.pose().popPose();


        float versionScale = 1.4f;

        versionScale += pulsationIncrease;

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(centerX + 70, centerY-90, 0.1);

        pGuiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(-20f));

        pGuiGraphics.pose().scale(versionScale, versionScale, versionScale);

        pGuiGraphics.drawCenteredString(this.font, WarriorCatsEvents.MOD_VERSION, 0, 0, ChatFormatting.GOLD.getColor());

        pGuiGraphics.pose().popPose();




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
        lines.add("1.9.0 | Diseases and more!");
        lines.add("It has been requested and asked for so much... It took a while and a hard work but it's finally here...\n" +
                "\n" +
                "Allow me to introduce the diseases system!\n" +
                "These will add a brand new level of complexity and challenge to the gameplay. \n" +
                "\n" +
                "Diseases will be obtained in different ways. Some are more common in certain environments, some others are caused by other circumstances such as falls or the presence of other conditions.\n" +
                "\n" +
                "The new diseases and conditions added for this update are: Greencough, Whitecough, Broken Leg, Chills, Deathberries Poisoning, Fever, Sore Pads.\n" +
                "\n" +
                "If you like to use the mod Serene Seasons, compatibility has been added for it so that Greencough might be a real issue during Leaf-bare...\n" +
                "\n" +
                "This update also adds a few more herbs and a few adjustements to the ones that were already in.\n" +
                "\n" +
                "You better start collecting herbs, because now those will be really needed.");
        lines.add("Changelog");

        lines.add("- Added the new Diseases system. Check the Warrior's Guide in game or the Wiki for more information.");
        lines.add("- Added Server config for Diseases system. You can also use `/gamerule wceDiseases`");
        lines.add("- The Serene Seasons season will now be displayed on the upper left side of the screen. You can disable this through the client config.");
        lines.add("- Added new Crafting Rock recipes for Comfrey poultice, leg wrap, poppy seeds, and yarrow poultice.");
        lines.add("- Reworked and improved the Changelog screen.");
        lines.add("- Reworked the Deathberries. It is no longer an effect, and will now be a disease. This will make Deathberries poisoning now actually deadly unless its treated.");
        lines.add("- Balanced some skills from the skill tree. Nerfed the Jump skill, you will now jump high only when sprinting.");
        lines.add("- Prey spawns for territory slightly balanced");
        lines.add("- Added `/wce disease ` commands.");
        lines.add("- Added `/wce changelog` command.");
        lines.add("- Fixed player morphs not showing correctly in the clan manage screen.");
        lines.add("- Fixed tree stumps not changing the clan name when a clan is renamed.");
        lines.add("- Added Feverfew, Feverfew leaves, Juniper, Juniper berries, Poppy seeds, Comfrey, Comfrey leaves, Comfrey root, Yarrow poultice, Comfrey poultice, Cobweb on a stick, Leg wrap.");
        lines.add("- Spiders now have a chance to drop Cobweb.");
        lines.add("- Added suspicious prey, mixed on a Crafting Rock using any wce prey or meat and deathberries. This prey will cause the same effect as eating Deathberries. Hold shift while hovering a prey item in your inventory to see if it is poisoned.");
        lines.add("- Added preview for kits spawned through the Kit item.");
        lines.add("- Boosted vanilla rabbit spawns.");
        lines.add("- Whiskers will now repair through time when they are in your inventory and are not being used. \n");
        lines.add("- Improved Claws and Whiskers tooltips. You can now view all their usages in the item itself when holding shift while hovering it in your inventory.");
        lines.add("- Other minor adjustments");

        lines.add("Thank you for reading 🐈");
    }

//    private void defineChangelogLines() {
//        lines.add("");
//        lines.add("");
//        lines.add("Changelog");

//        lines.add("");
//        lines.add("");
//        lines.add("");
//        lines.add("");
//        lines.add("");
//        lines.add("");
//        lines.add("");
//        lines.add("");
//        lines.add("");
//        lines.add("");
//        lines.add("");
//        lines.add("");
//        lines.add("");
//
//        lines.add("Thank you for reading 🐈");
//    }



}
