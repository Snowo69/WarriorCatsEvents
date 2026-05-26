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
import java.util.UUID;

public class StCOpenPatrolScreenPacket implements CustomPacketPayload {
    public final List<Integer> entityIds;
    public final UUID clanUUID;
    public final int deputyID;
    public final boolean isPlayerValidDeputy;

    public StCOpenPatrolScreenPacket(List<Integer> catIDs, UUID clanUUID, int deputyID, boolean playerValidDeputy) {
        this.entityIds = catIDs;
        this.clanUUID = clanUUID;
        this.deputyID = deputyID;
        this.isPlayerValidDeputy = playerValidDeputy;
    }

    public static void encode(StCOpenPatrolScreenPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.entityIds.size());
        for (int id : msg.entityIds) buf.writeVarInt(id);
        buf.writeUUID(msg.clanUUID);
        buf.writeInt(msg.deputyID);
        buf.writeBoolean(msg.isPlayerValidDeputy);
    }

    public static StCOpenPatrolScreenPacket decode(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < size; i++) ids.add(buf.readVarInt());
        UUID clanUUID = buf.readUUID();
        int deputyID = buf.readInt();
        boolean playerValidDeputy = buf.readBoolean();

        return new StCOpenPatrolScreenPacket(ids, clanUUID, deputyID, playerValidDeputy);
    }

    public static void handle(StCOpenPatrolScreenPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPacketHandles.openPatrolScreen(msg.entityIds, msg.clanUUID, msg.deputyID, msg.isPlayerValidDeputy);
        });
    }

    public static final Type<StCOpenPatrolScreenPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "open_patrol_screen"));

    public static final StreamCodec<FriendlyByteBuf, StCOpenPatrolScreenPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
