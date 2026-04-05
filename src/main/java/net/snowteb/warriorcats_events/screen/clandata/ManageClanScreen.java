package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.ChunkPos;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClanInfo;
import net.snowteb.warriorcats_events.client.ClientTerritoryData;
import net.snowteb.warriorcats_events.entity.ModEntities;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.c2s.clan.CtSClaimTerritory;
import net.snowteb.warriorcats_events.network.packet.c2s.clan.CtSManageClanMemberPacket;
import net.snowteb.warriorcats_events.network.packet.c2s.clan.CtSUnclaimTerritory;
import net.snowteb.warriorcats_events.util.ClanSymbol;
import net.snowteb.warriorcats_events.util.GradientButton;
import net.snowteb.warriorcats_events.util.MemberScrollList;
import net.snowteb.warriorcats_events.util.ModButton;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.UUID;

public class ManageClanScreen extends Screen {

    private final ClanInfo clanInfo;
    private MemberScrollList membersList;
    private ClanInfo.Member selectedMember;
    private String currentMenu = "";
    private String currentPermissionLevel = "";

    private ModButton changeRank;
    private ModButton kickPlayer;
    private ModButton changePerms;
    private ModButton closeTab;

    private GradientButton claimTerritory;
    private GradientButton unclaimTerritory;

    private ModButton closeClaimTerritory;

    private ModButton saveAndClaim;
    private ModButton saveAndUnclaim;

    private EditBox territoryName;

    private ChunkPos currentChunkPos;
    private boolean isInOwnTerritory = false;
    private boolean isTheCore = false;


