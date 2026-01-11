package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.function.Supplier;

public class OpenCatDataScreenPacket {

    public final int catId;

    public OpenCatDataScreenPacket(int catId) {
        this.catId = catId;
    }

    public OpenCatDataScreenPacket(FriendlyByteBuf buf) {
        this.catId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(catId);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {
            ClientPacketHandles.openCatScreen(catId);
        });

        ctx.setPacketHandled(true);
    }
}
