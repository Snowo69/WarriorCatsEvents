package net.snowteb.warriorcats_events.network.packet.s2c.cats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.ArrayList;
import java.util.List;

public class OpenAncientStickScreenPacket implements CustomPacketPayload {
    public final List<Integer> entityIds;

    public final List<Integer> eagleIds;

    public OpenAncientStickScreenPacket(List<Integer> catIDs, List<Integer> eagleIDs) {
        this.entityIds = catIDs;
        this.eagleIds = eagleIDs;
    }

    public static void encode(OpenAncientStickScreenPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.entityIds.size());
        for (int id : msg.entityIds) buf.writeVarInt(id);

        buf.writeVarInt(msg.eagleIds.size());
        for (int id : msg.eagleIds) buf.writeVarInt(id);
    }

    public static OpenAncientStickScreenPacket decode(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < size; i++) ids.add(buf.readVarInt());

        int eagleSize = buf.readVarInt();
        List<Integer> eagleids = new ArrayList<>();
        for (int i = 0; i < eagleSize; i++) eagleids.add(buf.readVarInt());

        return new OpenAncientStickScreenPacket(ids, eagleids);
    }

    public static void handle(OpenAncientStickScreenPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPacketHandles.openAncientStickScreen(msg.entityIds, msg.eagleIds);
        });
    }


    public static final Type<OpenAncientStickScreenPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "open_ancient_stick_screen"));

    public static final StreamCodec<FriendlyByteBuf, OpenAncientStickScreenPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
