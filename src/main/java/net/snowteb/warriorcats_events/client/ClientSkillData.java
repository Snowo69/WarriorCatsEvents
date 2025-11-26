package net.snowteb.warriorcats_events.client;

public class ClientSkillData {

    private static int speedLevel = 0;
    private static int hpLevel = 0;

    public static void set(int speed, int hp) {
        speedLevel = speed;
        hpLevel = hp;
    }

    public static int getSpeedLevel() {
        return speedLevel;
    }

    public static int getHPLevel() {
        return hpLevel;
    }

    public static void setSpeedLevel(int level) {
        speedLevel = level;
    }

    public static void setHPLevel(int level) {
        hpLevel = level;
    }

    public static void addSpeed(int amount) {
        speedLevel += amount;
    }

    public static void addHP(int amount) {
        hpLevel += amount;
    }

    public static void subSpeed(int amount) {
        speedLevel -= amount;
    }

    public static void subHP(int amount) {
        hpLevel -= amount;
    }
}
