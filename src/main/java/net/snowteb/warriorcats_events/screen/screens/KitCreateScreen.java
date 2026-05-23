package net.snowteb.warriorcats_events.screen.screens;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.cats.CtSNameKitPacket;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class KitCreateScreen extends Screen {
    private int textCooldown = 0;

    private EditBox kitPrefixBox;

    private Button saveButton;

//    private VariantScrollList variantScrollList;

    private final WCatEntity kitten;


    public KitCreateScreen(WCatEntity kitten) {
        super(Component.literal("Kit"));
        this.kitten = kitten;
    }

    private static final ResourceLocation BANNER =
            ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "textures/gui/clan_setup/banner.png");


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

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



        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font,
                morphNameShow,
                centerx, centery + 10, 0xFFFFFFFF);


        if (kitPrefix.isEmpty()) {
                pGuiGraphics.drawString(Minecraft.getInstance().font, "<Prefix>", centerx-43, centery +34, 0xFF7d7d7d);
        }

        if (textCooldown > 0) {
            pGuiGraphics.drawString(Minecraft.getInstance().font, "Some fields are empty",
                    centerx - 55, centery + 75, 0xFFFF0000);
        }


        for (Renderable renderable : this.renderables) {
            renderable.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }



        kitten.setOnGround(true);
        kitten.setYRot(0);
        kitten.yHeadRot = 0;
        kitten.yBodyRot = 0;

        pGuiGraphics.pose().pushPose();

        pGuiGraphics.pose().translate(centerx, centery - 10, 0);

        float scale = 3.4f;

        pGuiGraphics.pose().scale(scale, scale, scale);

        Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 0.0F);
        Quaternionf pose = new Quaternionf(0.8F, 0.0F, 0.3F, 0.0F);

        InventoryScreen.renderEntityInInventory(
                pGuiGraphics,
                0,
                0,
                48,
                new Vector3f(0,0,0),
                pose,
                rotation,
                kitten
        );

        pGuiGraphics.pose().popPose();


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

//        int listWidth = 140;
//        int listHeight = 40;
//        int top = centerY - 105;
//        int bottom = centerY - 50;
//
//        this.variantScrollList = new VariantScrollList(
//                Minecraft.getInstance(),
//                listWidth,
//                listHeight,
//                top,
//                bottom,
//                20
//        );
//        this.variantScrollList.setRenderTopAndBottom(false);
//        this.variantScrollList.setLeftPos(((centerX - (listWidth/2))));
//
//        variantScrollList.addOption("Calico", 0);
//        variantScrollList.addOption("Siamese", 1);
//        variantScrollList.addOption("Gray", 2);
//        variantScrollList.addOption("Abyssinian", 3);
//        variantScrollList.addOption("Black", 4);
//        variantScrollList.addOption("Maine Coon", 5);
//        variantScrollList.addOption("Russian Blue", 6);
//        variantScrollList.addOption("Dark Brown Tabby", 7);
//        variantScrollList.addOption("White", 8);
//        variantScrollList.addOption("Calico 2", 9);
//        variantScrollList.addOption("Munchkin", 10);
//        variantScrollList.addOption("Light Gray Tabby", 11);
//        variantScrollList.addOption("Chestnutpatch (Bookwom)", 12);
//        variantScrollList.addOption("Ratstar (Telefonjoker)", 13);
//        variantScrollList.addOption("Twitchstream (Cat)", 14);
//        variantScrollList.addOption("Blazepit (Cat)", 15);
//        variantScrollList.addOption("Bengalpelt (Klyonstar)", 16);
//        variantScrollList.addOption("Sparrowstar (Whale_shark)", 17);
//        variantScrollList.addOption("Foxeater (Sejr)", 18);
//        variantScrollList.addOption("Willowsong (Sejr)", 19);
//        variantScrollList.addOption("White 2", 20);
//        variantScrollList.addOption("Dalmatian", 21);
//        variantScrollList.addOption("Gray Tabby", 22);
//        variantScrollList.addOption("Brown", 23);
//        variantScrollList.addOption("Pale Ginger", 24);
//        variantScrollList.addOption("Black 2", 25);
//        variantScrollList.addOption("Bengal", 26);
//        variantScrollList.addOption("Snowshoe", 27);
//        variantScrollList.addOption("Toyger", 28);
//        variantScrollList.addOption("Turkish Van", 29);
//        variantScrollList.addOption("Albino (CoffeeCat)", 30);
//        variantScrollList.addOption("Bengal (CoffeeCat)", 31);
//        variantScrollList.addOption("Brindle Tortie (Mswolfy81)", 32);
//        variantScrollList.addOption("Cream Calico 1 (Lightley)", 33);
//        variantScrollList.addOption("Cream Calico 2 (Lightley)", 34);
//        variantScrollList.addOption("Cream Calico 3 (Lightley)", 35);
//        variantScrollList.addOption("Caramel (CoffeeCat)", 36);
//        variantScrollList.addOption("Frostdawn (whitenoisewife)", 37);
//        variantScrollList.addOption("Gray-white Tabby (Slay)", 38);
//        variantScrollList.addOption("Hailflake (pvppet, Mswolfy81)", 39);
//        variantScrollList.addOption("Karpati (whitenoisewife)", 40);
//        variantScrollList.addOption("Leafstar (whitenoisewife)", 41);
//        variantScrollList.addOption("Longtail (whitenoisewife)", 42);
//        variantScrollList.addOption("Mothpaw (CoffeeCat)", 43);
//        variantScrollList.addOption("Redtail (whitenoisewife)", 44);
//        variantScrollList.addOption("Salem (CoffeeCat, Mswolfy81)", 45);
//        variantScrollList.addOption("Short hair (CoffeeCat)", 46);
//        variantScrollList.addOption("Stoneflare (Feathered Melodica)", 47);
//        variantScrollList.addOption("Tortie point (whitenoisewife)", 48);
//        variantScrollList.addOption("Turtleheart (RainbowServal, Mswolfy81)", 49);
//        variantScrollList.addOption("Violetdew (bem te vi, Mswolfy81)", 50);
//        variantScrollList.addOption("Patch (Feathered Melodica)", 51);
//        variantScrollList.addOption("Parlee (PsychicStudios, CoffeeCat)", 52);
//
//
//        if (!ClientClanData.get().isOnGeneticalSkin()) {
//            this.addRenderableWidget(this.variantScrollList);
//        }


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

        if (kitPrefix.isEmpty()) {
            textCooldown = 100;
            return;
        }


        String prefix = kitPrefixBox.getValue().trim();

        this.minecraft.setScreen(null);

        ModPackets.sendToServer(new CtSNameKitPacket(prefix, kitten.getId()));

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            Minecraft.getInstance().setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

}