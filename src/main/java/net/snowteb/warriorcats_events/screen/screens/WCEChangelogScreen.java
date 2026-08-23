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
                Component.translatable("screen.changelog.back"),
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
        lines.add("$(##) 1.12.2 | Fixed crashes when opening clan list, and other issues");

        lines.add("$(#) 1.12.0 | Dock bags, first person paw, and more!");
        lines.add("Greetings, I took and i'm still on a break to focus more on my personal life, but i'm still here.\n" +
                "\n" +
                "This update is mostly focused on fixes of some translation keys and other issues, but also includes the very much requested Dock bags!\n" +
                "\n" +
                "Dock bags can be crafted from the Herb Rock, these work as any other backpack. You can put items in them and carry them anywhere.\n" +
                "\n" +
                "This update also introduces background music for territories! Whenever you are in a clan territory, tracks from a internal playlist will play. Special thanks to Sharon Hurvitz and Joabi for allowing their songs to be part of this.");
        lines.add("$(##) Changelog");

        lines.add("- Added Bee Costume");
        lines.add("- Added Morph Cosmetics for contributors (more to come in the future)");
        lines.add("- Added Levitating emote for contributors.");
        lines.add("- Added Paw Bracelets");
        lines.add("- Fixed Golden Eagles. They will no longer fly away, no longer eat double food, and will now be able to stay or wander.");
        lines.add("- Fixed Moss balls, can no longer be filled with lava.");
        lines.add("- Added Dock Bags, they can be equipped as a chest armor piece, and as a curio.");
        lines.add("- Added config field for tree stump cooldowns.");
        lines.add("- Added background music for clan territories.");
        lines.add("- The lock on target button for leap is now a configurable keybind.");
        lines.add("- Jumping while leaping will now longer reset the leap, but decrease it instead.");
        lines.add("- Blocked monster spawn in clan territories.");
        lines.add("- The paw of you character will now render in first person instead of the old two-leg hand.");
        lines.add("- Changed the position in which held items render in first person.");
        lines.add("- Improved the client config menu.");
        lines.add("- Replaced the morph pose menu with the new morph cosmetics menu.");
        lines.add("- Fixed probably all of the issues with translation keys.");
        lines.add("- Improved horizontal momentum while jumping with the Jump skill.");
        lines.add("- Fall damage adjusted and fixed.");
        lines.add("- Added Collar and Paw Bracelets recipes to vanilla recipe book.");
        lines.add("- Adjustements to Broken paws, now they shouldn't be as annoying as before.");
        lines.add("- Added config field to allow or unallow anyone from creating a clan.");
        lines.add("- Replaced the way NPCs display their dialogues.");
        lines.add("- Removed Herobrinepaw");
        lines.add("- Other minor adjustements and fixes");


        lines.add("$(##) Discord Boosters");
        lines.add("Boost our Discord server to be a contributor and have exclusive emotes, bigger cat sizes, and other features!");

        lines.add("$(##) Our Minecraft server!");
        lines.add("Join our Minecraft server today! More information in the Discord server.");


        lines.add("$(/#) Thank you for reading 🐈");
    }


//    private void defineChangelogLines() {
//        lines.add("$(#) ");
//        lines.add("");
//        lines.add("$(##) Changelog");

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
//        lines.add("");
//        lines.add("");
//        lines.add("");
//        lines.add("");
//
//        lines.add("$(/#) Thank you for reading 🐈");
//    }




}
