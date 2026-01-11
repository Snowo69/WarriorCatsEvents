package net.snowteb.warriorcats_events.client;

import net.snowteb.warriorcats_events.clan.PlayerClanData;

public class ClientClanData {
    private static PlayerClanData data;

    public static void set(PlayerClanData newData) {
        data = newData;
    }

    public static PlayerClanData get() {
        return data;
    }
}
