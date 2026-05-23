package net.snowteb.warriorcats_events.attachments;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.skilltree.StCStealthSyncPacket;
import org.jetbrains.annotations.UnknownNullability;

public class PlayerStealth implements IStealthData, INBTSerializable<CompoundTag> {
    private boolean on = false;
    private boolean unlocked = false;
    private boolean isSwitchOn = true;

    @Override
    public boolean isUnlocked() { return unlocked; }

    @Override
    public void setUnlocked(boolean value) { this.unlocked = value; }

    @Override
    public boolean isOn() { return isSwitchOn; }

    @Override
    public void setOn(boolean value) { this.isSwitchOn = value; }

    @Override
    public boolean isStealthOn() {
        return on;
    }

    @Override
    public void setStealthOn(boolean value) {
        this.on = value;
    }

    @Override
    public void copyFrom(IStealthData other) {
        this.unlocked = other.isUnlocked();
        this.on = other.isStealthOn();
        this.isSwitchOn = other.isOn();
    }


    public void sync(ServerPlayer player) {
        ModPackets.sendToPlayer(
                new StCStealthSyncPacket(this.unlocked, this.on, this.isSwitchOn),
                player
        );
    }

    public void saveNBT(CompoundTag tag) {
        tag.putBoolean("unlocked", this.unlocked);
        tag.putBoolean("on", this.on);
        tag.putBoolean("switchOn", this.isSwitchOn);
    }

    public void loadNBT(CompoundTag tag) {
        unlocked = tag.getBoolean("unlocked");
        on = tag.getBoolean("on");
        isSwitchOn = tag.getBoolean("switchOn");
    }


    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveNBT(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        loadNBT(tag);
    }
}
