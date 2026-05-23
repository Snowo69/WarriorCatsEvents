package net.snowteb.warriorcats_events.network.packet.s2c.others;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

public class SyncDiseasesPacket implements CustomPacketPayload {
    private final int entityID;
    private final CompoundTag tag;

    public SyncDiseasesPacket(int id, CompoundTag tag) {
        this.entityID = id;
        this.tag = tag;
    }

    public SyncDiseasesPacket(FriendlyByteBuf buf) {
        this.entityID = buf.readInt();
        this.tag = buf.readNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeNbt(tag);
    }

    public boolean handle(IPayloadContext context) {
        context.enqueueWork(() -> {

            ClientPacketHandles.setDiseaseData(tag, entityID);
        });
        return true;
    }

    public static final Type<SyncDiseasesPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "sync_diseases"));

    public static final StreamCodec<FriendlyByteBuf, SyncDiseasesPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new SyncDiseasesPacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
