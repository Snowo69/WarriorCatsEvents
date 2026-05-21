package net.snowteb.warriorcats_events.network.packet.s2c.others;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.function.Supplier;

public class SyncDiseasesPacket {
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

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            ClientPacketHandles.setDiseaseData(tag, entityID);
        });
        return true;
    }


}
