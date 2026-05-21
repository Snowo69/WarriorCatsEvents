package net.snowteb.warriorcats_events.compat.sereneseasons;

import net.minecraft.world.level.Level;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

public class WCESereneSeasons {

    public static boolean isNewLeaf(Level sLevel) {
        return SeasonHelper.getSeasonState(sLevel).getSeason() == (Season.SPRING);
    }

    public static boolean isGreenLeaf(Level sLevel) {
        return SeasonHelper.getSeasonState(sLevel).getSeason() == (Season.SUMMER);
    }

    public static boolean isLeafFall(Level sLevel) {
        return SeasonHelper.getSeasonState(sLevel).getSeason() == (Season.AUTUMN);
    }

    public static boolean isLeafBare(Level sLevel) {
        return SeasonHelper.getSeasonState(sLevel).getSeason() == (Season.WINTER);
    }
    



}
