package net.snowteb.warriorcats_events.network.packet.s2c.others;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WCEClient;
import net.snowteb.warriorcats_events.WarriorCatsEvents;

public class SyncExhaustionPacket implements CustomPacketPayload {
    private final int exhaustionLevel;

    public SyncExhaustionPacket(int exhaustionLevel) {
        this.exhaustionLevel = exhaustionLevel;

    }

    public SyncExhaustionPacket(FriendlyByteBuf buf) {
        this.exhaustionLevel = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(exhaustionLevel);
    }

    public boolean handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            WCEClient.setExhaustionLevel(exhaustionLevel);
        });
        return true;
    }

    public static final Type<SyncExhaustionPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "sync_exhaustion"));

    public static final StreamCodec<FriendlyByteBuf, SyncExhaustionPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new SyncExhaustionPacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
