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
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.clan.SavePlayerGeneticsPacket;
import net.snowteb.warriorcats_events.util.FloatSliderButton;
import net.snowteb.warriorcats_events.util.GradientToggleButton;
import net.snowteb.warriorcats_events.util.IntSliderButton;

import static net.snowteb.warriorcats_events.screen.clandata.CreateClanScreen.BANNER;
import static net.snowteb.warriorcats_events.screen.clandata.CreateClanScreen.BG_TEXTURE;

public class CreateMorphGeneticsScreen extends Screen {

    private float animationTime = 0f;
    private float duration = 20f;
    private boolean closing = false;

    private final float startX = 0;
    private final float endX = -700;
    private float menuX = 0;


    private static String eyeColorLeft = "yellow";
    private static String eyeColorRight = "yellow";

    private static int rufousingVariant = 0;
    private static int blueRufousingVariant = 0;

    private static int orangeBaseVariant = 0;
    private static int whiteRatioVariant = 0;
    private static int tabbyStripesVariant = 0;
    private static int albinoVariant = 0;
    private static int eyeColorVariantLeft = 0;
    private static int eyeColorVariantRight = 0;
    private static int noise = 0;
    private static float size = 0;

    private String chimeraGene = setNotChimera;


    // BASE SECTION
    // ORANGE SECTION
    // WHITE SECTION
    // ALBINO SECTION
    // DILUTE SECTION
    // AGOUTI AND TABBY SECTION
    // EYES AND EYES ANOMALY SECTION
    // FUR AND BOBTAIL AND NOISE SECTION

    private VariantScrollList variantScrollList;

    private String mainSectionActiveMenu = "";
    private GradientToggleButton baseSection;
    private GradientToggleButton orangeSection;
    private GradientToggleButton whiteSection;
    private GradientToggleButton albinoSection;
    private GradientToggleButton diluteSection;
    private GradientToggleButton agoutiAndTabbySection;
    private GradientToggleButton eyesAndAnomalySection;
    private GradientToggleButton furBobtailNoiseSection;
    private GradientToggleButton extraSection;

    private GradientToggleButton chimeraSection;
    private GradientSwitchButton chimeraSwitch;
    private GradientToggleButton editChimera;


    private GradientSwitchButton bobtailSwitch;
    private GradientSwitchButton chestFurSwitch;
    private GradientSwitchButton bellyFurSwitch;
    private GradientSwitchButton legsFurSwitch;
    private GradientSwitchButton headFurSwitch;
    private GradientSwitchButton cheekFurSwitch;
    private GradientSwitchButton backFurSwitch;
    private GradientSwitchButton tailFurSwitch;
    private VariantScrollList noiseList;
    private FloatSliderButton sizeSlider;
    private IntSliderButton rufousingSlider;
    private IntSliderButton blueRufousingSlider;


    private GradientToggleButton setBlackButton;
    private GradientToggleButton setChocolateButton;
    private GradientToggleButton setCinnamonButton;

    private GradientToggleButton setOrangeButton;
    private GradientToggleButton setTortieButton;
    private GradientToggleButton setNotOrangeButton;
    private VariantScrollList orangeVariantList;

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



    private GradientSwitchButton heterochromiaSwitch;

    private GradientToggleButton setLeftEyeBlueButton;
    private GradientToggleButton setLeftEyeGreenButton;
    private GradientToggleButton setLeftEyeYellowButton;
    private GradientToggleButton setLeftEyeRedButton;
    private VariantScrollList leftEyeVariantList;

    private GradientToggleButton setRightEyeBlueButton;
    private GradientToggleButton setRightEyeGreenButton;
    private GradientToggleButton setRightEyeYellowButton;
    private GradientToggleButton setRightEyeRedButton;
    private VariantScrollList rightEyeVariantList;



    // 1 SWITCH BUTTON FOR TAIL
    private static final String setBobtail = "b-b";
    private static final String setFulltail = "B-b";

    // 1 SWITCH BUTTON FOR EACH PART
    private static final String setShortFur = "s-s";
    private static final String setLongFur = "L-s";


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


    // 3 TOGGLE BUTTON FOR EACH EYE
    private static final String setEyeBlue = "blue";
    private static final String setEyeYellow = "yellow";
    private static final String setEyeGreen = "green";
    private static final String setEyeRed = "red";
    // 1 SCROLL MENUS FOR EACH EYE

    // 1 SWITCH BUTTON
    private static final String setHeteroChromia = "h-h";
    private static final String setNonHeteroChromia = "H-h";

    private static final String setChimera = "c-c";
    private static final String setNotChimera = "C-C";



    private boolean onGeneticalSkin = false;
    private GradientToggleButton setOnGeneticalSkinButton;
    private GradientToggleButton setOnPresetSkinButton;
    private GradientToggleButton saveButton;


    WCGenetics genetics = new WCGenetics();
    WCGenetics geneticsChimera = new WCGenetics();
    WCGenetics.GeneticalChimeraVariants variantsChimera = new WCGenetics.GeneticalChimeraVariants();

    public CreateMorphGeneticsScreen() {
        super(Component.literal("Create Morph Genetics"));
    }

    private boolean comingBackFromChimeraMenu = false;
    private boolean goingToChimeraMenu = false;


