package net.snowteb.warriorcats_events.client;

import net.minecraft.client.Minecraft;
import net.snowteb.warriorcats_events.attachments.ModAttachments;
import net.snowteb.warriorcats_events.attachments.WCEPlayerData;

public class ClientClanData {
    public static void set(WCEPlayerData newData) {
        if (Minecraft.getInstance().player == null) return;
        Minecraft.getInstance().player.setData(ModAttachments.PLAYER_WCE_DATA, newData);
    }

    public static WCEPlayerData get() {
        if (Minecraft.getInstance().player == null) return new WCEPlayerData();
        return Minecraft.getInstance().player.getData(ModAttachments.PLAYER_WCE_DATA);
    }
}
