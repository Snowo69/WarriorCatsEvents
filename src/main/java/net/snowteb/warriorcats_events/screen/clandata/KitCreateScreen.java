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
import net.snowteb.warriorcats_events.network.packet.zcatmodifiers.CtSCreateAndSpawnKitPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KitCreateScreen extends Screen {
    private int textCooldown = 0;

    private EditBox kitPrefixBox;

    private Button saveButton;

    private VariantScrollList variantScrollList;




    public KitCreateScreen() {
        super(Component.literal("Kit"));
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


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        this.renderBackground(pGuiGraphics);

        int centerx = (this.width) / 2;
        int centery = (this.height) / 2;

        boolean prefixToolTip = pMouseX >= centerx - 220 && pMouseY >= centery - 40
                && pMouseX <= centerx - 130 && pMouseY <= centery - 20;

        if (prefixToolTip) {
                pGuiGraphics.renderTooltip(Minecraft.getInstance().font,
                        Component.empty()
                                .append(Component.literal("The prefix of your kit. eg: ").withStyle(ChatFormatting.GRAY))
                                .append(Component.literal("'Bengal'").withStyle(ChatFormatting.YELLOW))
                                .append(Component.literal("kit").withStyle(ChatFormatting.GRAY))
                        ,pMouseX, pMouseY);
        }

        String kitPrefix = kitPrefixBox.getValue().trim();

        String morphNameShow = "...";

        if (!kitPrefix.isEmpty()) {
            morphNameShow = kitPrefix + "kit";
        }

        pGuiGraphics.renderOutline(centerx-71, centery - 106, 148, 38, 0xFFFFFFFF);


        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font,
                morphNameShow,
                centerx, centery + 10, 0xFFFFFFFF);


        if (kitPrefix.isEmpty()) {
                pGuiGraphics.drawString(Minecraft.getInstance().font, "<Prefix>", centerx-43, centery +34, 0xFF7d7d7d);
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


        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.enableScissor(centerx - 32, centery - 60, centerx + 32, centery +4);
        pGuiGraphics.blit(currentVariant, centerx - 32, centery - 60,
                0, 0,
                256, 256,
                64, 64
        );
        pGuiGraphics.disableScissor();


//        int xPosition = -230;
//        int yPosition = -120;

//        pGuiGraphics.enableScissor(centerx + xPosition, centery + yPosition, centerx + xPosition + 200, centery + yPosition + 56);
//        pGuiGraphics.blit(
//                BANNER,
//                centerx - 230, centery - 120,
//                0, 0,
//                800, 225,
//                200, 56
//        );
//        pGuiGraphics.disableScissor();

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
        int top = centerY - 105;
        int bottom = centerY - 70;

        this.variantScrollList = new VariantScrollList(
                Minecraft.getInstance(),
                listWidth,
                listHeight,
                top,
                bottom,
                20
        );
        this.variantScrollList.setRenderTopAndBottom(false);
        this.variantScrollList.setLeftPos(((centerX - (listWidth/2))));

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


        kitPrefixBox = new EditBox(
                this.font,
                centerX-45, centerY + 28,
                90, 20,
                Component.literal("Prefix")
        );
        kitPrefixBox.setMaxLength(13);
        this.addRenderableWidget(kitPrefixBox);

        saveButton = Button.builder(
                Component.literal("Generate"),
                btn -> onSave()
        ).bounds(centerX-40, centerY + 85, 80, 20).build();

        this.addRenderableWidget(saveButton);
    }

    private void onSave() {
        String kitPrefix = kitPrefixBox.getValue().trim();
        VariantScrollList.VariantEntry selectedVariant = variantScrollList.getSelectedEntry();

        if (kitPrefix.isEmpty() || selectedVariant == null) {
            textCooldown = 100;
            return;
        }

        String prefix = kitPrefixBox.getValue().trim();

        int variant = 0;
        VariantScrollList.VariantEntry selected = variantScrollList.getSelectedEntry();

        if (selected != null) {
            variant = selected.getId();
        }


        this.minecraft.setScreen(null);

        ModPackets.sendToServer(new CtSCreateAndSpawnKitPacket(prefix, variant));

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

}