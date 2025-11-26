package net.snowteb.warriorcats_events.stealth;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.StCStealthSyncPacket;

public class PlayerStealth implements IStealthData {
    private boolean on = false;
    private boolean unlocked = false;

    @Override
    public boolean isUnlocked() { return unlocked; }

    @Override
    public void setUnlocked(boolean value) { this.unlocked = value; }

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
    }


    public void sync(ServerPlayer player) {
        ModPackets.sendToPlayer(
                new StCStealthSyncPacket(this.unlocked, this.on),
                player
        );
    }

    public void saveNBT(CompoundTag tag) {
        tag.putBoolean("unlocked", this.unlocked);
        tag.putBoolean("on", this.on);
    }

    public void loadNBT(CompoundTag tag) {
        unlocked = tag.getBoolean("unlocked");
        on = tag.getBoolean("on");
    }


}
