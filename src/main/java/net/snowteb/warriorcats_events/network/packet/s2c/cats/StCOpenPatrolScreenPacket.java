package net.snowteb.warriorcats_events.network.packet.s2c.cats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class StCOpenPatrolScreenPacket {
    public final List<Integer> entityIds;
    public final UUID clanUUID;
    public final int deputyID;

    public StCOpenPatrolScreenPacket(List<Integer> catIDs, UUID clanUUID, int deputyID) {
        this.entityIds = catIDs;
        this.clanUUID = clanUUID;
        this.deputyID = deputyID;
    }

    public static void encode(StCOpenPatrolScreenPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.entityIds.size());
        for (int id : msg.entityIds) buf.writeVarInt(id);
        buf.writeUUID(msg.clanUUID);
        buf.writeInt(msg.deputyID);
    }

    public static StCOpenPatrolScreenPacket decode(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < size; i++) ids.add(buf.readVarInt());
        UUID clanUUID = buf.readUUID();
        int deputyID = buf.readInt();

        return new StCOpenPatrolScreenPacket(ids, clanUUID, deputyID);
    }

    public static void handle(StCOpenPatrolScreenPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ClientPacketHandles.openPatrolScreen(msg.entityIds, msg.clanUUID, msg.deputyID);
        });
        ctx.setPacketHandled(true);
    }

}
