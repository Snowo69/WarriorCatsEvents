package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientClanData;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCGenetics;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.util.GradientToggleButton;
import net.snowteb.warriorcats_events.util.IntSliderButton;

import static net.snowteb.warriorcats_events.screen.clandata.CreateClanScreen.BANNER;
import static net.snowteb.warriorcats_events.screen.clandata.CreateClanScreen.BG_TEXTURE;

public class CreateChimeraMorphGeneticsScreen extends Screen {

    private float animationTime = 0f;
    private float duration = 20f;
    private boolean closing = false;

    private final float startX = 0;
    private final float endX = 700;
    private float menuX = 0;

    // BASE SECTION
    // ORANGE SECTION
    // WHITE SECTION
    // ALBINO SECTION
    // DILUTE SECTION
    // AGOUTI AND TABBY SECTION
    // EYES AND EYES ANOMALY SECTION
    // FUR AND BOBTAIL AND NOISE SECTION

    private String mainSectionActiveMenu = "";
    private GradientToggleButton baseSection;
    private GradientToggleButton orangeSection;
    private GradientToggleButton whiteSection;
    private GradientToggleButton albinoSection;
    private GradientToggleButton diluteSection;
    private GradientToggleButton agoutiAndTabbySection;
    private GradientToggleButton noiseSection;
    private GradientToggleButton extraSection;


    private VariantScrollList noiseList;
    private IntSliderButton rufousingSlider;
    private IntSliderButton blueRufousingSlider;


    private GradientToggleButton setBlackButton;
    private GradientToggleButton setChocolateButton;
    private GradientToggleButton setCinnamonButton;

    private GradientToggleButton setOrangeButton;
    private GradientToggleButton setTortieButton;
    private GradientToggleButton setNotOrangeButton;
    private VariantScrollList orangeVariantList;

    private VariantScrollList baseChimeraVariantList;

    private GradientToggleButton setFullWhiteButton;
    private GradientToggleButton setHighWhiteButton;
    private GradientToggleButton setLowWhiteButton;
    private GradientToggleButton setNoWhiteButton;
    private VariantScrollList whiteVariantList;

    private GradientToggleButton setNotAlbinoButton;
    private GradientToggleButton setTrueAlbinoButton;
    private GradientToggleButton setSepiaButton;
    private GradientToggleButton setMinkButton;
    private GradientToggleButton setSiameseButton;
    private VariantScrollList albinoVariantList;


    private GradientSwitchButton diluteSwitch;

    private GradientSwitchButton agoutiSwitch;
    private GradientToggleButton setMackerelButton;
    private GradientToggleButton setClassicButton;
    private VariantScrollList tabbyVariantList;




    // 3 TOGGLE BUTTON
    private static final String setBlack = "B-b";
    private static final String setChocolate = "b-b1";
    private static final String setCinnamon = "b1-b1";


    // 3 TOGGLE BUTTON
    private static final String setOrange = "O-O";
    private static final String setTortie = "O-o";
    private static final String setNotOrange = "o-o";
    // 1 SCROLL LIST FOR TORTIE VARIANT


    // 4 TOGGLE BUTTON
    private static final String setFullWhite = "Wd-w";
    private static final String setHighWhite = "S-S";
    private static final String setLowWhite = "S-w";
    private static final String setNotWhite = "w-w";
    // 1 SCROLL LIST


    // 5 TOGGLE BUTTON
    private static final String setNotAlbino = "C-cs";

    private static final String setTrueAlbino = "c-c";
    private static final String setSepia = "cb-c";
    private static final String setMink = "cs-cb";
    private static final String setSiamese = "cs-c";
    // 1 SCROLL MENU


    // 1 SWITCH BUTTON
    private static final String setDilute = "D-d";
    private static final String setNonDilute = "d-d";


    // 1 SWITCH BUTTON
    private static final String setAgoutiTabby = "A-a";
    private static final String setNonAgoutiTabby = "a-a";

    // 2 TOGGLE BUTTON
    private static final String setTabbyStripesMackerel = "Mc-mc";
    private static final String setTabbyStripesClassic = "mc-mc";
    // 1 SCROLL MENU


