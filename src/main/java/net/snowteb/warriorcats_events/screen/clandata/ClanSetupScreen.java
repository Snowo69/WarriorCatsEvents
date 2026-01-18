package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.client.ClientClanData;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.SaveClanDataPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClanSetupScreen extends Screen {
    private int textCooldown = 0;

    private EditBox clanNameBox;
    private EditBox morphPrefixBox;
    private EditBox morphSufixBox;

    private ToggleButton ageKit;
    private ToggleButton ageApprentice;
    private ToggleButton ageAdult;

    private ToggleButton genderMale;
    private ToggleButton genderFemale;

    private Button saveButton;

    private Button randomizeButton;

    private VariantScrollList variantScrollList;

    private SwitchButton automaticSufix;
    private  SwitchButton useSufixes;


    private static final String[] SUFIX = {
            "claw", "fur", "feather", "pelt", "eye",
            "heart", "tail", "wing", "whisker", "blaze",
            "fang", "shade", "step", "fall", "song",
            "stripe", "light", "leap", "foot", "spring",
            "pit", "stream", "patch"
    };

    private int randomSufix = Minecraft.getInstance().player.getRandom().nextInt(SUFIX.length);

    public ClanSetupScreen() {
        super(Component.literal("Clan Setup"));
    }


    private static final ResourceLocation[] VARIANTS = {
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var_empty.png"),

            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var1.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var2.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var3.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var4.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var5.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var6.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var7.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var8.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var9.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var10.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var11.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var12.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var13.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var14.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var15.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var16.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var17.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var18.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var19.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var20.png"),

            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var21.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var22.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var23.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var24.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var25.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var26.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var27.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var28.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var29.png"),
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/var30.png")
    };

    private static final ResourceLocation BANNER =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/banner.png");

    private static final ResourceLocation BG_TEXTURE =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/background_scene.png");


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        pGuiGraphics.blit(BG_TEXTURE, 0, 0, 0, 0, this.width, this.height, this.width, this.height);


        int centerx = (this.width) / 2;
        int centery = (this.height) / 2;

        boolean prefixToolTip = pMouseX >= centerx - 220 && pMouseY >= centery - 40
                && pMouseX <= centerx - 130 && pMouseY <= centery - 20;
        boolean suffixToolTip =  pMouseX >= centerx - 120 && pMouseY >= centery - 40
                && pMouseX <= centerx - 60 && pMouseY <= centery - 20;
        boolean useSuffixToolTip =  pMouseX >= centerx + 30 && pMouseY >= centery - 40
                && pMouseX <= centerx + 80 && pMouseY <= centery - 20;
        boolean clanNameTooltip = pMouseX >= centerx - 220 && pMouseY >= centery - 10
                && pMouseX <= centerx - 120 && pMouseY <= centery + 10;
        boolean ageTooltip = pMouseX >= centerx - 230 && pMouseY >= centery + 20
                && pMouseX <= centerx - 20 && pMouseY <= centery + 40;

        if (prefixToolTip) {
            if (useSufixes.getValue()) {
                pGuiGraphics.renderTooltip(Minecraft.getInstance().font,
                        Component.empty()
                                .append(Component.literal("The prefix of your morph. eg: ").withStyle(ChatFormatting.GRAY))
                                .append(Component.literal("'Bengal'").withStyle(ChatFormatting.YELLOW))
                                .append(Component.literal("pelt").withStyle(ChatFormatting.GRAY))
                        ,pMouseX, pMouseY);
            } else {
                pGuiGraphics.renderTooltip(Minecraft.getInstance().font,
                        Component.empty()
                                .append(Component.literal("The name of your morph. eg: ").withStyle(ChatFormatting.GRAY))
                                .append(Component.literal("'Daisy'").withStyle(ChatFormatting.YELLOW))

                        ,pMouseX, pMouseY);
            }
        }

        if (suffixToolTip && useSufixes.getValue()) pGuiGraphics.renderTooltip(Minecraft.getInstance().font,
                Component.empty()
                        .append(Component.literal("The suffix of your morph. eg: Bengal").withStyle(ChatFormatting.GRAY))
                        .append(Component.literal("'pelt'").withStyle(ChatFormatting.YELLOW))
                ,pMouseX, pMouseY);

        if (useSuffixToolTip) {
            List<Component> tooltip = new ArrayList<>();

            tooltip.add(Component.literal("Controls whether your morph uses")
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("<prefix> + <suffix> warrior names")
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("or a single-word name.")
                    .withStyle(ChatFormatting.GRAY));

            pGuiGraphics.renderTooltip(
                    Minecraft.getInstance().font,
                    tooltip,
                    Optional.empty(),
                    pMouseX,
                    pMouseY
            );
        }

        if (clanNameTooltip) pGuiGraphics.renderTooltip(Minecraft.getInstance().font,
                Component.literal("The name of your clan. eg: 'Bengalclan'")
                        .withStyle(ChatFormatting.GRAY)
                ,pMouseX, pMouseY);

        if (ageTooltip) pGuiGraphics.renderTooltip(Minecraft.getInstance().font,
                Component.literal("The age your character begins with.")
                        .withStyle(ChatFormatting.GRAY)
                ,pMouseX, pMouseY);


        String morphPrefix = morphPrefixBox.getValue().trim();
        String morphSufix = morphSufixBox.getValue().trim();
        String clanName = clanNameBox.getValue().trim();
        Boolean isUseSufixes = useSufixes.getValue();

        String sufix = "";
        if (isUseSufixes) {
            if (ageKit.isSelected()) {
                sufix = "kit";
            } else if (ageApprentice.isSelected()) {
                sufix = "paw";
            } else {
                if (automaticSufix.getValue()) {
                    sufix = SUFIX[randomSufix];
                } else {
                    sufix = morphSufixBox.getValue().trim();
                }
            }
        }
        String clanNameShow = "...";
        String morphNameShow = "...";
        String genderS = "";
        if (genderFemale.isSelected()) {
            genderS = " ♀";
        } else if (genderMale.isSelected()) {
            genderS = " ♂";
        }

        if (!morphPrefix.isEmpty()) {
            morphNameShow = morphPrefix + sufix + genderS;
        }
        if (!clanNameBox.getValue().isEmpty()) {
            clanNameShow = clanNameBox.getValue();
        }

        pGuiGraphics.drawString(Minecraft.getInstance().font,
                "''I am " + morphNameShow + " from " +  clanNameShow + "!''",
                centerx-217, centery - 54, 0xFFFFFFFF);


        if (clanName.isEmpty()) {
            pGuiGraphics.drawString(Minecraft.getInstance().font, "<Warriorclan>", centerx-217, centery - 4, 0xFF7d7d7d);
        }
        if (morphPrefix.isEmpty()) {
            if (isUseSufixes){
                pGuiGraphics.drawString(Minecraft.getInstance().font, "<Prefix>", centerx-217, centery -34, 0xFF7d7d7d);
            } else {
                pGuiGraphics.drawString(Minecraft.getInstance().font, "<Name>", centerx-217, centery -34, 0xFF7d7d7d);
            }
        }
        if (morphSufix.isEmpty() && isUseSufixes) {
            pGuiGraphics.drawString(Minecraft.getInstance().font, "<Suffix>", centerx-117, centery -34, 0xFF7d7d7d);
        }

        ResourceLocation currentVariant;

        VariantScrollList.VariantEntry selected = variantScrollList.getSelectedEntry();

        if (selected != null) {
            int variant = selected.getId() + 1;
            currentVariant = VARIANTS[variant];

        } else {
            currentVariant = VARIANTS[0];
        }

        if (textCooldown > 0) {
            pGuiGraphics.drawString(Minecraft.getInstance().font, "Some fields are empty",
                    centerx - 55, centery + 75, 0xFFFF0000);
        }

        boolean autoSufix = automaticSufix.getValue();
        String sufixText = SUFIX[randomSufix];

        if (autoSufix) {
            morphSufixBox.setValue(sufixText);
        }


        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.enableScissor(this.width - 148, centery - 120, this.width - 20, centery + 8);
        pGuiGraphics.blit(currentVariant, this.width - 148, centery - 120,
                0, 0,
                256, 256,
                128, 128
        );
        pGuiGraphics.disableScissor();


        int xPosition = -230;
        int yPosition = -120;

        pGuiGraphics.enableScissor(centerx + xPosition, centery + yPosition, centerx + xPosition + 200, centery + yPosition + 56);
        pGuiGraphics.blit(
                BANNER,
                centerx - 230, centery - 120,
                0, 0,
                800, 225,
                200, 56
        );
        pGuiGraphics.disableScissor();

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

    @Override
    public void tick() {

        if (textCooldown > 0) {
            textCooldown--;
        }

        super.tick();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int listWidth = 140;
        int listHeight = 40;
        int top = centerY + 20;
        int bottom = centerY + 100;

        this.variantScrollList = new VariantScrollList(
                Minecraft.getInstance(),
                listWidth,
                listHeight,
                top,
                bottom,
                20
        );
        this.variantScrollList.setRenderTopAndBottom(false);
        this.variantScrollList.setLeftPos(((this.width - listWidth - 20)));

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

        this.addRenderableWidget(this.variantScrollList);


        morphPrefixBox = new EditBox(
                this.font,
                centerX - 220, centerY - 40,
                90, 20,
                Component.literal("Prefix")
        );
        morphPrefixBox.setMaxLength(13);
        this.addRenderableWidget(morphPrefixBox);

        morphSufixBox = new EditBox(
                this.font,
                centerX - 120, centerY - 40,
                60, 20,
                Component.literal("Suffix")
        );
        morphSufixBox.setMaxLength(12);
        this.addRenderableWidget(morphSufixBox);

        automaticSufix = new SwitchButton(
                centerX - 50, centerY - 40,
                50, 20,
                "Random Suffix",
                false,
                btn -> {
                    randomSufix = Minecraft.getInstance().player.getRandom().nextInt(SUFIX.length);
                    if (automaticSufix.getValue()) useSufixes.setValue(true);
                }
        );

        randomizeButton = Button.builder(
                Component.literal("@"),
                btn -> randomSufix = Minecraft.getInstance().player.getRandom().nextInt(SUFIX.length)
        ).bounds(centerX + 5, centerY - 40, 20, 20).build();
        this.addRenderableWidget(randomizeButton);

        useSufixes = new SwitchButton(
                centerX + 30, centerY - 40,
                50, 20,
                "Use suffixes",
                true,
                btn -> {
                }
        );
        this.addRenderableWidget(useSufixes);

        clanNameBox = new EditBox(
                this.font,
                centerX - 220, centerY - 10,
                100, 20,
                Component.literal("Clan Name")
        );
        clanNameBox.setMaxLength(15);
        this.addRenderableWidget(clanNameBox);

        ageKit = new ToggleButton(
                centerX - 230, centerY + 20, 60, 20,
                "Kit",
                btn -> selectAge(ageKit)
        );

        ageApprentice = new ToggleButton(
                centerX - 165, centerY + 20, 80, 20,
                "Apprentice",
                btn -> selectAge(ageApprentice)
        );

        ageAdult = new ToggleButton(
                centerX - 80, centerY + 20, 60, 20,
                "Adult",
                btn -> selectAge(ageAdult)
        );

        genderMale = new ToggleButton(
                centerX - 195, centerY + 45, 60, 20,
                "Tom-cat",
                btn -> selectGender(genderMale)
        );
        genderFemale = new ToggleButton(
                centerX - 115, centerY + 45, 60, 20,
                "She-cat",
                btn -> selectGender(genderFemale)
        );



        this.addRenderableWidget(automaticSufix);

        this.addRenderableWidget(genderMale);
        this.addRenderableWidget(genderFemale);

        this.addRenderableWidget(ageKit);
        this.addRenderableWidget(ageApprentice);
        this.addRenderableWidget(ageAdult);


        saveButton = Button.builder(
                Component.literal("Next"),
                btn -> onSave()
        ).bounds(centerX-40, centerY + 85, 80, 20).build();

        this.addRenderableWidget(saveButton);
    }

    private void selectAge(ToggleButton selected) {
        ageKit.setSelected(false);
        ageApprentice.setSelected(false);
        ageAdult.setSelected(false);

        selected.setSelected(true);
    }

    private void selectGender(ToggleButton selected) {
        genderFemale.setSelected(false);
        genderMale.setSelected(false);

        selected.setSelected(true);
    }

    private void onSave() {
        String clanName = clanNameBox.getValue().trim();
        String morphPrefix = morphPrefixBox.getValue().trim();
        String morphSufix = morphSufixBox.getValue().trim();
        VariantScrollList.VariantEntry selectedVariant = variantScrollList.getSelectedEntry();
        boolean isGenderSelected = (genderMale.isSelected() || genderFemale.isSelected());
        boolean isAgeSelected = (ageKit.isSelected() || ageApprentice.isSelected() || ageAdult.isSelected());
        boolean autoSufix = automaticSufix.getValue();

        if (clanName.isEmpty() || morphPrefix.isEmpty()
                || selectedVariant == null || (useSufixes.getValue() && morphSufix.isEmpty())
                || !isGenderSelected || !isAgeSelected) {
            textCooldown = 100;
            return;
        }

        String sufix = "";
        if (useSufixes.getValue()) {
            if (ageKit.isSelected()) {
                sufix = "kit";
            } else if (ageApprentice.isSelected()) {
                sufix = "paw";
            } else {
                if (autoSufix) {
                    sufix = SUFIX[randomSufix];
                } else {
                    sufix = morphSufixBox.getValue().trim();
                }
            }
        }


        String Name = morphPrefixBox.getValue().trim() + sufix;


        PlayerClanData data = new PlayerClanData();

        data.setClanName(clanNameBox.getValue());
        data.setPrefix(morphPrefixBox.getValue());
        data.setSufix(morphSufixBox.getValue());
        data.setMorphName(Name);
        data.setUseSufixes(useSufixes.getValue());

        int variant = 0;
        VariantScrollList.VariantEntry selected = variantScrollList.getSelectedEntry();

        if (selected != null) {
            variant = selected.getId();
        }
        data.setVariantData(variant);


        if (genderFemale.isSelected()) {
            data.setGenderData(1);
        } else if (genderMale.isSelected()) {
            data.setGenderData(0);
        } else {
            data.setGenderData(1);
        }


        if (ageKit.isSelected()) {
            data.setMorphAge(PlayerClanData.Age.KIT);
        } else if (ageApprentice.isSelected()) {
            data.setMorphAge(PlayerClanData.Age.APPRENTICE);
        } else {
            data.setMorphAge(PlayerClanData.Age.ADULT);
        }


        this.minecraft.setScreen(null);
        if (!ClientClanData.get().isFirstLoginHandled()){
            this.minecraft.setScreen(new SpawnLocationScreen());
        }


        data.setFirstLoginHandled(true);
        ModPackets.sendToServer(new SaveClanDataPacket(data));

    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

}