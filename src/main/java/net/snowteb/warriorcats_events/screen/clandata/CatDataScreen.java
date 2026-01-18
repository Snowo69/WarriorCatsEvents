package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientClanData;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.zcatmodifiers.*;
import net.snowteb.warriorcats_events.network.packet.zcatmodifiers.SyncCatDataPacket;
import net.snowteb.warriorcats_events.util.ModButton;
import org.lwjgl.glfw.GLFW;

import static net.snowteb.warriorcats_events.entity.custom.WCatEntity.AGE_SYNC;

public class CatDataScreen extends Screen {
    WCatEntity wCatEntity;
    Component preText = Component.literal("✧ ").withStyle(ChatFormatting.GOLD);

    Component name = Component.literal("...");
    String nameToString = "...";
    Component clanName = Component.literal("...");
    Component genderText = Component.literal("...");
    Component ageText = Component.literal("...");
    Component personalityText = Component.literal("...");
    Component rankText = Component.literal("...");
    Component catMate = Component.literal("...");
    Component expectingText = Component.literal("...");
    Component KitTime = Component.literal("...");
    Component moodText = Component.literal("...");
    int friendshipLevel = 0;
    Component friendshipLevelText = Component.literal("...");
    Component helloSentence = Component.literal("...");
    Component setModeSentence = Component.literal("...");
    Component catMother = Component.literal("...");
    Component catFather = Component.literal("...");
    Component parentsText = Component.literal("...");


    boolean expectingKits;
    boolean interactionCooldownTooltip = false;
    float kittingTime;
    float moons;
    int ticks = 0;
    double friendshipPointsPixels;

    private String activeMenu = "main";


    private static final ResourceLocation BG_TEXTURE =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/catdata_template.png");
    private static final ResourceLocation CAT_NAME_TOAST =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/catname_toast.png");


    private static final ResourceLocation SOCIALHEARTS_EMPTY =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/socialhearts_empty.png");

    private static final ResourceLocation SOCIALHEARTS_FILL =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/socialhearts_fill.png");


    public CatDataScreen(Component pTitle, WCatEntity cat) {
        super(pTitle);
        this.wCatEntity = cat;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int xPositionPanel = 10;


//        pGuiGraphics.blit(BG_TEXTURE, 0, 0, 0, 0
//                , this.width, this.height, this.width, this.height);

        pGuiGraphics.blit(CAT_NAME_TOAST, 0, 0, 0, 0
                , 172, 45, 172, 45);


        pGuiGraphics.blit(SOCIALHEARTS_EMPTY, 43, 28,
                0, 0
                , 99, 9,
                99, 9);

        pGuiGraphics.enableScissor( 43, 28, 43 + (friendshipLevel), 37);
        pGuiGraphics.blit(SOCIALHEARTS_FILL, 43, 28,
                0, 0
                , 99, 9,
                99, 9);
        pGuiGraphics.disableScissor();


        if (name != null){
            if (interactionCooldownTooltip && wCatEntity.isTame()) {
                pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(name).append(" already interacted recently."), this.width, 30);
            }
        }


        if (activeMenu.equals("interact")) {
            int textWidth = Math.max(Minecraft.getInstance().font.width(helloSentence), 70);
            int boxTop = centerY - 50;
            int boxBottom = boxTop + 110;
            int boxLeft = centerX - (textWidth / 2) - 16;
            int boxRight = centerX + (textWidth / 2) + 16;

            pGuiGraphics.fill(boxLeft, boxTop, boxRight, boxBottom, 0x50000000);

            pGuiGraphics.renderOutline(boxLeft, boxTop, boxRight - boxLeft, boxBottom - boxTop, 0xFFFFFFFF);

            int textY = boxTop + 8;
            if (helloSentence != null) {
                pGuiGraphics.drawCenteredString(font, helloSentence, centerX, textY, 0xFFFFFFFF);
            }
            int lineY = textY + 14;
            pGuiGraphics.hLine(boxLeft + 8, boxRight - 8, lineY, 0xFFFFFFFF);

        }

