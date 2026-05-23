package net.snowteb.warriorcats_events.network.packet.s2c.clan;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.attachments.WCEPlayerData;
import net.snowteb.warriorcats_events.client.ClientClanData;

public class S2CSyncClanDataPacket implements CustomPacketPayload {

    private final WCEPlayerData data;

    public S2CSyncClanDataPacket(WCEPlayerData data) {
        this.data = data;
    }

    public static S2CSyncClanDataPacket decode(FriendlyByteBuf buf) {
        WCEPlayerData data = new WCEPlayerData();
        CompoundTag tag = buf.readNbt();
        if (tag != null) {
            data.loadNBT(tag);
        }
        return new S2CSyncClanDataPacket(data);
    }

    public static void encode(S2CSyncClanDataPacket packet, FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        packet.data.saveNBT(tag);
        buf.writeNbt(tag);
    }

    public static void handle(S2CSyncClanDataPacket packet, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientClanData.set(packet.data);
        });
    }


    public static final Type<S2CSyncClanDataPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "sync_clan_data"));

    public static final StreamCodec<FriendlyByteBuf, S2CSyncClanDataPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) ->encode(pkt, buf),
                    buf -> decode(buf)
            );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