    public static final Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 0.0F);
    public static final Quaternionf pose = new Quaternionf(1.9F, 0.0F, 0.6F, 0.0F);

    private float menuY;

    public ManageClanScreen(ClanInfo clanInfo) {
        super(Component.literal("Manage Clan"));
        this.clanInfo = clanInfo;
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int centerY = height / 2;

        ClanInfo.Member thisMember = clanInfo.getMember(Minecraft.getInstance().player.getUUID());
        if (thisMember != null) {
            currentPermissionLevel = thisMember.getPerms();
        }

        claimTerritory = new GradientButton(
                centerX - 85, 10,
                80, 20,
                Component.literal("Claim Territory"),
                btn -> {
                    drawClaimChunkMenu();
                }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20, 0xFF00104C
        );

        unclaimTerritory = new GradientButton(
                centerX + 5, 10,
                80, 20,
                Component.literal("Unclaim Territory"),
                btn -> {
                    drawUnclaimChunkMenu();
                }, new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20, 0xFF00104C
        );

        this.membersList = new MemberScrollList(
                Minecraft.getInstance(),
                100,
                20,
                centerY + 0,
                centerY + 80,
                20
        );
        this.membersList.setRenderTopAndBottom(false);
        this.membersList.setLeftPos(((20)));

        for (ClanInfo.Member member : clanInfo.playersInClan.values()) {
            membersList.addOption(member.getPlayerMorphName(), member.getPlayerUUID());
        }

        kickPlayer = new ModButton(
                this.width - 140,
                centerY + 20,
                100, 20,
                Component.literal("Kick"),
                btn -> {
                    drawConfirmKickMenu();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        );

        changeRank = new ModButton(
                        this.width - 140,
                        centerY + 45,
                        100, 20,
                        Component.literal("Change Rank"),
                        btn -> {
                                drawChangeRankMenu();
                            },
                        new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
                );

        changePerms = new ModButton(
                this.width - 140,
                centerY + 70,
                100, 20,
                Component.literal("Change Permissions"),
                btn -> {
                    drawChangePermsMenu();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        );

        int locX = this.width - 135;
        int initialY = (this.height / 2) + 5;
        closeTab = new ModButton(
                locX - 25,
                initialY - 90,
                20, 20,
                Component.literal("X").withStyle(ChatFormatting.GRAY),
                btn -> {
                    this.membersList.setSelected(null);
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                20, 20
        );

        closeTab.visible = false;
        changeRank.visible = false;
        changePerms.visible = false;
        kickPlayer.visible = false;

        LocalPlayer player =  Minecraft.getInstance().player;

        if (player != null) {
            currentChunkPos = player.chunkPosition();
            ClientTerritoryData.ClientClanTerritories clanTerritory = ClientTerritoryData.CLIENT_TERRITORIES.get(clanInfo.uuid);
            if (clanTerritory != null) {

                if (clanTerritory.core() != null) {
                    if (clanTerritory.core().equals(currentChunkPos)) {
                        isTheCore = true;
                    }
                }


                for (ClientTerritoryData.ClientChunk chunk : clanTerritory.claimedTerritory()) {
                    if (chunk.chunkPos.equals(currentChunkPos)) {
                        isInOwnTerritory = true;
                        break;
                    }
                }
            }
        }


        drawMainMenu();

        menuY = 500;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

        if (menuY > 0) {
            menuY -= (menuY) * 0.10f;
            if (menuY < 0) menuY = 0;
        }

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0, menuY, 0);

        int centerX = width / 2;
        int centerY = height / 2;

        pGuiGraphics.fill(10, centerY - 100, 150, centerY + 95, 0, 0xFF000017);
        pGuiGraphics.fillGradient(10, centerY - 100, 150, centerY + 95, 0x8800104C, 0);
        pGuiGraphics.renderOutline(10, centerY - 100, 140, 195, 0xFFFFFFFF);
        pGuiGraphics.renderOutline(19, centerY - 1, 110, 82, 0x88FFFFFF);


        pGuiGraphics.pose().pushPose();

        pGuiGraphics.pose().translate(
                80 - (font.width(clanInfo.name) * 1.2f / 2f),
                centerY - 94,
                0
        );

        pGuiGraphics.pose().mulPoseMatrix(new Matrix4f().scaling(1.2f, 1.2f, 1f));

        font.drawInBatch(
                clanInfo.name,
                0,
                0,
                clanInfo.color,
                false,
                pGuiGraphics.pose().last().pose(),
                pGuiGraphics.bufferSource(),
                Font.DisplayMode.NORMAL,
                0,
                15728880
        );

        pGuiGraphics.pose().popPose();



        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.literal("Leader: " + clanInfo.leaderName).withStyle(ChatFormatting.GRAY),
                80, centerY - 80, 0);

        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.literal("Access: " + currentPermissionLevel).withStyle(ChatFormatting.DARK_GRAY),
                80, centerY - 70, 0);

        int color = ChatFormatting.GRAY.getColor();

        float a = ((color >> 24) & 0xFF) / 255f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        pGuiGraphics.setColor(r, g, b, a);

        pGuiGraphics.blit(
                ClanSymbol.SPRITE,
                63, centerY - 55,
                (float) ((float) ClanSymbol.getSymbolCoordinate(clanInfo.symbolIndex) / 1.5), (float) 0,
                (int) (ClanSymbol.SYMBOL_SIZE / 1.5), (int) (ClanSymbol.SYMBOL_SIZE/1.5),
                (int) ((ClanSymbol.SYMBOL_SIZE * ClanSymbol.SYMBOLS_AMOUNT) / 1.5),
                (int) (ClanSymbol.SYMBOL_SIZE/1.5)
        );

        pGuiGraphics.setColor(1, 1, 1, 1);

        pGuiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.literal("Players: " + clanInfo.memberCount).withStyle(ChatFormatting.GRAY),
                80, centerY - 15, 0);




        if (membersList.getSelected() != null) {
            this.selectedMember = clanInfo.getMember(membersList.getSelected().getId());
        }

        changeRank.visible = false;
        kickPlayer.visible = false;
        changePerms.visible = false;
        closeTab.visible = false;

        if (selectedMember != null) {

            int infoX = this.width - 90;
            int infoY = centerY - 65;

            pGuiGraphics.fill(this.width - 160, centerY - 100, this.width - 20, centerY + 95, 0, 0xFF000017);
            pGuiGraphics.fillGradient(this.width - 160, centerY - 100, this.width - 20, centerY + 95, 0x8800104C, 0);
            pGuiGraphics.renderOutline(this.width - 160, centerY - 100, 140, 195, 0xFFFFFFFF);



            WCatEntity entityToRender = new WCatEntity(ModEntities.WCAT.get(), Minecraft.getInstance().level);
            entityToRender.setAnImage(true);
            entityToRender.setPlayerBoundUuid(UUID.nameUUIDFromBytes(ModEntities.WCAT.get().toString().getBytes()));
            entityToRender.setShowMorphName(false);
            entityToRender.setVariant(selectedMember.getVariantData());
            entityToRender.setOnGround(true);
            entityToRender.setYRot(0);
            entityToRender.yHeadRot = 0;
            entityToRender.yBodyRot = 0;
            entityToRender.setXRot(0);

            int entityScale = 12;
            int entityYLocation = centerY - 20;
            if (currentMenu.equals("changeRank")) {
                entityScale = 10;
                entityYLocation -= 13;
            }


            InventoryScreen.renderEntityInInventory(pGuiGraphics, infoX+2, entityYLocation, entityScale, pose , rotation, entityToRender);

            pGuiGraphics.pose().pushPose();

            pGuiGraphics.pose().translate(
                    (infoX) - (font.width(selectedMember.getPlayerMorphName()) * 1.2f / 2f),
                    infoY - 25 ,
                    0
            );

            pGuiGraphics.pose().mulPoseMatrix(new Matrix4f().scaling(1.2f, 1.2f, 1f));

            font.drawInBatch(
                    selectedMember.getPlayerMorphName(),
                    0,
                    0,
                    0xFFFFFF,
                    false,
                    pGuiGraphics.pose().last().pose(),
                    pGuiGraphics.bufferSource(),
                    Font.DisplayMode.NORMAL,
                    0,
                    15728880
            );

            pGuiGraphics.pose().popPose();

            pGuiGraphics.drawCenteredString(font, selectedMember.getPerms(), infoX, infoY - 15, 0xAAAAAA);


            /*
            float scale = 1.2f;
            String name = selectedMember.getPlayerMorphName();

            int x = infoX - 65;
            int y = infoY - 30;

            PoseStack poseStack = new PoseStack();
            poseStack.translate(x, y, 0);
            poseStack.scale(scale, scale, 1f);

            font.drawInBatch(
                    name,
                    0,
                    0,
                    0xFFFFFF,
                    false,
                    poseStack.last().pose(),
                    pGuiGraphics.bufferSource(),
                    Font.DisplayMode.NORMAL,
                    0,
                    15728880
            );

            pGuiGraphics.flush();
            */


            infoY += 50;

            if (currentMenu.equals("main")) {
                pGuiGraphics.drawCenteredString(font, "Rank: " + selectedMember.getRank(), infoX, infoY, 0xAAAAAA);
                infoY += 12;

                pGuiGraphics.drawCenteredString(font, "Age: " + selectedMember.getPlayerMorphAge(), infoX, infoY, 0xAAAAAA);
                infoY += 12;

                pGuiGraphics.drawCenteredString(font,
                        (selectedMember.isPlayerOnline() ? "Online" : "Offline"),
                        infoX,
                        infoY,
                        selectedMember.isPlayerOnline() ? 0x009100 : 0x910000
                );

                if (selectedMember.isPlayerOnline()) {
                    if (Minecraft.getInstance().player != null) {
                        if (!selectedMember.getPlayerUUID().equals(Minecraft.getInstance().player.getUUID())) {
                            changeRank.visible = true;
                            kickPlayer.visible = true;
                            changePerms.visible = true;
                            closeTab.visible = true;
                        }
                    }
                }
            }

            if (currentMenu.equals("confirmKick")) {
                Component text = Component.empty()
                                .append("Do you want to kick ")
                                        .append(Component.literal(selectedMember.getPlayerMorphName()).withStyle(ChatFormatting.GOLD))
                                                .append(" from ")
                                                        .append(Component.literal(clanInfo.name).withStyle(Style.EMPTY.withColor(clanInfo.color)))
                                                                .append("?");

                int textWidth = 130;

                List<FormattedCharSequence> lines = font.split(text, textWidth);

                for (int i = 0; i < lines.size(); i++) {
                    pGuiGraphics.drawCenteredString(
                            font,
                            lines.get(i),
                            infoX,
                            infoY + (i * font.lineHeight),
                            0xFFFFFF
                    );
                }

            }

        }

        if (currentMenu.equals("claimChunk") || currentMenu.equals("unclaimChunk")) {

            pGuiGraphics.fill(centerX - 70, centerY - 80, centerX + 70, centerY + 65, 0, 0xFF000017);
            pGuiGraphics.fillGradient(centerX - 70, centerY - 80, centerX + 70, centerY + 65, 0x8800104C, 0);
            pGuiGraphics.renderOutline(centerX - 70, centerY - 80, 140, 145, 0xFFFFFFFF);

            if (currentMenu.equals("claimChunk")) {
                pGuiGraphics.drawCenteredString(this.font,
                        Component.literal("Claim territory for").withStyle(ChatFormatting.GRAY),
                        centerX, centerY - 70, 0xFFFFFF);

                pGuiGraphics.drawCenteredString(this.font,
                        Component.literal(clanInfo.name).withStyle(Style.EMPTY.withColor(clanInfo.color)),
                        centerX, centerY - 60, 0xFFFFFF);

                pGuiGraphics.drawCenteredString(this.font,
                        Component.literal("Cost: 300XP").withStyle(ChatFormatting.GRAY),
                        centerX, centerY - 40, 0xFFFFFF);
            } else {
                String message = "";
                if (isInOwnTerritory) {
                    if (isTheCore) {
                        message = "You are in core territory! Unclaiming this piece will unclaim every other piece of territory.";
                    } else {
                        message = "Unclaiming territory might also unclaim territory not connected to the core. Proceed with caution.";
                    }
                    saveAndUnclaim.visible = true;
                } else {
                    message = "You cannot unclaim territory you don't own.";
                    saveAndUnclaim.visible = false;
                }
                int y = 60;

                List<FormattedCharSequence> wrapped = this.font.split(FormattedText.of(message), 120);
                for (FormattedCharSequence subLine : wrapped) {

                    pGuiGraphics.drawCenteredString(this.font,subLine, centerX, y, 0xFFFFFFFF);

                    y += this.font.lineHeight;
                }
            }
        }

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        pGuiGraphics.pose().popPose();

    }

    private void drawMainMenu() {
        this.clearWidgets();
        currentMenu = "main";

        this.addRenderableWidget(changeRank);
        this.addRenderableWidget(kickPlayer);
        this.addRenderableWidget(changePerms);
        this.addRenderableWidget(membersList);

        this.addRenderableWidget(claimTerritory);
        this.addRenderableWidget(unclaimTerritory);
    }

    private void drawChangeRankMenu() {
        this.clearWidgets();
        currentMenu = "changeRank";

        int locX = this.width - 135;
        int initialY = (this.height / 2) - 28;

        this.addRenderableWidget(membersList);

        this.addRenderableWidget(new ModButton(
                locX - 25,
                initialY - 73,
                20, 20,
                Component.literal("←"),
                btn -> {
                    drawMainMenu();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                20, 20, 1.1f
        ));

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 13,
                Component.literal("Make Kit"),
                btn -> {
                    ModPackets.sendToServer(new CtSManageClanMemberPacket("changerank", "kit", selectedMember.getPlayerUUID(), selectedMember.getPlayerMorphName()));
                    this.onClose();
                    },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        ));

        initialY += 15;

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 13,
                Component.literal("Make Apprentice"),
                btn -> {
                    ModPackets.sendToServer(new CtSManageClanMemberPacket("changerank", "apprentice", selectedMember.getPlayerUUID(), selectedMember.getPlayerMorphName()));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                90, 20
        ));
        initialY += 15;

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 13,
                Component.literal("Make Warrior"),
                btn -> {
                    ModPackets.sendToServer(new CtSManageClanMemberPacket("changerank", "warrior", selectedMember.getPlayerUUID(), selectedMember.getPlayerMorphName()));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        ));
        initialY += 15;

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 13,
                Component.literal("Make Elder"),
                btn -> {
                    ModPackets.sendToServer(new CtSManageClanMemberPacket("changerank", "elder", selectedMember.getPlayerUUID(), selectedMember.getPlayerMorphName()));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        ));
        initialY += 15;

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 13,
                Component.literal("Make Queen"),
                btn -> {
                    ModPackets.sendToServer(new CtSManageClanMemberPacket("changerank", "queen", selectedMember.getPlayerUUID(), selectedMember.getPlayerMorphName()));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        ));
        initialY += 15;

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 13,
                Component.literal("Make Medicine App"),
                btn -> {
                    ModPackets.sendToServer(new CtSManageClanMemberPacket("changerank", "medapp", selectedMember.getPlayerUUID(), selectedMember.getPlayerMorphName()));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        ));
        initialY += 15;

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 13,
                Component.literal("Make Medicine"),
                btn -> {
                    ModPackets.sendToServer(new CtSManageClanMemberPacket("changerank", "medicine", selectedMember.getPlayerUUID(), selectedMember.getPlayerMorphName()));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        ));
        initialY += 15;

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 13,
                Component.literal("Make Deputy"),
                btn -> {
                    ModPackets.sendToServer(new CtSManageClanMemberPacket("changerank", "deputy", selectedMember.getPlayerUUID(), selectedMember.getPlayerMorphName()));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        ));

    }

    private void drawChangePermsMenu() {
        this.clearWidgets();
        currentMenu = "changePerms";

        int locX = this.width - 135;
        int initialY = (this.height / 2) -10;

        this.addRenderableWidget(membersList);

        this.addRenderableWidget(new ModButton(
                locX - 25,
                initialY - 90,
                20, 20,
                Component.literal("←"),
                btn -> {
                    drawMainMenu();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                20, 20, 1.1f
        ));

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 15,
                Component.literal("Make Admin"),
                btn -> {
                    ModPackets.sendToServer(new CtSManageClanMemberPacket("changeperms", "admin", selectedMember.getPlayerUUID(), selectedMember.getPlayerMorphName()));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        ));

        initialY += 20;

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 15,
                Component.literal("Make Member"),
                btn -> {
                    ModPackets.sendToServer(new CtSManageClanMemberPacket("changeperms", "member", selectedMember.getPlayerUUID(), selectedMember.getPlayerMorphName()));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                90, 20
        ));
        initialY += 20;

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 15,
                Component.literal("Make Guest"),
                btn -> {
                    ModPackets.sendToServer(new CtSManageClanMemberPacket("changeperms", "guest", selectedMember.getPlayerUUID(), selectedMember.getPlayerMorphName()));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        ));


    }

    private void drawConfirmKickMenu() {
        this.clearWidgets();
        currentMenu = "confirmKick";

        int locX = this.width - 135;
        int initialY = (this.height / 2) + 5;

        this.addRenderableWidget(membersList);

        this.addRenderableWidget(new ModButton(
                locX - 25,
                initialY - 105,
                20, 20,
                Component.literal("←"),
                btn -> {
                    drawMainMenu();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                20, 20,1.1f
        ));

        initialY += 20;

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 20,
                Component.literal("Confirm"),
                btn -> {
                    ModPackets.sendToServer(new CtSManageClanMemberPacket("kick", "none", selectedMember.getPlayerUUID(), selectedMember.getPlayerMorphName()));
                    this.onClose();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        ));

        initialY += 30;

        this.addRenderableWidget(new ModButton(
                locX,
                initialY,
                90, 20,
                Component.literal("Cancel"),
                btn -> {
                    drawMainMenu();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                90, 20
        ));
    }

    private void drawClaimChunkMenu() {
        this.removeWidget(territoryName);
        this.removeWidget(closeClaimTerritory);
        this.removeWidget(saveAndClaim);
        this.removeWidget(saveAndUnclaim);

        currentMenu = "claimChunk";

        int centerY = this.height / 2;
        int centerX = this.width / 2;


        territoryName = new EditBox(this.font, centerX - 50, centerY,
                100, 20,
                Component.literal("Optional name"));
        territoryName.setHint(Component.literal("Optional name").withStyle(ChatFormatting.DARK_GRAY));
        territoryName.setMaxLength(25);

        closeClaimTerritory = new ModButton(
                centerX - 65,
                centerY + 40,
                60, 15,
                Component.literal("Back"),
                btn -> {
                    drawMainMenu();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        );

        saveAndClaim = new ModButton(
                centerX + 5,
                centerY + 40,
                60, 15,
                Component.literal("Claim"),
                btn -> {
                    saveAndClaimChunk();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        );

        this.addRenderableWidget(saveAndClaim);
        this.addRenderableWidget(closeClaimTerritory);
        this.addRenderableWidget(territoryName);

    }

    private void saveAndClaimChunk() {
        if (territoryName != null) {
            String chunkName = territoryName.getValue();

            ModPackets.sendToServer(new CtSClaimTerritory(chunkName));
            this.onClose();
        }
    }

    private void drawUnclaimChunkMenu() {
        this.removeWidget(territoryName);
        this.removeWidget(closeClaimTerritory);
        this.removeWidget(saveAndClaim);
        this.removeWidget(saveAndUnclaim);

        currentMenu = "unclaimChunk";

        int centerY = this.height / 2;
        int centerX = this.width / 2;

        closeClaimTerritory = new ModButton(
                centerX - 65,
                centerY + 40,
                60, 15,
                Component.literal("Back"),
                btn -> {
                    drawMainMenu();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        );

        saveAndUnclaim = new ModButton(
                centerX + 5,
                centerY + 40,
                60, 15,
                Component.literal("Unclaim"),
                btn -> {
                    saveAndUnclaimChunk();
                },
                new ResourceLocation(WarriorCatsEvents.MODID, "textures/empty.png"),
                80, 20
        );

        this.addRenderableWidget(saveAndUnclaim);
        this.addRenderableWidget(closeClaimTerritory);

    }

    private void saveAndUnclaimChunk() {
        ModPackets.sendToServer(new CtSUnclaimTerritory(currentChunkPos));
        this.onClose();
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (territoryName != null && territoryName.isFocused()) {
            if (territoryName.keyPressed(pKeyCode, pScanCode, pModifiers)) {
                return true;
            }
        }

        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
