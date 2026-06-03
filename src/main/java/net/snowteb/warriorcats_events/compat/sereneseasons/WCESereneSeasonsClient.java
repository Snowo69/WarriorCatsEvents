package net.snowteb.warriorcats_events.compat.sereneseasons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.Level;
import net.snowteb.warriorcats_events.client.ClientTerritoryEvents;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;

public class WCESereneSeasonsClient {

    public static void seasonOverlay(GuiGraphics pGuiGraphics, Level level) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) return;
        if (mc.options.hideGui) return;

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        ISeasonState state = SeasonHelper.getSeasonState(level);
        Component text = switch (state.getSubSeason()) {
            case EARLY_SPRING -> Component.literal("─ Early Newleaf \uD83C\uDF31").withStyle(Style.EMPTY.withColor(0xff50d773));
            case MID_SPRING -> Component.literal("─ Mid Newleaf \uD83C\uDF31").withStyle(Style.EMPTY.withColor(0xff49d956));
            case LATE_SPRING -> Component.literal("─ Late Newleaf \uD83C\uDF31").withStyle(Style.EMPTY.withColor(0xff2ddc3a));
            case EARLY_SUMMER -> Component.literal("─ Early Greenleaf \uD83C\uDF3F").withStyle(Style.EMPTY.withColor(0xff34bf30));
            case MID_SUMMER -> Component.literal("─ Mid Greenleaf \uD83C\uDF3F").withStyle(Style.EMPTY.withColor(0xff56bc13));
            case LATE_SUMMER -> Component.literal("─ Late Greenleaf \uD83C\uDF3F").withStyle(Style.EMPTY.withColor(0xff69b81a));
            case EARLY_AUTUMN -> Component.literal("─ Early Leaf-fall \uD83C\uDF43").withStyle(Style.EMPTY.withColor(0xff94ad2b));
            case MID_AUTUMN -> Component.literal("─ Mid Leaf-fall \uD83C\uDF43").withStyle(Style.EMPTY.withColor(0xffb79f29));
            case LATE_AUTUMN -> Component.literal("─ Late Leaf-fall \uD83C\uDF43").withStyle(Style.EMPTY.withColor(0xffce9249));
            case EARLY_WINTER -> Component.literal("─ Early Leaf-bare ❄").withStyle(Style.EMPTY.withColor(0xffa19cb3));
            case MID_WINTER -> Component.literal("─ Mid Leaf-bare ❄").withStyle(Style.EMPTY.withColor(0xff879aeb));
            case LATE_WINTER -> Component.literal("─ Late Leaf-bare ❄").withStyle(Style.EMPTY.withColor(0xff5dadc1));
        };

        {
            int lineLenght = mc.font.width(text.getString()) + 5;

            float scale = 0.9f;

            int xPosition = (int) (mc.getWindow().getGuiScaledWidth() - (lineLenght*scale) - 10);

            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(xPosition, 5, 0);
            pGuiGraphics.pose().scale(scale, scale, scale);
            pGuiGraphics.drawString(mc.font, text, 0, 0, 0xFFFFFFFF);

            int color = text.getStyle().getColor() != null
                    ? (0xFF000000 | text.getStyle().getColor().getValue())
                    : 0xFFFFFFFF;

            pGuiGraphics.fill(-2, 10, lineLenght, 12, color);

            pGuiGraphics.pose().popPose();
        }

    }

}
