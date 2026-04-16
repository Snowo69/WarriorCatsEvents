package net.snowteb.warriorcats_events.network.packet.s2c.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.function.Supplier;

public class OpenPoseMenuPacket {

    public OpenPoseMenuPacket() {
    }


    public OpenPoseMenuPacket(FriendlyByteBuf buf) {
    }


    public void toBytes(FriendlyByteBuf buf) {
    }


    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ClientPacketHandles.openPoseMenu();
        });
        ctx.setPacketHandled(true);
    }
}
