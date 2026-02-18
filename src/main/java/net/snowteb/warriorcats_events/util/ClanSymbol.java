package net.snowteb.warriorcats_events.util;

import net.minecraft.resources.ResourceLocation;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public final class ClanSymbol {

    public static final ResourceLocation SPRITE = new ResourceLocation(WarriorCatsEvents.MODID, "textures/gui/clan_symbols_sprite.png");
    public static final int SYMBOLS_AMOUNT = 147;
    public static final int SYMBOL_SIZE = 50;

    private ClanSymbol() {}

    public static int getSymbolCoordinate(int index) {
        return index * SYMBOL_SIZE;
    }

}
