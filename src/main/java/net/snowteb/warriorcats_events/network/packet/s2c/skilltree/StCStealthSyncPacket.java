package net.snowteb.warriorcats_events.network.packet.s2c.skilltree;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

public class StCStealthSyncPacket implements CustomPacketPayload {

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


    public boolean handle(IPayloadContext ctx) {

        ctx.enqueueWork(() -> {
            ClientPacketHandles.syncStealth(unlocked, isStealthOn, isSwitchOn);
        });
        return true;
    }



    public static final Type<StCStealthSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "stealth_sync"));

    public static final StreamCodec<FriendlyByteBuf, StCStealthSyncPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new StCStealthSyncPacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