        if (activeMenu.equals("mode")) {
            if (setModeSentence != null) {
                int textWidth = Math.max(Minecraft.getInstance().font.width(setModeSentence), 85);
                int boxTop = centerY - 50;
                int boxBottom = boxTop + 90;
                int boxLeft = centerX - (textWidth / 2) - 16;
                int boxRight = centerX + (textWidth / 2) + 16;

                pGuiGraphics.fill(boxLeft, boxTop, boxRight, boxBottom, 0x50000000);

                pGuiGraphics.renderOutline(boxLeft, boxTop, boxRight - boxLeft, boxBottom - boxTop, 0xFFFFFFFF);

                int textY = boxTop + 8;
                pGuiGraphics.drawCenteredString(font, setModeSentence, centerX, textY, 0xFFFFFFFF);
                int lineY = textY + 14;
                pGuiGraphics.hLine(boxLeft + 8, boxRight - 8, lineY, 0xFFFFFFFF);
            }
        }

        if (activeMenu.equals("home")) {
            if (setModeSentence != null) {
                int textWidth = Math.max(Minecraft.getInstance().font.width(setModeSentence), 85);
                int boxTop = centerY - 50;
                int boxBottom = boxTop + 90;
                int boxLeft = centerX - (textWidth / 2) - 16;
                int boxRight = centerX + (textWidth / 2) + 16;

                pGuiGraphics.fill(boxLeft, boxTop, boxRight, boxBottom, 0x50000000);

                pGuiGraphics.renderOutline(boxLeft, boxTop, boxRight - boxLeft, boxBottom - boxTop, 0xFFFFFFFF);

                int textY = boxTop + 8;
                pGuiGraphics.drawCenteredString(font, setModeSentence, centerX, textY, 0xFFFFFFFF);
                int lineY = textY + 14;
                pGuiGraphics.hLine(boxLeft + 8, boxRight - 8, lineY, 0xFFFFFFFF);
            }
        }

        float scale1 = 1.25f;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scale1, scale1, 1f);
        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, name, 75, 11, 0xFFFFFF);
        pGuiGraphics.pose().popPose();

