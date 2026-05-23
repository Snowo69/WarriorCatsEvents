package net.snowteb.warriorcats_events.network.packet.s2c.others;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientThirstData;

public class ThirstDataSyncStCPacket implements CustomPacketPayload {
    private final int thirst;

    public ThirstDataSyncStCPacket(int thirst) {
        this.thirst = thirst;
    }

    public ThirstDataSyncStCPacket(FriendlyByteBuf buf) {
        this.thirst = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(thirst);
    }

    public boolean handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientThirstData.set(thirst);
        });
        return true;
    }

    public static final Type<ThirstDataSyncStCPacket> TYPE
            = new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "thirst_data_sync"));

    public static final StreamCodec<FriendlyByteBuf, ThirstDataSyncStCPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new ThirstDataSyncStCPacket(buf)
            );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
