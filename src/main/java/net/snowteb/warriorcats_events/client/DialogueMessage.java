package net.snowteb.warriorcats_events.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.entity.custom.wcat.WCatEntity;
import net.snowteb.warriorcats_events.sound.ModSounds;
import org.joml.Quaternionf;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Mod.EventBusSubscriber(modid = WarriorCatsEvents.MODID, value = Dist.CLIENT)
public class DialogueMessage {

    private static DialogueMessageDistributor.Dialogue message = null;

    public static final IGuiOverlay DIALOGUE_OVERLAY = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (message == null) return;

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;
        Font font = Minecraft.getInstance().font;


        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(centerX, centerY + 20, 0);
        float scale = 0.6f;
        guiGraphics.pose().scale(scale, scale, scale);

        int size = 180;

        int finalSize = (int) (size*(1/scale));

        List<FormattedCharSequence> lines = font.split(message.getComponent(), finalSize);
        if (lines.isEmpty()) return;

        guiGraphics.setColor(1f, 1f, 1f, message.getAlpha());

        int nameWidth = (int) (font.width(lines.get(0))*1.3);
        int realWidth = nameWidth + 25;
        for (FormattedCharSequence line : lines) {
            int w = font.width(line);
            if (w > realWidth) realWidth = w;
        }

        renderDialogue(guiGraphics, screenWidth, screenHeight, lines, font, realWidth);

        guiGraphics.setColor(1f, 1f, 1f, 1f);


        guiGraphics.pose().popPose();

    };

    private static void renderDialogue(GuiGraphics pGuiGraphics, int width,
                                       int height, List<FormattedCharSequence> lines,
                                       Font font, int finalSize) {

        if (lines.isEmpty()) return;

        int initialY = 0;

        int y = initialY;
        int x = -finalSize/2;
        float nameScale = 1.3f;

        renderDialogueBackground(pGuiGraphics, lines, font, x, y, nameScale);

        for (FormattedCharSequence line : lines) {
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(x,y,0);

            if (y == initialY) {

                pGuiGraphics.pose().translate(0,3,0);
                pGuiGraphics.pose().scale(nameScale, nameScale, nameScale);
            }
            pGuiGraphics.drawString(font, line, 0, 0, 0xFFFFFFFF);
            y += font.lineHeight;
            pGuiGraphics.pose().popPose();
        }

    }

    private static void renderDialogueBackground(GuiGraphics pGuiGraphics, List<FormattedCharSequence> lines, Font font, int x, int y, float nameScale) {

        int nameWidth = (int) (font.width(lines.get(0))*nameScale);
        int maxX = nameWidth + 25;
        for (FormattedCharSequence line : lines) {
            int currentSize = font.width(line);
            if (currentSize > maxX) {
                maxX = currentSize;
            }
        }

        int x0 = x - 5;
        int y0 = y - 3;
        int x1 = x + maxX + 5;
        int y1 = y + font.lineHeight*lines.size() + 3;

        int overallColor = 0xFF333333;
        int gradientColor1 = 0xFFFFD478;
        int gradientColor2 = 0xFFFF8E4D;

        int spacing = 3;
        int halfSpacing = (spacing / 2) + 1;


        pGuiGraphics.fill(
                x0 - spacing, y0 - halfSpacing,
                x1 + spacing, y1 + halfSpacing,
                overallColor);
        pGuiGraphics.fill(
                x0 - halfSpacing, y0 - spacing,
                x1 + halfSpacing, y1 + spacing,
                overallColor);

        pGuiGraphics.fillGradient(
                x0 - halfSpacing, y0 - halfSpacing,
                x1 + halfSpacing, y1 + halfSpacing,
                gradientColor1, gradientColor2);

        pGuiGraphics.fill(
                x0, y0,
                x1, y1,
                overallColor);

        renderCharacter(pGuiGraphics, x + nameWidth + 10, y0 + 3, 18);
    }

    private static void renderCharacter(GuiGraphics pGuiGraphics, int x, int y, int pScale) {

        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(x + 5, y + 15,0);

        WCatEntity pEntity = message.cat;

        if (pEntity == null) return;

        float f = (float)Math.atan(300 / (40.0F));
        float f1 = (float)Math.atan(0 / (40.0F));

        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float) Math.PI);
        Quaternionf quaternionf1 = (new Quaternionf()).rotateX(f1 * 20.0F * ((float) Math.PI / 180F));
        quaternionf.mul(quaternionf1);
        boolean nameVisible = pEntity.isCustomNameVisible();
        Component name = pEntity.hasCustomName() ? pEntity.getCustomName() : null;
        float f2 = pEntity.yBodyRot;
        float f3 = pEntity.getYRot();
        float f4 = pEntity.getXRot();
        float f5 = pEntity.yHeadRotO;
        float f6 = pEntity.yHeadRot;

        float magicNumber = 20F;

        pEntity.yBodyRot = 180.0F + f * magicNumber;
        pEntity.setYRot(180.0F + f * 40.0F);
        pEntity.setXRot(-f1 * 20f);
        pEntity.yHeadRot = pEntity.getYRot();
        pEntity.yHeadRotO = pEntity.getYRot();
        pEntity.setCustomNameVisible(false);
        pEntity.setCustomName(null);
        pEntity.setAnImage(true);
        InventoryScreen.renderEntityInInventory(pGuiGraphics,
                0, 0,
                pScale,
                quaternionf, quaternionf1,
                pEntity);
        pEntity.yBodyRot = f2;
        pEntity.setYRot(f3);
        pEntity.setXRot(f4);
        pEntity.yHeadRotO = f5;
        pEntity.yHeadRot = f6;
        pEntity.setCustomNameVisible(nameVisible);
        pEntity.setCustomName(name);
        pEntity.setAnImage(false);

        pGuiGraphics.pose().popPose();
    }

    public static void send(Component sender, String dialogue, WCatEntity cat) {
        message = new DialogueMessageDistributor.Dialogue(sender, dialogue, cat);
    }

    public static void send(Component sender, String dialogue, int time, WCatEntity cat) {
        message = new DialogueMessageDistributor.Dialogue(sender, dialogue, time, cat);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (mc.isPaused()) return;

        if (message != null) {
            message.tick();

            if (message.shouldRemove()) {
                message = null;
            }
        }
    }

}
