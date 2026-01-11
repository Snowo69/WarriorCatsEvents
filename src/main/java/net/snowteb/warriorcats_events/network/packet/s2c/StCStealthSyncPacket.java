package net.snowteb.warriorcats_events.network.packet.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.stealth.PlayerStealthProvider;

import java.util.function.Supplier;

public class StCStealthSyncPacket {

    private final boolean unlocked;
    private final boolean isStealthOn;
    private final boolean isSwitchOn;


    public StCStealthSyncPacket(boolean unlocked, boolean isStealthOn, boolean isSwitchOn) {
        this.unlocked = unlocked;
        this.isStealthOn = isStealthOn;
        this.isSwitchOn = isSwitchOn;
    }


    public StCStealthSyncPacket(FriendlyByteBuf buf) {
        this.unlocked = buf.readBoolean();
        this.isStealthOn = buf.readBoolean();
        this.isSwitchOn = buf.readBoolean();
    }


    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(unlocked);
        buf.writeBoolean(isStealthOn);
        buf.writeBoolean(isSwitchOn);
    }


    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null) return;

            player.getCapability(PlayerStealthProvider.STEALTH_MODE).ifPresent(cap -> {
                cap.setUnlocked(unlocked);
                cap.setStealthOn(isStealthOn);
                cap.setOn(isSwitchOn);
            });
        });
        return true;
    }
}
