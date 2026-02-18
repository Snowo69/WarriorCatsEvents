package net.snowteb.warriorcats_events.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.function.Supplier;

public class S2COpenRegisterClanScreenPacket {

    private final String clanName;
    private final String morphName;

    public S2COpenRegisterClanScreenPacket(String clanName, String morphName) {
        this.clanName = clanName;
        this.morphName = morphName;
    }

    public static void encode(S2COpenRegisterClanScreenPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.clanName);
        buf.writeUtf(msg.morphName);
    }

    public static S2COpenRegisterClanScreenPacket decode(FriendlyByteBuf buf) {
        return new S2COpenRegisterClanScreenPacket(
                buf.readUtf(), buf.readUtf()
        );
    }

    public static void handle(S2COpenRegisterClanScreenPacket msg,
                              Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {
            ClientPacketHandles.openClanCreateScreen(msg.clanName, msg.morphName);
        });

        ctx.get().setPacketHandled(true);
    }
}



