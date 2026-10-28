package net.snowteb.warriorcats_events.network.packet.s2c.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientClans;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

import java.util.UUID;

public class OpenSpecificClanScreen implements CustomPacketPayload {

    private final ClientClans.Entry entry;

    public OpenSpecificClanScreen(ClientClans.Entry entry) {
        this.entry = entry;
    }

    public static void encode(OpenSpecificClanScreen msg, FriendlyByteBuf buf) {
        msg.entry.writeToBuf(buf);
    }

    public static OpenSpecificClanScreen decode(FriendlyByteBuf buf) {
        return new OpenSpecificClanScreen(ClientClans.Entry.readFromBuf(buf));
    }

    public static void handle(OpenSpecificClanScreen msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPacketHandles.openSpecificClan(msg.entry);
        });
    }

    public static final Type<OpenSpecificClanScreen> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "open_specific_clan_screen"));

    public static final StreamCodec<FriendlyByteBuf, OpenSpecificClanScreen> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> encode(pkt, buf),
                    buf -> decode(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}



