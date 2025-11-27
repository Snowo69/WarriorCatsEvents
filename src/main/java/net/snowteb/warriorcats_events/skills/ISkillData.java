package net.snowteb.warriorcats_events.skills;

public interface ISkillData {

    int getSpeedLevel();
    void setSpeedLevel(int level);

    int getHPLevel();
    void setHPLevel(int level);

    int getDMGLevel();
    void setDMGLevel(int level);

    int getJumpLevel();
    void setJumpLevel(int level);

    int getArmorLevel();
    void setArmorLevel(int level);

    void copyFrom(ISkillData other);
}
