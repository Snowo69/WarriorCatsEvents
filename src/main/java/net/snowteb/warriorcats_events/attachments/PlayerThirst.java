package net.snowteb.warriorcats_events.attachments;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class PlayerThirst implements INBTSerializable<CompoundTag> {

    private int thirst = 20;
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

    public boolean canDrink() {
        return thirst < MAX_THIRST;
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


    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        loadNBT(tag);
    }
}
