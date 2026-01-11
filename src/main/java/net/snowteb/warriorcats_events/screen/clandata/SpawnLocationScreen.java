package net.snowteb.warriorcats_events.screen.clandata;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientClanData;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.CtSTeleportToLocationPacket;
import net.snowteb.warriorcats_events.sound.ModSounds;

public class SpawnLocationScreen extends Screen {
    private int textCooldown = 0;

    private ToggleButton somewhereButton;
    private ToggleButton taigaButton;
    private ToggleButton plainsButton;
    private ToggleButton savannahButton;
    private ToggleButton forestButton;
    private ToggleButton taigaVillageButton;
    private ToggleButton plainsVillageButton;
    private ToggleButton savannahVillageButton;
    private ToggleButton junglePyramidButton;

    private Button saveButton;

    public SpawnLocationScreen() {
        super(Component.literal("Spawn Location"));
    }


    private static final ResourceLocation BANNER =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/banner.png");

    private static final ResourceLocation BG_TEXTURE =
            new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_setup/background_scene.png");


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        boolean somewhereTooltip = pMouseX >= centerX - 125 && pMouseY >= centerY - 10
                && pMouseX <= centerX - 45 && pMouseY <= centerY + 10;




        pGuiGraphics.blit(
                BG_TEXTURE,
                0,
                0,
                0,
                0,
                this.width, this.height,
                this.width, this.height
        );


        int centerx = (this.width) / 2;
        int centery = (this.height) / 2;

        String morphName = ClientClanData.get().getMorphName();
        String genderWord;
        int genderValue = ClientClanData.get().getGenderData();

        if (genderValue == 0) {
            genderWord = "his";
        } else {
            genderWord = "her";
        }

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(1.2f, 1.2f, 1.2f);
        pGuiGraphics.drawString(Minecraft.getInstance().font,
                morphName + " began " + genderWord + " journey at...",
                centerx - 125, centery - 55, 0xFFFFFFFF);
        pGuiGraphics.pose().popPose();

        if (textCooldown > 0) {
            pGuiGraphics.drawString(Minecraft.getInstance().font, "Some fields are empty",
                    centerx - 55, centery + 75, 0xFFFF0000);
        }

        if (somewhereTooltip) pGuiGraphics.renderTooltip(Minecraft.getInstance().font,
                Component.literal("This option wont change your current location.")
                        .withStyle(ChatFormatting.GRAY)
                ,pMouseX, pMouseY);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

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
            ModPackets.sendToServer(new CtSTeleportToLocationPacket(32));

            Minecraft.getInstance().getSoundManager().play(
                    new SimpleSoundInstance(
                            ModSounds.GENERATIONS_BG.get().getLocation(),
                            SoundSource.MUSIC, 0.4F, 1.0F,
                            SoundInstance.createUnseededRandom(),
                            false, 0,
                            SoundInstance.Attenuation.NONE,
                            0, 0, 0,
                            true
                    )
            );
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

        int offsetX = 105;
        int offsetY = 10;

