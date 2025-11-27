package net.snowteb.warriorcats_events.skills;

import java.util.UUID;

public class PlayerSkill implements ISkillData {
    public static int defaultSpeedCost = 85;
    public static int defaultHPcost = 250;
    public static int defaultDMGcost = 150;
    public static int defaultJumpcost = 500;
    public static int defaultArmorcost = 500;

    public static int defaultStealthcost = 3500;

    public static int maxSpeedLevel = 10;
    public static int maxHPLevel = 5;
    public static int maxDMGLevel = 10;
    public static int maxJumpLevel = 3;
    public static int maxArmorLevel = 3;

    private int speedLevel = 0;
    private int HPLevel = 0;
    private int DMGLevel = 0;
    private int jumpLevel = 0;
    private int armorLevel = 0;

    public static final UUID SPEED_SKILL_UUID =
            UUID.fromString("a3d2c21a-9e65-4e8b-92b0-d9d239a5f6ac");
    public static final UUID HP_SKILL_UUID =
            UUID.fromString("a3d2c21a-9e66-4e8b-92b0-d9d239a5f6ac");
    public static final UUID DMG_SKILL_UUID =
            UUID.fromString("a3d2c21a-9e67-4e8b-92b0-d9d239a5f6ac");
    public static final UUID JUMP_SKILL_UUID =
            UUID.fromString("a3d2c21a-9e68-4e8b-92b0-d9d239a5f6ac");
    public static final UUID ARMOR_SKILL_UUID =
            UUID.fromString("a3d2c21a-9e69-4e8b-92b0-d9d239a5f6ac");


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
    public int getDMGLevel() {
        return DMGLevel;
    }
    @Override
    public void setDMGLevel(int level) {
        this.DMGLevel = level;
    }

    @Override
    public int getJumpLevel() {
        return jumpLevel;
    }
    @Override
    public void setJumpLevel(int level) {
        this.jumpLevel = level;
    }

    @Override
    public int getArmorLevel() {
        return armorLevel;
    }
    @Override
    public void setArmorLevel(int level) {
        this.armorLevel = level;
    }


    @Override
    public void copyFrom(ISkillData other) {
        this.speedLevel = other.getSpeedLevel();
        this.HPLevel = other.getHPLevel();
        this.DMGLevel = other.getDMGLevel();
        this.jumpLevel = other.getJumpLevel();
        this.armorLevel = other.getArmorLevel();
    }


}

