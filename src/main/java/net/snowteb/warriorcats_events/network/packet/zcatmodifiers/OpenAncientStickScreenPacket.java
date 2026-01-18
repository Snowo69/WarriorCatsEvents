package net.snowteb.warriorcats_events.network.packet.zcatmodifiers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class OpenAncientStickScreenPacket {
    public final List<Integer> entityIds;

    public OpenAncientStickScreenPacket(List<Integer> catIDs) {
        this.entityIds = catIDs;
    }

    public static void encode(OpenAncientStickScreenPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.entityIds.size());
        for (int id : msg.entityIds) buf.writeVarInt(id);
    }

    public static OpenAncientStickScreenPacket decode(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < size; i++) ids.add(buf.readVarInt());
        return new OpenAncientStickScreenPacket(ids);
    }

    public static void handle(OpenAncientStickScreenPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ClientPacketHandles.openAncientStickScreen(msg.entityIds);
        });
        ctx.setPacketHandled(true);
    }

}