        somewhereButton = new ToggleButton(
                centerX - 230 + offsetX, centerY - 20 + offsetY, 80, 20,
                "Somewhere",
                btn -> selectLocation(somewhereButton)
        );
        taigaButton = new ToggleButton(
                centerX - 145 + offsetX, centerY - 20 + offsetY, 80, 20,
                "Taiga",
                btn -> selectLocation(taigaButton)
        );
        plainsButton = new ToggleButton(
                centerX - 60 + offsetX, centerY - 20 + offsetY, 80, 20,
                "Plains",
                btn -> selectLocation(plainsButton)
        );
        savannahButton = new ToggleButton(
                centerX - 230 + offsetX, centerY + 5 + offsetY, 80, 20,
                "Savannah",
                btn -> selectLocation(savannahButton)
        );
        forestButton = new ToggleButton(
                centerX - 145 + offsetX, centerY + 5 + offsetY, 80, 20,
                "Forest",
                btn -> selectLocation(forestButton)
        );
        taigaVillageButton = new ToggleButton(
                centerX - 60 + offsetX, centerY + 5 + offsetY, 80, 20,
                "Taiga Village",
                btn -> selectLocation(taigaVillageButton)
        );
        plainsVillageButton = new ToggleButton(
                centerX - 230 + offsetX, centerY + 30 + offsetY, 80, 20,
                "Plains Village",
                btn -> selectLocation(plainsVillageButton)
        );
        savannahVillageButton = new ToggleButton(
                centerX - 145 + offsetX, centerY + 30 + offsetY, 80, 20,
                "Savannah Village",
                btn -> selectLocation(savannahVillageButton)
        );
        junglePyramidButton = new ToggleButton(
                centerX - 60 + offsetX, centerY + 30 + offsetY, 80, 20,
                "Jungle Pyramid",
                btn -> selectLocation(junglePyramidButton)
        );

        this.addRenderableWidget(somewhereButton);
        this.addRenderableWidget(taigaButton);
        this.addRenderableWidget(plainsButton);
        this.addRenderableWidget(savannahButton);
        this.addRenderableWidget(forestButton);
        this.addRenderableWidget(taigaVillageButton);
        this.addRenderableWidget(plainsVillageButton);
        this.addRenderableWidget(savannahVillageButton);
        this.addRenderableWidget(junglePyramidButton);



        saveButton = Button.builder(
                Component.literal("Finish"),
                btn -> onFinish()
        ).bounds(centerX-40, centerY + 85, 80, 20).build();

        this.addRenderableWidget(saveButton);
    }

    private void selectLocation(ToggleButton selected) {
        somewhereButton.setSelected(false);
        taigaButton.setSelected(false);
        plainsButton.setSelected(false);
        savannahButton.setSelected(false);
        forestButton.setSelected(false);
        taigaVillageButton.setSelected(false);
        plainsVillageButton.setSelected(false);
        savannahVillageButton.setSelected(false);
        junglePyramidButton.setSelected(false);

        selected.setSelected(true);
    }

    private void onFinish() {

        boolean isLocationSelected =
                (somewhereButton.isSelected() || taigaButton.isSelected()
                || plainsButton.isSelected() || savannahButton.isSelected()
                ||  forestButton.isSelected() || taigaVillageButton.isSelected()
                || plainsVillageButton.isSelected() || savannahVillageButton.isSelected()
                || junglePyramidButton.isSelected());

        if (!isLocationSelected) {
            textCooldown = 100;
            return;
        }

        int locationValue =0;

        if (somewhereButton.isSelected()) {
            locationValue = 0;
        }
        if (taigaButton.isSelected()) {
            locationValue = 1;
        }
        if (plainsButton.isSelected()) {
            locationValue = 2;
        }
        if (savannahButton.isSelected()) {
            locationValue = 3;
        }
        if (forestButton.isSelected()) {
            locationValue = 4;
        }
        if (taigaVillageButton.isSelected()) {
            locationValue = 5;
        }
        if (plainsVillageButton.isSelected()) {
            locationValue = 6;
        }
        if (savannahVillageButton.isSelected()) {
            locationValue = 7;
        }
        if (junglePyramidButton.isSelected()) {
            locationValue = 8;
        }

        Minecraft mc = Minecraft.getInstance();
        mc.getSoundManager().play(
                new SimpleSoundInstance(
                        ModSounds.GENERATIONS_BG.get().getLocation(),
                        SoundSource.MASTER,
                        0.4F,
                        1.0F,
                        SoundInstance.createUnseededRandom(),
                        false,
                        0,
                        SoundInstance.Attenuation.NONE,
                        0, 0, 0,
                        true
                )
        );
        ModPackets.sendToServer(new CtSTeleportToLocationPacket(locationValue));


        this.minecraft.setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

}
