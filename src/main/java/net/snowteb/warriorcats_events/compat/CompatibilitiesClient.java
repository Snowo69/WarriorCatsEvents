package net.snowteb.warriorcats_events.compat;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.snowteb.warriorcats_events.zconfig.WCEClientConfig;

import java.util.UUID;

public class CompatibilitiesClient {
    public static final boolean CURIOS_LOADED = ModList.get().isLoaded("curios");
    public static final boolean SERENESEASONS_LOADED = ModList.get().isLoaded("sereneseasons");

    public static boolean hasCuriosItem(UUID boundUUID, Class<?> clazz) {
        if (CURIOS_LOADED) {
            return net.snowteb.warriorcats_events.compat.curios.WCECurios.hasCuriosItem(boundUUID,  clazz);
        }
        return false;
    }

    public static boolean hasCuriosItem(UUID boundUUID, Item item) {
        if (CURIOS_LOADED) {
            return net.snowteb.warriorcats_events.compat.curios.WCECurios.hasCuriosItem(boundUUID,  item);
        }
        return false;
    }

    public static ItemStack getCuriosItem(UUID boundUUID, Class<?> clazz) {
        if (CURIOS_LOADED) {
            return net.snowteb.warriorcats_events.compat.curios.WCECurios.getCuriosItem(boundUUID,  clazz);
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getCuriosItem(UUID boundUUID, Item item) {
        if (CURIOS_LOADED) {
            return net.snowteb.warriorcats_events.compat.curios.WCECurios.getCuriosItem(boundUUID,  item);
        }
        return ItemStack.EMPTY;
    }

    public static void sereneSeasonsOverlay(GuiGraphics guiGraphics, Level level) {
        if (!level.isClientSide()) return;
        if (!SERENESEASONS_LOADED) return;
        if (!WCEClientConfig.CLIENT.SERENE_SEASONS_OVERLAY.get()) return;

        net.snowteb.warriorcats_events.compat.sereneseasons.WCESereneSeasonsClient.seasonOverlay(guiGraphics, level);
    }

}
