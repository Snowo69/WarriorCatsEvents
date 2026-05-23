package net.snowteb.warriorcats_events.network.packet.s2c.clan;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

public class OpenClanSetupScreenPacket implements CustomPacketPayload {

    public OpenClanSetupScreenPacket() {
    }


    public OpenClanSetupScreenPacket(FriendlyByteBuf buf) {
    }


    public void toBytes(FriendlyByteBuf buf) {
    }


    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPacketHandles.openClanSetupScreen();
        });
    }

    public static final Type<OpenClanSetupScreenPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "open_clan_setup"));

    public static final StreamCodec<FriendlyByteBuf, OpenClanSetupScreenPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) ->pkt.toBytes(buf),
                    buf -> new OpenClanSetupScreenPacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