    private String chestFur = setShortFur;
    private String bellyFur = setShortFur;
    private String legsFur = setShortFur;
    private String headFur = setShortFur;
    private String cheekFur = setShortFur;
    private String backFur = setShortFur;
    private String tailFur = setShortFur;

    private String bobtail = setFulltail;

    private String base = setBlack;
    private String orangeBase = setNotOrange;
    private String whiteRatio = setNotWhite;
    private String albino = setNotAlbino;
    private String dilute = setNonDilute;
    private String agouti = setNonAgoutiTabby;
    private String tabbyStripes = setTabbyStripesMackerel;
    private String eyesAnomaly = setNonHeteroChromia;
    private int rufousing = 0;
    private int blueRufousing = 0;
    private int storedNoise = 0;


    public CreateMorphGeneticsScreen(boolean isOnGeneticalSkin, WCGenetics genetics, WCGenetics.GeneticalVariants variants,
        WCGenetics chimeraGenetics, WCGenetics.GeneticalChimeraVariants chimeraVariants) {
        super(Component.literal("Create Morph Genetics"));
        onGeneticalSkin = isOnGeneticalSkin;

        chestFur = genetics.chestFur;
        bellyFur = genetics.bellyFur;
        legsFur = genetics.legsFur;
        headFur = genetics.headFur;
        cheekFur = genetics.cheekFur;
        backFur = genetics.backFur;
        tailFur = genetics.tailFur;
        bobtail = genetics.bobtail;
        base = genetics.base;
        orangeBase = genetics.orangeBase;
        whiteRatio = genetics.whiteRatio;
        albino = genetics.albino;
        dilute = genetics.dilute;
        agouti = genetics.agouti;
        tabbyStripes = genetics.tabbyStripes;
        eyesAnomaly = genetics.eyesAnomaly;
        rufousing = genetics.rufousing;
        blueRufousing = genetics.blueRufousing;
        storedNoise = genetics.noise;

        comingBackFromChimeraMenu = true;

        eyeColorLeft = variants.eyeColorLeft;
        eyeColorRight = variants.eyeColorRight;
        rufousingVariant = variants.rufousingVariant;
        blueRufousingVariant = variants.blueRufousingVariant;
        orangeBaseVariant = variants.whiteVar;
        tabbyStripesVariant = variants.tabbyVar;
        albinoVariant = variants.albinoVar;
        eyeColorVariantLeft = variants.leftEyeVar;
        eyeColorVariantRight = variants.rightEyeVar;
        storedNoise = variants.noise;
        size = variants.size;

        geneticsChimera = chimeraGenetics;
        variantsChimera = chimeraVariants;

        chimeraGene = setChimera;

    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        {
            int listWidth = 130;
            int listHeight = 40;
            int top = centerY - 35;
            int bottom = centerY + 110;

            this.variantScrollList = new VariantScrollList(
                    Minecraft.getInstance(),
                    listWidth,
                    listHeight,
                    top,
                    bottom,
                    20
            );
            this.variantScrollList.setRenderTopAndBottom(false);
            this.variantScrollList.setLeftPos(((this.width - 160)));

            variantScrollList.addOption("Calico", 0);
            variantScrollList.addOption("Siamese", 1);
            variantScrollList.addOption("Gray", 2);
            variantScrollList.addOption("Abyssinian", 3);
            variantScrollList.addOption("Black", 4);
            variantScrollList.addOption("Maine Coon", 5);
            variantScrollList.addOption("Russian Blue", 6);
            variantScrollList.addOption("Dark Brown Tabby", 7);
            variantScrollList.addOption("White", 8);
            variantScrollList.addOption("Calico 2", 9);
            variantScrollList.addOption("Munchkin", 10);
            variantScrollList.addOption("Light Gray Tabby", 11);
            variantScrollList.addOption("Chestnutpatch (Bookwom)", 12);
            variantScrollList.addOption("Ratstar (Telefonjoker)", 13);
            variantScrollList.addOption("Twitchstream (Cat)", 14);
            variantScrollList.addOption("Blazepit (Cat)", 15);
            variantScrollList.addOption("Bengalpelt (Klyonstar)", 16);
            variantScrollList.addOption("Sparrowstar (Whale_shark)", 17);
            variantScrollList.addOption("Foxeater (Sejr)", 18);
            variantScrollList.addOption("Willowsong (Sejr)", 19);
            variantScrollList.addOption("White 2", 20);
            variantScrollList.addOption("Dalmatian", 21);
            variantScrollList.addOption("Gray Tabby", 22);
            variantScrollList.addOption("Brown", 23);
            variantScrollList.addOption("Pale Ginger", 24);
            variantScrollList.addOption("Black 2", 25);
            variantScrollList.addOption("Bengal", 26);
            variantScrollList.addOption("Snowshoe", 27);
            variantScrollList.addOption("Toyger", 28);
            variantScrollList.addOption("Turkish Van", 29);
            variantScrollList.addOption("Albino (CoffeeCat)", 30);
            variantScrollList.addOption("Bengal (CoffeeCat)", 31);
            variantScrollList.addOption("Brindle Tortie (Mswolfy81)", 32);
            variantScrollList.addOption("Cream Calico 1 (Lightley)", 33);
            variantScrollList.addOption("Cream Calico 2 (Lightley)", 34);
            variantScrollList.addOption("Cream Calico 3 (Lightley)", 35);
            variantScrollList.addOption("Caramel (CoffeeCat)", 36);
            variantScrollList.addOption("Frostdawn (whitenoisewife)", 37);
            variantScrollList.addOption("Gray-white Tabby (Slay)", 38);
            variantScrollList.addOption("Hailflake (pvppet, Mswolfy81)", 39);
            variantScrollList.addOption("Karpati (whitenoisewife)", 40);
            variantScrollList.addOption("Leafstar (whitenoisewife)", 41);
            variantScrollList.addOption("Longtail (whitenoisewife)", 42);
            variantScrollList.addOption("Mothpaw (CoffeeCat)", 43);
            variantScrollList.addOption("Redtail (whitenoisewife)", 44);
            variantScrollList.addOption("Salem (CoffeeCat, Mswolfy81)", 45);
            variantScrollList.addOption("Short hair (CoffeeCat)", 46);
            variantScrollList.addOption("Stoneflare (Feathered Melodica)", 47);
            variantScrollList.addOption("Tortie point (whitenoisewife)", 48);
            variantScrollList.addOption("Turtleheart (RainbowServal, Mswolfy81)", 49);
            variantScrollList.addOption("Violetdew (bem te vi, Mswolfy81)", 50);
            variantScrollList.addOption("Patch (Feathered Melodica)", 51);
            variantScrollList.addOption("Parlee (PsychicStudios, CoffeeCat)", 52);


        }

        mainSectionActiveMenu = "base";

        genetics.chestFur = setShortFur;
        genetics.bellyFur = setShortFur;
        genetics.legsFur = setShortFur;
        genetics.headFur = setShortFur;
        genetics.cheekFur = setShortFur;
        genetics.backFur = setShortFur;
        genetics.tailFur = setShortFur;

        genetics.bobtail = setFulltail;

        genetics.base = setBlack;
        genetics.orangeBase = setNotOrange;
        genetics.whiteRatio = setNotWhite;
        genetics.albino = setNotAlbino;
        genetics.dilute = setNonDilute;
        genetics.agouti = setNonAgoutiTabby;
        genetics.tabbyStripes = setTabbyStripesMackerel;
        genetics.eyesAnomaly = setNonHeteroChromia;
        genetics.rufousing = 0;
        genetics.blueRufousing = 0;
        genetics.noise = 0;

        if (comingBackFromChimeraMenu) {
            genetics.chestFur = chestFur;
            genetics.bellyFur = bellyFur;
            genetics.legsFur = legsFur;
            genetics.headFur = headFur;
            genetics.cheekFur = cheekFur;
            genetics.backFur = backFur;
            genetics.tailFur = tailFur;
            genetics.bobtail = bobtail;
            genetics.base = base;
            genetics.orangeBase = orangeBase;
            genetics.whiteRatio = whiteRatio;
            genetics.albino = albino;
            genetics.dilute = dilute;
            genetics.agouti = agouti;
            genetics.tabbyStripes = tabbyStripes;
            genetics.eyesAnomaly = eyesAnomaly;
            genetics.rufousing = rufousing;
            genetics.blueRufousing = blueRufousing;
            genetics.noise = noise;
            noise = storedNoise;
        }

        setOnGeneticalSkinButton = new GradientToggleButton(
                150, 10,
                90, 20,
                Component.literal("Custom morph"),
                btn -> {
                    onGeneticalSkin = true;
                    this.removeWidget(this.variantScrollList);
                    selectMode(setOnGeneticalSkinButton);
                    mainSectionActiveMenu = "base";
                    if (!baseSection.isSelected()) selectMainSection(baseSection);
                }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20, 1f, 0xFFFFFFFF
        );

        setOnPresetSkinButton = new GradientToggleButton(
                250, 10,
                90, 20,
                Component.literal("Preset morph"),
                btn -> {
                    onGeneticalSkin = false;
                    this.removeWidget(this.variantScrollList);
                    this.addRenderableWidget(this.variantScrollList);
                    selectMode(setOnPresetSkinButton);
                    mainSectionActiveMenu = "base";
                    selectMainSection(baseSection);
                }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20, 1f, 0xFFFFFFFF
        );

        saveButton = new GradientToggleButton(
                this.width - 80, 10,
                70, 20,
                Component.literal("Save morph"),
                btn -> {
                    closing = true;
                    animationTime = 0f;
                }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                60, 20, 1f, 0xFFFFFFFF
        );

        // MANDATORY INIT
        {
            int top1 = centerY - 35;
            int bottom1 = centerY + 35;
            this.leftEyeVariantList = new VariantScrollList(
                    Minecraft.getInstance(),
                    80,
                    40,
                    top1,
                    bottom1,
                    20
            );
            this.leftEyeVariantList.setRenderTopAndBottom(false);
            this.leftEyeVariantList.setLeftPos(((centerX + 120)));

            for (int i = 0; i < WCGenetics.Values.MAX_EYE_VARIANTS; i++) {
                leftEyeVariantList.addOption("Color " + (i + 1), i);
            }


            int top2 = centerY + 45;
            int bottom2 = centerY + 115;
            this.rightEyeVariantList = new VariantScrollList(
                    Minecraft.getInstance(),
                    80,
                    40,
                    top2,
                    bottom2,
                    20
            );
            this.rightEyeVariantList.setRenderTopAndBottom(false);
            this.rightEyeVariantList.setLeftPos(((centerX + 120)));

            for (int i = 0; i < WCGenetics.Values.MAX_EYE_VARIANTS; i++) {
                rightEyeVariantList.addOption("Color " + (i + 1), i);
            }
        }

        // SECTION BUTTONS
        {
            int buttonHeight = centerY - 80;


            int xOffset1 = -35;

            baseSection = new GradientToggleButton(
                    centerX - 135 + xOffset1, buttonHeight, 60, 16,
                    Component.literal("Base"),
                    btn -> {
                        selectMainSection(baseSection);
                        mainSectionActiveMenu = "base";
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

            chimeraSection = new GradientToggleButton(
                    centerX + 145 + xOffset1, buttonHeight, 60, 16,
                    Component.literal("Chimerism"),
                    btn -> {
                        selectMainSection(chimeraSection);
                        mainSectionActiveMenu = "chimera";

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            buttonHeight += 20;

            int xOffset = -35;

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


            eyesAndAnomalySection = new GradientToggleButton(
                    centerX + 5 + xOffset, buttonHeight, 60, 16,
                    Component.literal("Eyes"),
                    btn -> {
                        selectMainSection(eyesAndAnomalySection);
                        mainSectionActiveMenu = "eyes";
                        this.addRenderableWidget(leftEyeVariantList);
                        if (heterochromiaSwitch.getValue()) this.addRenderableWidget(rightEyeVariantList);

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            furBobtailNoiseSection = new GradientToggleButton(
                    centerX + 75 + xOffset, buttonHeight, 60, 16,
                    Component.literal("Details"),
                    btn -> {
                        selectMainSection(furBobtailNoiseSection);
                        mainSectionActiveMenu = "details";
                        this.removeWidget(noiseList);
                        this.addRenderableWidget(noiseList);
                        this.removeWidget(sizeSlider);
                        this.addRenderableWidget(sizeSlider);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            extraSection = new GradientToggleButton(
                    centerX + 145 + xOffset, buttonHeight, 60, 16,
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
                        genetics.base = setBlack;
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setChocolateButton = new GradientToggleButton(
                    35, centerY + 10, 80, 16,
                    Component.literal("Chocolate"),
                    btn -> {
                        selectBase(setChocolateButton);
                        genetics.base = setChocolate;
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setCinnamonButton = new GradientToggleButton(
                    35, centerY + 35, 80, 16,
                    Component.literal("Cinnamon"),
                    btn -> {
                        selectBase(setCinnamonButton);
                        genetics.base = setCinnamon;
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );
        }

        // ORANGE BUTTONS
        {
            setOrangeButton = new GradientToggleButton(
                    35, centerY - 15, 80, 16,
                    Component.literal("Orange"),
                    btn -> {
                        selectOrange(setOrangeButton);
                        genetics.orangeBase = setOrange;
                        this.removeWidget(orangeVariantList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setTortieButton = new GradientToggleButton(
                    35, centerY + 10, 80, 16,
                    Component.literal("Tortie"),
                    btn -> {
                        selectOrange(setTortieButton);
                        genetics.orangeBase = setTortie;
                        this.addRenderableWidget(orangeVariantList);

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setNotOrangeButton = new GradientToggleButton(
                    35, centerY + 35, 80, 16,
                    Component.literal("No orange"),
                    btn -> {
                        selectOrange(setNotOrangeButton);
                        genetics.orangeBase = setNotOrange;
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
                        genetics.whiteRatio = setFullWhite;
                        this.removeWidget(whiteVariantList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setHighWhiteButton = new GradientToggleButton(
                    35, centerY + 10, 80, 16,
                    Component.literal("High spots"),
                    btn -> {
                        selectWhite(setHighWhiteButton);
                        genetics.whiteRatio = setHighWhite;
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
                        genetics.whiteRatio = setLowWhite;
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
                        genetics.whiteRatio = setNotWhite;
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
                        genetics.albino = setNotAlbino;
                        this.removeWidget(albinoVariantList);
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setTrueAlbinoButton = new GradientToggleButton(
                    35, centerY + 10, 80, 16,
                    Component.literal("True albino"),
                    btn -> {
                        selectAlbino(setTrueAlbinoButton);
                        genetics.albino = setTrueAlbino;
                        this.removeWidget(albinoVariantList);

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setMinkButton = new GradientToggleButton(
                    35, centerY + 35, 80, 16,
                    Component.literal("Mink"),
                    btn -> {
                        selectAlbino(setMinkButton);
                        genetics.albino = setMink;
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
                        genetics.albino = setSepia;
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
                        genetics.albino = setSiamese;
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
                            genetics.dilute = setDilute;
                        } else {
                            genetics.dilute = setNonDilute;
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
                            genetics.agouti = setAgoutiTabby;
                        } else {
                            genetics.agouti = setNonAgoutiTabby;
                        }
                    }, 0xFFFFFFFF
            );

            setMackerelButton = new GradientToggleButton(
                    35, centerY + 15, 80, 16,
                    Component.literal("Mackerel"),
                    btn -> {
                        selectAgoutiPattern(setMackerelButton);
                        genetics.tabbyStripes = setTabbyStripesMackerel;
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
                        genetics.tabbyStripes = setTabbyStripesClassic;
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

        // EYES
        {
            heterochromiaSwitch = new GradientSwitchButton(
                    35, centerY - 15, 80, 16,
                    "Heterochromia",
                    false,
                    btn -> {
                        if (heterochromiaSwitch.getValue()) {
                            genetics.eyesAnomaly = setHeteroChromia;
                            this.addRenderableWidget(rightEyeVariantList);
                        } else {
                            genetics.eyesAnomaly = setNonHeteroChromia;
                            this.removeWidget(rightEyeVariantList);
                        }
                    }, 0xFFFFFFFF
            );

            setLeftEyeBlueButton = new GradientToggleButton(
                    30, centerY + 15, 40, 16,
                    Component.literal("Blue"),
                    btn -> {
                        selectLeftEye(setLeftEyeBlueButton);
                        eyeColorRight = setEyeBlue;

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setLeftEyeGreenButton = new GradientToggleButton(
                    30, centerY + 35, 40, 16,
                    Component.literal("Green"),
                    btn -> {
                        selectLeftEye(setLeftEyeGreenButton);
                        eyeColorRight = setEyeGreen;

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );
            setLeftEyeYellowButton = new GradientToggleButton(
                    30, centerY + 55, 40, 16,
                    Component.literal("Yellow"),
                    btn -> {
                        selectLeftEye(setLeftEyeYellowButton);
                        eyeColorRight = setEyeYellow;

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );
            setLeftEyeRedButton = new GradientToggleButton(
                    30, centerY + 75, 40, 16,
                    Component.literal("Red"),
                    btn -> {
                        selectLeftEye(setLeftEyeRedButton);
                        eyeColorRight = setEyeRed;

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );


            setRightEyeBlueButton = new GradientToggleButton(
                    80, centerY + 15, 40, 16,
                    Component.literal("Blue"),
                    btn -> {
                        selectRightEye(setRightEyeBlueButton);
                        eyeColorLeft = setEyeBlue;

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );

            setRightEyeGreenButton = new GradientToggleButton(
                    80, centerY + 35, 40, 16,
                    Component.literal("Green"),
                    btn -> {
                        selectRightEye(setRightEyeGreenButton);
                        eyeColorLeft = setEyeGreen;

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );
            setRightEyeYellowButton = new GradientToggleButton(
                    80, centerY + 55, 40, 16,
                    Component.literal("Yellow"),
                    btn -> {
                        selectRightEye(setRightEyeYellowButton);
                        eyeColorLeft = setEyeYellow;

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );
            setRightEyeRedButton = new GradientToggleButton(
                    80, centerY + 75, 40, 16,
                    Component.literal("Red"),
                    btn -> {
                        selectRightEye(setRightEyeRedButton);
                        eyeColorLeft = setEyeRed;

                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20, 1f, 0xFFFFFFFF
            );


        }

        // DETAILS
        {
            bobtailSwitch = new GradientSwitchButton(
                    35, centerY - 15, 80, 16,
                    "Bobtail",
                    false,
                    btn -> {
                        if (bobtailSwitch.getValue()) {
                            genetics.bobtail = setBobtail;
                        } else {
                            genetics.bobtail = setFulltail;
                        }
                    }, 0xFFFFFFFF
            );

            chestFurSwitch = new GradientSwitchButton(
                    15, centerY + 15, 55, 16,
                    "Chest fur",
                    false,
                    btn -> {
                        if (chestFurSwitch.getValue()) {
                            genetics.chestFur = setLongFur;
                        } else {
                            genetics.chestFur = setShortFur;
                        }
                    }, 0xFFFFFFFF
            );

            bellyFurSwitch = new GradientSwitchButton(
                    15, centerY + 40, 55, 16,
                    "Belly fur", false,
                    btn -> {
                        if (bellyFurSwitch.getValue()) {
                            genetics.bellyFur = setLongFur;
                        } else {
                            genetics.bellyFur = setShortFur;
                        }
                    }, 0xFFFFFFFF
            );
            legsFurSwitch = new GradientSwitchButton(
                    15, centerY + 65, 55, 16,
                    "Legs fur", false,
                    btn -> {
                        if (legsFurSwitch.getValue()) {
                            genetics.legsFur = setLongFur;
                        } else {
                            genetics.legsFur = setShortFur;
                        }
                    }, 0xFFFFFFFF
            );


            headFurSwitch = new GradientSwitchButton(
                    80, centerY + 15, 55, 16,
                    "Head fur", false,
                    btn -> {
                        if (headFurSwitch.getValue()) {
                            genetics.headFur = setLongFur;
                        } else {
                            genetics.headFur = setShortFur;
                        }
                    }, 0xFFFFFFFF
            );

            cheekFurSwitch = new GradientSwitchButton(
                    80, centerY + 40, 55, 16,
                    "Face fur", false,
                    btn -> {
                        if (cheekFurSwitch.getValue()) {
                            genetics.cheekFur = setLongFur;
                        } else {
                            genetics.cheekFur = setShortFur;
                        }
                    }, 0xFFFFFFFF
            );
            backFurSwitch = new GradientSwitchButton(
                    80, centerY + 65, 55, 16,
                    "Neck fur", false,
                    btn -> {
                        if (backFurSwitch.getValue()) {
                            genetics.backFur = setLongFur;
                        } else {
                            genetics.backFur = setShortFur;
                        }
                    }, 0xFFFFFFFF
            );

            tailFurSwitch = new GradientSwitchButton(
                    35, centerY + 90, 80, 16,
                    "Tail fur",
                    false,
                    btn -> {
                        if (tailFurSwitch.getValue()) {
                            genetics.tailFur = setLongFur;
                        } else {
                            genetics.tailFur = setShortFur;
                        }
                    }, 0xFFFFFFFF
            );

            sizeSlider = new FloatSliderButton(centerX + 110, centerY - 35,
                    100, 20,
                    0.6f, 1.2f, 0.9f);

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
            this.noiseList.setLeftPos(((centerX + 120)));

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

        // CHIMERISM
        {
            editChimera = new GradientToggleButton(
                    40, centerY +15,
                    70, 20,
                    Component.literal("Edit Chimera"),
                    btn -> {
                        closing = true;
                        animationTime = 0f;
                        goingToChimeraMenu = true;
                    }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    60, 20, 1f, 0xFFFFFFFF
            );

            chimeraSwitch = new GradientSwitchButton(
                    35, centerY - 15, 80, 16,
                    "Chimera",
                    false,
                    btn -> {
                        if (chimeraSwitch.getValue()) {
                            chimeraGene = setChimera;
                        } else {
                            chimeraGene = setNotChimera;
                        }
                    }, 0xFFFFFFFF
            );
        }

        this.addRenderableWidget(bobtailSwitch);
        this.addRenderableWidget(chestFurSwitch);
        this.addRenderableWidget(legsFurSwitch);
        this.addRenderableWidget(headFurSwitch);
        this.addRenderableWidget(bellyFurSwitch);
        this.addRenderableWidget(backFurSwitch);
        this.addRenderableWidget(tailFurSwitch);
        this.addRenderableWidget(cheekFurSwitch);


        this.addRenderableWidget(heterochromiaSwitch);
        this.addRenderableWidget(setLeftEyeYellowButton);
        this.addRenderableWidget(setLeftEyeBlueButton);
        this.addRenderableWidget(setLeftEyeGreenButton);
        this.addRenderableWidget(setLeftEyeRedButton);
        this.addRenderableWidget(setRightEyeYellowButton);
        this.addRenderableWidget(setRightEyeGreenButton);
        this.addRenderableWidget(setRightEyeBlueButton);
        this.addRenderableWidget(setRightEyeRedButton);

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

        this.addRenderableWidget(chimeraSwitch);
        this.addRenderableWidget(editChimera);

        this.addRenderableWidget(baseSection);
        this.addRenderableWidget(orangeSection);
        this.addRenderableWidget(whiteSection);
        this.addRenderableWidget(albinoSection);
        this.addRenderableWidget(chimeraSection);
        this.addRenderableWidget(diluteSection);
        this.addRenderableWidget(agoutiAndTabbySection);
        this.addRenderableWidget(eyesAndAnomalySection);
        this.addRenderableWidget(furBobtailNoiseSection);
        this.addRenderableWidget(extraSection);

        this.addRenderableWidget(setOnGeneticalSkinButton);
        this.addRenderableWidget(setOnPresetSkinButton);
        this.addRenderableWidget(saveButton);

        menuX = 500;
        if (comingBackFromChimeraMenu) menuX = -500;

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
                saveAndNext();
            }
        } else {
            if (comingBackFromChimeraMenu) {
                if (menuX < 0) {
                    menuX -= (menuX) * 0.03f;
                    if (menuX > 0) menuX = 0;
                }
            } else {
                if (menuX > 0) {
                    menuX -= (menuX) * 0.03f;
                    if (menuX < 0) menuX = 0;
                }
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



        baseSection.visible = false;
        orangeSection.visible = false;
        whiteSection.visible = false;
        albinoSection.visible = false;
        diluteSection.visible = false;
        eyesAndAnomalySection.visible = false;
        furBobtailNoiseSection.visible = false;
        extraSection.visible = false;
        chimeraSection.visible = false;
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
        heterochromiaSwitch.visible = false;
        setLeftEyeYellowButton.visible = false;
        setRightEyeYellowButton.visible = false;
        setLeftEyeGreenButton.visible = false;
        setRightEyeGreenButton.visible = false;
        setLeftEyeBlueButton.visible = false;
        setRightEyeBlueButton.visible = false;
        setLeftEyeRedButton.visible = false;
        setRightEyeRedButton.visible = false;
        bobtailSwitch.visible = false;
        chestFurSwitch.visible = false;
        bellyFurSwitch.visible = false;
        cheekFurSwitch.visible = false;
        legsFurSwitch.visible = false;
        headFurSwitch.visible = false;
        backFurSwitch.visible = false;
        tailFurSwitch.visible = false;
        editChimera.visible = false;
        chimeraSwitch.visible = false;
        if (onGeneticalSkin) {
            baseSection.visible = true;
            orangeSection.visible = true;
            whiteSection.visible = true;
            albinoSection.visible = true;
            diluteSection.visible = true;
            eyesAndAnomalySection.visible = true;
            furBobtailNoiseSection.visible = true;
            extraSection.visible = true;
            chimeraSection.visible = true;
            agoutiAndTabbySection.visible = true;

            if (mainSectionActiveMenu.equals("base")) {
                setBlackButton.visible = true;
                setChocolateButton.visible = true;
                setCinnamonButton.visible = true;

                pGuiGraphics.drawCenteredString(font, "Black base", 76, centerY - 30, 0xFFFFFFFF);
            }
            if (mainSectionActiveMenu.equals("orange")) {
                setOrangeButton.visible = true;
                setTortieButton.visible = true;
                setNotOrangeButton.visible = true;
                orangeVariantList.setVisible(true);

                if (orangeVariantList.getSelectedEntry() != null) {
                    orangeBaseVariant = orangeVariantList.getSelectedEntry().getId();
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
                    whiteRatioVariant = whiteVariantList.getSelectedEntry().getId();
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
                    albinoVariant = albinoVariantList.getSelectedEntry().getId();
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
                    tabbyStripesVariant = tabbyVariantList.getSelectedEntry().getId();
                }

                pGuiGraphics.drawCenteredString(font, "Agouti", 76, centerY - 30, 0xFFFFFFFF);
            }

            if (mainSectionActiveMenu.equals("eyes")) {
                heterochromiaSwitch.visible = true;
                setLeftEyeYellowButton.visible = true;
                setLeftEyeGreenButton.visible = true;
                setLeftEyeBlueButton.visible = true;
                setLeftEyeRedButton.visible = true;

                setRightEyeYellowButton.visible = true;
                setRightEyeGreenButton.visible = true;
                setRightEyeBlueButton.visible = true;
                setRightEyeRedButton.visible = true;


                if (heterochromiaSwitch.getValue()) {
                    setRightEyeYellowButton.active = true;
                    setRightEyeGreenButton.active = true;
                    setRightEyeBlueButton.active = true;
                } else {
                    setRightEyeYellowButton.active = false;
                    setRightEyeGreenButton.active = false;
                    setRightEyeBlueButton.active = false;

                    eyeColorLeft = eyeColorRight;
                }

                if (leftEyeVariantList.getSelectedEntry() != null) {
                    eyeColorVariantRight = leftEyeVariantList.getSelectedEntry().getId();
                    if (!heterochromiaSwitch.getValue()) eyeColorVariantLeft = leftEyeVariantList.getSelectedEntry().getId();
                    if (heterochromiaSwitch.getValue() && rightEyeVariantList.getSelectedEntry() != null) {
                        eyeColorVariantLeft = rightEyeVariantList.getSelectedEntry().getId();
                    }
                }

                pGuiGraphics.drawCenteredString(font, "Eyes", 76, centerY - 30, 0xFFFFFFFF);
            }

            if (mainSectionActiveMenu.equals("details")) {
                bobtailSwitch.visible = true;
                chestFurSwitch.visible = true;
                bellyFurSwitch.visible = true;
                legsFurSwitch.visible = true;
                headFurSwitch.visible = true;
                backFurSwitch.visible = true;
                cheekFurSwitch.visible = true;
                tailFurSwitch.visible = true;

                if (noiseList.getSelectedEntry() != null) {
                    genetics.noise = noiseList.getSelectedEntry().getId();
                    noise = noiseList.getSelectedEntry().getId();
                }

                size = sizeSlider.getActualValue();

                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().translate(centerX + 160, centerY - 42, 0);
                pGuiGraphics.pose().scale(0.7f,0.7f,0.7f);
                pGuiGraphics.drawCenteredString(font, Component.literal("¡The usual is 0.9!").withStyle(ChatFormatting.ITALIC),
                        0, 0, 0x44FFFFFF);
                pGuiGraphics.pose().popPose();

                pGuiGraphics.drawCenteredString(font, "Details", 76, centerY - 30, 0xFFFFFFFF);

            }

            if (mainSectionActiveMenu.equals("extra")) {

                rufousingVariant = rufousingSlider.getActualValue();
                blueRufousingVariant = blueRufousingSlider.getActualValue();

                pGuiGraphics.drawCenteredString(font, "Extra", 76, centerY - 30, 0xFFFFFFFF);
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().translate(76, centerY - 3, 0);
                pGuiGraphics.pose().scale(0.8f,0.8f,0.7f);
                pGuiGraphics.drawCenteredString(font, "Rufousing", 0, 0, 0xFFFFFFFF);
                pGuiGraphics.drawCenteredString(font, "Blue tint", 0, 50, 0xFFFFFFFF);
                pGuiGraphics.pose().popPose();

            }

            if (mainSectionActiveMenu.equals("chimera")) {
                if (chimeraSwitch.getValue()) {
                    editChimera.visible = true;
                }
                chimeraSwitch.visible = true;

                pGuiGraphics.drawCenteredString(font, "Chimerism", 76, centerY - 30, 0xFFFFFFFF);
            }

        } else {


//            chestFurSwitch.visible = true;
//            tailFurSwitch.visible = true;
//            headFurSwitch.visible = true;
//            legsFurSwitch.visible = true;
//            cheekFurSwitch.visible = true;
//            bellyFurSwitch.visible = true;
//            backFurSwitch.visible = true;
//            bobtailSwitch.visible = true;
        }

//        if (!onGeneticalSkin) centerX += -15;
        if (!onGeneticalSkin) centerX += -50;

        WCatEntity entityToRender = new WCatEntity(ModEntities.WCAT.get(), Minecraft.getInstance().level);

        entityToRender.setOnGeneticalSkin(onGeneticalSkin);
        entityToRender.setGenetics(genetics);
        entityToRender.setGender(1);
        entityToRender.setGeneticalVariants(eyeColorLeft, eyeColorRight, rufousingVariant, blueRufousingVariant,
                orangeBaseVariant, whiteRatioVariant, tabbyStripesVariant, albinoVariant, eyeColorVariantLeft,
                eyeColorVariantRight, noise, size);

        geneticsChimera.chimeraGene = chimeraGene;
        entityToRender.setChimeraGenetics(geneticsChimera);
        entityToRender.setGeneticalVariantsChimera(variantsChimera.chimeraVariant, variantsChimera.rufousingVariant,
                variantsChimera.blueRufousingVariant, variantsChimera.orangeVar, variantsChimera.whiteVar, variantsChimera.tabbyVar,
                variantsChimera.albinoVar, variantsChimera.noise);

        entityToRender.setOnGround(true);
        if (variantScrollList.getSelectedEntry() != null) {
            entityToRender.setVariant(variantScrollList.getSelectedEntry().getId());
        }
        entityToRender.setYRot(0);
        entityToRender.yHeadRot = 0;
        entityToRender.yBodyRot = 0;


        pGuiGraphics.renderOutline(centerX - 90, centerY - 40, 180, 157, 0x44FFFFFF);
        pGuiGraphics.fill(centerX - 90, centerY - 40, centerX + 90, centerY + 115, 0x07FFFFFF);

        pGuiGraphics.pose().pushPose();

        pGuiGraphics.pose().translate(centerX, centerY + 90, 0);

        float scale = 3.0f;
        if (this.variantScrollList.getSelectedEntry() != null && !onGeneticalSkin) {
            scale = 2.0f;
        } else if (this.variantScrollList.getSelectedEntry() != null && onGeneticalSkin) {
            variantScrollList.setSelected(null);
        }

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

    private void selectMode(GradientToggleButton button) {
        setOnPresetSkinButton.setSelected(false);
        setOnGeneticalSkinButton.setSelected(false);

        button.setSelected(true);
    }

    private void selectMainSection(GradientToggleButton button) {
        baseSection.setSelected(false);
        orangeSection.setSelected(false);
        whiteSection.setSelected(false);
        albinoSection.setSelected(false);
        diluteSection.setSelected(false);
        agoutiAndTabbySection.setSelected(false);
        eyesAndAnomalySection.setSelected(false);
        furBobtailNoiseSection.setSelected(false);
        extraSection.setSelected(false);
        chimeraSection.setSelected(false);

        this.removeWidget(orangeVariantList);
        this.removeWidget(whiteVariantList);
        this.removeWidget(albinoVariantList);
        this.removeWidget(tabbyVariantList);
        this.removeWidget(leftEyeVariantList);
        this.removeWidget(rightEyeVariantList);
        this.removeWidget(noiseList);
        this.removeWidget(sizeSlider);
        this.removeWidget(rufousingSlider);
        this.removeWidget(blueRufousingSlider);

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

    private void selectLeftEye(GradientToggleButton button) {
        setLeftEyeYellowButton.setSelected(false);
        setLeftEyeBlueButton.setSelected(false);
        setLeftEyeGreenButton.setSelected(false);
        setLeftEyeRedButton.setSelected(false);

        button.setSelected(true);
    }

    private void selectRightEye(GradientToggleButton button) {
        setRightEyeYellowButton.setSelected(false);
        setRightEyeBlueButton.setSelected(false);
        setRightEyeGreenButton.setSelected(false);
        setRightEyeRedButton.setSelected(false);

        button.setSelected(true);
    }

    private void saveAndNext() {



        WCGenetics.GeneticalVariants variants =
                new WCGenetics.GeneticalVariants(eyeColorLeft, eyeColorRight,
                        rufousingVariant, blueRufousingVariant, orangeBaseVariant, whiteRatioVariant,
                        tabbyStripesVariant, albinoVariant, eyeColorVariantLeft, eyeColorVariantRight, noise, size);

        int defaultVariant = 0;
        if (variantScrollList.getSelectedEntry() != null) defaultVariant = variantScrollList.getSelectedEntry().getId();

        geneticsChimera.chimeraGene = chimeraGene;
        genetics.chimeraGene = chimeraGene;

        if (goingToChimeraMenu) {
            this.minecraft.setScreen(new CreateChimeraMorphGeneticsScreen(onGeneticalSkin, genetics, variants, geneticsChimera, variantsChimera));
            return;
        }



        ModPackets.sendToServer(new SavePlayerGeneticsPacket(onGeneticalSkin, genetics, variants, geneticsChimera, variantsChimera, defaultVariant));

        this.minecraft.setScreen(null);
        if (!ClientClanData.get().isFirstLoginHandled()){
            this.minecraft.setScreen(new SpawnLocationScreen());
        }
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
