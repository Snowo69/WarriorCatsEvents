package net.snowteb.warriorcats_events.compat;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import java.util.UUID;

public class Compatibilities {
    public static final boolean CURIOS_LOADED = ModList.get().isLoaded("curios");

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
}
