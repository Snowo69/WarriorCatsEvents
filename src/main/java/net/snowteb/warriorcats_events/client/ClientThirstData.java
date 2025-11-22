package net.snowteb.warriorcats_events.client;

public class ClientThirstData {

    private static int playerThirst = 20;
    private static final int MAX_THIRST = 20;
    private static final int MIN_THIRST = 0;

    public static void set(int thirst) {
        playerThirst = Math.min(Math.max(thirst, MIN_THIRST), MAX_THIRST);
    }

    public static int getPlayerThirst() {
        return playerThirst;
    }

    public static void add(int amount) {
        set(playerThirst + amount);
    }

    public static void sub(int amount) {
        set(playerThirst - amount);
    }
}
