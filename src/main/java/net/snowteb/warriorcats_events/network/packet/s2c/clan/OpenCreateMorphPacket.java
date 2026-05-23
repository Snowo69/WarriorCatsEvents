package net.snowteb.warriorcats_events.network.packet.s2c.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

public class OpenCreateMorphPacket implements CustomPacketPayload {
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

    public static void handle(OpenCreateMorphPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPacketHandles.openCreateMorphScreen(msg.isSummoning);
        });
    }

    public static final Type<OpenCreateMorphPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "open_create_morph"));

    public static final StreamCodec<FriendlyByteBuf, OpenCreateMorphPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new OpenCreateMorphPacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
