package net.snowteb.warriorcats_events.network.packet.s2c.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.UUID;
import java.util.function.Supplier;

public class OpenSpecificClanScreen {

    private final String clanName;
    private final UUID clanUUID;

    public OpenSpecificClanScreen(String clanName, UUID clanUUID) {
        this.clanName = clanName;
        this.clanUUID = clanUUID;
    }

    public static void encode(OpenSpecificClanScreen msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.clanName);
        buf.writeUUID(msg.clanUUID);
    }

    public static OpenSpecificClanScreen decode(FriendlyByteBuf buf) {
        return new OpenSpecificClanScreen(
                buf.readUtf(), buf.readUUID()
        );
    }

    public static void handle(OpenSpecificClanScreen msg,
                              Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            ClientPacketHandles.openSpecificClan(msg.clanName, msg.clanUUID);
        });

        ctx.get().setPacketHandled(true);
    }
}



