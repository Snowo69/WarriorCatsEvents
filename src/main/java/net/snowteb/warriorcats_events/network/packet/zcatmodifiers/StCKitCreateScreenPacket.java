package net.snowteb.warriorcats_events.network.packet.zcatmodifiers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.function.Supplier;

public class StCKitCreateScreenPacket {

    public StCKitCreateScreenPacket() {
    }

    public StCKitCreateScreenPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {
            ClientPacketHandles.openKitSpawnScreen();
        });

        ctx.setPacketHandled(true);
    }
}

