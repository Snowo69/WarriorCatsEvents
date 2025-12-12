package net.snowteb.warriorcats_events.skills;

import net.snowteb.warriorcats_events.WCEConfig;

import java.util.UUID;

public class PlayerSkill implements ISkillData {
    /*
    public static int defaultSpeedCost = 30;
    public static int defaultHPcost = 160;
    public static int defaultDMGcost = 40;
    public static int defaultJumpcost = 420;
    public static int defaultArmorcost = 420;

    public static int defaultStealthcost = 2921;

     */

public static int getDefaultSpeedCost() {
    return (int) (30 * WCEConfig.COMMON.SKILL_COST_MULTIPLIER.get());
}
public static int getDefaultHPcost() {
    return (int) (160 * WCEConfig.COMMON.SKILL_COST_MULTIPLIER.get());
}
public static int getDefaultDMGcost() {
    return (int) (40 * WCEConfig.COMMON.SKILL_COST_MULTIPLIER.get());
}
public static int getDefaultJumpcost() {
    return (int) (420 * WCEConfig.COMMON.SKILL_COST_MULTIPLIER.get());
}
public static int getDefaultArmorcost() {
    return (int) (420 * WCEConfig.COMMON.SKILL_COST_MULTIPLIER.get());
}
public static int getDefaultStealthcost() {
    return (int) (2921 * WCEConfig.COMMON.SKILL_COST_MULTIPLIER.get());
}


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

