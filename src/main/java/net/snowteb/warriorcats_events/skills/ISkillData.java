package net.snowteb.warriorcats_events.skills;

public interface ISkillData {

    int getSpeedLevel();
    void setSpeedLevel(int level);

    int getHPLevel();
    void setHPLevel(int level);

    void copyFrom(ISkillData other);
}
