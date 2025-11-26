package net.snowteb.warriorcats_events.skills;

import java.util.UUID;

public class PlayerSkill implements ISkillData {
    public static int defaultSpeedCost = 85;
    public static int defaultHPcost = 250;
    public static int defaultStealthcost = 2500;

    public static int maxSpeedLevel = 10;
    public static int maxHPLevel = 5;

    private int speedLevel = 0;
    private int HPLevel = 0;

    public static final UUID SPEED_SKILL_UUID =
            UUID.fromString("a3d2c21a-9e65-4e8b-92b0-d9d239a5f6ac");
    public static final UUID HP_SKILL_UUID =
            UUID.fromString("a3d2c21a-9e66-4e8b-92b0-d9d239a5f6ac");


    @Override
    public int getSpeedLevel() {
        return speedLevel;
    }
    @Override
    public void setSpeedLevel(int level) {
        this.speedLevel = level;
    }


    @Override
    public int getHPLevel() {
        return HPLevel;
    }
    @Override
    public void setHPLevel(int level) {
        this.HPLevel = level;
    }


    @Override
    public void copyFrom(ISkillData other) {
        this.speedLevel = other.getSpeedLevel();
        this.HPLevel = other.getHPLevel();
    }


}

