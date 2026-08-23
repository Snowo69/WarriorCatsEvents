package net.snowteb.warriorcats_events.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

import java.util.List;

@EventBusSubscriber(modid = WarriorCatsEvents.MODID, value = Dist.CLIENT)
public class HUDClientMessage {

    private static class HUDMessage {
        private final Component text;
        private final int initialTime;
        int time;

        private HUDMessage(Component message, int time) {
            this.text = message;
            this.time = time;
            this.initialTime = time;
        }

        public void tick() {
            if (time > 0) time--;
        }

        public boolean isDone() {
            return time <= 0;
        }

        public float getAlpha() {
            int fadingTime = getFadingTime();
            float fadeIn = (float) (initialTime - time) / fadingTime;
            float fadeOut = (float) time / fadingTime;
            return Math.max(0.1f, Math.min(1f, Math.min(fadeIn, fadeOut)));
        }

        private int getFadingTime() {
            return 30;
        }
    }

    private static HUDMessage message = null;

    public static final LayeredDraw.Layer MESSAGE_OVERLAY = (guiGraphics, partialTick) -> {
        if (message == null) return;

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;
        Font font = Minecraft.getInstance().font;

        int alpha = (int) (255*message.getAlpha());
        int color = (alpha << 24) | 0xFFFFFF;

        List<FormattedCharSequence> lines = font.split(message.text, Integer.MAX_VALUE);
        int lineHeight = font.lineHeight + 2;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(centerX, centerY - 80, 0);
        float scale = 0.6f;
        guiGraphics.pose().scale(scale, scale, scale);

        int y1 = 0;
        for (FormattedCharSequence line : lines) {
            guiGraphics.drawCenteredString(font, line, 0, y1, color);
            y1 += lineHeight;
        }

        guiGraphics.pose().popPose();

    };

    public static void send(Component message) {
        send(message, 140);
    }

    public static void send(Component component, int time) {
        message = new HUDMessage(component, time);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if (mc.isPaused()) return;

        if (message != null) {
            message.tick();

            if (message.isDone()) {
                message = null;
            }
        }
    }

}
