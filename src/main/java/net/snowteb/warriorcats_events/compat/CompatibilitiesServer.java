package net.snowteb.warriorcats_events.compat;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;

import java.util.List;
import java.util.Optional;

public class CompatibilitiesServer {
    public static final boolean SERENESEASONS_LOADED = ModList.get().isLoaded("sereneseasons");
    public static final boolean CURIOS_LOADED = ModList.get().isLoaded("curios");

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

    public static Optional<ItemStack> findCuriosItem(Player player, Class<?> clazz) {
        if (CURIOS_LOADED) {
            return net.snowteb.warriorcats_events.compat.curios.WCECuriosServer.findCuriosItem(player, clazz);
        }
        return Optional.empty();
    }

    public static List<ItemStack> getAllCurios(Player player) {
        if (CURIOS_LOADED) {
            return net.snowteb.warriorcats_events.compat.curios.WCECuriosServer.getAllCuriosItems(player);
        }
        return List.of();
    }

}
