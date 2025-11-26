package net.snowteb.warriorcats_events.client;

public class ClientStealthData {

    private static boolean unlocked = false;
    private static boolean stealthOn = false;

    public static void set(boolean isUnlocked, boolean isStealthOn) {
        unlocked = isUnlocked;
        stealthOn = isStealthOn;
    }

    public static boolean isUnlocked() {
        return unlocked;
    }

    public static boolean isStealthOn() {
        return stealthOn;
    }

    public static void unlock() {
        unlocked = true;
    }

    public static void lock() {
        unlocked = false;
    }

    public static void toggleStealth() {
        stealthOn = !stealthOn;
    }

    public static void setStealth(boolean value) {
        stealthOn = value;
    }
}
