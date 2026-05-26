package net.snowteb.warriorcats_events.network.packet.s2c.cats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.snowteb.warriorcats_events.WarriorCatsEvents;
import net.snowteb.warriorcats_events.client.ClientPacketHandles;

public class OpenCatDataScreenPacket implements CustomPacketPayload {

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

    public void handle(IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ClientPacketHandles.openCatScreen(catId, isDeputy);
        });
    }

    public static final Type<OpenCatDataScreenPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(WarriorCatsEvents.MODID, "open_cat_data_screen"));

    public static final StreamCodec<FriendlyByteBuf, OpenCatDataScreenPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> pkt.toBytes(buf),
                    buf -> new  OpenCatDataScreenPacket(buf)
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
