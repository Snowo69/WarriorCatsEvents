package net.snowteb.warriorcats_events.network.packet.s2c.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientClans;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

public class UpdateClanEntryPacket implements CustomPacketPayload {

    private final ClientClans.Entry entry;

    public UpdateClanEntryPacket(ClientClans.Entry entry) {
        this.entry = entry;
    }


    public UpdateClanEntryPacket(FriendlyByteBuf buf) {
        this.entry = ClientClans.Entry.readFromBuf(buf);
    }


    public void toBytes(FriendlyByteBuf buf) {
        this.entry.writeToBuf(buf);
    }

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPacketHandles.updateClanEntry(this.entry);
        });
    }

    public static final Type<UpdateClanEntryPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "update_clan_entry"));

    public static final StreamCodec<FriendlyByteBuf, UpdateClanEntryPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new UpdateClanEntryPacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}



