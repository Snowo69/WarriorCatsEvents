package net.snowteb.warriorcats_events.stealth;

public interface IStealthData {
    boolean isUnlocked();

    void setUnlocked(boolean value);

    boolean isOn();

    void setOn(boolean value);

    boolean isStealthOn();
    void setStealthOn(boolean value);

    void copyFrom(IStealthData other);
}
