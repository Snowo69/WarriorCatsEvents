package net.snowteb.warriorcats_events.network.packet.s2c.others;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

public class StCFishingScreenPacket implements CustomPacketPayload {

    private final BlockPos clickedPos;
    public StCFishingScreenPacket(BlockPos pos) {
        this.clickedPos = pos;
    }


    public StCFishingScreenPacket(FriendlyByteBuf buf) {
        this.clickedPos = buf.readBlockPos();
    }


    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.clickedPos);
    }


    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientPacketHandles.openFishingScreen(this.clickedPos);
        });
    }

    public static final Type<StCFishingScreenPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "fishing_screen"));

    public static final StreamCodec<FriendlyByteBuf, StCFishingScreenPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new StCFishingScreenPacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
