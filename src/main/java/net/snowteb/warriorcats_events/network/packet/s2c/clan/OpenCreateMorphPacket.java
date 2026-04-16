package net.snowteb.warriorcats_events.network.packet.s2c.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.function.Supplier;

public class OpenCreateMorphPacket {
    private final boolean isSummoning;
    public OpenCreateMorphPacket(boolean isSummoning) {
        this.isSummoning = isSummoning;
    }

    public OpenCreateMorphPacket(FriendlyByteBuf buf) {
        this.isSummoning = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(isSummoning);
    }

    public static void handle(OpenCreateMorphPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPacketHandles.openCreateMorphScreen(msg.isSummoning);
        });

        ctx.get().setPacketHandled(true);
    }
}