//        pGuiGraphics.drawString(Minecraft.getInstance().font, moodText, centerX - 55, centerY-110, 0xFFFFFF);
//        pGuiGraphics.drawString(Minecraft.getInstance().font, clanName, centerX + xOffset, centerY-50 + yOffset, 0xFFFFFF);
//        pGuiGraphics.drawString(Minecraft.getInstance().font, genderText, centerX + xOffset, centerY-30 + yOffset, 0xFFFFFF);
//        pGuiGraphics.drawString(Minecraft.getInstance().font, ageText, centerX + xOffset, centerY-10 + yOffset, 0xFFFFFF);
//        pGuiGraphics.drawString(Minecraft.getInstance().font, personalityText, centerX + xOffset, centerY+10 + yOffset, 0xFFFFFF);
//        pGuiGraphics.drawString(Minecraft.getInstance().font, rankText, centerX + xOffset, centerY+30 + yOffset, 0xFFFFFF);
//        pGuiGraphics.drawString(Minecraft.getInstance().font, catMate, centerX + xOffset, centerY+50 + yOffset, 0xFFFFFF);
//        pGuiGraphics.drawString(Minecraft.getInstance().font, expectingText, centerX  + xOffset, centerY+70 + yOffset, 0xFFFFFF);
//        if (expectingKits) {
//            pGuiGraphics.drawString(Minecraft.getInstance().font, KitTime, centerX +xOffset, centerY+90 + yOffset, 0xFFFFFF);
//        }

        int boxX = xPositionPanel + 100;
        int boxY = 60;
        int boxWidth = 50;
        int boxHeight = 16;

        boolean renderFriendshipText = pMouseX >= 43 && pMouseX <= 142 && pMouseY >= 28 && pMouseY <= 37 && wCatEntity.isTame();

        {
            if (friendshipLevel >= 0 && wCatEntity.isTame()) {
                friendshipLevelText = Component.empty().append(Component.literal("Clanmates"))
                        .append(Component.literal( " (" + friendshipLevel + "/100)").withStyle(ChatFormatting.GRAY));
            }
            if (friendshipLevel > 20) {
                friendshipLevelText = Component.empty().append(Component.literal("Trusted clanmates"))
                        .append(Component.literal( " (" + friendshipLevel + "/100)").withStyle(ChatFormatting.GRAY));
            }
            if (friendshipLevel > 40) {
                friendshipLevelText = Component.empty().append(Component.literal("Friends"))
                        .append(Component.literal( " (" + friendshipLevel + "/100)").withStyle(ChatFormatting.GRAY));
            }
            if (friendshipLevel > 60) {
                friendshipLevelText = Component.empty().append(Component.literal("Good friends"))
                        .append(Component.literal( " (" + friendshipLevel + "/100)").withStyle(ChatFormatting.GRAY));
            }
            if (friendshipLevel > 80) {
                friendshipLevelText = Component.empty().append(Component.literal("Real friends"))
                        .append(Component.literal( " (" + friendshipLevel + "/100)").withStyle(ChatFormatting.GRAY));
            }
            if (friendshipLevel > 95) {
                friendshipLevelText = Component.empty().append(Component.literal("Best friends"))
                        .append(Component.literal( " (" + friendshipLevel + "/100)").withStyle(ChatFormatting.GRAY));
                if (Minecraft.getInstance().player != null) {
                    if (ClientClanData.get().getMateUUID() != null) {
                        if (ClientClanData.get().getMateUUID().equals(wCatEntity.getUUID())) {
                            friendshipLevelText = Component.empty().append(Component.literal("Mates"))
                                    .append(Component.literal( " (" + friendshipLevel + "/100)").withStyle(ChatFormatting.GRAY));
                        }
                    }
                }
            }
        }


        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(0.9f,0.9f,0.9f);

        if (renderFriendshipText) {
            pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(friendshipLevelText) , pMouseX, pMouseY);
        }


        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(clanName) , xPositionPanel, 70);
        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(genderText), xPositionPanel, 90);
        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(parentsText), xPositionPanel, 110);
        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(ageText), xPositionPanel, 130);
        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(personalityText), xPositionPanel, 150);
        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(moodText), xPositionPanel, 170);
        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(rankText), xPositionPanel, 190);
        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(catMate), xPositionPanel, 210);
        pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(expectingText), xPositionPanel, 230);
        if (expectingKits) {
            pGuiGraphics.renderTooltip(Minecraft.getInstance().font, Component.empty().append(preText).append(KitTime), xPositionPanel, 250);
        }
        pGuiGraphics.pose().popPose();

//        pGuiGraphics.enableScissor(centerX - 135, centerY - 65, centerX - 71, centerY - 1);
//        pGuiGraphics.blit(currentVariant, centerX - 135, centerY - 65,
//                0, 0,
//                256, 256,
//                64, 64
//        );
//        pGuiGraphics.disableScissor();

