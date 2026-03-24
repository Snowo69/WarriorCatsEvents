package net.snowteb.warriorcats_events.network.packet.s2c.cats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OpenAncientStickScreenPacket {
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

    public static void handle(OpenAncientStickScreenPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ClientPacketHandles.openAncientStickScreen(msg.entityIds, msg.eagleIds);
        });
        ctx.setPacketHandled(true);
    }

}
