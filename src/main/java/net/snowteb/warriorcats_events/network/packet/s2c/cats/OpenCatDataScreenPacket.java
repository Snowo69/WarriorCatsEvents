package net.snowteb.warriorcats_events.network.packet.s2c.cats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.function.Supplier;

public class OpenCatDataScreenPacket {

    public final int catId;
    public final boolean isDeputy;

    public OpenCatDataScreenPacket(int catId, boolean isDeputy) {
        this.catId = catId;
        this.isDeputy = isDeputy;
    }

    public OpenCatDataScreenPacket(FriendlyByteBuf buf) {
        this.catId = buf.readInt();
        this.isDeputy = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(catId);
        buf.writeBoolean(isDeputy);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();

        ctx.enqueueWork(() -> {
            ClientPacketHandles.openCatScreen(catId, isDeputy);
        });

        ctx.setPacketHandled(true);
    }
}