//        pGuiGraphics.enableScissor(xPositionPanel + 85, centerY - 65, xPositionPanel + 149, centerY - 1);
//        pGuiGraphics.blit(currentVariant, xPositionPanel + 85, centerY - 65,
//                0, 0,
//                256, 256,
//                64, 64
//        );
//        pGuiGraphics.disableScissor();

    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        name = wCatEntity.hasCustomName() ? wCatEntity.getCustomName() : Component.literal("Unknown cat");
        nameToString = wCatEntity.hasCustomName() ? wCatEntity.getCustomName().getString() : "Unknown cat";
        genderText = wCatEntity.isMale() ? Component.literal("Tom-cat") : Component.literal("She-cat");

        catMother = wCatEntity.getMother();
        catFather = wCatEntity.getFather();

        if (catMother.equals(Component.literal("None")) && catFather.equals(Component.literal("None"))) {
            parentsText = Component.literal("Parents: Parents unknown");
        } else {
            if (catMother.equals(Component.literal("None")) && !catFather.equals(Component.literal("None"))) {
                parentsText = Component.empty()
                        .append(Component.literal("Parents: ").withStyle(ChatFormatting.WHITE))
                        .append(catFather.copy().withStyle(ChatFormatting.AQUA));
            } else if (!catMother.equals(Component.literal("None")) && catFather.equals(Component.literal("None"))) {
                parentsText = Component.empty()
                        .append(Component.literal("Parents: ").withStyle(ChatFormatting.WHITE))
                        .append(catMother.copy().withStyle(ChatFormatting.AQUA));
            } else {
                parentsText = Component.empty()
                        .append(Component.literal("Parents: ").withStyle(ChatFormatting.WHITE))
                        .append(catMother.copy().withStyle(ChatFormatting.AQUA))
                        .append(Component.literal(" & ").withStyle(ChatFormatting.WHITE))
                        .append(catFather.copy().withStyle(ChatFormatting.AQUA));
            }
        }

        if (wCatEntity.getMate().equals(Component.literal("None"))) {
            catMate = Component.literal("No mate");
        } else {
            catMate = Component.literal("Mate: ").append(wCatEntity.getMate().copy().withStyle(ChatFormatting.AQUA));
        }

        if (wCatEntity.getClan().equals(Component.literal("None")) || wCatEntity.getClan() == null) {
            clanName = Component.literal("No clan");
        } else {
            clanName = Component.literal("From ").append(wCatEntity.getClan().copy());
        }

        if (wCatEntity.getPersonality() != null) {
            if (wCatEntity.getRank() == WCatEntity.Rank.KIT) {
                switch (wCatEntity.getPersonality()) {
                    case CAUTIOUS -> helloSentence = Component.literal("Meow");
                    case CALM ->  helloSentence = Component.literal("Meow!");
                    case AMBITIOUS -> helloSentence = Component.literal("Meow!");
                    case RECKLESS -> helloSentence = Component.literal("Meow!");
                    case SHY ->  helloSentence = Component.literal("Mrrow");
                    case GRUMPY ->  helloSentence = Component.literal("...");
                    case HUMBLE ->   helloSentence = Component.literal("Meow");
                    case FRIENDLY ->   helloSentence = Component.literal("Meow!");
                    case INDEPENDENT ->   helloSentence = Component.literal("Meow");
                }
            } else if (wCatEntity.getRank() == WCatEntity.Rank.APPRENTICE) {
                switch (wCatEntity.getPersonality()) {
                    case CAUTIOUS -> helloSentence = Component.literal("Hey ").append(ClientClanData.get().getMorphName());
                    case CALM ->  helloSentence = Component.literal("Hello, ").append(ClientClanData.get().getMorphName());
                    case AMBITIOUS -> helloSentence = Component.literal("Hello, ").append(ClientClanData.get().getMorphName());
                    case RECKLESS -> helloSentence = Component.literal("Hi ").append(ClientClanData.get().getMorphName()).append("!");
                    case SHY ->  helloSentence = Component.literal("Oh, h-hi ").append(ClientClanData.get().getMorphName()).append("!");
                    case GRUMPY ->  helloSentence = Component.literal("Huh, ").append(ClientClanData.get().getMorphName());
                    case HUMBLE ->   helloSentence = Component.literal("Hi ").append(ClientClanData.get().getMorphName()).append("!");
                    case FRIENDLY ->   helloSentence = Component.literal("").append(ClientClanData.get().getMorphName()).append("!");
                    case INDEPENDENT ->   helloSentence = Component.literal("Hi, ").append(ClientClanData.get().getMorphName());
                }
            } else {
                switch (wCatEntity.getPersonality()) {
                    case CAUTIOUS -> helloSentence = Component.literal("Oh, hey ").append(ClientClanData.get().getMorphName());
                    case CALM ->  helloSentence = Component.literal("Hello, ").append(ClientClanData.get().getMorphName());
                    case AMBITIOUS -> helloSentence = Component.literal("Hello there, ").append(ClientClanData.get().getMorphName());
                    case RECKLESS -> helloSentence = Component.literal("Hi ").append(ClientClanData.get().getMorphName()).append("!");
                    case SHY ->  helloSentence = Component.literal("Oh, h-hi ").append(ClientClanData.get().getMorphName()).append("!");
                    case GRUMPY ->  helloSentence = Component.literal("Hello, any good news, ").append(ClientClanData.get().getMorphName()).append("?");
                    case HUMBLE ->   helloSentence = Component.literal("Hi ").append(ClientClanData.get().getMorphName()).append("!");
                    case FRIENDLY ->   helloSentence = Component.literal("").append(ClientClanData.get().getMorphName()).append("! So good to see you!");
                    case INDEPENDENT ->   helloSentence = Component.literal("Greetings, ").append(ClientClanData.get().getMorphName());
                }
            }
        }


        if (wCatEntity.getRank() == WCatEntity.Rank.KIT) {
            setModeSentence = switch (wCatEntity.getPersonality()) {
                case NONE -> Component.empty();
                case CALM -> Component.literal("Meow?");
                case GRUMPY -> Component.literal("...?");
                case CAUTIOUS -> Component.literal("Meow?");
                case INDEPENDENT -> Component.literal("Meow!");
                case FRIENDLY -> Component.literal("").append(ClientClanData.get().getMorphName()).append("?");
                case SHY -> Component.literal("M-meow?");
                case AMBITIOUS -> Component.literal("Adventure!");
                case HUMBLE -> Component.literal("Meow?");
                case RECKLESS -> Component.literal("Meow!");
            };
        } else if (wCatEntity.getRank() == WCatEntity.Rank.APPRENTICE || wCatEntity.isBaby()) {
            setModeSentence = switch (wCatEntity.getPersonality()) {
                case NONE -> Component.empty();
                case CALM -> Component.literal("Yes ").append(ClientClanData.get().getMorphName()).append("?");
                case GRUMPY -> Component.literal("Huh?");
                case CAUTIOUS -> Component.literal("How can I be of help?");
                case INDEPENDENT -> Component.literal("Yes ").append(ClientClanData.get().getMorphName()).append("!");
                case FRIENDLY -> Component.literal("").append(ClientClanData.get().getMorphName()).append("!");
                case SHY -> Component.literal("M-me? Yes!");
                case AMBITIOUS -> Component.literal("Are we going on an adventure, ").append(ClientClanData.get().getMorphName()).append("?!");
                case HUMBLE -> Component.literal("How can I help, ").append(ClientClanData.get().getMorphName()).append("?");
                case RECKLESS -> Component.literal("I'm ready!");
            };
        } else {
            setModeSentence = switch (wCatEntity.getPersonality()) {
                case NONE -> Component.empty();
                case CALM -> Component.literal("What can i do for you, ").append(ClientClanData.get().getMorphName()).append("?");
                case GRUMPY -> Component.literal("What do you want?");
                case CAUTIOUS -> Component.literal("How can I help you, ").append(ClientClanData.get().getMorphName()).append("?");
                case INDEPENDENT -> Component.literal("What do you need from me?");
                case FRIENDLY -> Component.literal("Tell me what I can do for you, ").append(ClientClanData.get().getMorphName()).append("!");
                case SHY -> Component.literal("M-me? Sure, what do you need?");
                case AMBITIOUS -> Component.literal("Yes, ").append(ClientClanData.get().getMorphName()).append("?");
                case HUMBLE -> Component.literal("I'll do my best, what can I do, ").append(ClientClanData.get().getMorphName()).append("?");
                case RECKLESS -> Component.literal("Trouble? Action? How can I help?");
            };
        }

        rankText = switch (wCatEntity.getRank()) {
            case NONE -> Component.literal("Loner");
            case KIT -> Component.literal("Kit");
            case APPRENTICE -> Component.literal("Apprentice");
            case WARRIOR -> Component.literal("Warrior");
            case MEDICINE -> Component.literal("Medicine Cat");
        };

        personalityText = switch (wCatEntity.getPersonality()) {
            case NONE -> Component.literal("Personality: None");
            case CALM -> Component.literal("Personality: Gentle");
            case GRUMPY -> Component.literal("Personality: Grumpy");
            case CAUTIOUS -> Component.literal("Personality: Cautious");
            case INDEPENDENT -> Component.literal("Personality: Independent");
            case FRIENDLY -> Component.literal("Personality: Friendly");
            case SHY -> Component.literal("Personality: Shy");
            case AMBITIOUS -> Component.literal("Personality: Ambitious");
            case HUMBLE -> Component.literal("Personality: Humble");
            case RECKLESS -> Component.literal("Personality: Reckless");
        };

        if (wCatEntity.hasCustomName()) {
            moodText = switch (wCatEntity.getMood()) {
                case HAPPY -> Component.empty().append(name).append(Component.literal(" feels happy"));
                case CALM -> Component.empty().append(name).append(Component.literal(" feels calm"));
                case SAD -> Component.empty().append(name).append(Component.literal(" feels sad"));
                case STRESSED ->  Component.empty().append(name).append(Component.literal(" feels stressed"));
            };
        } else {
            moodText = switch (wCatEntity.getMood()) {
                case HAPPY -> Component.empty().append("This cat").append(Component.literal(" feels happy"));
                case CALM -> Component.empty().append("This cat").append(Component.literal(" feels calm"));
                case SAD -> Component.empty().append("This cat").append(Component.literal(" feels sad"));
                case STRESSED ->  Component.empty().append("This cat").append(Component.literal(" feels stressed"));
            };
        }


