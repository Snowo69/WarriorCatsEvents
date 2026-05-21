package net.snowteb.warriorcats_events.network.packet.s2c.cats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.function.Supplier;

public class StCKitCreateScreenPacket {
    private final int entityID;

    public StCKitCreateScreenPacket(int entityID) {
        this.entityID = entityID;
    }

    public StCKitCreateScreenPacket(FriendlyByteBuf buf) {
        this.entityID = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {
            ClientPacketHandles.openKitSpawnScreen(entityID);
        });

        ctx.setPacketHandled(true);
    }
}