    // 1 SWITCH BUTTON


    private static final String setChimera = "c-c";



    private boolean onGeneticalSkin = true;
    private GradientToggleButton saveButton;




    public CreateChimeraMorphGeneticsScreen() {
        super(Component.literal("Create Morph Genetics"));
    }


    WCGenetics genetics = new WCGenetics();
    WCGenetics.GeneticalVariants storedVariants = new WCGenetics.GeneticalVariants();

    WCGenetics geneticsChimera = new WCGenetics();
    WCGenetics.GeneticalChimeraVariants variantsChimera = new WCGenetics.GeneticalChimeraVariants();

    public CreateChimeraMorphGeneticsScreen(boolean isOnGeneticalSkin, WCGenetics genetics, WCGenetics.GeneticalVariants variants,
                                            WCGenetics chimeraGenetics, WCGenetics.GeneticalChimeraVariants chimeraVariants) {
        super(Component.literal("Create Morph Genetics"));
        onGeneticalSkin = isOnGeneticalSkin;

        this.genetics = genetics;
        this.storedVariants = variants;

        geneticsChimera = chimeraGenetics;
        variantsChimera = chimeraVariants;

    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;


        mainSectionActiveMenu = "base";


        saveButton = new GradientToggleButton(
                this.width - 80, 10,
                70, 20,
                Component.literal("Done"),
                btn -> {
                    closing = true;
                    animationTime = 0f;
                }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                60, 20, 1f, 0xFFFFFFFF
        );


        // SECTION BUTTONS
        {
            int buttonHeight = centerY - 80;


            int xOffset1 = -0;

            baseSection = new GradientToggleButton(
                    centerX - 135 + xOffset1, buttonHeight, 60, 16,
                    Component.literal("Base"),
                    btn -> {
                        selectMainSection(baseSection);
                        mainSectionActiveMenu = "base";
                        this.removeWidget(baseChimeraVariantList);
                        this.addRenderableWidget(baseChimeraVariantList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            orangeSection = new GradientToggleButton(
                    centerX - 65 + xOffset1, buttonHeight, 60, 16,
                    Component.literal("Orange"),
                    btn -> {
                        selectMainSection(orangeSection);
                        mainSectionActiveMenu = "orange";
                        if (setTortieButton.isSelected()) this.addRenderableWidget(orangeVariantList);


                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            whiteSection = new GradientToggleButton(
                    centerX + 5 + xOffset1, buttonHeight, 60, 16,
                    Component.literal("White"),
                    btn -> {
                        selectMainSection(whiteSection);
                        mainSectionActiveMenu = "white";
                        if (setHighWhiteButton.isSelected() || setLowWhiteButton.isSelected()) this.addRenderableWidget(whiteVariantList);


                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            albinoSection = new GradientToggleButton(
                    centerX + 75 + xOffset1, buttonHeight, 60, 16,
                    Component.literal("Albinism"),
                    btn -> {
                        selectMainSection(albinoSection);
                        mainSectionActiveMenu = "albinism";
                        if (setSiameseButton.isSelected() || setSepiaButton.isSelected() || setMinkButton.isSelected())
                            this.addRenderableWidget(albinoVariantList);


                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            buttonHeight += 20;

            int xOffset = -0;

            diluteSection = new GradientToggleButton(
                    centerX - 135 + xOffset, buttonHeight, 60, 16,
                    Component.literal("Dilute"),
                    btn -> {
                        selectMainSection(diluteSection);
                        mainSectionActiveMenu = "dilute";

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            agoutiAndTabbySection = new GradientToggleButton(
                    centerX - 65 + xOffset, buttonHeight, 60, 16,
                    Component.literal("Agouti"),
                    btn -> {
                        selectMainSection(agoutiAndTabbySection);
                        mainSectionActiveMenu = "agouti";

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );



            noiseSection = new GradientToggleButton(
                    centerX + 5 + xOffset, buttonHeight, 60, 16,
                    Component.literal("Details"),
                    btn -> {
                        selectMainSection(noiseSection);
                        mainSectionActiveMenu = "details";
                        this.removeWidget(noiseList);
                        this.addRenderableWidget(noiseList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            extraSection = new GradientToggleButton(
                    centerX + 75 + xOffset, buttonHeight, 60, 16,
                    Component.literal("Extra"),
                    btn -> {
                        selectMainSection(extraSection);
                        mainSectionActiveMenu = "extra";
                        this.removeWidget(rufousingSlider);
                        this.addRenderableWidget(rufousingSlider);
                        this.removeWidget(blueRufousingSlider);
                        this.addRenderableWidget(blueRufousingSlider);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

        }

        // BASE BUTTONS
        {
            setBlackButton = new GradientToggleButton(
                    35, centerY - 15, 80, 16,
                    Component.literal("Black"),
                    btn -> {
                        selectBase(setBlackButton);
                        geneticsChimera.base = setBlack;
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setChocolateButton = new GradientToggleButton(
                    35, centerY + 10, 80, 16,
                    Component.literal("Chocolate"),
                    btn -> {
                        selectBase(setChocolateButton);
                        geneticsChimera.base = setChocolate;
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setCinnamonButton = new GradientToggleButton(
                    35, centerY + 35, 80, 16,
                    Component.literal("Cinnamon"),
                    btn -> {
                        selectBase(setCinnamonButton);
                        geneticsChimera.base = setCinnamon;
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            int top = centerY - 20;
            int bottom = centerY + 110;
            this.baseChimeraVariantList = new VariantScrollList(
                    Minecraft.getInstance(),
                    80,
                    40,
                    top,
                    bottom,
                    20
            );
            this.baseChimeraVariantList.setRenderTopAndBottom(false);
            this.baseChimeraVariantList.setLeftPos(((centerX + 120)));

            for (int i = 0; i < WCGenetics.Values.MAX_CHIMERISM_VARIANTS; i++) {
                baseChimeraVariantList.addOption("Chimera " + (i + 1), i);
            }

            this.addRenderableWidget(baseChimeraVariantList);
        }

        // ORANGE BUTTONS
        {
            setOrangeButton = new GradientToggleButton(
                    35, centerY - 15, 80, 16,
                    Component.literal("Orange"),
                    btn -> {
                        selectOrange(setOrangeButton);
                        geneticsChimera.orangeBase = setOrange;
                        this.removeWidget(orangeVariantList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setTortieButton = new GradientToggleButton(
                    35, centerY + 10, 80, 16,
                    Component.literal("Tortie"),
                    btn -> {
                        selectOrange(setTortieButton);
                        geneticsChimera.orangeBase = setTortie;
                        this.addRenderableWidget(orangeVariantList);

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setNotOrangeButton = new GradientToggleButton(
                    35, centerY + 35, 80, 16,
                    Component.literal("No orange"),
                    btn -> {
                        selectOrange(setNotOrangeButton);
                        geneticsChimera.orangeBase = setNotOrange;
                        this.removeWidget(orangeVariantList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            int top = centerY - 20;
            int bottom = centerY + 110;
            this.orangeVariantList = new VariantScrollList(
                    Minecraft.getInstance(),
                    80,
                    40,
                    top,
                    bottom,
                    20
            );
            this.orangeVariantList.setRenderTopAndBottom(false);
            this.orangeVariantList.setLeftPos(((centerX + 120)));

            for (int i = 0; i < WCGenetics.Values.MAX_ORANGE_VARIANTS; i++) {
                orangeVariantList.addOption("Tortie " + (i + 1), i);
            }
        }

        // WHITE BUTTONS
        {
            setFullWhiteButton = new GradientToggleButton(
                    35, centerY - 15, 80, 16,
                    Component.literal("Full white"),
                    btn -> {
                        selectWhite(setFullWhiteButton);
                        geneticsChimera.whiteRatio = setFullWhite;
                        this.removeWidget(whiteVariantList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setHighWhiteButton = new GradientToggleButton(
                    35, centerY + 10, 80, 16,
                    Component.literal("High spots"),
                    btn -> {
                        selectWhite(setHighWhiteButton);
                        geneticsChimera.whiteRatio = setHighWhite;
                        this.removeWidget(whiteVariantList);
                        this.addRenderableWidget(whiteVariantList);

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setLowWhiteButton = new GradientToggleButton(
                    35, centerY + 35, 80, 16,
                    Component.literal("Low spots"),
                    btn -> {
                        selectWhite(setLowWhiteButton);
                        geneticsChimera.whiteRatio = setLowWhite;
                        this.removeWidget(whiteVariantList);
                        this.addRenderableWidget(whiteVariantList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setNoWhiteButton = new GradientToggleButton(
                    35, centerY + 60, 80, 16,
                    Component.literal("No white"),
                    btn -> {
                        selectWhite(setNoWhiteButton);
                        geneticsChimera.whiteRatio = setNotWhite;
                        this.removeWidget(whiteVariantList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            int top = centerY - 20;
            int bottom = centerY + 110;
            this.whiteVariantList = new VariantScrollList(
                    Minecraft.getInstance(),
                    80,
                    40,
                    top,
                    bottom,
                    20
            );
            this.whiteVariantList.setRenderTopAndBottom(false);
             this.whiteVariantList.setLeftPos(((centerX + 120)));

            for (int i = 0; i < WCGenetics.Values.MAX_WHITE_VARIANTS; i++) {
                whiteVariantList.addOption("Spots " + (i + 1), i);
            }
        }

        // ALBINO BUTTONS
        {
            setNotAlbinoButton = new GradientToggleButton(
                    35, centerY - 15, 80, 16,
                    Component.literal("Not albino"),
                    btn -> {
                        selectAlbino(setNotAlbinoButton);
                        geneticsChimera.albino = setNotAlbino;
                        this.removeWidget(albinoVariantList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setTrueAlbinoButton = new GradientToggleButton(
                    35, centerY + 10, 80, 16,
                    Component.literal("True albino"),
                    btn -> {
                        selectAlbino(setTrueAlbinoButton);
                        geneticsChimera.albino = setTrueAlbino;
                        this.removeWidget(albinoVariantList);

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setMinkButton = new GradientToggleButton(
                    35, centerY + 35, 80, 16,
                    Component.literal("Mink"),
                    btn -> {
                        selectAlbino(setMinkButton);
                        geneticsChimera.albino = setMink;
                        this.removeWidget(albinoVariantList);
                        this.addRenderableWidget(albinoVariantList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setSepiaButton = new GradientToggleButton(
                    35, centerY + 60, 80, 16,
                    Component.literal("Sepia"),
                    btn -> {
                        selectAlbino(setSepiaButton);
                        geneticsChimera.albino = setSepia;
                        this.removeWidget(albinoVariantList);
                        this.addRenderableWidget(albinoVariantList);

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setSiameseButton = new GradientToggleButton(
                    35, centerY + 85, 80, 16,
                    Component.literal("Siamese"),
                    btn -> {
                        selectAlbino(setSiameseButton);
                        geneticsChimera.albino = setSiamese;
                        this.removeWidget(albinoVariantList);
                        this.addRenderableWidget(albinoVariantList);

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            int top = centerY - 20;
            int bottom = centerY + 110;
            this.albinoVariantList = new VariantScrollList(
                    Minecraft.getInstance(),
                    80,
                    40,
                    top,
                    bottom,
                    20
            );
            this.albinoVariantList.setRenderTopAndBottom(false);
//            this.orangeVariantList.setRenderBackground(false);
            this.albinoVariantList.setLeftPos(((centerX + 120)));

            for (int i = 0; i < WCGenetics.Values.MAX_ALBINO_VARIANTS; i++) {
                albinoVariantList.addOption("Albino " + (i + 1), i);
            }
        }

        // DILUTE SWITCH
        {
            diluteSwitch = new GradientSwitchButton(
                    35, centerY - 15, 80, 16,
                    "Dilute",
                    false,
                    btn -> {
                        if (diluteSwitch.getValue()) {
                            geneticsChimera.dilute = setDilute;
                        } else {
                            geneticsChimera.dilute = setNonDilute;
                        }
                    }, 0xFFFFFFFF
            );
        }

        // AGOUTI SWITCH, BUTTONS, VARIANT LIST
        {
            agoutiSwitch = new GradientSwitchButton(
                    35, centerY - 15, 80, 16,
                    "Agouti",
                    false,
                    btn -> {
                        if (agoutiSwitch.getValue()) {
                            geneticsChimera.agouti = setAgoutiTabby;
                        } else {
                            geneticsChimera.agouti = setNonAgoutiTabby;
                        }
                    }, 0xFFFFFFFF
            );

            setMackerelButton = new GradientToggleButton(
                    35, centerY + 15, 80, 16,
                    Component.literal("Mackerel"),
                    btn -> {
                        selectAgoutiPattern(setMackerelButton);
                        geneticsChimera.tabbyStripes = setTabbyStripesMackerel;
                        this.removeWidget(tabbyVariantList);
                        this.addRenderableWidget(tabbyVariantList);

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setClassicButton = new GradientToggleButton(
                    35, centerY + 45, 80, 16,
                    Component.literal("Classic"),
                    btn -> {
                        selectAgoutiPattern(setClassicButton);
                        geneticsChimera.tabbyStripes = setTabbyStripesClassic;
                        this.removeWidget(tabbyVariantList);
                        this.addRenderableWidget(tabbyVariantList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            int top = centerY - 20;
            int bottom = centerY + 110;
            this.tabbyVariantList = new VariantScrollList(
                    Minecraft.getInstance(),
                    80,
                    40,
                    top,
                    bottom,
                    20
            );
            this.tabbyVariantList.setRenderTopAndBottom(false);
            this.tabbyVariantList.setLeftPos(((centerX + 120)));

            for (int i = 0; i < WCGenetics.Values.MAX_TABBY_VARIANTS; i++) {
                tabbyVariantList.addOption("Tabby " + (i + 1), i);
            }
        }


        // DETAILS
        {

            int top = centerY - 10;
            int bottom = centerY + 110;
            this.noiseList = new VariantScrollList(
                    Minecraft.getInstance(),
                    80,
                    40,
                    top,
                    bottom,
                    20
            );
            this.noiseList.setRenderTopAndBottom(false);
            this.noiseList.setLeftPos(((35)));

            for (int i = 0; i < WCGenetics.Values.MAX_NOISE_VARIANTS; i++) {
                noiseList.addOption("Noise " + (i + 1), i);
            }
        }

        // EXTRA
        {
            rufousingSlider = new IntSliderButton(36, centerY + 5,
                    80, 20,
                    0, 6, 0);

            blueRufousingSlider = new IntSliderButton(36, centerY + 45,
                    80, 20,
                    0, 6, 0);
        }

        this.addRenderableWidget(agoutiSwitch);
        this.addRenderableWidget(setClassicButton);
        this.addRenderableWidget(setMackerelButton);

        this.addRenderableWidget(diluteSwitch);

        this.addRenderableWidget(setNotAlbinoButton);
        this.addRenderableWidget(setTrueAlbinoButton);
        this.addRenderableWidget(setMinkButton);
        this.addRenderableWidget(setSepiaButton);
        this.addRenderableWidget(setSiameseButton);

        this.addRenderableWidget(setFullWhiteButton);
        this.addRenderableWidget(setHighWhiteButton);
        this.addRenderableWidget(setLowWhiteButton);
        this.addRenderableWidget(setNoWhiteButton);

        this.addRenderableWidget(setOrangeButton);
        this.addRenderableWidget(setTortieButton);
        this.addRenderableWidget(setNotOrangeButton);

        this.addRenderableWidget(setBlackButton);
        this.addRenderableWidget(setChocolateButton);
        this.addRenderableWidget(setCinnamonButton);

        this.addRenderableWidget(baseSection);
        this.addRenderableWidget(orangeSection);
        this.addRenderableWidget(whiteSection);
        this.addRenderableWidget(albinoSection);
        this.addRenderableWidget(diluteSection);
        this.addRenderableWidget(agoutiAndTabbySection);
        this.addRenderableWidget(noiseSection);
        this.addRenderableWidget(extraSection);

        this.addRenderableWidget(saveButton);

        menuX = 500;

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        pGuiGraphics.blit(BG_TEXTURE, 0, 0, 0, 0, this.width, this.height, this.width, this.height);

        if (closing) {
            animationTime += pPartialTick;

            float progress = Math.min(animationTime / duration, 1f);

            float eased = progress * progress * progress;

            menuX = startX + (endX - startX) * eased;

            if (progress >= 1f) {
                back();
            }
        } else {
            if (menuX > 0) {
                menuX -= (menuX) * 0.03f;
                if (menuX < 0) menuX = 0;
            }
        }

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(menuX, 0, 0);

        pGuiGraphics.enableScissor((int) (10 + menuX), 5, (int) (143 + menuX), 5 + 37);
        pGuiGraphics.blit(
                BANNER,
                10, 5,
                0, 0,
                800, 225,
                133, 37
        );
        pGuiGraphics.disableScissor();

        pGuiGraphics.blit(WCEClient.WCE_TITLE,
                centerX - 200 - this.width,
                centerY - 62, 0, 0,
                250, 125,250,125);

        pGuiGraphics.blit(WCEClient.WCE_TITLE,
                centerX - 125 + this.width,
                centerY - 62, 0, 0,
                250, 125,250,125);

        {
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(centerX, 15, 0);
            float scale = 1.6f;
            pGuiGraphics.pose().scale(scale, scale, scale);

            pGuiGraphics.drawCenteredString(this.font, Component.literal("Editing Chimerism").withStyle(ChatFormatting.GOLD),
                    0, 0, 0);
            pGuiGraphics.pose().popPose();
        }

        baseSection.visible = false;
        orangeSection.visible = false;
        whiteSection.visible = false;
        albinoSection.visible = false;
        diluteSection.visible = false;
        noiseSection.visible = false;
        extraSection.visible = false;
        agoutiAndTabbySection.visible = false;
        setBlackButton.visible = false;
        setChocolateButton.visible = false;
        setCinnamonButton.visible = false;
        setOrangeButton.visible = false;
        setTortieButton.visible = false;
        setNotOrangeButton.visible = false;
        orangeVariantList.setVisible(false);
        whiteVariantList.setVisible(false);
        tabbyVariantList.setVisible(false);
        setFullWhiteButton.visible = false;
        setHighWhiteButton.visible = false;
        setLowWhiteButton.visible = false;
        setNoWhiteButton.visible = false;
        setNotAlbinoButton.visible = false;
        setTrueAlbinoButton.visible = false;
        setMinkButton.visible = false;
        setSiameseButton.visible = false;
        setSepiaButton.visible = false;
        diluteSwitch.visible = false;
        agoutiSwitch.visible = false;
        setMackerelButton.visible = false;
        setClassicButton.visible = false;

        if (onGeneticalSkin) {
            baseSection.visible = true;
            orangeSection.visible = true;
            whiteSection.visible = true;
            albinoSection.visible = true;
            diluteSection.visible = true;
            noiseSection.visible = true;
            extraSection.visible = true;
            agoutiAndTabbySection.visible = true;

            if (mainSectionActiveMenu.equals("base")) {
                setBlackButton.visible = true;
                setChocolateButton.visible = true;
                setCinnamonButton.visible = true;

                variantsChimera.chimeraVariant = 0;
                if (baseChimeraVariantList.getSelectedEntry() != null) {
                    variantsChimera.chimeraVariant = baseChimeraVariantList.getSelectedEntry().getId();
                }

                pGuiGraphics.drawCenteredString(font, "Black base", 76, centerY - 30, 0xFFFFFFFF);
            }
            if (mainSectionActiveMenu.equals("orange")) {
                setOrangeButton.visible = true;
                setTortieButton.visible = true;
                setNotOrangeButton.visible = true;
                orangeVariantList.setVisible(true);

                if (orangeVariantList.getSelectedEntry() != null) {
                    variantsChimera.orangeVar = orangeVariantList.getSelectedEntry().getId();
                }

                pGuiGraphics.drawCenteredString(font, "Orange base", 76, centerY - 30, 0xFFFFFFFF);
            }

            if (mainSectionActiveMenu.equals("white")) {
                setFullWhiteButton.visible = true;
                setHighWhiteButton.visible = true;
                setLowWhiteButton.visible = true;
                setNoWhiteButton.visible = true;
                whiteVariantList.setVisible(true);

                if (whiteVariantList.getSelectedEntry() != null) {
                    variantsChimera.whiteVar = whiteVariantList.getSelectedEntry().getId();
                }

                pGuiGraphics.drawCenteredString(font, "White", 76, centerY - 30, 0xFFFFFFFF);
            }

            if (mainSectionActiveMenu.equals("albinism")) {
                setNotAlbinoButton.visible = true;
                setTrueAlbinoButton.visible = true;
                setMinkButton.visible = true;
                setSepiaButton.visible = true;
                setSiameseButton.visible = true;
                albinoVariantList.setVisible(true);

                if (albinoVariantList.getSelectedEntry() != null) {
                    variantsChimera.albinoVar = albinoVariantList.getSelectedEntry().getId();
                }

                pGuiGraphics.drawCenteredString(font, "Albinism", 76, centerY - 30, 0xFFFFFFFF);
            }

            if (mainSectionActiveMenu.equals("dilute")) {
                diluteSwitch.visible = true;
                pGuiGraphics.drawCenteredString(font, "Dilute", 76, centerY - 30, 0xFFFFFFFF);
            }

            if (mainSectionActiveMenu.equals("agouti")) {
                agoutiSwitch.visible = true;
                setClassicButton.visible = true;
                setMackerelButton.visible = true;
                tabbyVariantList.setVisible(true);

                if (tabbyVariantList.getSelectedEntry() != null) {
                    variantsChimera.tabbyVar = tabbyVariantList.getSelectedEntry().getId();
                }

                pGuiGraphics.drawCenteredString(font, "Agouti", 76, centerY - 30, 0xFFFFFFFF);
            }

            if (mainSectionActiveMenu.equals("details")) {


                if (noiseList.getSelectedEntry() != null) {
                    geneticsChimera.noise = noiseList.getSelectedEntry().getId();
                    variantsChimera.noise = noiseList.getSelectedEntry().getId();
                }

                pGuiGraphics.drawCenteredString(font, "Details", 76, centerY - 30, 0xFFFFFFFF);

            }

            if (mainSectionActiveMenu.equals("extra")) {

                variantsChimera.rufousingVariant = rufousingSlider.getActualValue();
                variantsChimera.blueRufousingVariant = blueRufousingSlider.getActualValue();

                pGuiGraphics.drawCenteredString(font, "Extra", 76, centerY - 30, 0xFFFFFFFF);
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().translate(76, centerY - 3, 0);
                pGuiGraphics.pose().scale(0.8f,0.8f,0.7f);
                pGuiGraphics.drawCenteredString(font, "Rufousing", 0, 0, 0xFFFFFFFF);
                pGuiGraphics.drawCenteredString(font, "Blue tint", 0, 50, 0xFFFFFFFF);
                pGuiGraphics.pose().popPose();

            }

            if (mainSectionActiveMenu.equals("chimera")) {

                pGuiGraphics.drawCenteredString(font, "Chimerism", 76, centerY - 30, 0xFFFFFFFF);
            }

        }

        if (!onGeneticalSkin) centerX += -50;

        WCatEntity entityToRender = new WCatEntity(ModEntities.WCAT.get(), Minecraft.getInstance().level);

        entityToRender.setOnGeneticalSkin(onGeneticalSkin);
        entityToRender.setGenetics(genetics);
        entityToRender.setGender(1);
        entityToRender.setGeneticalVariants(storedVariants.eyeColorLeft, storedVariants.eyeColorRight, storedVariants.rufousingVariant, storedVariants.blueRufousingVariant,
                storedVariants.orangeVar, storedVariants.whiteVar, storedVariants.tabbyVar, storedVariants.albinoVar, storedVariants.leftEyeVar,
                storedVariants.rightEyeVar, storedVariants.noise, storedVariants.size);

        entityToRender.setChimeraGenetics(geneticsChimera);

        entityToRender.setGeneticalVariantsChimera(variantsChimera.chimeraVariant, variantsChimera.rufousingVariant,
                variantsChimera.blueRufousingVariant, variantsChimera.orangeVar, variantsChimera.whiteVar, variantsChimera.tabbyVar,
                variantsChimera.albinoVar, variantsChimera.noise);

        entityToRender.setOnGround(true);
        entityToRender.setYRot(0);
        entityToRender.yHeadRot = 0;
        entityToRender.yBodyRot = 0;


        pGuiGraphics.renderOutline(centerX - 90, centerY - 40, 180, 157, 0x44FFFFFF);
        pGuiGraphics.fill(centerX - 90, centerY - 40, centerX + 90, centerY + 115, 0x07FFFFFF);

        pGuiGraphics.pose().pushPose();

        pGuiGraphics.pose().translate(centerX, centerY + 90, 0);

        float scale = 3.0f;

        pGuiGraphics.pose().scale(scale, scale, scale);

        InventoryScreen.renderEntityInInventoryFollowsMouse(
                pGuiGraphics,
                0,
                0,
                48,
                (float) (centerX - pMouseX),
                (float) ((centerY + 15) - pMouseY),
                entityToRender
        );

        pGuiGraphics.pose().popPose();


        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.pose().popPose();
    }

    private void selectMainSection(GradientToggleButton button) {
        baseSection.setSelected(false);
        orangeSection.setSelected(false);
        whiteSection.setSelected(false);
        albinoSection.setSelected(false);
        diluteSection.setSelected(false);
        agoutiAndTabbySection.setSelected(false);
        noiseSection.setSelected(false);
        extraSection.setSelected(false);

        this.removeWidget(orangeVariantList);
        this.removeWidget(whiteVariantList);
        this.removeWidget(albinoVariantList);
        this.removeWidget(tabbyVariantList);
        this.removeWidget(noiseList);
        this.removeWidget(rufousingSlider);
        this.removeWidget(blueRufousingSlider);
        this.removeWidget(baseChimeraVariantList);

        if (button != null) button.setSelected(true);
    }

    private void selectBase(GradientToggleButton button) {
        setBlackButton.setSelected(false);
        setChocolateButton.setSelected(false);
        setCinnamonButton.setSelected(false);

        button.setSelected(true);
    }

    private void selectOrange(GradientToggleButton button) {
        setOrangeButton.setSelected(false);
        setTortieButton.setSelected(false);
        setNotOrangeButton.setSelected(false);

        button.setSelected(true);
    }

    private void selectWhite(GradientToggleButton button) {
        setFullWhiteButton.setSelected(false);
        setHighWhiteButton.setSelected(false);
        setLowWhiteButton.setSelected(false);
        setNoWhiteButton.setSelected(false);

        button.setSelected(true);
    }

    private void selectAlbino(GradientToggleButton button) {
        setNotAlbinoButton.setSelected(false);
        setTrueAlbinoButton.setSelected(false);
        setSiameseButton.setSelected(false);
        setMinkButton.setSelected(false);
        setSepiaButton.setSelected(false);

        button.setSelected(true);
    }

    private void selectAgoutiPattern(GradientToggleButton button) {
        setMackerelButton.setSelected(false);
        setClassicButton.setSelected(false);

        button.setSelected(true);
    }

    private void back() {

        Minecraft.getInstance().setScreen(new CreateMorphGeneticsScreen(onGeneticalSkin, genetics, storedVariants, geneticsChimera, variantsChimera));

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            if (!ClientClanData.get().isFirstLoginHandled()) {
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