//        talkInteractButton = Button.builder(
//                Component.literal("Talk"),
//                btn -> {
//                    ModPackets.sendToServer(new PerformInteractionTalkPacket(wCatEntity.getId()));
//                    this.onClose();
//                    Minecraft.getInstance().setScreen(null);
//                }
//        ).bounds(this.width - 85, 10, 80, 20).build();
//
//        givePreyInteractButton = Button.builder(
//                Component.literal("Give Prey"),
//                btn -> {
//                    ModPackets.sendToServer(new PerformInteractionGivePreyPacket(wCatEntity.getId()));
//                    this.onClose();
//                    Minecraft.getInstance().setScreen(null);
//                }
//        ).bounds(this.width - 85, 35, 80, 20).build();
//
//        showAffectionInteractButton = Button.builder(
//                Component.literal("Show Affection"),
//                btn -> {
//                    ModPackets.sendToServer(new PerformInteractionShowAffectionPacket(wCatEntity.getId()));
//                    this.onClose();
//                    Minecraft.getInstance().setScreen(null);
//                }
//        ).bounds(this.width - 85, 60, 80, 20).build();
//
//        if (wCatEntity.isTame() && wCatEntity.getInteractionCooldown() <=0) {
//            this.addRenderableWidget(givePreyInteractButton);
//            this.addRenderableWidget(talkInteractButton);
//            this.addRenderableWidget(showAffectionInteractButton);
//        } else {
//            interactionCooldownTooltip = true;
//        }
        activeMenu = "main";
        drawMainMenu();


        super.init();
    }

    @Override
    public void tick() {

        ticks++;
        if (ticks % 20 == 0) {
            if (wCatEntity.getAge() < 0) {
                moons = wCatEntity.getEntityData().get(AGE_SYNC);
                ageText = Component.literal(String.format("%.2f moons", moons));
            } else {
                ageText = Component.literal("Fully grown");
            }

            kittingTime = ((wCatEntity.getKittingTime()) - wCatEntity.getKittingTicks()) / (20f * 60f);

            if (wCatEntity.getKittingTicks() > 20) {
                KitTime = Component.literal(String.format("%.2f min", kittingTime));
            } else {
                KitTime = Component.literal("Not expecting kits");
            }

            expectingKits = wCatEntity.isExpectingKits();
            expectingText = expectingKits ? Component.literal("Expecting kits") : Component.literal("Not expecting kits");

            if (Minecraft.getInstance().player != null) {
                friendshipLevel = wCatEntity.getEntityData().get(WCatEntity.FRIENDSHIP_SYNC);
                friendshipPointsPixels = friendshipLevel/20;


            }

        }

        super.tick();
    }

    private void drawMainMenu() {
        this.clearWidgets();

        ModPackets.sendToServer(new SyncCatDataPacket(wCatEntity.getId()));

        if (wCatEntity.isTame() && wCatEntity.getInteractionCooldown() <=0) {
            this.addRenderableWidget(Button.builder(
                    Component.literal("Interact"),
                    btn -> {
                        activeMenu = "interact";
                        drawInteractMenu();
                    }
            ).bounds(this.width - 85, 10, 80, 20).build());
        } else {
            interactionCooldownTooltip = true;
        }

        if (wCatEntity.isTame() && wCatEntity.getOwner() == Minecraft.getInstance().player) {
            this.addRenderableWidget(Button.builder(
                    Component.literal("Mode"),
                    btn -> {
                        activeMenu = "mode";
                        drawModeMenu();
                    }
            ).bounds(this.width - 85, 35, 80, 20).build());
        }

        if (wCatEntity.isTame() && wCatEntity.getOwner() == Minecraft.getInstance().player) {
            this.addRenderableWidget(Button.builder(
                    Component.literal("Home"),
                    btn -> {
                        activeMenu = "home";
                        drawHomeMenu();
                    }
            ).bounds(this.width - 85, 60, 80, 20).build());
        }

        this.addRenderableWidget(Button.builder(
                Component.literal("Close"),
                btn -> {
                    this.onClose();
                }
        ).bounds(this.width - 85, this.height - 30, 80, 20).build());
    }

    private void drawInteractMenu() {
        this.clearWidgets();
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addRenderableWidget(Button.builder(
                Component.literal("< Back"),
                btn -> {
                    activeMenu = "main";
                    drawMainMenu();
                }
        ).bounds(this.width - 85, 10, 80, 20).build());



        this.addRenderableWidget(new ModButton(
                centerX - 40,
                centerY - 20,
                80, 15,
                Component.literal("Give prey"),
                b ->  {
                    ModPackets.sendToServer(new PerformInteractionPacket(wCatEntity.getId(), WCatEntity.CatInteraction.GIVE_ITEM));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20, 0.9f
        ));
        this.addRenderableWidget(new ModButton(
                centerX - 40,
                centerY + 0,
                80, 15,
                Component.literal("Show affection"),
                b ->  {
                    ModPackets.sendToServer(new PerformInteractionPacket(wCatEntity.getId(), WCatEntity.CatInteraction.SHOW_AFFECTION));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20, 0.9f
        ));
        if (wCatEntity.getRank() != WCatEntity.Rank.KIT && wCatEntity.getRank() != WCatEntity.Rank.APPRENTICE) {
            this.addRenderableWidget(new ModButton(
                    centerX - 40,
                    centerY + 20,
                    80, 15,
                    Component.literal("Talk"),
                    b ->  {
                        ModPackets.sendToServer(new PerformInteractionPacket(wCatEntity.getId(), WCatEntity.CatInteraction.TALK));
                        this.onClose();
                    },
                    new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                    80, 20,0.9f
            ));
        }

        if (Minecraft.getInstance().player != null) {
            if (ClientClanData.get().getMateUUID() != null) {
                if (ClientClanData.get().getMateUUID().equals(wCatEntity.getUUID())) {
                    this.addRenderableWidget(new ModButton(
                            centerX - 40,
                            centerY + 40,
                            80, 15,
                            Component.literal("Have kits"),
                            b ->  {
                                if (wCatEntity.getKittingInteractCooldown() > 0) {
                                    Minecraft.getInstance().player.displayClientMessage(Component.empty()
                                            .append(wCatEntity.hasCustomName() ? wCatEntity.getCustomName().copy() : Component.literal("This cat").withStyle(ChatFormatting.YELLOW))
                                            .append(Component.literal(" already had kits recently!").withStyle(ChatFormatting.YELLOW))
                                            , true
                                    );
                                    this.onClose();
                                } else {
                                    ModPackets.sendToServer(new KittingInteractionPacket(wCatEntity.getId()));
                                    this.onClose();
                                }
                            },
                            new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                            80, 20,0.9f
                    ));
                }
            }
        }


    }

    private void drawModeMenu() {
        this.clearWidgets();
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addRenderableWidget(Button.builder(
                Component.literal("< Back"),
                btn -> {
                    activeMenu = "main";
                    drawMainMenu();
                }
        ).bounds(this.width - 85, 35, 80, 20).build());



        this.addRenderableWidget(new ModButton(
                centerX - 40,
                centerY - 20,
                80, 15,
                Component.literal("Follow me"),
                b ->  {
                    ModPackets.sendToServer(new CatSetModePacket(wCatEntity.getId(), WCatEntity.CatMode.FOLLOW));
                    Minecraft.getInstance().setScreen(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20,0.9f
        ));
        this.addRenderableWidget(new ModButton(
                centerX - 40,
                centerY,
                80, 15,
                Component.literal("Stay"),
                b ->  {
                    ModPackets.sendToServer(new CatSetModePacket(wCatEntity.getId(), WCatEntity.CatMode.SIT));
                    Minecraft.getInstance().setScreen(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20, 0.9f
        ));
        this.addRenderableWidget(new ModButton(
                centerX - 55,
                centerY + 20,
                110, 15,
                Component.literal("Do whatever you want"),
                b ->  {
                    ModPackets.sendToServer(new CatSetModePacket(wCatEntity.getId(), WCatEntity.CatMode.WANDER));
                    Minecraft.getInstance().setScreen(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20, 0.9f
        ));

    }

    private void drawHomeMenu() {
        this.clearWidgets();
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addRenderableWidget(Button.builder(
                Component.literal("< Back"),
                btn -> {
                    activeMenu = "main";
                    drawMainMenu();
                }
        ).bounds(this.width - 85, 60, 80, 20).build());



        this.addRenderableWidget(new ModButton(
                centerX - 40,
                centerY - 20,
                80, 15,
                Component.literal("Set home"),
                b ->  {
                    ModPackets.sendToServer(new CatHomeActionsPacket(wCatEntity.getId(), 1));
                    Minecraft.getInstance().setScreen(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20,0.9f
        ));
        this.addRenderableWidget(new ModButton(
                centerX - 40,
                centerY,
                80, 15,
                Component.literal("Return home"),
                b ->  {
                    ModPackets.sendToServer(new CatHomeActionsPacket(wCatEntity.getId(), 0));
                    Minecraft.getInstance().setScreen(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20, 0.9f
        ));

    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        ModPackets.sendToServer(new RetrieveLastCatModePacket(wCatEntity.getId()));
        super.onClose();
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == GLFW.GLFW_KEY_E) {
            this.onClose();
            return true;
        }
        if (pKeyCode == GLFW.GLFW_KEY_T) {
            this.onClose();
            return true;
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }
}
