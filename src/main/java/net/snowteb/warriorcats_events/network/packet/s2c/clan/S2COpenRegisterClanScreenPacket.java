package net.snowteb.warriorcats_events.network.packet.s2c.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

public class S2COpenRegisterClanScreenPacket implements CustomPacketPayload {

    private final String morphName;

    public S2COpenRegisterClanScreenPacket(String morphName) {
        this.morphName = morphName;
    }

    public static void encode(S2COpenRegisterClanScreenPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.morphName);
    }

    public static S2COpenRegisterClanScreenPacket decode(FriendlyByteBuf buf) {
        return new S2COpenRegisterClanScreenPacket(buf.readUtf());
    }

    public static void handle(S2COpenRegisterClanScreenPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPacketHandles.openClanCreateScreen(msg.morphName);
        });
    }

    public static final Type<S2COpenRegisterClanScreenPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "open_register_clan_screen"));

    public static final StreamCodec<FriendlyByteBuf, S2COpenRegisterClanScreenPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}



