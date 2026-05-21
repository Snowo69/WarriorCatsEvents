package net.snowteb.warriorcats_events.compat;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

import java.util.UUID;

public class CompatibilitiesServer {
    public static final boolean SERENESEASONS_LOADED = ModList.get().isLoaded("sereneseasons");

    public static boolean isNewLeaf(Level sLevel) {
        if (SERENESEASONS_LOADED){
            return net.snowteb.warriorcats_events.compat.sereneseasons.WCESereneSeasons.isNewLeaf(sLevel);
        }
        return false;
    }

    public static boolean isGreenLeaf(Level sLevel) {
        if (SERENESEASONS_LOADED){
            return net.snowteb.warriorcats_events.compat.sereneseasons.WCESereneSeasons.isGreenLeaf(sLevel);
        }
        return false;
    }

    public static boolean isLeafFall(Level sLevel) {
        if (SERENESEASONS_LOADED){
            return net.snowteb.warriorcats_events.compat.sereneseasons.WCESereneSeasons.isLeafFall(sLevel);
        }
        return false;
    }

    public static boolean isLeafBare(Level sLevel) {
        if (SERENESEASONS_LOADED){
            return net.snowteb.warriorcats_events.compat.sereneseasons.WCESereneSeasons.isLeafBare(sLevel);
        }
        return false;
    }

}
