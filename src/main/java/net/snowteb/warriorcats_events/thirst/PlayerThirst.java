package net.snowteb.warriorcats_events.thirst;

import net.minecraft.nbt.CompoundTag;

public class PlayerThirst {

    private int thirst;
    private final int MAX_THIRST = 20;
    private final int MIN_THIRST = 0;
    private int thirstDamageTimer = 0;

    public int getThirst() {
        return thirst;
    }
    public void addThirst(int add) {
        this.thirst = Math.min(thirst + add, MAX_THIRST);
    }

    public void subThirst(int sub) {
        this.thirst = Math.max(thirst - sub, MIN_THIRST);
    }

    public void copyFrom(PlayerThirst source) {
        this.thirst = source.thirst;
    }

    public void saveNBT(CompoundTag nbt) {
        nbt.putInt("thirstLevel", this.thirst);
    }

    public void loadNBT(CompoundTag nbt) {
        thirst = nbt.getInt("thirstLevel");
    }


    public void tick() {
        thirstDamageTimer++;
    }

    public int getThirstDamageTimer() {
        return thirstDamageTimer;
    }

    public void resetThirstDamageTimer() {
        thirstDamageTimer = 0;
    }
    public void reset() {
        this.thirst = MAX_THIRST;
        resetThirstDamageTimer();
    }



}
